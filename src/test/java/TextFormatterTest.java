import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import textFormatter.Color;
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
		var formatter = new TextFormatter("Hello World").concat("!");
		check("Hello World!", formatter.toString());
	}

	@Test
	public void testSubFormatters() {
		var formatter = new TextFormatter("Hello World")
			.concat("! ")
			.concat(
				new TextFormatter("(Here is a sub-formatter")
					.concat(new TextFormatter(" (with another").concat(" sub-formatter)"))
					.concat(")")
			)
			.concat("!");

		check("Hello World! (Here is a sub-formatter (with another sub-formatter))!", formatter.toString());
	}

	@Test
	public void testForegroundColoring() {
		var formatter = new TextFormatter("red text here, ", Color.RED)
			.concat(
				new TextFormatter("blue text here, ", Color.BLUE)
					.concat(new TextFormatter("now yellow", Color.BRIGHT_YELLOW))
					.concat(" and back to blue")
			)
			.concat(". back to red");

		check(
			Color.RED + "red text here, " + Color.BLUE + "blue text here, " + Color.BRIGHT_YELLOW
				+ "now yellow" + Color.BLUE + " and back to blue" + Color.RED + ". back to red" + Color.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testBackgroundColoring() {
		var formatter = new TextFormatter("red background here, ")
			.withBackgroundColor(Color.RED)
			.concat(
				new TextFormatter("blue background here, ")
					.withBackgroundColor(Color.BLUE)
					.concat(
						new TextFormatter("now yellow")
							.withBackgroundColor(Color.BRIGHT_YELLOW)
					)
					.concat(" and back to blue")
			)
			.concat(". back to red");

		check(
			Color.BRIGHT_WHITE + Color.RED.bg() + "red background here, " + Color.BLUE.bg()
				+ "blue background here, " + Color.BRIGHT_YELLOW.bg() + "now yellow" + Color.BLUE.bg()
				+ " and back to blue" + Color.RED.bg() + ". back to red" + FormatOption.RESET_ALL + Color.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testMiddleBackground() {
		var formatter = new TextFormatter("yellow ", Color.BRIGHT_YELLOW)
			.concat(
				new TextFormatter("blue ")
					.withBackgroundColor(Color.BLUE)
			)
			.concat(" and back to yellow");

		check(
			Color.BRIGHT_YELLOW + "yellow " + Color.BLUE.bg() + "blue " + FormatOption.RESET_ALL
				+ Color.BRIGHT_YELLOW + " and back to yellow" + Color.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testEmpty() {
		var formatter = new TextFormatter("parent start. ", Color.RED)
			.concat(
				new TextFormatter("red text here, ")
					.concat(new TextFormatter().addFormat(FormatOption.ITALIC).concat("subsub"))
			).concat(" end str");

		check(
			Color.RED + "parent start. red text here, " + FormatOption.ITALIC + "subsub"
				+ FormatOption.ITALIC.reset() + " end str" + Color.BRIGHT_WHITE,
			formatter.toString()
		);
	}

	@Test
	public void testStartWithDefault() {
		check(Color.BRIGHT_WHITE + "test", new TextFormatter("test", Color.BRIGHT_WHITE).toString());
	}

	@Test
	public void testNewline() {
		check(
			Color.BRIGHT_YELLOW + "hello\n" + Color.BRIGHT_YELLOW + "world" + Color.BRIGHT_WHITE,
			new TextFormatter("hello\nworld", Color.BRIGHT_YELLOW).toString()
		);
	}
}
