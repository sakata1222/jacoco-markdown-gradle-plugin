package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class CoverageMarkdownColTest {

  @Test
  void alignRight_throws_IllegalArg_when_length_is_less_than_col_length() {
    CoverageMarkdownCol col = CoverageMarkdownCol.currentOnly("aaa");
    assertThatThrownBy(() -> col.alignRight(2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("aaa length is greater than align length:2");
  }

  @Test
  void alignLeft_throws_IllegalArg_when_length_is_less_than_col_length() {
    CoverageMarkdownCol col = CoverageMarkdownCol.currentOnly("bbb");
    assertThatThrownBy(() -> col.alignLeft(2))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("bbb length is greater than align length:2");
  }
}
