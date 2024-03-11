package textFormatter;

import org.jetbrains.annotations.NotNull;

public interface Color {
	@NotNull String fg();
	@NotNull String bg();
	@NotNull String toString();
}