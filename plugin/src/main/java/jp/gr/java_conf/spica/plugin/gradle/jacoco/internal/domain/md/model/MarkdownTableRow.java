package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MarkdownTableRow {

  private final List<MarkdownTableCol> cols;

  public MarkdownTableRow(
      List<MarkdownTableCol> cols) {
    this.cols = cols;
  }

  String toFormattedMarkdownLine(List<MarkdownTableColAlignment> alignmentList,
      int[] maxColLengths) {
    if (maxColLengths.length != cols.size() || alignmentList.size() != cols.size()) {
      throw new IllegalArgumentException(Arrays.toString(maxColLengths));
    }
    return IntStream.range(0, cols.size())
        .mapToObj(i -> alignmentList.get(i).align(cols.get(i), maxColLengths[i]))
        .collect(Collectors.joining("|", "|", "|"));
  }

  public int getColLength(int n) {
    return cols.get(n).length();
  }
}
