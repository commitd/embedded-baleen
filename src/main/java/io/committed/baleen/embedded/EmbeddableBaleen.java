package io.committed.baleen.embedded;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.apache.uima.jcas.JCas;

import uk.gov.dstl.baleen.exceptions.BaleenException;

public interface EmbeddableBaleen {

  void setup(String yaml) throws IllegalStateException, BaleenException;

  void shutdown();

  default <T> Optional<T> process(
      String source, InputStream content, BaleenOutputConverter<T> consumer)
      throws BaleenException {
    return process(source, content, jCas -> {}, consumer);
  }

  default <T> Optional<T> process(
      String source,
      InputStream content,
      Consumer<JCas> annotationCreator,
      BaleenOutputConverter<T> consumer)
      throws BaleenException {
    return process(
        jCas -> {
          annotationCreator.accept(jCas);
          return jCas;
        },
        consumer,
        source,
        content);
  }

  <T> Optional<T> process(
      Function<JCas, JCas> jCasWrapper,
      BaleenOutputConverter<T> consumer,
      String source,
      InputStream content)
      throws BaleenException;
}
