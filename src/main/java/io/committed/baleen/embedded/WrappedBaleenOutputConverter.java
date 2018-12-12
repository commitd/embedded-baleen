package io.committed.baleen.embedded;

import java.util.Optional;
import java.util.function.BiFunction;

import org.apache.uima.jcas.JCas;

import io.committed.baleen.embedded.internal.BaleenContext;

@FunctionalInterface
public interface WrappedBaleenOutputConverter<J extends JCas, T>
    extends BiFunction<BaleenContext, J, Optional<T>> {}
