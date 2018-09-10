package io.committed.baleen.embedded.components;

import java.io.IOException;

import org.apache.uima.UimaContext;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import uk.gov.dstl.baleen.core.utils.BaleenDefaults;
import uk.gov.dstl.baleen.exceptions.InvalidParameterException;
import uk.gov.dstl.baleen.uima.BaleenCollectionReader;
import uk.gov.dstl.baleen.uima.IContentExtractor;
import uk.gov.dstl.baleen.uima.UimaMonitor;
import uk.gov.dstl.baleen.uima.UimaSupport;

public class EmbeddedCollectionReader extends BaleenCollectionReader {

  /**
   * The content extractor to use to extract content from files
   *
   * @baleen.config Value of BaleenDefaults.DEFAULT_CONTENT_EXTRACTOR
   */
  private static final String PARAM_CONTENT_EXTRACTOR = "contentExtractor";

  @ConfigurationParameter(
    name = PARAM_CONTENT_EXTRACTOR,
    defaultValue = BaleenDefaults.DEFAULT_CONTENT_EXTRACTOR
  )
  private String contentExtractor;

  private IContentExtractor extractor;

  private InputDocument inputDocument = null;

  @Override
  protected void doInitialize(final UimaContext context) throws ResourceInitializationException {
    try {
      extractor = getContentExtractor(contentExtractor);
    } catch (final InvalidParameterException ipe) {
      throw new ResourceInitializationException(ipe);
    }
    extractor.initialize(context, getConfigParameters(context));
  }

  @Override
  protected void doGetNext(final JCas jCas) throws IOException {
    if (inputDocument != null) {
      extractor.processStream(
          inputDocument.getContentInputStream(), inputDocument.getSource(), jCas);
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
    extractor.destroy();
  }

  @Override
  public boolean doHasNext() {
    // Always assume we have another one coming
    return true;
  }

  public void setNextDocument(final InputDocument inputDocument) {
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
