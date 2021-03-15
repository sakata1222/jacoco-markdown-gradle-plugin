package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class ClassListExportCondition {

  private final ClassCoverageLimit limit;
  private final ClassNameExcludeFilter excludeFilter;
  private final ClassBranchCoveragesLessThanFilter branchCoveragesThreshold;

  public ClassListExportCondition(
      ClassCoverageLimit limit,
      ClassNameExcludeFilter excludeFilter,
      ClassBranchCoveragesLessThanFilter branchCoveragesThreshold) {
    this.limit = limit;
    this.excludeFilter = excludeFilter;
    this.branchCoveragesThreshold = branchCoveragesThreshold;
  }

  public ClassCoverageLimit limit() {
    return limit;
  }

  public ClassNameExcludeFilter excludeFilter() {
    return excludeFilter;
  }

  public ClassBranchCoveragesLessThanFilter branchCoverageLessThan() {
    return branchCoveragesThreshold;
  }
}
