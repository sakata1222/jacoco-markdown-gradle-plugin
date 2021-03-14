package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

public class JacocoMarkdownExtension {

  final Property<Boolean> enabled;
  final Property<Boolean> diffEnabled;
  final Property<Boolean> stdout;
  final Property<Boolean> classListEnabled;
  final Property<Integer> classListLimit;

  @Inject
  public JacocoMarkdownExtension(ObjectFactory objectFactory) {
    this.enabled = objectFactory.property(Boolean.class).convention(true);
    this.diffEnabled = objectFactory.property(Boolean.class).convention(true);
    this.stdout = objectFactory.property(Boolean.class).convention(true);
    this.classListEnabled = objectFactory.property(Boolean.class).convention(true);
    this.classListLimit = objectFactory.property(Integer.class).convention(5);
  }

  public void setEnabled(boolean enabled) {
    this.enabled.set(enabled);
  }

  public void setDiffEnabled(boolean diffEnabled) {
    this.diffEnabled.set(diffEnabled);
  }

  public void setStdout(boolean stdout) {
    this.stdout.set(stdout);
  }

  public void setClassListEnabled(boolean classListEnabled) {
    this.classListEnabled.set(classListEnabled);
  }

  public void setClassListLimit(int classListLimit) {
    this.classListLimit.set(classListLimit);
  }
}
