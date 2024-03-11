import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import textFormatter.color.SimpleColor;
import textFormatter.FormatOption;
import textFormatter.TextFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextFormatterTest {
	private static void check(@NotNull String expected, @NotNull String actual) {
		System.out.printf("Expected : %s%nActual   : %s%n", expected, actual);
		assertEquals(expected, actual);
	}

	@Test
	public void testSimple() {
		var formatter = TextFormatter.of("Hello World").concat("!");
		check("Hello World!", formatter.toString());
	}

	@Test
	public void testSubFormatters() {
		var formatter = TextFormatter.of("Hello World")
			.concat("! ")
			.concat(
				TextFormatter.of("(Here is a sub-formatter")
					.concat(TextFormatter.of(" (with another").concat(" sub-formatter)"))
					.concat(")")
			)
			.concat("!");

		check("Hello World! (Here is a sub-formatter (with another sub-formatter))!", formatter.toString());
	}

	@Test
	public void testForegroundColoring() {
		var formatter = TextFormatter.of("red text here, ", SimpleColor.RED)
			.concat(
				TextFormatter.of("blue text here, ", SimpleColor.BLUE)
					.concat(TextFormatter.of("now yellow", SimpleColor.BRIGHT_YELLOW))
					.concat(" and back to blue")
			)
			.concat(". back to red");

		check(
			SimpleColor.RED + "red text here, " + SimpleColor.BLUE + "blue text here, " + SimpleColor.BRIGHT_YELLOW
				+ "now yellow" + SimpleColor.BLUE + " and back to blue" + SimpleColor.RED + ". back to red" + SimpleColor.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testBackgroundColoring() {
		var formatter = TextFormatter.of("red background here, ")
			.withBackgroundColor(SimpleColor.RED)
			.concat(
				TextFormatter.of("blue background here, ")
					.withBackgroundColor(SimpleColor.BLUE)
					.concat(
						TextFormatter.of("now yellow")
							.withBackgroundColor(SimpleColor.BRIGHT_YELLOW)
					)
					.concat(" and back to blue")
			)
			.concat(". back to red");

		check(
			SimpleColor.BRIGHT_WHITE + SimpleColor.RED.bg() + "red background here, " + SimpleColor.BLUE.bg()
				+ "blue background here, " + SimpleColor.BRIGHT_YELLOW.bg() + "now yellow" + SimpleColor.BLUE.bg()
				+ " and back to blue" + SimpleColor.RED.bg() + ". back to red" + FormatOption.RESET_ALL + SimpleColor.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testMiddleBackground() {
		var formatter = TextFormatter.of("yellow ", SimpleColor.BRIGHT_YELLOW)
			.concat(
				TextFormatter.of("blue ")
					.withBackgroundColor(SimpleColor.BLUE)
			)
			.concat(" and back to yellow");

		check(
			SimpleColor.BRIGHT_YELLOW + "yellow " + SimpleColor.BLUE.bg() + "blue " + FormatOption.RESET_ALL
				+ SimpleColor.BRIGHT_YELLOW + " and back to yellow" + SimpleColor.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testEmpty() {
		var formatter = TextFormatter.of("parent start. ", SimpleColor.RED)
			.concat(
				TextFormatter.of("red text here, ")
					.concat(TextFormatter.create().addFormat(FormatOption.ITALIC).concat("subsub"))
			).concat(" end str");

		check(
			SimpleColor.RED + "parent start. red text here, " + FormatOption.ITALIC + "subsub"
				+ FormatOption.ITALIC.reset() + " end str" + SimpleColor.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testStartWithDefault() {
		check(SimpleColor.BRIGHT_WHITE + "test", TextFormatter.of("test", SimpleColor.BRIGHT_WHITE).toString());
	}

	@Test
	public void testDisabledFormatting() {
		TextFormatter.enableSequences = false;
		var formatter = TextFormatter.of("red text here", SimpleColor.RED);

		check("red text here", formatter.toString());
		TextFormatter.enableSequences = true;
	}
}