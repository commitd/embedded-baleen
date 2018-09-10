package io.committed.baleen.embedded;

import io.committed.baleen.embedded.internal.BaleenContext;
import java.util.Optional;
import java.util.function.BiFunction;
import org.apache.uima.jcas.JCas;

@FunctionalInterface
public interface BaleenOutputConverter<T> extends BiFunction<BaleenContext, JCas, Optional<T>> {

}
