package io.committed.baleen.embedded.pool;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.apache.uima.jcas.JCas;

import io.committed.baleen.embedded.EmbeddableBaleen;
import io.committed.baleen.embedded.WrappedBaleenOutputConverter;

import uk.gov.dstl.baleen.exceptions.BaleenException;

public class BaleenPool implements EmbeddableBaleen {

  private final int poolSize;
  private final String pipelineName;
  private GenericObjectPool<EmbeddableBaleen> pool;

  public BaleenPool(final String pipelineName, final int poolSize) {
    this.pipelineName = pipelineName;
    this.poolSize = poolSize;
  }

  public int getPoolSize() {
    return poolSize;
  }

  @Override
  public void setup(final String yaml) {

    final GenericObjectPoolConfig config = new GenericObjectPoolConfig();
    config.setMaxTotal(poolSize);
    pool = new GenericObjectPool<>(new BaleenPoolFactory(pipelineName, yaml), config);
  }

  @Override
  public void shutdown() {
    if (pool != null) {
      pool.close();
      pool = null;
    }
  }

  @Override
  public <J extends JCas, T> Optional<T> process(
      Function<JCas, J> jCasWrapper,
      WrappedBaleenOutputConverter<J, T> consumer,
      String source,
      InputStream content)
      throws BaleenException {

    EmbeddableBaleen baleen = borrowBaleenInstance();
    try {
      return baleen.process(jCasWrapper, consumer, source, content);
    } finally {
      returnBaleenInstance(baleen);
    }
  }

  private EmbeddableBaleen borrowBaleenInstance() throws BaleenException {
    EmbeddableBaleen baleen;
    try {
      baleen = pool.borrowObject();
    } catch (final Exception e) {
      throw new BaleenException("Unable to access Baleen from pool", e);
    }
    return baleen;
  }

  private void returnBaleenInstance(EmbeddableBaleen baleen) {
    pool.returnObject(baleen);
  }
}
