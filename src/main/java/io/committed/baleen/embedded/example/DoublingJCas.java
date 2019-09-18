package io.committed.baleen.embedded.example;

import java.util.function.Function;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.DocumentAnnotation;
import uk.gov.dstl.baleen.uima.UimaSupport;

/**
 * An example of how the JCas can be completed modified before entering the pipeline.
 *
 * <p>This is just a simple example of repeasting the text.
 *
 * <p>In a real example you have more work to do in order to copy over annotations (in this case
 * you'd need to repeat those too).
 */
public class DoublingJCas implements Function<JCas, JCas> {

  @Override
  public JCas apply(JCas jCas) {
    // If you want to modify the document text once set, you can't in UIMA.
    // Do instead we reset the jCas provided and copy the text over to the new jCas.
    // You'd also have to copy all the annotations, etc over as well in a proper example

    String text = jCas.getDocumentText();
    String lang = jCas.getDocumentLanguage();
    DocumentAnnotation oldDa = UimaSupport.getDocumentAnnotation(jCas);

    jCas.reset();
    jCas.setDocumentLanguage(lang);
    jCas.setDocumentText(text + " " + text);
    DocumentAnnotation da = UimaSupport.getDocumentAnnotation(jCas);
    da.setSourceUri(oldDa.getSourceUri());
    // and for all the other things you want

    return jCas;
  }
}
