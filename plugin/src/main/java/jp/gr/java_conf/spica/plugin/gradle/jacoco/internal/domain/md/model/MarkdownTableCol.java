package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarkdownTableCol {

  private final String col;

  public MarkdownTableCol(String col) {
    this.col = col;
  }

  public static MarkdownTableCol header(String header) {
    return new MarkdownTableCol(header);
  }

  public static MarkdownTableCol separatorAlinRight() {
    return new MarkdownTableCol("---:");
  }

  public static MarkdownTableCol separatorAlinLeft() {
    return new MarkdownTableCol(":---");
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
