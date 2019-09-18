package io.committed.baleen.embedded.single;

import io.committed.baleen.embedded.BaleenOutputConverter;
import io.committed.baleen.embedded.EmbeddableBaleen;
import io.committed.baleen.embedded.components.EmbeddedCollectionReader;
import io.committed.baleen.embedded.components.EmbeddedConsumer;
import io.committed.baleen.embedded.components.InputDocument;
import io.committed.baleen.embedded.internal.BaleenContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;
import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;
import uk.gov.dstl.baleen.core.pipelines.BaleenPipeline;
import uk.gov.dstl.baleen.core.pipelines.PipelineBuilder;
import uk.gov.dstl.baleen.core.pipelines.YamlPipelineConfiguration;
import uk.gov.dstl.baleen.core.pipelines.orderers.AnalysisEngineActionStore;
import uk.gov.dstl.baleen.exceptions.BaleenException;

public class EmbeddedBaleen implements EmbeddableBaleen {

  private final String pipelineName;
  private BaleenPipeline pipeline = null;
  private JCas jCas;

  public EmbeddedBaleen(String pipelineName) {
    this.pipelineName = pipelineName;
  }

  @Override
  public void setup(final String yaml) throws BaleenException {
    if (pipeline != null) {
      throw new IllegalStateException("Attempt to configure an existing Baleen");
    }

    if (yaml.contains("collectionreader:") || yaml.contains("consumers:")) {
      throw new IllegalArgumentException(
          "Yaml file should be resources and annotators only. Collection reader and consumer are managed by the embedded process");
    }

    // We need to add on OUR collection reader and consumer...

    final String enhancedYaml =
        String.format(
            "%s\n\n"
                + "collectionreader:\n"
                + "  class: %s\n"
                + "\n"
                + "consumers:\n"
                + "  - class: %s\n"
                + "\n",
            yaml, EmbeddedCollectionReader.class.getName(), EmbeddedConsumer.class.getName());

    try {
      final PipelineBuilder builder =
          new PipelineBuilder(pipelineName, new YamlPipelineConfiguration(enhancedYaml));
      pipeline = builder.createNewPipeline();
      jCas = JCasFactory.createJCas();
    } catch (final IOException | UIMAException e) {
      throw new BaleenException(e);
    }
  }

  @Override
  public void shutdown() {
    if (pipeline == null) {
      return;
    }

    // Taken from BaleenPipeline.run()
    pipeline.collectionReader().destroy();
    for (AnalysisEngine ae : pipeline.annotators()) {
      AnalysisEngineActionStore.getInstance()
          .remove((String) ae.getConfigParameterValue(PipelineBuilder.ANNOTATOR_UUID));
      ae.destroy();
    }
    for (AnalysisEngine ae : pipeline.consumers()) {
      AnalysisEngineActionStore.getInstance()
          .remove((String) ae.getConfigParameterValue(PipelineBuilder.ANNOTATOR_UUID));
      ae.destroy();
    }

    pipeline = null;
    jCas = null;
  }

  @Override
  public <T> Optional<T> process(
      Function<JCas, JCas> jCasWrapper,
      BaleenOutputConverter<T> consumer,
      String source,
      InputStream content)
      throws BaleenException {
    // This is based on BaleenPipeline.run

    if (pipeline == null) {
      throw new IllegalStateException("Baleen is not configured");
    }

    try {
      // Prepare the JCas for the next document
      jCas.reset();

      // Get next document from Collection Reader
      EmbeddedCollectionReader collectionReader =
          (EmbeddedCollectionReader) pipeline.collectionReader();
      collectionReader.setNextDocument(new InputDocument(source, content));
      collectionReader.getNext(jCas);

      JCas toProcess = jCas;

      // If we have an annotation creator run it now.
      if (jCasWrapper != null) {
        toProcess = jCasWrapper.apply(jCas);
      }

      // Process JCas with each annotator in turn
      for (AnalysisEngine ae : pipeline.annotators()) {
        // Taken from pipeline.processAnalysisEngine
        ae.process(toProcess);
      }

      // Process JCas with each consumer in turn
      for (AnalysisEngine ae : pipeline.consumers()) {
        // Taken from pipeline.processAnalysisEngine
        ae.process(toProcess);
      }

      // Would be nice to have these in a consumer, but we can't easily get at the consumer instance
      // in order to plugin that together
      // So instead we use the support/monitor from collection reader in order to fake it
      // Passing to the output consumer as per the method params
      return consumer.apply(
          new BaleenContext(collectionReader.getMonitor(), collectionReader.getSupport()), jCas);
    } catch (CollectionException | IOException | AnalysisEngineProcessException e) {
      throw new BaleenException(e);
    }
  }
}
