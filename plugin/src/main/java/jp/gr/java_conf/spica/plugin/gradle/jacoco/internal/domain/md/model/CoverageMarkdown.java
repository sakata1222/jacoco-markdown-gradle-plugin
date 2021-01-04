package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CoverageMarkdown {

  private final List<CoverageMarkdownRow> rows;

  public CoverageMarkdown() {
    this.rows = new ArrayList<>();
    rows.add(CoverageMarkdownRow.header());
    rows.add(CoverageMarkdownRow.separator());
  }

  public void addRow(CoverageMarkdownRow row) {
    rows.add(row);
  }

  public String toMarkdown() {
    int[] maxColLengths = new int[3];
    for (int j = 0; j < maxColLengths.length; j++) {
      maxColLengths[j] = maxLengthOfNthCol(j);
    }
    return rows.stream()
        .map(row -> row.toFormattedMarkdownLine(maxColLengths))
        .collect(Collectors.joining("\n")) + "\n";
  }

  private int maxLengthOfNthCol(int n) {
    return rows.stream()
        .mapToInt(row -> row.getColLength(n))
        .max().orElseThrow(IllegalStateException::new);
  }
}
