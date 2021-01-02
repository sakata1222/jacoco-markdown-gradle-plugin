package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JacocoMarkdown {

  private static final String LINE_SEPARATOR = "\n";

  private final List<Row> rows;

  public JacocoMarkdown() {
    this.rows = new ArrayList<>();
    rows.add(Row.of("Type", "Missed/Total", "Coverage"));
    rows.add(Row.of(":---", "---:", "---:"));
  }

  public void addRow(JacocoCoverageRow coverage) {
    rows.add(Row.of(
        coverage.type(),
        coverage.missedPerTotal(),
        coverage.coveragePercent()
        )
    );
  }

  public String toMarkdown() {
    int[] maxColLengths = new int[3];
    for (int j = 0; j < maxColLengths.length; j++) {
      maxColLengths[j] = maxLengthOfColumns(j);
    }

    List<String> lines =
        rows.stream()
            .map(row -> row.toString(maxColLengths))
            .collect(Collectors.toList());

    return lines.stream()
        .collect(Collectors.joining(LINE_SEPARATOR)) + LINE_SEPARATOR;
  }


  private int maxLengthOfColumns(int colNumber) {
    return rows.stream()
        .mapToInt(row -> row.getColLength(colNumber))
        .max().getAsInt();
  }

  private static class Row {

    private final List<Col> cols;

    static Row of(String... cols) {
      return new Row(
          Arrays.stream(cols)
              .map(Col::new)
              .collect(Collectors.toList())
      );
    }

    private Row(List<Col> cols) {
      this.cols = cols;
    }

    private int size() {
      return cols.size();
    }

    private int getColLength(int j) {
      return cols.get(j).length();
    }

    private String toString(int[] colLength) {
      return IntStream.range(0, colLength.length)
          .mapToObj(i -> {
            int length = colLength[i];
            return cols.get(i).leftPad(length);
          })
          .collect(Collectors.joining("|", "|", "|"));
    }
  }

  private static class Col {

    private final String col;

    Col(String col) {
      this.col = col;
    }

    private int length() {
      return col.length();
    }

    private String leftPad(int length) {
      if (col.length() > length) {
        return col;
      }
      int paddingSize = length - col.length();
      StringBuilder padding = new StringBuilder();
      IntStream.range(0, paddingSize).forEach(i -> padding.append(" "));
      return padding.toString() + col;
    }
  }
}
