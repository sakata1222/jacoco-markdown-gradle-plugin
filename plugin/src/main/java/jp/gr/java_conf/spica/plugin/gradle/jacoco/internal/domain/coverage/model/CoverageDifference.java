package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Objects;

public class CoverageDifference {

  private final Coverage previous;
  private final Coverage current;

  public CoverageDifference(
      Coverage previous,
      Coverage current) {
    if (Objects.nonNull(previous)
        && !previous.getType().equals(current.getType())) {
      throw new IllegalArgumentException("Different types are specified");
    }
    this.previous = previous;
    this.current = current;
  }

  public Coverage getPrevious() {
    return previous;
  }

  public Coverage getCurrent() {
    return current;
  }
}
