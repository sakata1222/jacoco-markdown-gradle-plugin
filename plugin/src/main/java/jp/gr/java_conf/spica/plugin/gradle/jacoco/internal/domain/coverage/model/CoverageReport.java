package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Map;

public class CoverageReport {

  private final CoverageSummary summary;
  private final Map<String, Coverages> classToCoverages;

  public CoverageReport(
      CoverageSummary summary,
      Map<String, Coverages> classToCoverages) {
    this.summary = summary;
    this.classToCoverages = classToCoverages;
  }

  public CoverageSummary summary() {
    return summary;
  }

  public Map<String, Coverage> summaryTypeToCoverage() {
    return summary.typeToCoverage();
  }

  public Coverages summaryCoverages() {
    return summary.coverages();
  }

  public CoveragesDifference summaryDifference(CoverageSummary other) {
    return summary.diff(other);
  }
}
