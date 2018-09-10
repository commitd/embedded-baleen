package io.committed.baleen.embedded.single;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.collection.CollectionException;
import org.apache.uima.fit.factory.JCasFactory;
import org.apache.uima.jcas.JCas;

import io.committed.baleen.embedded.BaleenOutputConverter;
import io.committed.baleen.embedded.EmbeddableBaleen;
import io.committed.baleen.embedded.components.EmbeddedCollectionReader;
import io.committed.baleen.embedded.components.EmbeddedConsumer;
import io.committed.baleen.embedded.components.InputDocument;
import io.committed.baleen.embedded.internal.BaleenContext;

import uk.gov.dstl.baleen.core.pipelines.BaleenPipeline;
import uk.gov.dstl.baleen.core.pipelines.PipelineBuilder;
import uk.gov.dstl.baleen.core.pipelines.YamlPiplineConfiguration;
import uk.gov.dstl.baleen.core.pipelines.orderers.AnalysisEngineActionStore;
import uk.gov.dstl.baleen.exceptions.BaleenException;

public class EmbeddedBaleen implements EmbeddableBaleen {

  private final String pipelineName;
  private BaleenPipeline pipeline = null;
  private JCas jCas;

  public EmbeddedBaleen(final String pipelineName) {
    this.pipelineName = pipelineName;
  }

  /*
   * (non-Javadoc)
   *
   * @see io.committed.baleen.embedded.lib.EmbeddableBaleen#setup(java.lang.String)
   */
  @Override
  public void setup(final String yaml) throws IllegalStateException, BaleenException {
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
      YamlPiplineConfiguration piplineConfiguration = new YamlPiplineConfiguration(enhancedYaml);
      final PipelineBuilder builder = new PipelineBuilder(pipelineName, piplineConfiguration);
      pipeline = builder.createNewPipeline();

      jCas = JCasFactory.createJCas();
    } catch (final IOException | UIMAException e) {
      throw new BaleenException(e);
    }
  }

  /*
   * (non-Javadoc)
   *
   * @see io.committed.baleen.embedded.lib.EmbeddableBaleen#shutdown()
   */
  @Override
  public void shutdown() {
    if (pipeline == null) {
      return;
    }

    // Taken from BaleenPipeline.run()
    pipeline.collectionReader().destroy();
    for (final AnalysisEngine ae : pipeline.annotators()) {
      AnalysisEngineActionStore.getInstance()
          .remove((String) ae.getConfigParameterValue(PipelineBuilder.ANNOTATOR_UUID));
      ae.destroy();
    }
    for (final AnalysisEngine ae : pipeline.consumers()) {
      AnalysisEngineActionStore.getInstance()
          .remove((String) ae.getConfigParameterValue(PipelineBuilder.ANNOTATOR_UUID));
      ae.destroy();
    }

    pipeline = null;
    jCas = null;
  }

  @Override
  public synchronized <T> Optional<T> process(
      final String source, final InputStream content, final BaleenOutputConverter<T> consumer)
      throws BaleenException {
    return process(source, content, null, consumer);
  }

  /*
   * (non-Javadoc)
   *
   * @see io.committed.baleen.embedded.lib.EmbeddableBaleen#process(java.lang.String,
   * java.io.InputStream, io.committed.baleen.embedded.lib.BaleenOutputConverter)
   */
  @Override
  public synchronized <T> Optional<T> process(
      final String source,
      final InputStream content,
      final Consumer<JCas> annotationCreator,
      final BaleenOutputConverter<T> consumer)
      throws BaleenException {
    // This is based on BaleenPipeline.run

    if (pipeline == null) {
      throw new IllegalStateException("Baleen is not configured");
    }

    try {
      // Prepare the JCas for the next document
      jCas.reset();

      // Get next document from Collection Reader
      final EmbeddedCollectionReader collectionReader =
          (EmbeddedCollectionReader) pipeline.collectionReader();
      collectionReader.setNextDocument(new InputDocument(source, content));
      collectionReader.getNext(jCas.getCas());

      if (annotationCreator != null) {
        annotationCreator.accept(jCas);
      }

      // Process JCas with each annotator in turn
      for (final AnalysisEngine ae : pipeline.annotators()) {
        // Taken from pipeline.processAnalysisEngine
        ae.process(jCas);
      }

      // Process JCas with each consumer in turn
      for (final AnalysisEngine ae : pipeline.consumers()) {
        // Taken from pipeline.processAnalysisEngine
        ae.process(jCas);
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
