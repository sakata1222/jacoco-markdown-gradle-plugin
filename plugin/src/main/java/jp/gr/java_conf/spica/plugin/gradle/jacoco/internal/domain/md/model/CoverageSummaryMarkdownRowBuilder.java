package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.Coverage;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model.CoverageDifference;

public class CoverageSummaryMarkdownRowBuilder {

  private CoverageSummaryMarkdownRowBuilder() {
  }

  public static MarkdownTableRow header() {
    return new MarkdownTableRow(Arrays.asList(
        MarkdownTableCol.header("Type"),
        MarkdownTableCol.header("Missed/Total"),
        MarkdownTableCol.header("Coverage"))
    );
  }

  public static List<MarkdownTableColAlignment> alignments() {
    return Arrays.asList(
        MarkdownTableColAlignment.LEFT,
        MarkdownTableColAlignment.RIGHT,
        MarkdownTableColAlignment.RIGHT
    );
  }

  public static MarkdownTableRow currentReport(Coverage current) {
    return new MarkdownTableRow(Arrays.asList(
        CoverageSummaryMarkdownColBuilder.currentOnly(current.getType()),
        CoverageSummaryMarkdownColBuilder.currentOnly(current.missedPerTotal()),
        CoverageSummaryMarkdownColBuilder.currentPercentageOnly(current.coveragePercentAsFloat()))
    );
  }

  public static MarkdownTableRow differenceReport(CoverageDifference difference) {
    Coverage previous = difference.getPrevious();
    Coverage current = difference.getCurrent();
    if (Objects.isNull(previous)) {
      return currentReport(current);
    }
    return new MarkdownTableRow(Arrays.asList(
        CoverageSummaryMarkdownColBuilder.currentOnly(current.getType()),
        CoverageSummaryMarkdownColBuilder
            .differenceCol(previous.missedPerTotal(), current.missedPerTotal()),
        CoverageSummaryMarkdownColBuilder.differencePercentageCol(
            previous.coveragePercentAsFloat(),
            current.coveragePercentAsFloat()))
    );
  }
}
