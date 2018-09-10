package io.committed.baleen.embedded.pool;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;

import io.committed.baleen.embedded.EmbeddableBaleen;
import io.committed.baleen.embedded.single.EmbeddedBaleen;

public class BaleenPoolFactory implements PooledObjectFactory<EmbeddableBaleen> {

  private final String yaml;

  private final AtomicInteger nextId = new AtomicInteger(0);

  private final String pipelineName;

  public BaleenPoolFactory(final String pipelineName, final String yaml) {
    this.pipelineName = pipelineName;
    this.yaml = yaml;
  }

  @Override
  public PooledObject<EmbeddableBaleen> makeObject() throws Exception {
    final int id = nextId.incrementAndGet();
    final String name = String.format("%s-%d", pipelineName, id);
    final EmbeddableBaleen baleen = new EmbeddedBaleen(name);
    baleen.setup(yaml);
    return new DefaultPooledObject<>(baleen);
  }

  @Override
  public void destroyObject(final PooledObject<EmbeddableBaleen> o) {
    o.getObject().shutdown();
  }

  @Override
  public void activateObject(final PooledObject<EmbeddableBaleen> o) {
    // Do nothing
  }

  @Override
  public void passivateObject(final PooledObject<EmbeddableBaleen> o) {
    // Do nothing
    // TODO: Could attempt to break out of a for loops since (interrupt but not at a thread level)
  }

  @Override
  public boolean validateObject(final PooledObject<EmbeddableBaleen> o) {
    return true;
  }
}
