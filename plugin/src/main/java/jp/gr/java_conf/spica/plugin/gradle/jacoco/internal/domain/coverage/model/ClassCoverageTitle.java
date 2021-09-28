package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class ClassCoverageTitle {

  private final ClassCoverageLimit limit;

  public ClassCoverageTitle(ClassCoverageLimit limit) {
    this.limit = limit;
  }

  @Override
  public String toString() {
    return "Class list with less coverage"
        + (limit.hasLimit() ? " (Worst " + limit.limit() + ")" : "") + "\n";
  }
}
