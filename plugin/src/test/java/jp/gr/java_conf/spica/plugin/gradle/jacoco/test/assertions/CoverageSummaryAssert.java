package jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions;

import static jp.gr.java_conf.spica.plugin.gradle.jacoco.test.assertions.CustomAssertions.assertThat;

import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageSummary;
import org.assertj.core.api.AbstractAssert;

public class CoverageSummaryAssert extends AbstractAssert<CoverageSummaryAssert, CoverageSummary> {

  CoverageSummaryAssert(
      CoverageSummary coverageSummary) {
    super(coverageSummary, CoverageSummaryAssert.class);
  }

  public CoverageSummaryAssert isEqualTo(CoverageSummary expected) {
    assertThat(actual.coverages())
        .isEqualTo(expected.coverages());
    return this;
  }
}
