package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClassCoverageTitleTest {

  @Test
  void toString_returns_worstN() {
    assertThat(new ClassCoverageTitle(new ClassCoverageLimit(3)))
        .hasToString("Class list with less coverage (Worst 3)\n");
  }

  @Test
  void toString_returns_without_limit() {
    assertThat(new ClassCoverageTitle(new ClassCoverageLimit(0)))
        .hasToString("Class list with less coverage\n");
  }
}
