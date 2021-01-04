package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CoveragesDifference {

  private final Map<String, CoverageDifference> typeToDifferences;

  public CoveragesDifference(
      Map<String, CoverageDifference> typeToDifferences) {
    this.typeToDifferences = typeToDifferences;
  }

  public List<CoverageDifference> filter(CoverageTypes types) {
    return typeToDifferences.entrySet().stream()
        .filter(e -> types.contains(e.getKey()))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }
}
