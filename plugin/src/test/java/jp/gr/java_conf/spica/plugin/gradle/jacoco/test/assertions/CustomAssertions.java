package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.ClassCoverages;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverages;

public class CustomAssertions {

  private CustomAssertions() {
  }

  public static CoveragesAssert assertThat(Coverages actual) {
    return new CoveragesAssert(actual);
  }

  public static CoverageSummaryAssert assertThat(CoverageSummary actual) {
    return new CoverageSummaryAssert(actual);
  }

  public static ClassToCoverageAssert assertThat(ClassCoverages actual) {
    return new ClassToCoverageAssert(actual);
  }

  public static CoverageReportAssert assertThat(CoverageReport actual) {
    return new CoverageReportAssert(actual);
  }
}
