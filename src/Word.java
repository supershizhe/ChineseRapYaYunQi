import java.util.*;

public class Word {
	public String yunjiao;
	public Set<String> words;
	public Map<String, Word> children;
	public Word(Word w, String s, Integer i) { // dead constructor
		yunjiao = "ｂ";
	}
	public Word(String s) {
		yunjiao = s;
		words = new HashSet<String>();
		children = new HashMap<String, Word>();
	}

	public Word(String y, String w) {
		yunjiao = y;
		words = new HashSet<String>();
		words.add(w);
		children = new HashMap<String, Word>();
	}
}
