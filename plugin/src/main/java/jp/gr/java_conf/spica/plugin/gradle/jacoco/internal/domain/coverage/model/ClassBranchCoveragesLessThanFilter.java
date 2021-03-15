package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

public class ClassBranchCoveragesLessThanFilter implements IClassCoverageThreshold {

  private final boolean notFilter;
  private final double threshold;

  public ClassBranchCoveragesLessThanFilter(double threshold) {
    this.notFilter = threshold <= 0;
    this.threshold = threshold;
  }

  @Override
  public boolean lessThan(ClassCoverage coverage) {
    if (notFilter) {
      return true;
    }
    return coverage.getBranchCoverage()
        .map(Coverage::coveragePercentAsFloat)
        .map(percent -> percent < threshold)
        .orElse(false);
  }
}
