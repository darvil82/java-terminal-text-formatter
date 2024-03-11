import org.junit.jupiter.api.Test;
import textFormatter.FormatOption;
import textFormatter.TextFormatter;
import textFormatter.color.TrueColor;

public class TestColor {
	@Test
	public void testSimple(){
		var formatter = TextFormatter.of("test", TrueColor.CYAN);

		System.out.println(formatter);
	}
}