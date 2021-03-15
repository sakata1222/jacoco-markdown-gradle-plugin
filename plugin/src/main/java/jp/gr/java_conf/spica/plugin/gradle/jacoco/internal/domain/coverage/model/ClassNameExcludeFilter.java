package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ClassNameExcludeFilter implements Predicate<ClassCoverage> {

  private final List<IClassNameExcludeFilter> excludeFilters;

  public ClassNameExcludeFilter(
      List<ClassNameFilterString> excludeFilterStrings) {
    this.excludeFilters = excludeFilterStrings.stream()
        .map(ClassNameExcludeFilter::toFilter)
        .collect(Collectors.toList());
  }

  static IClassNameExcludeFilter toFilter(ClassNameFilterString filterString) {
    if (filterString.isRegex()) {
      return new ClassNameRegexExcludeFilter(filterString);
    }
    return new ClassNameEqualToExcludeFilter(filterString);
  }

  @Override
  public boolean test(ClassCoverage classCoverage) {
    return excludeFilters.stream().noneMatch(f -> f.matches(classCoverage.className()));
  }
}
