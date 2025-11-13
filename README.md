 # Terminal Text Formatter

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.darvil82/terminal-text-formatter)](https://central.sonatype.com/artifact/io.github.darvil82/terminal-text-formatter)
[![APIdia](https://apidia.net/mvn/io.github.darvil82/terminal-text-formatter/badge.svg)](https://apidia.net/mvn/io.github.darvil82/terminal-text-formatter)

Text formatting utilities to easily format text in the terminal for Java.


## Usage Example

```java
var text = TextFormatter.of("blue text here, ", TrueColor.of(50, 50, 255))
   .addFormat(FormatOption.BOLD, FormatOption.ITALIC)
   .concat(TextFormatter.of("now yellow", SimpleColor.BRIGHT_YELLOW))
   .concat(" and back to blue");

System.out.println(text);
```

Javadocs for the latest stable version are available online hosted on [APIdia](https://apidia.net/mvn/io.github.darvil82/terminal-text-formatter)
and on [GitHub pages](https://darvil82.github.io/java-terminal-text-formatter).


## Installation

The package is currently available on Maven Central.

Add the following to your `dependencies` block:
```kotlin
implementation("io.github.darvil82:terminal-text-formatter:+")
```

> [!NOTE]
> The `+` symbol is a wildcard that will automatically use the latest version of the package.
> You can also specify a specific version.
