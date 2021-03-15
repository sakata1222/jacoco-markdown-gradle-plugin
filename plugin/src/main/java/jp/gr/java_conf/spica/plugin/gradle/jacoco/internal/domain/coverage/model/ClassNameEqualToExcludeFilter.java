package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class ClassNameEqualToExcludeFilter implements IClassNameExcludeFilter {

  private final String excludeClassName;

  public ClassNameEqualToExcludeFilter(ClassNameFilterString excludeClassName) {
    this.excludeClassName = excludeClassName.filterString();
  }

  @Override
  public boolean matches(ClassName className) {
    return excludeClassName.equals(className.canonicalName());
  }
}
