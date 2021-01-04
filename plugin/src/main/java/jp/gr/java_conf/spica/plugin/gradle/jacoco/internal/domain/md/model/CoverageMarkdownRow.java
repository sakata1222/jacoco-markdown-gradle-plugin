package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageDifference;

public class CoverageMarkdownRow {

  private final List<CoverageMarkdownCol> cols;

  private final CoverageMarkdownCol type;
  private final CoverageMarkdownCol missedPerTotal;
  private final CoverageMarkdownCol coveragePercent;

  public CoverageMarkdownRow(
      CoverageMarkdownCol type,
      CoverageMarkdownCol missedPerTotal,
      CoverageMarkdownCol coveragePercent) {
    this.type = type;
    this.missedPerTotal = missedPerTotal;
    this.coveragePercent = coveragePercent;
    cols = Arrays.asList(type, missedPerTotal, coveragePercent);
  }

  public static CoverageMarkdownRow header() {
    return new CoverageMarkdownRow(
        CoverageMarkdownCol.header("Type"),
        CoverageMarkdownCol.header("Missed/Total"),
        CoverageMarkdownCol.header("Coverage")
    );
  }

  public static CoverageMarkdownRow separator() {
    return new CoverageMarkdownRow(
        CoverageMarkdownCol.separatorAlinLeft(),
        CoverageMarkdownCol.separatorAlinRight(),
        CoverageMarkdownCol.separatorAlinRight()
    );
  }

  public static CoverageMarkdownRow currentReport(Coverage current) {
    return new CoverageMarkdownRow(
        CoverageMarkdownCol.currentOnly(current.getType()),
        CoverageMarkdownCol.currentOnly(current.missedPerTotal()),
        CoverageMarkdownCol.currentPercentageOnly(current.coveragePercentAsFloat())
    );
  }

  public static CoverageMarkdownRow differenceReport(CoverageDifference difference) {
    Coverage previous = difference.getPrevious();
    Coverage current = difference.getCurrent();
    if (Objects.isNull(previous)) {
      return currentReport(current);
    }
    return new CoverageMarkdownRow(
        CoverageMarkdownCol.currentOnly(current.getType()),
        CoverageMarkdownCol.differenceCol(previous.missedPerTotal(), current.missedPerTotal()),
        CoverageMarkdownCol.differencePercentageCol(
            previous.coveragePercentAsFloat(),
            current.coveragePercentAsFloat())
    );
  }

  int getColLength(int n) {
    return cols.get(n).length();
  }

  String toFormattedMarkdownLine(int[] maxColLengths) {
    if (maxColLengths.length != cols.size()) {
      throw new IllegalArgumentException(Arrays.toString(maxColLengths));
    }
    return Stream.of(
        type.alignLeft(maxColLengths[0]),
        missedPerTotal.alignRight(maxColLengths[1]),
        coveragePercent.alignRight(maxColLengths[2])
    ).collect(Collectors.joining("|", "|", "|"));
  }
}
