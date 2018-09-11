package io.committed.baleen.embedded.example;

import java.util.Map;
import java.util.Optional;

import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;

import io.committed.baleen.embedded.BaleenOutputConverter;
import io.committed.baleen.embedded.internal.BaleenContext;

import uk.gov.dstl.baleen.consumers.utils.DefaultFields;
import uk.gov.dstl.baleen.consumers.utils.SingleDocumentConsumerFormat;
import uk.gov.dstl.baleen.uima.UimaSupport;

public class SingleDocumentOutputConverter implements BaleenOutputConverter<OutputDocument> {

  @Override
  public Optional<OutputDocument> apply(final BaleenContext context, final JCas jCas) {
    final Map<String, Object> output =
        SingleDocumentConsumerFormat.formatCas(
            jCas, new DefaultFields(), true, context.getMonitor(), context.getSupport());
    final DocumentAnnotation documentAnnotation = UimaSupport.getDocumentAnnotation(jCas);
    return Optional.of(new OutputDocument(documentAnnotation, output));
  }
}
