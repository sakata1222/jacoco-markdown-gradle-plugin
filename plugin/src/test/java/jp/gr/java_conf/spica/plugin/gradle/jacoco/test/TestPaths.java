package jp.gr.java_conf.spica.plugin.gradle.jacoco.test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FileUtils;

public class TestPaths {

  private TestPaths() {
  }

  public static Path testData() {
    return Paths.get("testData");
  }

  public static Path temp() {
    return testData().resolve("temp");
  }

  public static void initTemp() {
    try {
      removeTemp();
      Files.createDirectory(temp());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }

  public static void removeTemp() {
    try {
      FileUtils.deleteDirectory(temp().toFile());
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
