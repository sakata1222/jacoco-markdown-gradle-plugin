# jacoco-markdown-gradle-plugin

[![portal](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fgithub%2Fsakata1222%2Fjacoco-markdown%2Fcom.github.sakata1222.jacoco-markdown.gradle.plugin%2Fmaven-metadata.xml&label=Gradle+Plugin+Portal&logo=Gradle)](https://plugins.gradle.org/plugin/com.github.sakata1222.jacoco-markdown)

[![build](https://github.com/sakata1222/jacoco-markdown-gradle-plugin/workflows/Java%20CI%20with%20Gradle/badge.svg)](https://github.com/sakata1222/jacoco-markdown-gradle-plugin/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
[![codecov](https://codecov.io/gh/sakata1222/jacoco-markdown-gradle-plugin/branch/main/graph/badge.svg)](https://codecov.io/gh/sakata1222/jacoco-markdown-gradle-plugin)

[![sonar-reliability-rating](https://sonarcloud.io/api/project_badges/measure?project=sakata1222_jacoco-markdown-gradle-plugin&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=sakata1222_jacoco-markdown-gradle-plugin)
[![sonar-security-rating](https://sonarcloud.io/api/project_badges/measure?project=sakata1222_jacoco-markdown-gradle-plugin&metric=security_rating)](https://sonarcloud.io/dashboard?id=sakata1222_jacoco-markdown-gradle-plugin)
[![sonar-sqale-rating](https://sonarcloud.io/api/project_badges/measure?project=sakata1222_jacoco-markdown-gradle-plugin&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=sakata1222_jacoco-markdown-gradle-plugin)

Gradle plugin to parse jacoco report as a markdown.

```shell
$ ./gradlew build

> Task :example:jacocoTestReportMarkdown
|Type       | Missed/Total|            Coverage|
|:---       |         ---:|                ---:|
|INSTRUCTION|~~8/18~~ 8/24|    ~~55.56~~ 66.67%|
|BRANCH     |  ~~0/2~~ 0/4|(Not Changed)100.00%|
|LINE       |  ~~2/6~~ 2/8|    ~~66.67~~ 75.00%|
```

The markdown is output by a file, so you can put the coverage on a Pull Request by using some CI
tools. For Github actions, you can use [github-script](https://github.com/actions/github-script) to
read the markdown and put it to the PR.

Gradle versions:

- 6.x
- 5.6.4 (the latest 5.x)

Java versions:

- 8 or later

## Usage

### Apply plugin

This plugin depends on
the [jacoco plugin](https://docs.gradle.org/current/userguide/jacoco_plugin.html). So apply both the
jacoco plugin and this plugin.

```groovy
plugins {
    id 'java'
    id 'jacoco'
    id "com.github.sakata1222.jacoco-markdown" version "X.Y.Z"
}
```

See also
[Gradle plugin portal](https://plugins.gradle.org/plugin/com.github.sakata1222.jacoco-markdown) to
check the latest version.

### Configuration

First, configure the jacoco plugin based on the [jacoco plugin guide](
https://docs.gradle.org/current/userguide/jacoco_plugin.html).

#### Default task

**A task to output coverage report as a markdown will be created by default, and dependencies are
also configured automatically.**

Configuration of the default task:

- Name is `<name-of-default-jacoco-report>Markdown` (i.e. jacocoTestReportMarkdown)
- The task depends on a default JacocoReport task
- A default JacocoReport task finalizedBy the task
- A markdown file will be output in `<jacoco-report-directory>/jacocoSummary.md`

#### Define a new task

```groovy
task myJacocoMarkdown(type: jp.gr.java_conf.spica.plugin.gradle.jacoco.JacocoMarkdownTask) {
    jacocoReportTask your_JacocoReport_task // auto configuration for the JacocoReportTask
}
```

#### Customize

For default task:

```groovy
jacocoMarkdown {
    diffEnabled false // default true
    stdout false // default true
}
```

For a specific task:

```groovy
myJacocoMarkdown {
    jacocoXml file("path-to-jacoco-xml")
    diffEnabled false
    stdout false
    previousJson file("path-to-a-base-json-to-show-the-coverage-changes")
    targetTypes(["INSTRUCTION", "BRANCH", "LINE", "COMPLEXITY", "METHOD", "CLASS"])
    outputJson file("path-to-output-json")
    outputMd file("path-to-markdown")
}
```
