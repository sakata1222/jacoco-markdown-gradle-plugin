package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CoverageMarkdownCol {

  private final String col;

  private CoverageMarkdownCol(String col) {
    this.col = col;
  }

  public static CoverageMarkdownCol header(String colName) {
    return new CoverageMarkdownCol(colName);
  }

  public static CoverageMarkdownCol separatorAlinRight() {
    return new CoverageMarkdownCol("---:");
  }

  public static CoverageMarkdownCol separatorAlinLeft() {
    return new CoverageMarkdownCol(":---");
  }

  public static CoverageMarkdownCol currentOnly(String current) {
    return new CoverageMarkdownCol(current);
  }

  public static CoverageMarkdownCol currentPercentageOnly(float current) {
    return new CoverageMarkdownCol(formatPercent(current) + "%");
  }

  public static CoverageMarkdownCol differenceCol(String previous, String current) {
    if (previous.equals(current)) {
      return new CoverageMarkdownCol("(Not Changed)" + current);
    }
    return new CoverageMarkdownCol("~~" + previous + "~~ " + current);
  }

  public static CoverageMarkdownCol differencePercentageCol(float previous, float current) {
    if (previous == current) {
      return new CoverageMarkdownCol("(Not Changed)" + formatPercent(current) + "%");
    }
    return new CoverageMarkdownCol(
        "~~" + formatPercent(previous) + "~~ " + formatPercent(current) + "%");
  }

  private static String formatPercent(float percent) {
    return String.format(Locale.ENGLISH, "%.2f", percent);
  }

  int length() {
    return col.length();
  }

  String alignRight(int length) {
    verifyAlignLength(length);
    return padding(length) + col;
  }

  String alignLeft(int length) {
    verifyAlignLength(length);
    return col + padding(length);
  }

  private void verifyAlignLength(int length) {
    if (col.length() > length) {
      throw new IllegalArgumentException(col + " length is greater than align length:" + length);
    }
  }

  private String padding(int length) {
    int paddingSize = length - col.length();
    return IntStream.range(0, paddingSize).mapToObj(i -> " ").collect(Collectors.joining(""));
  }
}
