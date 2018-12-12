package io.committed.baleen.embedded;

import org.apache.uima.jcas.JCas;

@FunctionalInterface
public interface BaleenOutputConverter<T> extends WrappedBaleenOutputConverter<JCas, T> {}
