package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

public class CoverageSummaryMarkdownTable {

  private final MarkdownTable table;

  public CoverageSummaryMarkdownTable() {
    table = new MarkdownTable(
        CoverageSummaryMarkdownRowBuilder.header(),
        CoverageSummaryMarkdownRowBuilder.alignments());
  }

  public void addRow(MarkdownTableRow row) {
    table.addRow(row);
  }

  public String toMarkdown() {
    return table.toMarkdown();
  }
}
