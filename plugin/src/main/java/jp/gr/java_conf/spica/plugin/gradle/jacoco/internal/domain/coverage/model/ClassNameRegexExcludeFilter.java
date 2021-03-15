package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.regex.Pattern;

public class ClassNameRegexExcludeFilter implements IClassNameExcludeFilter {

  private final Pattern excludePattern;

  public ClassNameRegexExcludeFilter(ClassNameFilterString excludePattern) {
    if (!excludePattern.isRegex()) {
      throw new IllegalArgumentException(
          "exclude pattern(" + excludePattern + ") should be a pattern string");
    }
    this.excludePattern = Pattern.compile(excludePattern.filterString());
  }

  @Override
  public boolean matches(ClassName className) {
    return excludePattern.matcher(className.canonicalName()).matches();
  }
}
