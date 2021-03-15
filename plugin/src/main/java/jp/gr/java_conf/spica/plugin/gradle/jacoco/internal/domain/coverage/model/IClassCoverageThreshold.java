package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.function.Predicate;

public interface IClassCoverageThreshold extends Predicate<ClassCoverage> {

  boolean lessThan(ClassCoverage coverage);

  @Override
  default boolean test(ClassCoverage classCoverage) {
    return lessThan(classCoverage);
  }
}
