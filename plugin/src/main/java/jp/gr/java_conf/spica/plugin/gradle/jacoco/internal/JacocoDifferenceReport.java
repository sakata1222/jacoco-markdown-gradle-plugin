package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class JacocoDifferenceReport implements JacocoCoverageRows {

  private final JacocoReport currentReport;
  private final JacocoReport previousReport;

  public JacocoDifferenceReport(
      JacocoReport currentReport,
      JacocoReport previousReport) {
    this.currentReport = currentReport;
    this.previousReport = previousReport;
  }

  @Override
  public List<JacocoCoverageRow> rows() {
    if (Objects.isNull(previousReport)) {
      return currentReport.rows();
    }
    Map<String, JacocoCoverage> previousSummary = previousReport.summary();
    return currentReport.summary()
        .entrySet()
        .stream()
        .map(
            entry -> new JacocoCoverageDifferenceRow(
                entry.getValue(),
                previousSummary.get(entry.getKey()))
        ).collect(Collectors.toList());
  }
}
