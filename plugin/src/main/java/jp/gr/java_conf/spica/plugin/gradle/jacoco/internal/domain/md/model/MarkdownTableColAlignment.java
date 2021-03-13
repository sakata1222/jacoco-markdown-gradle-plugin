package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import java.util.List;
import java.util.stream.Collectors;

public enum MarkdownTableColAlignment {
  LEFT(MarkdownTableCol.separatorAlinLeft()) {
    @Override
    String align(MarkdownTableCol col, int maxLength) {
      return col.alignLeft(maxLength);
    }
  },
  RIGHT(MarkdownTableCol.separatorAlinRight()) {
    @Override
    String align(MarkdownTableCol col, int maxLength) {
      return col.alignRight(maxLength);
    }
  };

  private final MarkdownTableCol separator;

  MarkdownTableColAlignment(
      MarkdownTableCol separator) {
    this.separator = separator;
  }

  abstract String align(MarkdownTableCol col, int maxLength);

  static MarkdownTableRow toRow(List<MarkdownTableColAlignment> alignments) {
    return new MarkdownTableRow(
        alignments.stream()
            .map(a -> a.separator)
            .collect(Collectors.toList()));
  }
}
