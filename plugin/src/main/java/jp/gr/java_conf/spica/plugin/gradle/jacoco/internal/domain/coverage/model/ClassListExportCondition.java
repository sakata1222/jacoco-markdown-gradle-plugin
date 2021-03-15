package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class ClassListExportCondition {

  private final ClassCoverageLimit limit;
  private final ClassNameExcludeFilter excludeFilter;

  public ClassListExportCondition(
      ClassCoverageLimit limit,
      ClassNameExcludeFilter excludeFilter) {
    this.limit = limit;
    this.excludeFilter = excludeFilter;
  }

  public ClassCoverageLimit limit() {
    return limit;
  }

  public ClassNameExcludeFilter excludeFilter() {
    return excludeFilter;
  }

}
