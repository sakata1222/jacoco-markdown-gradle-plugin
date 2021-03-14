package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.List;
import java.util.Map;

public class CoverageSummary {

  private final Coverages summaryCoverages;

  public CoverageSummary(
      Coverages summaryCoverages) {
    this.summaryCoverages = summaryCoverages;
  }

  public CoverageSummary(List<Coverage> summary) {
    this(new Coverages(summary));
  }

  public Coverages coverages() {
    return summaryCoverages;
  }

  public Map<String, Coverage> typeToCoverage() {
    return summaryCoverages.typeToCoverage();
  }

  public CoveragesDifference diff(CoverageSummary other) {
    return coverages().diff(other.coverages());
  }

  public List<Coverage> filter(CoverageTypes targets) {
    return coverages().filter(targets);
  }
}
