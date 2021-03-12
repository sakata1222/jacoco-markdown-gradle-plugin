package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils.CustomCollectors;

public class Coverages {

  private final Map<String, Coverage> typeToCoverage;

  public Coverages(List<Coverage> coverageList) {
    this(coverageList.stream()
        .collect(CustomCollectors.toUniqueLinkedHashMap(
            Coverage::getType,
            Function.identity()
        ))
    );
  }

  public Coverages(Map<String, Coverage> typeToCoverage) {
    this.typeToCoverage = Collections.unmodifiableMap(typeToCoverage);
  }

  public Map<String, Coverage> typeToCoverage() {
    return typeToCoverage;
  }

  public List<Coverage> filter(CoverageTypes types) {
    return typeToCoverage.entrySet().stream()
        .filter(e -> types.contains(e.getKey()))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }

  public CoveragesDifference diff(Coverages previous) {
    Map<String, CoverageDifference> typeToDifferences = typeToCoverage.entrySet().stream()
        .collect(CustomCollectors.toUniqueLinkedHashMap(
            Map.Entry::getKey,
            e -> new CoverageDifference(
                previous.typeToCoverage.getOrDefault(e.getKey(), null),
                e.getValue())
        ));
    return new CoveragesDifference(typeToDifferences);
  }
}
