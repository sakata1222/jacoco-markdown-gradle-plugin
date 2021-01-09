package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils.CustomCollectors;

public class Coverages {

  private final Map<String, Coverage> coverageMap;

  public Coverages(List<Coverage> coverageList) {
    this(coverageList.stream()
        .collect(CustomCollectors.toUniqueLinkedHashMap(
            Coverage::getType,
            Function.identity()
        ))
    );
  }

  public Coverages(Map<String, Coverage> coverageMap) {
    this.coverageMap = Collections.unmodifiableMap(coverageMap);
  }

  public Map<String, Coverage> typeToCoverage() {
    return coverageMap;
  }

  public List<Coverage> filter(CoverageTypes types) {
    return coverageMap.entrySet().stream()
        .filter(e -> types.contains(e.getKey()))
        .map(Map.Entry::getValue)
        .collect(Collectors.toList());
  }

  public CoveragesDifference diff(Coverages previous) {
    Map<String, CoverageDifference> typeToDifferences = coverageMap.entrySet().stream()
        .collect(CustomCollectors.toUniqueLinkedHashMap(
            Map.Entry::getKey,
            e -> new CoverageDifference(
                previous.coverageMap.getOrDefault(e.getKey(), null),
                e.getValue())
        ));
    return new CoveragesDifference(typeToDifferences);
  }
}
