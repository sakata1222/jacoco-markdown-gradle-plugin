# jacoco-markdown-gradle-plugin

[![build](https://github.com/sakata1222/jacoco-markdown-gradle-plugin/workflows/Java%20CI%20with%20Gradle/badge.svg)](https://github.com/sakata1222/jacoco-markdown-gradle-plugin/actions?query=workflow%3A%22Java+CI+with+Gradle%22)
[![codecov](https://codecov.io/gh/sakata1222/jacoco-markdown-gradle-plugin/branch/main/graph/badge.svg)](https://codecov.io/gh/sakata1222/jacoco-markdown-gradle-plugin)
[![portal](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fgithub%2Fsakata1222%2Fjacoco-markdown%2Fcom.github.sakata1222.jacoco-markdown.gradle.plugin%2Fmaven-metadata.xml&label=Gradle+Plugin+Portal&logo=Gradle)](https://plugins.gradle.org/plugin/com.github.sakata1222.jacoco-markdown)

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

**A task to output coverage report as a markdown will be created automatically, and dependencies are
also configured automatically.**

Configuration of an auto created task:

- Name is `<name-of-jacoco-report>Markdown`
- The task depends on a JacocoReport task
- A jacocoReport task finalizedBy the task
