package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class CoveragesTest {

  @Test
  void branchMissedComparator() {
    Coverages c1 = new Coverages(
        Arrays.asList(
            new Coverage(Coverages.BRANCH, 10, 2),
            new Coverage(Coverages.INSTRUCTION, 10, 1)
        )
    );
    Coverages c2 = new Coverages(
        Arrays.asList(
            new Coverage(Coverages.BRANCH, 11, 2),
            new Coverage(Coverages.INSTRUCTION, 10, 1)
        )
    );
    Coverages c3 = new Coverages(
        Arrays.asList(
            new Coverage(Coverages.BRANCH, 10, 1),
            new Coverage(Coverages.INSTRUCTION, 10, 1)
        )
    );
    Coverages c4 = new Coverages(
        Arrays.asList(
            new Coverage(Coverages.BRANCH, 10, 1),
            new Coverage(Coverages.INSTRUCTION, 10, 2)
        )
    );
    Coverages c5 = new Coverages(
        Arrays.asList(
            new Coverage(Coverages.BRANCH, 10, 1),
            new Coverage(Coverages.INSTRUCTION, 11, 2)
        )
    );

    List<Coverages> csList = new ArrayList<>(
        Arrays.asList(c4, c3, c5, c1, c2)
    );
    csList.sort(Coverages.BRANCH_MISSED_COMPARATOR);
    Assertions.assertThat(csList).isEqualTo(
        Arrays.asList(c1, c2, c4, c5, c3)
    );
  }
}
