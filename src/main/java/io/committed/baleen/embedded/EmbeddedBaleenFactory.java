package io.committed.baleen.embedded;

import io.committed.baleen.embedded.pool.BaleenPool;
import io.committed.baleen.embedded.single.EmbeddedBaleen;

import uk.gov.dstl.baleen.exceptions.BaleenException;

public class EmbeddedBaleenFactory {

  private EmbeddedBaleenFactory() {
    // Singleton
  }

  public static EmbeddableBaleen create(final String pipelineName, final int poolSize) {
    EmbeddableBaleen baleen;
    if (poolSize >= 1) {
      baleen = new BaleenPool(pipelineName, poolSize);
    } else {
      baleen = new EmbeddedBaleen(pipelineName);
    }

    return baleen;
  }

  public static EmbeddableBaleen createAndSetup(
      final String pipelineName, final String yaml, final int poolSize) throws BaleenException {
    final EmbeddableBaleen baleen = create(pipelineName, poolSize);

    try {
      baleen.setup(yaml);
    } catch (final IllegalStateException e) {
      // Leave this as a single exception type since we are simplifying usage
      throw new BaleenException(e);
    }

    return baleen;
  }
}
