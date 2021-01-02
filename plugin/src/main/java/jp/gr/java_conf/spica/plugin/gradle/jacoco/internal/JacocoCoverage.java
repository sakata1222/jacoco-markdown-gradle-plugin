package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

import java.util.Locale;

public class JacocoCoverage implements JacocoCoverageRow {

  private final String type;
  private final int covered;
  private final int missed;

  public JacocoCoverage(String type, int covered, int missed) {
    this.type = type;
    this.covered = covered;
    this.missed = missed;
  }

  public String type() {
    return type;
  }

  @Override
  public String missedPerTotal() {
    return missed + "/" + total();
  }

  @Override
  public String coveragePercent() {
    return String.format(Locale.ENGLISH, "%.2f%%", coveragePercentAsFloat());
  }

  private int total() {
    return covered + missed;
  }

  public float coveragePercentAsFloat() {
    return (float) covered / total() * 100;
  }

  /* getters for json serialization */

  @SuppressWarnings("unused")
  public String getType() {
    return type;
  }

  @SuppressWarnings("unused")
  public int getCovered() {
    return covered;
  }

  @SuppressWarnings("unused")
  public int getMissed() {
    return missed;
  }
}
