package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static java.util.Comparator.nullsLast;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils.CustomCollectors;

public class Coverages {

  static final String BRANCH = "BRANCH";
  static final String INSTRUCTION = "INSTRUCTION";

  // BEGIN LONG LINE
  public static final Comparator<Coverages> BRANCH_MISSED_COMPARATOR = Comparator.<Coverages, Coverage>comparing(
      c -> c.typeToCoverage.get(BRANCH),
      nullsLast(Coverage.MISSED_COMPARATOR)
  ).thenComparing(
      c -> c.typeToCoverage.get(INSTRUCTION),
      nullsLast(Coverage.MISSED_COMPARATOR)
  );
  // END LONG LINE

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

  public Optional<Coverage> getBranch() {
    return getByType(BRANCH);
  }

  public Optional<Coverage> getInstruction() {
    return getByType(INSTRUCTION);
  }

  private Optional<Coverage> getByType(String type) {
    return Optional.ofNullable(typeToCoverage.get(type));
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

  public boolean hasMissednOC0C1() {
    int c1Missed = getBranch().map(Coverage::getMissed).orElse(0);
    int c0Missed = getInstruction().map(Coverage::getMissed).orElse(0);
    return c0Missed + c1Missed > 0;
  }
}
