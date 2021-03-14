package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Comparator;

public class ClassName {

  public static final Comparator<ClassName> CANONICAL_NAME_COMPARATOR = Comparator
      .comparing(c -> c.canonicalName);

  private final String canonicalName;

  public ClassName(String canonicalName) {
    this.canonicalName = canonicalName;
  }

  public String simpleName() {
    String[] split = canonicalName.split("\\.");
    return split[split.length - 1];
  }

  public String canonicalName() {
    return canonicalName;
  }
}
