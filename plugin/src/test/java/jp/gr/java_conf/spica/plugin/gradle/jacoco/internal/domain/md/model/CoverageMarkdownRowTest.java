package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CoverageMarkdownRowTest {

  @Test
  void toFormattedMarkdownLine_throws_IllegalArg() {
    CoverageMarkdownRow row = CoverageMarkdownRow.header();
    assertThatThrownBy(() -> row.toFormattedMarkdownLine(new int[]{0, 0, 0, 4}))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
