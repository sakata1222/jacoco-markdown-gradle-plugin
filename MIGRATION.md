# Migration guide

## Preparing for Gradle 10

### What changes

Gradle 10 removes the Groovy DSL syntax that sets a property without `=`, such as
`diffEnabled false`. Gradle 8.14 and 9.x already print this deprecation warning for it:

```text
Properties should be assigned using the 'propName = value' syntax. Setting a property via the
Gradle-generated 'propName value' or 'propName(value)' syntax in Groovy DSL has been deprecated.
This is scheduled to be removed in Gradle 10.
```

Earlier versions of this README used that syntax in every example, so many build scripts that use
this plugin are affected.

### How to migrate

Add `=` between each property name and its value. Configuration blocks such as
`classListCondition { ... }` stay as they are.

Before:

```groovy
jacocoMarkdown {
    diffEnabled false
    stdout false
    classListEnabled false
    classListCondition {
        limit = 2
    }
}

myJacocoMarkdown {
    jacocoXml file("path-to-jacoco-xml")
    previousJson file("path-to-previous-json")
    targetTypes(["INSTRUCTION", "BRANCH", "LINE"])
    outputJson file("path-to-output-json")
    outputMd file("path-to-markdown")
}
```

After:

```groovy
jacocoMarkdown {
    diffEnabled = false
    stdout = false
    classListEnabled = false
    classListCondition {
        limit = 2
    }
}

myJacocoMarkdown {
    jacocoXml = file("path-to-jacoco-xml")
    previousJson = file("path-to-previous-json")
    targetTypes = ["INSTRUCTION", "BRANCH", "LINE"]
    outputJson = file("path-to-output-json")
    outputMd = file("path-to-markdown")
}
```

| Property                          | Before                       | After                          |
|:----------------------------------|:-----------------------------|:-------------------------------|
| `enabled`                         | `enabled false`              | `enabled = false`              |
| `diffEnabled`                     | `diffEnabled false`          | `diffEnabled = false`          |
| `stdout`                          | `stdout false`               | `stdout = false`               |
| `classListEnabled`                | `classListEnabled false`     | `classListEnabled = false`     |
| `jacocoXml` (task only)           | `jacocoXml file("a.xml")`    | `jacocoXml = file("a.xml")`    |
| `previousJson` (task only)        | `previousJson file("a.json")`| `previousJson = file("a.json")`|
| `targetTypes` (task only)         | `targetTypes(["LINE"])`      | `targetTypes = ["LINE"]`       |
| `enableOutputJson` (task only)    | `enableOutputJson false`     | `enableOutputJson = false`     |
| `enableOutputMd` (task only)      | `enableOutputMd false`       | `enableOutputMd = false`       |
| `outputJson` (task only)          | `outputJson file("a.json")`  | `outputJson = file("a.json")`  |
| `outputMd` (task only)            | `outputMd file("a.md")`      | `outputMd = file("a.md")`      |

`jacocoReportTask your_JacocoReport_task` is a method, not a property, so it does not need to
change.

The `=` syntax works on every Gradle version this plugin supports (7.3 or later), so you can
migrate now without upgrading Gradle.

### Compatibility of the old syntax

This plugin does not keep the old syntax working. On Gradle 10, build scripts that still use
`diffEnabled false` fail to configure, so migrate to `=` before upgrading Gradle.

To find the places to fix, run your build with `--warning-mode all` on Gradle 8.14 or later.
