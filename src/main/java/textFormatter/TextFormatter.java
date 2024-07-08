package textFormatter;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import textFormatter.color.Color;
import textFormatter.color.SimpleColor;
import utils.UtlString;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Allows easily formatting of text for it to be displayed in a terminal.
 * <p>
 * Multiple formatters can be concatenated together. This is useful for when you want to
 * format a string that has multiple parts that need to be formatted differently.
 * </p>
 */
public class TextFormatter {
	/**
	 * When set to {@code false}, no formatting will be applied to text. Raw text will be generated without any
	 * color or formatting.
	 * <p>
	 * This will be set to {@code false} if the environment variable {@code NO_COLOR} is set.
	 * @see #isColorDisabledEnv()
	 */
	public static boolean enableSequences = !TextFormatter.isColorDisabledEnv();

	/**
	 * The default color that should be used when no foreground color is specified (if {@link #startWithDefaultColorIfNotDefined}
	 * is set to {@code true}), or when the foreground color is reset.
	 */
	public static @NotNull Color defaultColor = SimpleColor.BRIGHT_WHITE;

	/**
	 * When set to {@code true}, the default color will be used when no foreground color is specified.
	 */
	public static boolean startWithDefaultColorIfNotDefined = true;


	/**
	 * When set to {@code true}, the {@link #toString()} method will not add any terminal sequences, but rather
	 * return the sequences that would be added by marking them as {@code ESC[<sequence here>]}
	 */
	public static boolean debug = false;

	/** A list of all the formatting options that should be applied to the text. */
	private final @NotNull ArrayList<FormatOption> formatOptions = new ArrayList<>(5);

	/** A list of all the values that should be concatenated to this formatter. */
	private final @NotNull List<Object> concatList = new ArrayList<>(0);

	/** The parent formatter. Used when being concatenated to another formatter. */
	private @Nullable TextFormatter parent;
	private @Nullable Color foregroundColor;
	private @Nullable Color backgroundColor;
	private @Nullable String concatGap;
	private @NotNull String contents;

	/**
	 * Creates a new {@link TextFormatter} with the specified contents.
	 * @param contents The contents of the formatter.
	 */
	protected TextFormatter(@NotNull String contents) {
		this.contents = contents;
	}

	/**
	 * Creates a new {@link TextFormatter} with the specified contents.
	 * @param contents The contents of the formatter.
	 */
	public static TextFormatter of(@NotNull String contents) {
		return new TextFormatter(contents);
	}

	/**
	 * Creates a new {@link TextFormatter} with the specified contents and foreground color.
	 * @param contents The contents of the formatter.
	 * @param foreground The foreground color of the formatter.
	 */
	public static TextFormatter of(@NotNull String contents, @NotNull Color foreground) {
		return TextFormatter.of(contents).withForegroundColor(foreground);
	}

	/**
	 * Creates a new {@link TextFormatter} with the specified contents and colors.
	 * @param contents The contents of the formatter.
	 * @param foreground The foreground color of the formatter.
	 * @param background The background color of the formatter.
	 */
	public static TextFormatter of(@NotNull String contents, @NotNull Color foreground, @NotNull Color background) {
		return TextFormatter.of(contents).withColors(foreground, background);
	}

	/**
	 * Creates a new {@link TextFormatter} with empty contents.
	 * @return a new {@link TextFormatter} with empty contents
	 */
	public static TextFormatter create() {
		return new TextFormatter("");
	}

	/**
	 * Returns a new {@link TextFormatter} with the specified contents and the error formatting.
	 * <p>
	 * The error formatting is a bold, black text with a bright red background.
	 * @param msg The contents of the formatter.
	 * @return a new {@link TextFormatter} with the specified contents and the error formatting
	 * */
	public static @NotNull TextFormatter error(@NotNull String msg) {
		return TextFormatter.of(msg, SimpleColor.BLACK, SimpleColor.BRIGHT_RED).addFormat(FormatOption.BOLD);
	}


	/**
	 * Adds the specified formatting options to the formatter.
	 * @param options The formatting options to add.
	 */
	public TextFormatter addFormat(@NotNull FormatOption... options) {
		this.formatOptions.addAll(Arrays.asList(options));
		return this;
	}

	/**
	 * Removes the specified formatting options from the formatter.
	 * @param options The formatting options to remove.
	 */
	public TextFormatter removeFormat(@NotNull FormatOption... options) {
		this.formatOptions.removeAll(Arrays.asList(options));
		return this;
	}

	/**
	 * Sets the foreground color of the formatter.
	 * @param foreground The foreground color of the formatter.
	 */
	public TextFormatter withForegroundColor(@Nullable Color foreground) {
		this.foregroundColor = foreground;
		return this;
	}

	/**
	 * Sets the background color of the formatter.
	 * @param background The background color of the formatter.
	 */
	public TextFormatter withBackgroundColor(@Nullable Color background) {
		this.backgroundColor = background;
		return this;
	}

	/**
	 * Sets the foreground and background color of the formatter.
	 * @param foreground The foreground color of the formatter.
	 * @param background The background color of the formatter.
	 */
	public TextFormatter withColors(@Nullable Color foreground, @Nullable Color background) {
		this.foregroundColor = foreground;
		this.backgroundColor = background;
		return this;
	}

	/**
	 * Sets the contents of the formatter.
	 * @param contents The contents of the formatter.
	 */
	public TextFormatter withContents(@NotNull String contents) {
		this.contents = contents;
		return this;
	}

	/**
	 * Sets the gap between concatenated formatters.
	 * By default, this is {@code null}, which means that no gap will be added.
	 * @param gap The gap between concatenated formatters.
	 */
	public TextFormatter withConcatGap(@Nullable String gap) {
		this.concatGap = gap;
		return this;
	}

	/**
	 * Concatenates the specified values to the formatter. Another {@link TextFormatter} may also be concatenated.
	 * @param objects The values to concatenate.
	 */
	public TextFormatter concat(@NotNull Object... objects) {
		for (var object : objects) {
			if (object instanceof TextFormatter tf) {
				this.concatFormatter(tf);
				continue;
			}

			this.concatList.add(object);
		}

		return this;
	}

	/**
	 * Properly concatenates the specified formatter to this formatter by also linking both formatters together.
	 * @param formatter The formatter to concatenate.
	 */
	private void concatFormatter(@NotNull TextFormatter formatter) {
		// if it was already added to another formatter, throw an exception
		if (formatter.parent != null)
			throw new IllegalArgumentException("Cannot concatenate a formatter that is already concatenated to another formatter.");

		formatter.parent = this;
		this.concatList.add(formatter);
	}

	/**
	 * Returns whether the formatter is simple. A formatter is simple if it has no formatting options, no foreground
	 * color, no background color, and no concatenated formatters.
	 * @return {@code true} if the formatter is simple
	 */
	public boolean isSimple() {
		return (this.contents.isEmpty() || this.isFormattingNotDefined())
			&& this.concatList.isEmpty(); // we cant skip if we need to concat stuff!
	}

	/**
	 * Returns whether the formatter has no formatting options, no foreground color, and no background color.
	 * @return {@code true} if the formatter has no formatting options, no foreground color, and no background color
	 */
	public boolean isFormattingNotDefined() {
		return (
			this.foregroundColor == null
				&& this.backgroundColor == null
				&& this.formatOptions.isEmpty()
		);
	}

	/**
	 * Returns the start sequences to add to the contents of the formatter. This includes the foreground color, the
	 * background color, and all the formatting options.
	 * @return the start sequences to add to the contents of the formatter
	 */
	private @NotNull String getStartSequences() {
		if (this.isFormattingNotDefined()) return "";
		final var buffer = new StringBuilder();

		if (this.foregroundColor != null)
			buffer.append(this.foregroundColor);
		else if (TextFormatter.startWithDefaultColorIfNotDefined && this.parent == null)
			buffer.append(TextFormatter.defaultColor);

		if (this.backgroundColor != null)
			buffer.append(this.backgroundColor.bg());

		for (var option : this.formatOptions) {
			buffer.append(option);
		}

		return buffer.toString();
	}

	/**
	 * Returns the end sequences to add to the contents of the formatter. This should properly reset the foreground
	 * color, the background color, and all the formatting options.
	 * @return the end sequences to add to the contents of the formatter
	 */
	private @NotNull String getEndSequences() {
		if (this.isFormattingNotDefined()) return "";
		final var buffer = new StringBuilder();
		final Runnable addResetFg = () -> {
			var resetFgColor = this.getResetFgColor();
			if (resetFgColor != null)
				buffer.append(resetFgColor);
		};

		if (this.backgroundColor != null) {
			var bgColor = this.getResetBgColor();

			// if bg color is null, we can just reset everything then.
			// make sure to reset the foreground color as well (RESET_ALL gets rid of everything)
			if (bgColor == null) {
				buffer.append(FormatOption.RESET_ALL);
				addResetFg.run();
				return buffer.toString();
			}

			buffer.append(bgColor.bg());
		}

		for (var option : this.formatOptions) {
			buffer.append(option.reset());
		}

		if (this.foregroundColor != null) {
			addResetFg.run();
		}

		return buffer.toString();
	}

	/**
	 * Returns the {@link Color} that should properly reset the foreground color. This is determined by looking at the
	 * parent formatters. If no parent formatter has a foreground color, then {@link SimpleColor#BRIGHT_WHITE} is returned.
	 * @return the {@link Color} that should properly reset the foreground color
	 */
	private @Nullable Color getResetFgColor() {
		if (this.parent == null) {
			if (this.foregroundColor != TextFormatter.defaultColor)
				return TextFormatter.defaultColor;
			return null;
		}

		if (this.parent.foregroundColor != null)
			return this.parent.foregroundColor;

		return this.parent.getResetFgColor();
	}

	/**
	 * Returns the {@link Color} that should properly reset the background color. This is determined by looking at the
	 * parent formatters. If no parent formatter has a background color, then {@code null} is returned.
	 * @return the {@link Color} that should properly reset the background color
	 */
	private @Nullable Color getResetBgColor() {
		if (this.parent == null)
			return null;

		if (this.parent.backgroundColor != null)
			return this.parent.backgroundColor;

		return this.parent.getResetBgColor();
	}

	/**
	 * Creates a new {@link String} with the contents and all the formatting applied.
	 */
	@Override
	public @NotNull String toString() {
		if ((!TextFormatter.enableSequences && this.concatList.isEmpty()) || this.isSimple()) {
			return this.contents;
		}

		final var buff = new StringBuilder();

		if (TextFormatter.enableSequences)
			buff.append(this.getStartSequences());

		buff.append(this.contents);

		// then do the same thing for the concatenated formatters
		{
			var concatList = this.concatList;

			for (int i = 0; i < concatList.size(); i++) {
				buff.append(concatList.get(i));

				// add the gap if it exists
				if (this.concatGap != null && i < concatList.size() - 1) {
					buff.append(this.concatGap);

					// if the gap contains a new line, then add the start sequences
					if (this.concatGap.contains("\n"))
						buff.append(this.getStartSequences());
				}
			}
		}

		if (TextFormatter.enableSequences)
			buff.append(this.getEndSequences());

		return buff.toString();
	}

	/**
	 * Returns a string with a terminal sequence with the specified values, separated by a semicolon.
	 * (e.g. {@code "ESC[<values here>m"})
	 * <p>
	 * If {@link #debug} is set to {@code true}, then the text "ESC" will be used instead of the actual escape
	 * character.
	 * </p>
	 * If {@link #enableSequences} is set to {@code false}, then an empty string will be returned.
	 * @param values The values to add to the terminal sequence.
	 * @return a string with a terminal sequence with the specified values
	 */
	public static @NotNull String getSequence(@NotNull Object... values) {
		if (!TextFormatter.enableSequences)
			return "";

		var joined = String.join(
			";",
			Arrays.stream(values)
				.map(Object::toString)
				.toArray(String[]::new)
		);

		if (TextFormatter.debug)
			return "ESC[" + joined + "]";
		return "" + UtlString.ESCAPE_CHAR + '[' + joined + 'm';
	}

	/**
	 * Returns whether there is an environment variable that specifies that
	 * the terminal does not support color.
	 * <a href="https://no-color.org/">NO_COLOR.org</a>
	 * @return {@code true} if the terminal supports color
	 */
	public static boolean isColorDisabledEnv() {
		return System.getenv("NO_COLOR") != null;
	}
}