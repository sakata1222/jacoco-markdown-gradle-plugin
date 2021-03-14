package jp.gr.java_conf.spica.plugin.gradle.jacoco;

import groovy.lang.Closure;
import javax.inject.Inject;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.Property;

public class JacocoMarkdownExtension {

  final Property<Boolean> enabled;
  final Property<Boolean> diffEnabled;
  final Property<Boolean> stdout;
  final Property<Boolean> classListEnabled;
  final Property<JacocoMarkdownClassListCondition> classListCondition;

  @Inject
  public JacocoMarkdownExtension(ObjectFactory objectFactory) {
    this.enabled = objectFactory.property(Boolean.class).convention(true);
    this.diffEnabled = objectFactory.property(Boolean.class).convention(true);
    this.stdout = objectFactory.property(Boolean.class).convention(true);
    this.classListEnabled = objectFactory.property(Boolean.class).convention(true);
    this.classListCondition = objectFactory.property(JacocoMarkdownClassListCondition.class)
        .convention(new JacocoMarkdownClassListCondition());
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

  public void setClassListCondition(Closure<JacocoMarkdownClassListCondition> closure) {
    classListCondition.set(new JacocoMarkdownClassListCondition().configure(closure));
  }
}
