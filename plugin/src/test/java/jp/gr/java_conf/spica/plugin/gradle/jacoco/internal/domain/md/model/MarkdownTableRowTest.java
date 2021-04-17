package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.md.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarkdownTableRowTest {

  private MarkdownTableRow row;

  @BeforeEach
  void beforeEach() {
    row = new MarkdownTableRow(Arrays.asList(
        new MarkdownTableCol("aaa"),
        new MarkdownTableCol("bb"),
        new MarkdownTableCol("c")
    ));
  }

  @Test
  void toFormattedMarkdownLine() {
    assertThat(row.toFormattedMarkdownLine(
        Arrays.asList(
            MarkdownTableColAlignment.LEFT,
            MarkdownTableColAlignment.LEFT,
            MarkdownTableColAlignment.RIGHT),
        new int[]{
            5,
            4,
            3}
    )).isEqualTo("|aaa  |bb  |  c|");
  }

  @Test
  void toFormattedMarkdownLine_throws_Exception_when_alignmentList_size_is_invalid() {
    List<MarkdownTableColAlignment> alignmentList = Arrays.asList(
        MarkdownTableColAlignment.LEFT,
        MarkdownTableColAlignment.RIGHT);
    assertThatThrownBy(() ->
        row.toFormattedMarkdownLine(
            alignmentList,
            new int[]{
                5,
                4,
                3}
        )
    ).isInstanceOf(IllegalArgumentException.class);
  }

  @Test
  void toFormattedMarkdownLine_throws_Exception_when_length_size_is_invalid() {
    List<MarkdownTableColAlignment> alignmentList = Arrays.asList(
        MarkdownTableColAlignment.LEFT,
        MarkdownTableColAlignment.LEFT,
        MarkdownTableColAlignment.RIGHT);
    assertThatThrownBy(() ->
        row.toFormattedMarkdownLine(
            alignmentList,
            new int[]{
                4,
                3}
        )
    ).isInstanceOf(IllegalArgumentException.class);
  }
}
