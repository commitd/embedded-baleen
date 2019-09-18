package io.committed.baleen.embedded.components;

import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.jcas.JCas;

import uk.gov.dstl.baleen.uima.BaleenCollectionReader;
import uk.gov.dstl.baleen.uima.UimaMonitor;
import uk.gov.dstl.baleen.uima.UimaSupport;

public final class EmbeddedCollectionReader extends BaleenCollectionReader {

  private InputDocument inputDocument = null;

  @Override
  protected void doInitialize(UimaContext context) {
    // DO NOTHING
  }

  @Override
  protected void doGetNext(JCas jCas) throws IOException {
    if (inputDocument != null) {
      extractContent(inputDocument.getContentInputStream(), inputDocument.getSource(), jCas);
    } else {
      throw new IOException("No input document set on getNext()");
    }
    // Clear out to avoid reprocessing
    inputDocument = null;
  }

  @Override
  protected void doClose() {
    // Clear out our state
    inputDocument = null;
  }

  @Override
  public boolean doHasNext() {
    // Always assume we have another one coming
    return true;
  }

  public void setNextDocument(InputDocument inputDocument) {
    this.inputDocument = inputDocument;
  }

  @Override
  public UimaSupport getSupport() {
    return super.getSupport();
  }

  @Override
  public UimaMonitor getMonitor() {
    return super.getMonitor();
  }
}
