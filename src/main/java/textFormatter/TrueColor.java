package textFormatter;

import org.jetbrains.annotations.NotNull;

public class TrueColor implements Color {
	private final byte r, g, b;

	private TrueColor(byte r, byte g, byte b) {
		this.r = r;
		this.g = g;
		this.b = b;
	}

	public static @NotNull TrueColor of(int r, int g, int b) {
		return new TrueColor((byte)r, (byte)g, (byte)b);
	}

	public static @NotNull TrueColor of(int rgb) {
		return new TrueColor((byte)(rgb >> 16), (byte)(rgb >> 8), (byte)rgb);
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
		return TextFormatter.getSequence(fg ? 38 : 48, 2, this.r, this.g, this.b);
	}

	@Override
	public @NotNull String toString() {
		return this.fg();
	}
}