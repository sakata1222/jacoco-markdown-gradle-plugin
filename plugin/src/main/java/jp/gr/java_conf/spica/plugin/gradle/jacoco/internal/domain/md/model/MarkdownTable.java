package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MarkdownTable {

  private final List<MarkdownTableRow> rows;
  private final List<MarkdownTableColAlignment> alignments;

  MarkdownTable(MarkdownTableRow header, List<MarkdownTableColAlignment> alignments) {
    this.rows = new ArrayList<>();
    this.alignments = alignments;
    rows.add(header);
    rows.add(MarkdownTableColAlignment.toRow(alignments));
  }

  public void addRow(MarkdownTableRow tableRow) {
    rows.add(tableRow);
  }

  public String toMarkdown() {
    int[] maxColLengths = new int[3];
    for (int j = 0; j < maxColLengths.length; j++) {
      maxColLengths[j] = maxLengthOfNthCol(j);
    }
    return rows.stream()
        .map(row -> row.toFormattedMarkdownLine(alignments, maxColLengths))
        .collect(Collectors.joining("\n")) + "\n";
  }

  private int maxLengthOfNthCol(int n) {
    return rows.stream()
        .mapToInt(row -> row.getColLength(n))
        .max().orElseThrow(IllegalStateException::new);
  }
}
