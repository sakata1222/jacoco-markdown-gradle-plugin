package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils;

import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.provider.Provider;
import org.gradle.api.reporting.ConfigurableReport;
import org.gradle.api.reporting.Report;

public class GradleVersionSupport {

  private GradleVersionSupport() {
  }

  @SuppressWarnings("unchecked")
  public static Provider<? extends FileSystemLocation> getOutputLocation(
      ConfigurableReport configurableReport) {
    try {
      // https://github.com/sakata1222/jacoco-markdown-gradle-plugin/issues/66
      // signature is changed so call dynamic
      return (Provider<? extends FileSystemLocation>) Report.class.getMethod(
              "getOutputLocation")
          .invoke(configurableReport);
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}
