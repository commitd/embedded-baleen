package io.committed.baleen.embedded.components;

import java.io.InputStream;
import java.util.Iterator;
import java.util.ListIterator;

import org.apache.uima.cas.*;
import org.apache.uima.cas.impl.CASImpl;
import org.apache.uima.cas.impl.LowLevelCAS;
import org.apache.uima.cas.impl.LowLevelIndexRepository;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.JFSIndexRepository;
import org.apache.uima.jcas.cas.*;
import org.apache.uima.jcas.tcas.Annotation;

@SuppressWarnings({"deprecation", "squid:CallToDeprecatedMethod"})
public class JCasAdapter implements JCas {

  protected JCas jCas;

  public JCasAdapter(JCas jCas) {
    this.jCas = jCas;
  }

  @Override
  public void setDocumentText(String text) {
    jCas.setDocumentText(text);
  }

  @Override
  public String getDocumentText() {
    return jCas.getDocumentText();
  }

  @Override
  public void release() {
    jCas.release();
  }

  @Override
  public FSIndexRepository getFSIndexRepository() {
    return jCas.getFSIndexRepository();
  }

  @Override
  public LowLevelIndexRepository getLowLevelIndexRepository() {
    return jCas.getLowLevelIndexRepository();
  }

  @Override
  public CAS getCas() {
    return jCas.getCas();
  }

  @Override
  public CASImpl getCasImpl() {
    return jCas.getCasImpl();
  }

  @Override
  public LowLevelCAS getLowLevelCas() {
    return jCas.getLowLevelCas();
  }

  @Override
  public TOP_Type getType(int i) {
    return jCas.getType(i);
  }

  @Override
  public Type getCasType(int i) {
    return jCas.getCasType(i);
  }

  @Override
  public TOP_Type getType(TOP instance) {
    return jCas.getType(instance);
  }

  @Override
  public Type getRequiredType(String s) throws CASException {
    return jCas.getRequiredType(s);
  }

  @Override
  public Feature getRequiredFeature(Type t, String s) throws CASException {
    return jCas.getRequiredFeature(t, s);
  }

  @Override
  public Feature getRequiredFeatureDE(Type t, String s, String rangeName, boolean featOkTst) {
    return jCas.getRequiredFeatureDE(t, s, rangeName, featOkTst);
  }

  @Override
  public void putJfsFromCaddr(int casAddr, FeatureStructure fs) {
    jCas.putJfsFromCaddr(casAddr, fs);
  }

  @Override
  public <T extends TOP> T getJfsFromCaddr(int casAddr) {
    return jCas.getJfsFromCaddr(casAddr);
  }

  @Override
  public void checkArrayBounds(int fsRef, int pos) {
    jCas.checkArrayBounds(fsRef, pos);
  }

  @Override
  public void throwFeatMissing(String feat, String type) {
    jCas.throwFeatMissing(feat, type);
  }

  @Override
  public Sofa getSofa(SofaID sofaID) {
    return jCas.getSofa(sofaID);
  }

  @Override
  public Sofa getSofa() {
    return jCas.getSofa();
  }

  @Override
  public JCas createView(String sofaID) throws CASException {
    return jCas.createView(sofaID);
  }

  @Override
  public JCas getJCas(Sofa sofa) throws CASException {
    return jCas.getJCas(sofa);
  }

  @Override
  public JFSIndexRepository getJFSIndexRepository() {
    return jCas.getJFSIndexRepository();
  }

  @Override
  public TOP getDocumentAnnotationFs() {
    return jCas.getDocumentAnnotationFs();
  }

  @Override
  public StringArray getStringArray0L() {
    return jCas.getStringArray0L();
  }

  @Override
  public IntegerArray getIntegerArray0L() {
    return jCas.getIntegerArray0L();
  }

  @Override
  public FSArray getFSArray0L() {
    return jCas.getFSArray0L();
  }

  @Override
  public void processInit() {
    jCas.processInit();
  }

  @Override
  public JCas getView(String localViewName) throws CASException {
    return jCas.getView(localViewName);
  }

  @Override
  public JCas getView(SofaFS aSofa) throws CASException {
    return jCas.getView(aSofa);
  }

  @Override
  public TypeSystem getTypeSystem() {
    return jCas.getTypeSystem();
  }

  @Override
  public SofaFS createSofa(SofaID sofaID, String mimeType) {
    return jCas.createSofa(sofaID, mimeType);
  }

  @Override
  public FSIterator<SofaFS> getSofaIterator() {
    return jCas.getSofaIterator();
  }

  @Override
  public <T extends FeatureStructure> FSIterator<T> createFilteredIterator(
      FSIterator<T> it, FSMatchConstraint cons) {
    return jCas.createFilteredIterator(it, cons);
  }

  @Override
  public ConstraintFactory getConstraintFactory() {
    return jCas.getConstraintFactory();
  }

  @Override
  public FeaturePath createFeaturePath() {
    return jCas.createFeaturePath();
  }

  @Override
  public FSIndexRepository getIndexRepository() {
    return jCas.getIndexRepository();
  }

  @Override
  public <T extends FeatureStructure> ListIterator<T> fs2listIterator(FSIterator<T> it) {
    return jCas.fs2listIterator(it);
  }

  @Override
  public void reset() {
    jCas.reset();
  }

  @Override
  public String getViewName() {
    return jCas.getViewName();
  }

  @Override
  public int size() {
    return jCas.size();
  }

  @Override
  public FeatureValuePath createFeatureValuePath(String featureValuePath) {
    return jCas.createFeatureValuePath(featureValuePath);
  }

  @Override
  public void setSofaDataString(String text, String mimetype) {
    jCas.setSofaDataString(text, mimetype);
  }

  @Override
  public String getSofaDataString() {
    return jCas.getSofaDataString();
  }

  @Override
  public void setDocumentLanguage(String languageCode) {
    jCas.setDocumentLanguage(languageCode);
  }

  @Override
  public String getDocumentLanguage() {
    return jCas.getDocumentLanguage();
  }

  @Override
  public void setSofaDataArray(FeatureStructure array, String mime) {
    jCas.setSofaDataArray(array, mime);
  }

  @Override
  public FeatureStructure getSofaDataArray() {
    return jCas.getSofaDataArray();
  }

  @Override
  public void setSofaDataURI(String uri, String mime) {
    jCas.setSofaDataURI(uri, mime);
  }

  @Override
  public String getSofaDataURI() {
    return jCas.getSofaDataURI();
  }

  @Override
  public InputStream getSofaDataStream() {
    return jCas.getSofaDataStream();
  }

  @Override
  public String getSofaMimeType() {
    return jCas.getSofaMimeType();
  }

  @Override
  public void addFsToIndexes(FeatureStructure fs) {
    jCas.addFsToIndexes(fs);
  }

  @Override
  public void removeFsFromIndexes(FeatureStructure fs) {
    jCas.removeFsFromIndexes(fs);
  }

  @Override
  public void removeAllIncludingSubtypes(int i) {
    jCas.removeAllIncludingSubtypes(i);
  }

  @Override
  public void removeAllExcludingSubtypes(int i) {
    jCas.removeAllExcludingSubtypes(i);
  }

  @Override
  public Type getCasType(Class<? extends FeatureStructure> aClass) {
    return jCas.getCasType(aClass);
  }

  @Override
  public AnnotationIndex<Annotation> getAnnotationIndex() {
    return jCas.getAnnotationIndex();
  }

  @Override
  public <T extends Annotation> AnnotationIndex<T> getAnnotationIndex(Type type) {
    return jCas.getAnnotationIndex(type);
  }

  @Override
  public <T extends Annotation> AnnotationIndex<T> getAnnotationIndex(int type) {
    return jCas.getAnnotationIndex(type);
  }

  @Override
  public <T extends Annotation> AnnotationIndex<T> getAnnotationIndex(Class<T> clazz) {
    return jCas.getAnnotationIndex(clazz);
  }

  @Override
  public <T extends TOP> FSIterator<T> getAllIndexedFS(Class<T> clazz) {
    return jCas.getAllIndexedFS(clazz);
  }

  @Override
  public Iterator<JCas> getViewIterator() throws CASException {
    return jCas.getViewIterator();
  }

  @Override
  public Iterator<JCas> getViewIterator(String localViewNamePrefix) throws CASException {
    return jCas.getViewIterator(localViewNamePrefix);
  }

  @Override
  public AutoCloseable protectIndexes() {
    return jCas.protectIndexes();
  }

  @Override
  public void protectIndexes(Runnable runnable) {
    jCas.protectIndexes(runnable);
  }

  @Override
  public <T extends TOP> FSIndex<T> getIndex(String label, Class<T> clazz) {
    return jCas.getIndex(label, clazz);
  }
}
