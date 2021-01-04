package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

public class CoverageTypes {

  private final Set<String> types;

  public CoverageTypes(Collection<String> types) {
    this.types = Collections.unmodifiableSet(new LinkedHashSet<>(types));
  }

  public boolean contains(String type) {
    return types.contains(type);
  }

}
