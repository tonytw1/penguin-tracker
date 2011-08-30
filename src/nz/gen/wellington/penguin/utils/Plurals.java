package nz.gen.wellington.penguin.utils;

public class Plurals {

	public static String getPrural(String word, int count) {
		if (count != 1) {
			return word + "s";
		}
		return word;
	}

}
