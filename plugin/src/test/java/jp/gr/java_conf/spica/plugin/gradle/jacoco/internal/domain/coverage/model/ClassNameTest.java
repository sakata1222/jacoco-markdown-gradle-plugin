package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal.domain.coverage.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;

class ClassNameTest {

  @TestFactory
  Stream<DynamicTest> simpleName() {
    Map<String, String> canonicalToSimpleName = new LinkedHashMap<>();
    canonicalToSimpleName.put("hoge", "hoge");
    canonicalToSimpleName.put("hoge.foo", "foo");
    canonicalToSimpleName.put("hoge.foo.bar", "bar");
    return canonicalToSimpleName.entrySet().stream()
        .map(entry -> dynamicTest(
            "The simple name of " + entry.getKey() + " should be " + entry.getValue(),
            () -> assertThat(new ClassName(entry.getKey()).simpleName())
                .isEqualTo(entry.getValue())));
  }
}
