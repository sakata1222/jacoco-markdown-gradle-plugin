package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

public class JacocoMarkdownExtension {

  final Property<Boolean> diffEnabled;
  final Property<Boolean> stdout;

  @Inject
  public JacocoMarkdownExtension(ObjectFactory objectFactory) {
    this.diffEnabled = objectFactory.property(Boolean.class).convention(true);
    this.stdout = objectFactory.property(Boolean.class).convention(true);
  }

  @SuppressWarnings("unused")
  public void setDiffEnabled(boolean diffEnabled) {
    this.diffEnabled.set(diffEnabled);
  }

  @SuppressWarnings("unused")
  public void setStdout(boolean stdout) {
    this.stdout.set(stdout);
  }
}
