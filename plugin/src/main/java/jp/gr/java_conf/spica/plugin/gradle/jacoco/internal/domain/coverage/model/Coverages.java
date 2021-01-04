package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Coverages {

  private final Map<String, Coverage> coverageMap;

  public Coverages(List<Coverage> coverageList) {
    this((Map<String, Coverage>)
        coverageList.stream()
            .collect(Collectors.toMap(
                Coverage::getType,
                Function.identity(),
                (c1, c2) -> {
                  throw new IllegalArgumentException(c1 + ", " + c2);
                },
                LinkedHashMap::new
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
        .collect(Collectors.toMap(
            Map.Entry::getKey,
            e -> new CoverageDifference(
                previous.coverageMap.getOrDefault(e.getKey(), null),
                e.getValue()),
            (c1, c2) -> {
              throw new IllegalArgumentException(c1 + ", " + c2);
            },
            LinkedHashMap::new
        ));
    return new CoveragesDifference(typeToDifferences);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Coverages coverages = (Coverages) o;
    return Objects.equals(coverageMap, coverages.coverageMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(coverageMap);
  }
}
