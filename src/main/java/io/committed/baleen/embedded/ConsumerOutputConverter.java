package io.committed.baleen.embedded;

import io.committed.baleen.embedded.internal.BaleenContext;
import java.util.Optional;
import java.util.function.Consumer;
import org.apache.uima.jcas.JCas;

public class ConsumerOutputConverter implements BaleenOutputConverter<JCas> {

  private final Consumer<JCas> consumer;

  public ConsumerOutputConverter(Consumer<JCas> consumer) {
    this.consumer = consumer;
  }

  @Override
  public Optional<JCas> apply(BaleenContext baleenContext, JCas jCas) {

    if (consumer != null) {
      consumer.accept(jCas);
    }

    return Optional.of(jCas);
  }
}
