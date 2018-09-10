package io.committed.baleen.embedded.example;

import java.util.Map;

import org.apache.uima.jcas.tcas.DocumentAnnotation;

public class OutputDocument {

  private final DocumentAnnotation documentAnnotation;
  private final Map<String, Object> object;

  public OutputDocument(
      final DocumentAnnotation documentAnnotation, final Map<String, Object> object) {
    this.documentAnnotation = documentAnnotation;
    this.object = object;
  }

  public DocumentAnnotation getDocumentAnnotation() {
    return documentAnnotation;
  }

  public Map<String, Object> getObject() {
    return object;
  }
}
