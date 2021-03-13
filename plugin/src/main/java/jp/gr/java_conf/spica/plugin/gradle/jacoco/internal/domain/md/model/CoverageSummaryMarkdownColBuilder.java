package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.Locale;

public class CoverageSummaryMarkdownColBuilder {

  private CoverageSummaryMarkdownColBuilder() {
  }

  public static MarkdownTableCol currentOnly(String current) {
    return new MarkdownTableCol(current);
  }

  public static MarkdownTableCol currentPercentageOnly(float current) {
    return new MarkdownTableCol(formatPercent(current) + "%");
  }

  public static MarkdownTableCol differenceCol(String previous, String current) {
    if (previous.equals(current)) {
      return new MarkdownTableCol("(Not Changed)" + current);
    }
    return new MarkdownTableCol("~~" + previous + "~~ " + current);
  }

  public static MarkdownTableCol differencePercentageCol(float previous, float current) {
    if (previous == current) {
      return new MarkdownTableCol("(Not Changed)" + formatPercent(current) + "%");
    }
    return new MarkdownTableCol(
        "~~" + formatPercent(previous) + "~~ " + formatPercent(current) + "%");
  }

  private static String formatPercent(float percent) {
    return String.format(Locale.ENGLISH, "%.2f", percent);
  }
}
