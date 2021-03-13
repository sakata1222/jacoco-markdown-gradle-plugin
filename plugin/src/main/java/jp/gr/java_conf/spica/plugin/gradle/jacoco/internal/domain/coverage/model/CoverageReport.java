package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class CoverageReport {

  private final CoverageSummary summary;
  private final ClassCoverages classToCoverages;

  public CoverageReport(
      CoverageSummary summary,
      ClassCoverages classToCoverages) {
    this.summary = summary;
    this.classToCoverages = classToCoverages;
  }

  public CoverageSummary summary() {
    return summary;
  }

  public ClassCoverages classToCoverages() {
    return classToCoverages;
  }
}
