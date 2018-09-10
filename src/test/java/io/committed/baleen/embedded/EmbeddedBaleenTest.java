package io.committed.baleen.embedded;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;

import org.junit.Test;

import io.committed.baleen.embedded.example.OutputDocument;
import io.committed.baleen.embedded.example.SingleDocumentOutputConverter;
import io.committed.baleen.embedded.single.EmbeddedBaleen;

import uk.gov.dstl.baleen.exceptions.BaleenException;

public class EmbeddedBaleenTest {

  @Test
  public void test() throws BaleenException {
    final EmbeddedBaleen baleen = new EmbeddedBaleen("test");

    final String yaml = "annotators:\n  - regex.Email";
    final InputStream content = new ByteArrayInputStream("This is a text file".getBytes());

    baleen.setup(yaml);
    final Optional<OutputDocument> optional =
        baleen.process("test_source", content, new SingleDocumentOutputConverter());

    final OutputDocument document = optional.get();

    assertEquals("This is a text file", document.getObject().get("content"));
    assertEquals("test_source", document.getObject().get("sourceUri"));
  }
}