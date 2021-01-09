package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CustomCollectors {

  private CustomCollectors() {
  }

  public static <T, K, U> Collector<T, ?, Map<K, U>> toUniqueLinkedHashMap(
      Function<? super T, ? extends K> keyMapper,
      Function<? super T, ? extends U> valueMapper) {
    return Collectors.toMap(
        keyMapper,
        valueMapper,
        (v1, v2) -> {
          throw new IllegalArgumentException(
              "Duplicated keys are detected. Values of the duplicated key:" + v1 + ", " + v2);
        },
        LinkedHashMap::new
    );
  }
}
