package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;

class ClassNameExcludeFilterTest {

  private final List<ClassNameFilterString> filters = Arrays.asList(
      "foo.bar.MyExcludeClass",
      "/foo.bar.exclude.pkg.*/")
      .stream()
      .map(ClassNameFilterString::new).collect(Collectors.toList());
  private final Coverages coverages = new Coverages(Collections.emptyList());

  @Test
  void test_return_true() {
    ClassNameExcludeFilter filter = new ClassNameExcludeFilter(filters);
    assertThat(
        filter.test(new ClassCoverage(new ClassName("foo.bar.MyClass"), coverages)))
        .isTrue();
  }

  @Test
  void test_return_true_when_the_name_matches() {
    ClassNameExcludeFilter filter = new ClassNameExcludeFilter(filters);
    assertThat(
        filter.test(new ClassCoverage(new ClassName("foo.bar.MyExcludeClass"), coverages)))
        .isFalse();
  }

  @Test
  void test_return_true_when_the_name_partially_matches() {
    ClassNameExcludeFilter filter = new ClassNameExcludeFilter(filters);
    assertThat(
        filter.test(new ClassCoverage(new ClassName("foo.bar.MyExcludeClass1"), coverages)))
        .isTrue();
  }

  @Test
  void test_return_true_when_the_name_matches_regex() {
    ClassNameExcludeFilter filter = new ClassNameExcludeFilter(filters);
    assertThat(
        filter.test(new ClassCoverage(new ClassName("foo.bar.exclude.pkg.MyClass"), coverages)))
        .isFalse();
  }
}
