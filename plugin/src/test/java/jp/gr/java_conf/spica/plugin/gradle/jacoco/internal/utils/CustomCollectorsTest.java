package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

class CustomCollectorsTest {

  @Test
  void toUniqueLinkedHashMap_collects_to_LinkedHashMap() {
    Map<String, String> expected = new LinkedHashMap<>();
    expected.put("aaa", "AAA");
    expected.put("bbb", "BBB");
    expected.put("ccc", "CCC");
    Map<String, String> collected = Stream.of("aaa", "bbb", "ccc")
        .collect(CustomCollectors.toUniqueLinkedHashMap(
            Function.identity(),
            s -> s.toUpperCase(Locale.ENGLISH)
        ));

    assertThat(collected)
        .isExactlyInstanceOf(LinkedHashMap.class)
        .containsExactlyEntriesOf(expected);
    assertThat(new ArrayList<>(collected.keySet()))
        .as("Keep order")
        .isEqualTo(Arrays.asList(
            "aaa", "bbb", "ccc"
        ));
  }

  @Test
  void toUniqueLinkedHashMap_throws_when_keys_are_duplicated() {
    AtomicInteger counter = new AtomicInteger(0);
    Function<String, String> keyMapper = Function.identity();
    Stream<String> stream = Stream.of("aaa", "bbb", "aaa");
    assertThatThrownBy(() ->
        stream.collect(CustomCollectors.toUniqueLinkedHashMap(
            keyMapper,
            s -> s.toUpperCase(Locale.ENGLISH) + counter.getAndIncrement()
        ))
    )
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessage("Duplicated keys are detected. Values of the duplicated key:AAA0, AAA2");
  }
}
