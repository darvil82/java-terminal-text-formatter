package textFormatter.color;

import org.jetbrains.annotations.NotNull;
import textFormatter.TextFormatter;

/**
 * Represents a true color in the RGB color space.
 * <p>
 * <strong>Note:</strong> Support for true color is not universal. Expect this implementation to work in
 * most modern terminals, but not all.
 */
public class TrueColor implements Color {
	/** The color black. */
	public static final TrueColor BLACK = TrueColor.of(0, 0, 0);
	/** The color red. */
	public static final TrueColor RED = TrueColor.of(255, 0, 0);
	/** The color green. */
	public static final TrueColor GREEN = TrueColor.of(0, 255, 0);
	/** The color yellow. */
	public static final TrueColor YELLOW = TrueColor.of(255, 255, 0);
	/** The color blue. */
	public static final TrueColor BLUE = TrueColor.of(0, 0, 255);
	/** The color magenta. */
	public static final TrueColor MAGENTA = TrueColor.of(255, 0, 255);
	/** The color cyan. */
	public static final TrueColor CYAN = TrueColor.of(0, 255, 255);
	/** The color white. */
	public static final TrueColor WHITE = TrueColor.of(255, 255, 255);


	private final byte r, g, b;

	private TrueColor(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	/**
	 * Creates a new TrueColor object with the given RGB components.
	 * @param r The red component, in the range 0-255.
	 * @param g The green component, in the range 0-255.
	 * @param b The blue component, in the range 0-255.
	 * @return A new TrueColor object with the given RGB components.
	 * @throws IllegalArgumentException If any of the color components are not in the range 0-255.
	 */
	public static @NotNull TrueColor of(int r, int g, int b) {
		if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
			throw new IllegalArgumentException("Color components must be in the range 0-255");
		}
		return new TrueColor((byte)r, (byte)g, (byte)b);
	}

	/**
	 * Creates a new TrueColor object with the given RGB components packed into a single integer.
	 * @param rgb The RGB components packed into a single integer.
	 * @return A new TrueColor object with the given RGB components.
	 */
	public static @NotNull TrueColor of(int rgb) {
		return of((rgb >> 16) & 0xff, (rgb >> 8) & 0xff, rgb & 0xff);
	}

	/**
	 * Returns the red component of this color.
	 * @return The red component of this color.
	 */
	public byte r() {
		return this.r;
	}

	/**
	 * Returns the green component of this color.
	 * @return The green component of this color.
	 */
	public byte g() {
		return this.g;
	}

	/**
	 * Returns the blue component of this color.
	 * @return The blue component of this color.
	 */
	public byte b() {
		return this.b;
	}

	@Override
	public @NotNull String fg() {
		return this.getSequence(true);
	}

	@Override
	public @NotNull String bg() {
		return this.getSequence(false);
	}

	private @NotNull String getSequence(boolean fg) {
		return TextFormatter.getSequence(fg ? 38 : 48, 2, (this.r & 0xff), (this.g & 0xff), (this.b & 0xff));
	}

	@Override
	public @NotNull String toString() {
		return this.fg();
	}
}