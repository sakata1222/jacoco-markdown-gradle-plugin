package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class MarkdownRowTest {

  @Test
  void toFormattedMarkdownLine_throws_IllegalArg() {
    MarkdownTableRow row = new MarkdownTableRow(
        Arrays.asList(
            new MarkdownTableCol("1"),
            new MarkdownTableCol("2"),
            new MarkdownTableCol("3"))
    );
    List<MarkdownTableColAlignment> list = Arrays.asList(
        MarkdownTableColAlignment.LEFT,
        MarkdownTableColAlignment.LEFT,
        MarkdownTableColAlignment.LEFT);
    assertThatThrownBy(() -> row.toFormattedMarkdownLine(
        list,
        new int[]{0, 0, 0, 4}))
        .isInstanceOf(IllegalArgumentException.class);
  }
}
