package io.committed.baleen.embedded.components;

import java.io.InputStream;

public class InputDocument {

  private final String source;

  private final InputStream is;

  public InputDocument(final String source, final InputStream is) {
    this.source = source;
    this.is = is;
  }

  public InputStream getContentInputStream() {
    return is;
  }

  public String getSource() {
    return source;
  }
}
