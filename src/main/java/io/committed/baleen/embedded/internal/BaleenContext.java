package io.committed.baleen.embedded.internal;

import uk.gov.dstl.baleen.uima.UimaMonitor;
import uk.gov.dstl.baleen.uima.UimaSupport;

public class BaleenContext {

  private final UimaMonitor monitor;

  private final UimaSupport support;

  public BaleenContext(final UimaMonitor monitor, final UimaSupport support) {
    this.monitor = monitor;
    this.support = support;
  }

  public UimaMonitor getMonitor() {
    return monitor;
  }

  public UimaSupport getSupport() {
    return support;
  }
}
