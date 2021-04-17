package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;


import java.util.Comparator;
import java.util.Locale;

public class Coverage {

  public static final Comparator<Coverage> MISSED_COMPARATOR = Comparator
      .comparingInt(Coverage::getMissed)
      .reversed()
      .thenComparingInt(Coverage::getCovered);

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

  public String missedPerTotalAndPercentage() {
    return String.format(Locale.ENGLISH,
        "%s(%.2f%%)", missedPerTotal(), coveragePercentAsFloat());
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
