package io.github.darvil82.terminal.textformatter.color;

import org.jetbrains.annotations.NotNull;
import io.github.darvil82.terminal.textformatter.TextFormatter;

/**
 * Enumerates the ANSI color codes that a terminal can normally display.
 */
public enum SimpleColor implements Color {
	BLACK(30),
	RED(31),
	GREEN(32),
	YELLOW(33),
	BLUE(34),
	MAGENTA(35),
	CYAN(36),
	WHITE(37),
	GRAY(90),
	BRIGHT_RED(91),
	BRIGHT_GREEN(92),
	BRIGHT_YELLOW(93),
	BRIGHT_BLUE(94),
	BRIGHT_MAGENTA(95),
	BRIGHT_CYAN(96),
	BRIGHT_WHITE(97);

	private final byte value;

	SimpleColor(int value) {
		this.value = (byte)value;
	}

	@Override
	public @NotNull String fg() {
		return TextFormatter.getSequence(this.value);
	}

	@Override
	public @NotNull String bg() {
		return TextFormatter.getSequence(this.value + 10);
	}

	@Override
	public @NotNull String toString() {
		return this.fg();
	}

	/** Array of all the bright colors. */
	public static final @NotNull SimpleColor[] BRIGHT_COLORS = {
		BRIGHT_RED,
		BRIGHT_GREEN,
		BRIGHT_YELLOW,
		BRIGHT_BLUE,
		BRIGHT_MAGENTA,
		BRIGHT_CYAN,
		BRIGHT_WHITE
	};

	/** Array of all the dark colors. */
	public static final @NotNull SimpleColor[] DARK_COLORS = {
		RED,
		GREEN,
		YELLOW,
		BLUE,
		MAGENTA,
		CYAN,
		WHITE
	};
}