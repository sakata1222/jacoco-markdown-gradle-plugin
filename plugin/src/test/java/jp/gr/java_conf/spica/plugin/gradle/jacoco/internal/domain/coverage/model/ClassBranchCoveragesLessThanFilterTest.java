package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import org.junit.jupiter.api.Test;

class ClassBranchCoveragesLessThanFilterTest {

  // BEGIN LONG LINE
  private final ClassBranchCoveragesLessThanFilter filter80Percent = new ClassBranchCoveragesLessThanFilter(
      80);
  // END LONG LINE

  @Test
  void lessThan_returns_false_when_threshold_is_greater_than_threshold() {
    assertThat(filter80Percent.test(new ClassCoverage(
        new ClassName("MyClass"),
        new Coverages(Arrays.asList(new Coverage(Coverages.BRANCH, 81, 19)))))
    ).isFalse();
  }

  @Test
  void lessThan_returns_false_when_threshold_is_equal_to_threshold() {
    assertThat(filter80Percent.test(new ClassCoverage(
        new ClassName("MyClass"),
        new Coverages(Arrays.asList(new Coverage(Coverages.BRANCH, 80, 20)))))
    ).isFalse();
  }

  @Test
  void lessThan_returns_true_when_threshold_is_less_than_threshold() {
    assertThat(filter80Percent.test(new ClassCoverage(
        new ClassName("MyClass"),
        new Coverages(Arrays.asList(new Coverage(Coverages.BRANCH, 79, 21)))))
    ).isTrue();
  }

  @Test
  void lessThan_returns_always_true_when_threshold_is_zero() {
    ClassBranchCoveragesLessThanFilter filter = new ClassBranchCoveragesLessThanFilter(0);
    assertThat(filter.lessThan(null)).isTrue();
  }
}

