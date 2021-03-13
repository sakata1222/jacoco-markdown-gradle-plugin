package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ClassCoverages {

  private final SortedMap<ClassName, ClassCoverage> classToCoverages;

  public ClassCoverages(
      Map<ClassName, Coverages> classToCoverages) {
    this.classToCoverages = new TreeMap<>(ClassName.CANONICAL_NAME_COMPARATOR);
    classToCoverages.forEach((k, v) ->
        this.classToCoverages.put(k, new ClassCoverage(k, v)));
  }

  public Map<String, Coverages> classCanonicalNameToCoveragesMap() {
    return new LinkedHashMap<>(classToCoverages.entrySet().stream()
        .collect(Collectors.toMap(
            entry -> entry.getKey().canonicalName(),
            entry -> entry.getValue().coverages()
        )));
  }

  public List<ClassCoverage> branchMissedWorstN(int n) {
    return classToCoverages.values().stream()
        .filter(ClassCoverage::hasMissedOnOC0C1)
        .sorted(ClassCoverage.BRANCH_MISSED_COMPARATOR)
        .limit(n)
        .collect(Collectors.toList());
  }
}
