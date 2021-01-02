package jp.gr.java_conf.spica.plugin.gradle.jacoco.internal;

import groovy.util.Node;
import groovy.util.NodeList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class JacocoXmlReport implements JacocoReport, JacocoCoverageRows {

  private final Node root;

  public JacocoXmlReport(Node root) {
    this.root = root;
  }

  @Override
  public Map<String, JacocoCoverage> summary() {
    return counters().stream()
        .collect(Collectors.toMap(
            node -> node.get("@type").toString(),
            node -> new JacocoCoverage(
                node.get("@type").toString(),
                Integer.parseInt(node.get("@covered").toString()),
                Integer.parseInt(node.get("@missed").toString())
            ),
            (c1, c2) -> {
              throw new IllegalStateException();
            },
            LinkedHashMap::new
        ));
  }

  @Override
  public List<JacocoCoverageRow> rows() {
    return summary().values().stream().collect(Collectors.toList());
  }

  @SuppressWarnings("unchecked")
  private List<Node> counters() {
    return ((NodeList) root.get("counter"));
  }
}
