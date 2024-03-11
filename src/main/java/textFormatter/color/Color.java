package textFormatter.color;

import org.jetbrains.annotations.NotNull;

public interface Color {
	/**
	 * Returns the ANSI escape sequence for this color for the text foreground.
	 * @return The ANSI escape sequence for this color.
	 */
	@NotNull String fg();

	/**
	 * Returns the ANSI escape sequence for this color for the text background.
	 * @return The ANSI escape sequence for this color.
	 */
	@NotNull String bg();

	/**
	 * Returns the ANSI escape sequence for this color for the text foreground.
	 * @return The ANSI escape sequence for this color.
	 * @see SimpleColor#fg()
	 */
	@NotNull String toString();
}