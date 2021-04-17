package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CoverageTest {

  private Coverage coverage;

  @BeforeEach
  void beforeEach() {
    coverage = new Coverage("branch", 10, 40);
  }

  @Test
  void missedPerTotal() {
    assertThat(coverage.missedPerTotalAndPercentage())
        .isEqualTo("40/50(20.00%)");
  }
}
