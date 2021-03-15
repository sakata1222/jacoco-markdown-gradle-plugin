package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class ClassNameFilterString {

  private final String filterString;

  public ClassNameFilterString(String filterString) {
    this.filterString = filterString;
  }

  public boolean isRegex() {
    if (filterString.isEmpty()) {
      return false;
    }
    return filterString.charAt(0) == '/' && filterString.charAt(filterString.length() - 1) == '/';
  }

  public String regex() {
    if (!isRegex()) {
      throw new IllegalStateException("");
    }
    return filterString.substring(1, filterString.length() - 1);
  }

  public String filterString() {
    return filterString;
  }
}
