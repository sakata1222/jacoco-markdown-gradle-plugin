package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils;

import groovy.lang.Closure;
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

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static <T> T configureSelf(Closure configureClosure, T target) {
    // org.gradle.util.ConfigureUtil is deprecated,
    // but org.gradle.util.internal.ConfigureUtil which is available in gradle 7.1 or later
    // is not deprecated
    try {
      return org.gradle.util.internal.ConfigureUtil.configureSelf(configureClosure, target);
    } catch (NoClassDefFoundError | NoSuchMethodError e) {
      // for gradle 6.x and 7.0
      try {
        return (T) Class.forName("org.gradle.util.ConfigureUtil")
            .getMethod("configureSelf", Closure.class, Object.class)
            .invoke(null, configureClosure, target);
      } catch (ReflectiveOperationException ex) {
        throw new RuntimeException(ex);
      }
    }
  }
}
