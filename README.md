# Terminal Text Formatter

Text formatting utilities to easily format text in the terminal for Java.


## Usage Example

```java
var text = TextFormatter.of("blue text here, ", TrueColor.of(50, 50, 255))
   .addFormat(FormatOption.BOLD, FormatOption.ITALIC)
   .concat(TextFormatter.of("now yellow", SimpleColor.BRIGHT_YELLOW))
   .concat(" and back to blue");

System.out.println(text);
```

Javadocs for the latest stable version are provided online [here](https://darvil82.github.io/java-terminal-text-formatter/).


## Installation

The package is currently available on Repsy and GitHub Packages.

1. Add the following to your `repositories` block:
   ```kotlin
   maven("https://repsy.io/mvn/darvil/java")
   ```

2. And add the following to your `dependencies` block:
   ```kotlin
   implementation("com.darvil:terminal-text-formatter:+")
   ```
> [!NOTE]
> The `+` symbol is a wildcard that will automatically use the latest version of the package.
> You can also specify a specific version (e.g. `0.0.1`).