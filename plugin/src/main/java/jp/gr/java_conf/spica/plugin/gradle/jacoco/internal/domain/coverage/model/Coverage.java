package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Objects;

public class Coverage {

  private final String type;
  private final int covered;
  private final int missed;

  public Coverage(String type, int covered, int missed) {
    this.type = type;
    this.covered = covered;
    this.missed = missed;
  }

  public String missedPerTotal() {
    return missed + "/" + total();
  }

  private int total() {
    return covered + missed;
  }

  public float coveragePercentAsFloat() {
    return (float) covered / total() * 100;
  }

  public String getType() {
    return type;
  }

  public int getCovered() {
    return covered;
  }

  public int getMissed() {
    return missed;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coverage coverage = (Coverage) o;
    return covered == coverage.covered && missed == coverage.missed && Objects
        .equals(type, coverage.type);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, covered, missed);
  }
}
