package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageReport;
import org.assertj.core.api.AbstractAssert;

public class CoverageReportAssert extends AbstractAssert<CoverageReportAssert, CoverageReport> {

  public CoverageReportAssert(
      CoverageReport coverageReport) {
    super(coverageReport, CoverageReportAssert.class);
  }

  public CoverageReportAssert isEqualTo(CoverageReport expected) {
    new CoverageSummaryAssert(actual.summary()).isEqualTo(expected.summary());
    new ClassToCoverageAssert(actual.classToCoverages()).isEqualTo(expected.classToCoverages());
    return this;
  }
}
