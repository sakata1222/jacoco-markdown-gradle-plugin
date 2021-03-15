package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Comparator;
import java.util.Optional;

public class ClassCoverage {

  public static final Comparator<ClassCoverage> BRANCH_MISSED_COMPARATOR = Comparator.comparing(
      c -> c.coverages,
      Coverages.BRANCH_MISSED_COMPARATOR
  );

  private final ClassName className;
  private final Coverages coverages;

  public ClassCoverage(
      ClassName className,
      Coverages coverages) {
    this.className = className;
    this.coverages = coverages;
  }

  public ClassName className() {
    return className;
  }

  public String classCanonicalName() {
    return className.canonicalName();
  }

  Coverages coverages() {
    return coverages;
  }

  public Optional<Coverage> getInstructionCoverage() {
    return coverages.getInstruction();
  }

  public Optional<Coverage> getBranchCoverage() {
    return coverages.getBranch();
  }

  public boolean hasMissedOnOC0C1() {
    return coverages.hasMissednOC0C1();
  }
}
