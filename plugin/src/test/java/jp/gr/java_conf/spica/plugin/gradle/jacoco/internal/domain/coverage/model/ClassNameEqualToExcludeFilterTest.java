package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class ClassNameEqualToExcludeFilterTest {

  @Test
  void matches_return_true() {
    assertThat(new ClassNameEqualToExcludeFilter(new ClassNameFilterString("foo.bar.MyClass"))
        .matches(new ClassName("foo.bar.MyClass"))).isTrue();
  }

  @Test
  void matches_return_false_when_dot_part_does_not_matches() {
    assertThat(new ClassNameEqualToExcludeFilter(new ClassNameFilterString("foo.bar.MyClass"))
        .matches(new ClassName("fooAbar.MyClass"))).isFalse();
  }

}
