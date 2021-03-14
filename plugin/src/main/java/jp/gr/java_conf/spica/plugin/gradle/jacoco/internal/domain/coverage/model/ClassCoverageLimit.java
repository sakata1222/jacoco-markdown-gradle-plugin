package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class ClassCoverageLimit {

  private final int limit;

  public ClassCoverageLimit(int limit) {
    this.limit = limit;
  }

  public int limit() {
    if (noLimit()) {
      return Integer.MAX_VALUE;
    }
    return limit;
  }

  public boolean hasLimit() {
    return !noLimit();
  }

  public boolean noLimit() {
    return limit <= 0;
  }
}
