package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;


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
}
