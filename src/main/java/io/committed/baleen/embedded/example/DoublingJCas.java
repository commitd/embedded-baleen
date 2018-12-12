package io.committed.baleen.embedded.example;

import org.apache.uima.jcas.JCas;

import io.committed.baleen.embedded.components.JCasAdapter;

public class DoublingJCas extends JCasAdapter {

  public DoublingJCas(JCas jCas) {
    super(jCas);
  }

  @Override
  public void setDocumentText(String text) {
    super.setDocumentText(text + " " + text);
  }
}
