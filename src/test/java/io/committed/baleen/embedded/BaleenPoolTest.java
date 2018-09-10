package io.committed.baleen.embedded;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;

import io.committed.baleen.embedded.example.OutputDocument;
import io.committed.baleen.embedded.example.SingleDocumentOutputConverter;
import io.committed.baleen.embedded.pool.BaleenPool;

import uk.gov.dstl.baleen.exceptions.BaleenException;

public class BaleenPoolTest {

  @Test
  public void testSequential() throws BaleenException {
    final BaleenPool baleen = new BaleenPool("testPool", 5);

    final String yaml = "annotators:\n  - regex.Email";

    baleen.setup(yaml);

    for (int i = 0; i < 10; i++) {
      final InputStream content = new ByteArrayInputStream("This is a text file".getBytes());

      final Optional<OutputDocument> optional =
          baleen.process("test_source", content, new SingleDocumentOutputConverter());

      final OutputDocument document = optional.get();

      assertEquals("This is a text file", document.getObject().get("content"));
      assertEquals("test_source", document.getObject().get("sourceUri"));
    }
  }

  @Test
  public void testThreaded() throws BaleenException, InterruptedException {
    final BaleenPool baleen = new BaleenPool("testPool", 5);

    final String yaml = "annotators:\n  - regex.Email";

    final int numRuns = 20;

    baleen.setup(yaml);

    final CountDownLatch latch = new CountDownLatch(numRuns);
    final AtomicInteger errors = new AtomicInteger(0);

    for (int i = 0; i < numRuns; i++) {
      new Thread(
              () -> {
                try {
                  final InputStream content =
                      new ByteArrayInputStream("This is a text file".getBytes());

                  final Optional<OutputDocument> optional =
                      baleen.process(
                          "test_source",
                          content,
                          (context, jCas) -> {
                            try {
                              Thread.sleep(1000);
                            } catch (final InterruptedException e) {
                              e.printStackTrace();
                            }
                            return new SingleDocumentOutputConverter().apply(context, jCas);
                          });

                  final OutputDocument document = optional.get();

                  assertEquals("This is a text file", document.getObject().get("content"));
                  assertEquals("test_source", document.getObject().get("sourceUri"));
                } catch (final Exception e) {
                  errors.incrementAndGet();
                  e.printStackTrace();
                } finally {
                  latch.countDown();
                }
              })
          .start();
    }

    latch.await();
    assertEquals(0, errors.get());
  }
}
