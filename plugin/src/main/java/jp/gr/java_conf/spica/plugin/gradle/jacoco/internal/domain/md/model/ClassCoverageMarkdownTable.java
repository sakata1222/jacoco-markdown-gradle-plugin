package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

public class ClassCoverageMarkdownTable {

  private final MarkdownTable table;

  public ClassCoverageMarkdownTable() {
    this.table = new MarkdownTable(
        ClassCoverageMarkdownRowBuilder.header(),
        ClassCoverageMarkdownRowBuilder.alignments());
  }

  public void addRow(MarkdownTableRow row) {
    table.addRow(row);
  }

  public String toMarkdown() {
    return table.toMarkdown();
  }
}
