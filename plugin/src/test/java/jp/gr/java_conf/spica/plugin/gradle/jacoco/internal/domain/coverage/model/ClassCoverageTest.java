package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class ClassCoverageTest {

  @Test
  void branchMissedComparator() {
    ClassCoverage c1 = new ClassCoverage(
        new ClassName("foo.bar"),
        new Coverages(
            Arrays.asList(
                new Coverage(Coverages.BRANCH, 10, 2),
                new Coverage(Coverages.INSTRUCTION, 10, 1)
            )
        )
    );
    ClassCoverage c2 = new ClassCoverage(
        new ClassName("foo.bar2"),
        new Coverages(
            Arrays.asList(
                new Coverage(Coverages.BRANCH, 9, 1),
                new Coverage(Coverages.INSTRUCTION, 10, 1)
            )
        )
    );
    List<ClassCoverage> coverages = new ArrayList<>(Arrays.asList(c2, c1));
    coverages.sort(ClassCoverage.BRANCH_MISSED_COMPARATOR);
    assertThat(coverages).isEqualTo(Arrays.asList(c1, c2));
  }
}
