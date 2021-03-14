package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClassCoverageTitleTest {

  @Test
  void toString_returns_worstN() {
    assertThat(new ClassCoverageTitle(new ClassCoverageLimit(3)).toString())
        .isEqualTo("Class list with less coverage (Worst 3)");
  }

  @Test
  void toString_returns_without_limit() {
    assertThat(new ClassCoverageTitle(new ClassCoverageLimit(0)).toString())
        .isEqualTo("Class list with less coverage");
  }
}
