import java.util.*;
import java.io.*;

public class Rapper {
	private static final Integer LEVEL = 2;
	private static final Boolean SHOW_INSTR = true;
	private static final Boolean REPORT_ON_SCREEN = true;
	private static final List<Character> allPinYin = new ArrayList<Character>();
	private static final Map<Character, Character> allPinYinInEnglish = new HashMap<Character, Character>();
	private static final Map<Character, List<Character>> aoeiuv = new HashMap<Character, List<Character>>();
	private static final Set<Character> allYuanYinYunMu = new TreeSet<Character>();
	private static final Set<String> allYunMu = new TreeSet<String>();
	private static final String TEST_STRING = "试一试";
	private static final String DICT = "cidian2.txt";
	private static final String REPORT_ALL = "report_all.txt";
	private static final List<String> uToV_ShengMu = Arrays.asList("ｊ", "ｑ", "ｘ");
	private static final List<String> uToV_u = Arrays.asList("ｕ", "ū", "ú", "ǔ", "ù");
	private static final List<String> uToV_v = Arrays.asList("ü", "ǖ", "ǘ", "ǚ", "ǜ");
	private static Map<Character, Set<String>> chars = new HashMap<Character, Set<String>>();
	private static Word wordsTree = new Word("MasterTree");
	public static void main(String[] args) throws FileNotFoundException {
		System.out.println("*****Starting application...");
		System.out.println("*****Building chars...");
		buildAllPinYin();
		buildChars(DICT);
		(new PrintStream(new File("chars.txt"))).println(chars);
		// test();
		System.out.println("*****Building words...");
		buildWords(DICT);
		System.out.println("*****Reporting all...");
		reportAll(REPORT_ALL);
		System.out.println("*****Entering user console...");
		reportUser(new Scanner(System.in));
		System.out.println("*****Ending application. Goodbye.");
	}
	
	private static void test() throws FileNotFoundException {
		String s = "1234";
		int index = 0;
		System.out.print(s.substring(0, index) + 'x' + s.substring(index + 1));
	}
	
	private static void buildAllPinYin() {
		allPinYin.add('ā');
		allPinYin.add('á');
		allPinYin.add('ǎ');
		allPinYin.add('à');
		allPinYin.add('ō');
		allPinYin.add('ó');
		allPinYin.add('ǒ');
		allPinYin.add('ò');
		allPinYin.add('ē');
		allPinYin.add('é');
		allPinYin.add('ě');
		allPinYin.add('è');
		allPinYin.add('ī');
		allPinYin.add('í');
		allPinYin.add('ǐ');
		allPinYin.add('ì');
		allPinYin.add('ū');
		allPinYin.add('ú');
		allPinYin.add('ǔ');
		allPinYin.add('ù');
		allPinYin.add('ü');
		allPinYin.add('ǖ');
		allPinYin.add('ǘ');
		allPinYin.add('ǚ');
		allPinYin.add('ǜ');
		allYuanYinYunMu.addAll(allPinYin);
		for (int i = 0; i < 26; i++) {
			allPinYin.add((char) ('ａ' + i));
			allPinYinInEnglish.put((char) ('a' + i), (char) ('ａ' + i));
		}
		allPinYinInEnglish.put('v', 'ü');
		
		allYuanYinYunMu.add('ａ');
		allYuanYinYunMu.add('ｏ');
		allYuanYinYunMu.add('ｅ');
		allYuanYinYunMu.add('ｉ');
		allYuanYinYunMu.add('ｕ');
		allPinYinInEnglish.put('（', '(');
		allPinYinInEnglish.put('）', ')');
		allPinYinInEnglish.put('，', ',');
		
		List<Character> list = new ArrayList<Character>();
		list.add('ａ');
		list.add('ā');
		list.add('á');
		list.add('ǎ');
		list.add('à');
		aoeiuv.put('ａ', list);
		
		list = new ArrayList<Character>();
		list.add('ｏ');
		list.add('ō');
		list.add('ó');
		list.add('ǒ');
		list.add('ò');
		aoeiuv.put('ｏ', list);
		
		list = new ArrayList<Character>();
		list.add('ｅ');
		list.add('ē');
		list.add('é');
		list.add('ě');
		list.add('è');
		aoeiuv.put('ｅ', list);
		
		list = new ArrayList<Character>();
		list.add('ｉ');
		list.add('ī');
		list.add('í');
		list.add('ǐ');
		list.add('ì');
		aoeiuv.put('ｉ', list);
		
		list = new ArrayList<Character>();
		list.add('ｕ');
		list.add('ū');
		list.add('ú');
		list.add('ǔ');
		list.add('ù');
		aoeiuv.put('ｕ', list);
		
		list = new ArrayList<Character>();
		list.add('ü');
		list.add('ǖ');
		list.add('ǘ');
		list.add('ǚ');
		list.add('ǜ');
		aoeiuv.put('ü', list);
	}
	
	private static void buildChars(String filename)
			throws FileNotFoundException {
		Scanner input = new Scanner(new File(filename));
		while (input.hasNextLine()) {
			String line = input.nextLine().trim();
			if (line.startsWith("*")) {
				char ch = line.charAt(1);
				String yunMu = getYunMu(line.substring(2));
				if (!chars.containsKey(ch)) {
					chars.put(ch, new HashSet<String>());
				}
				chars.get(ch).add(yunMu);
				allYunMu.add(yunMu);
			}
		}
		input.close();
	}
	
	private static String getYunMu(String line) {
		String yunMu = "";
		String shengMu = "";
		boolean started = false;
		boolean recording = false;
		for (int i = 0; i < line.length(); i++) {
			char ch = line.charAt(i);
			if (!allPinYin.contains(ch) && !started) {
				continue;
			} else if (allPinYin.contains(ch)) {
				started = true;
				if (recording) {
					yunMu += ch;
				} else if (!allYuanYinYunMu.contains(ch)) {
					shengMu += ch;
				} else {
					recording = true;
					yunMu += ch;
				}
			} else {
				break;
			}
		}
		if (uToV_ShengMu.contains(shengMu) && uToV_u.contains(yunMu)) {
			int index = uToV_u.indexOf(yunMu);
			yunMu = uToV_v.get(index);
		}
		return yunMu;
	}

	private static void buildWords(String filename) throws FileNotFoundException {
		Scanner input = new Scanner(new File(filename));
		while (input.hasNextLine()) {
			String line = input.nextLine().trim();
			if (line.startsWith("【") && line.contains("】")) {
				String word = line.substring(line.indexOf('【') + 1, line.indexOf('】'));
				Set<Word> currentWords = new HashSet<Word>();
				Set<Word> previousWords = new HashSet<Word>();
				previousWords.add(wordsTree);
				for (int i = word.length() - 1; i >= 0; i--) {
					char ch = word.charAt(i);
					Set<String> prons = chars.get(ch);
					if (prons != null) {
						for (Word cur : previousWords) {
							Word stash = cur;
							for (String pron : prons) {
								cur = stash;
								if (!cur.children.containsKey(pron)) {
									cur.children.put(pron, new Word(pron));
								}
								cur = cur.children.get(pron);
								cur.words.add(word);
								Word temp = cur;
								currentWords.add(temp);
							}
						}
					}
					previousWords = currentWords;
					currentWords = new HashSet<Word>();
				}
			}
		}
		input.close();
	}
	
	private static void reportAll(String filename) throws FileNotFoundException {
		PrintStream output = new PrintStream(new File(filename));
		if (LEVEL < 1 || LEVEL > 3) {
			output.println("Please adjust LEVEL to be 1, 2, 3 or 4.");
		}
		for (Word w1 : wordsTree.children.values()) {
			if (LEVEL == 1) {
				output.println(w1.yunjiao + ": " + w1.words + "\n");
			} else {
				for (Word w2 : w1.children.values()) {
					if (LEVEL == 2) {
						output.println(w2.yunjiao + ", " + w1.yunjiao + ": " + w2.words + "\n");
					} else {
						for (Word w3 : w2.children.values()) {
							if (LEVEL == 3) {
								output.println(w3.yunjiao + ", " + w2.yunjiao + ", " + w1.yunjiao + ": " + w3.words + "\n");
							} else {
								for (Word w4 : w3.children.values()) {
									if (LEVEL == 4) {
										output.println(w4.yunjiao + ", " + w3.yunjiao + ", " + w2.yunjiao + ", "
												+ w1.yunjiao + ": " + w4.words + "\n");
									}
								}
							}
						}
					}
				}
			}
		}
		output.close();
	}
	
	private static void reportUser(Scanner console) throws FileNotFoundException {
		System.out.print("Go to customized search? (y/n) ");
		if (console.nextLine().toLowerCase().startsWith("y")) {
			if (SHOW_INSTR) {
				System.out.println("====== Instructions ======");
				System.out.println("Format of Input: “韵母1(声调1,声调2) 韵母2(声调3,声调4) ...”, 韵母用空格隔开");
				System.out.println("  e.g.1 给“嘻哈”押声调双韵，Input: “i(1) a(1)”");
				System.out.println("  e.g.2 给“嘻哈”押全调双韵，Input: “i(1,2,3,4,0) a(1,2,3,4,0)” 或 “i a”");
				System.out.println("Use “v” as “ü” in “nü、lü、nüe、lüe、jü、qü、xü”");
				System.out.println("==========================");
			}
			while (true) {
				System.out.print("Enter desired 韵母 (q to quit): ");
				String inputLine = console.nextLine().trim();
				if (inputLine.startsWith("q")) {
					break;
				}
				PrintStream output = new PrintStream(new File("Custom_" + inputLine.replace(' ', '_') + ".txt"));
				
				List<List<String>> yunJiao = convertInputToUTF8Array(inputLine);
				output.println("待匹配韵脚：" + yunJiao + "\n");
				if (REPORT_ON_SCREEN) {
					System.out.println("待匹配韵脚：" + yunJiao + "\n");
				}
				
				if (yunJiao.size() == 1) {
					for (String yj : yunJiao.get(0)) {
						String result = "";
						for (Character ch : chars.keySet()) {
							if (chars.get(ch).contains(yj)) {
								result += ", " + ch;
							}
						}
						if (result.length() > 0) {
							output.println(yj + ": [" + result.substring(2) + "]");
							if (REPORT_ON_SCREEN) {
								System.out.println(yj + ": [" + result.substring(2) + "]");
							}
						}
					}
					continue;
				}
				
				Set<String> keys = new HashSet<String>();
				buildCustomKeys(keys, yunJiao, 0, "");
				Scanner allReports = new Scanner(new File(REPORT_ALL));
				while (allReports.hasNextLine()) {
					String line = allReports.nextLine();
					for (String key : keys) {
						if (line.contains(":") && line.substring(0, line.indexOf(":")).trim().equals(key)) {
							output.println(line);
							if (REPORT_ON_SCREEN) {
								System.out.println(line);
							}
						}
					}
				}
				allReports.close();
				output.close();
				output.println("==========================");
				if (REPORT_ON_SCREEN) {
					System.out.println("==========================");
				}
			}
		}
	}
	
	private static void buildCustomKeys(Set<String> result, List<List<String>> yunJiao,
			int yunJiaoIndex, String builtKey) {
		if (yunJiaoIndex < yunJiao.size()) {
			for (int i = 0; i < yunJiao.get(yunJiaoIndex).size(); i++) {
				String curKey = builtKey + ", " + yunJiao.get(yunJiaoIndex).get(i);
				if (yunJiaoIndex == yunJiao.size() - 1) {
					result.add(curKey.substring(2));
				} else {
					buildCustomKeys(result, yunJiao, yunJiaoIndex + 1, curKey);
				}
			}
		}
	}
	
	private static List<List<String>> convertInputToUTF8Array(String input) {
		List<List<String>> result = new ArrayList<List<String>>();
		String convertedInput = "";
		for (int i = 0; i < input.length(); i++) {
			char inputCh = input.charAt(i);
			if (allPinYinInEnglish.containsKey(inputCh)) {
				convertedInput += allPinYinInEnglish.get(inputCh);
			} else {
				convertedInput += inputCh;
			}
		}
		
		String[] eachYunMu = convertedInput.split(" ");
		for (int i = 0; i < eachYunMu.length; i++) {
			result.add(new ArrayList<String>());
		}
		for (int i = 0; i < eachYunMu.length; i++) {
			String yunMuInfo = eachYunMu[i];
			if (!yunMuInfo.contains("(")) {
				yunMuInfo += "(0,1,2,3,4)";
			}
			String yunMu = yunMuInfo.substring(0, yunMuInfo.indexOf('('));
			String yinDiaoInfo = yunMuInfo.substring(yunMuInfo.indexOf('(') + 1, yunMuInfo.indexOf(')'));
			String[] yinDiaos = yinDiaoInfo.trim().split(",");
			for (String yinDiao : yinDiaos) {
				yinDiao = yinDiao.trim();
				String wholeYunMu = biaoYin(yunMu, yinDiao.trim());
				if (wholeYunMu.contains("*")) {
					result.get(i).addAll(getAllYunMuWithYinDiao(yinDiao));
				} else {
					result.get(i).add(wholeYunMu);
				}
			}
		}
		return result;
	}
	
	private static Set<String> getAllYunMuWithYinDiao(String yinDiao) {
		Set<String> result = new TreeSet<String>();
		for (String yunMu : allYunMu) {
			if (getYinDiao(yunMu).equals(yinDiao)) {
				result.add(yunMu);
			}
		}
		return result;
	}
	
	private static String getYinDiao(String ym) {
		if (ym.contains("ā") || ym.contains("ō") || ym.contains("ē") || ym.contains("ī") || ym.contains("ū") || ym.contains("ǖ")) {
			return "1";
		} else if (ym.contains("á") || ym.contains("ó") || ym.contains("é") || ym.contains("í") || ym.contains("ú") || ym.contains("ǘ")) {
			return "2";
		} else if (ym.contains("ǎ") || ym.contains("ǒ") || ym.contains("ě") || ym.contains("ǐ") || ym.contains("ǔ") || ym.contains("ǚ")) {
			return "3";
		} else if (ym.contains("à") || ym.contains("ò") || ym.contains("è") || ym.contains("ì") || ym.contains("ù") || ym.contains("ǜ")) {
			return "4";
		} else {
			return "0";
		}
	}
	
	private static String biaoYin(String yunMu, String yinDiao) {
		Integer yinDiaoNum = Integer.parseInt(yinDiao);
		int index = -2;
		if (yunMu.contains("*")) {
			return "*" + yinDiao;
		} else if (yunMu.contains("ａ")) {
			index = yunMu.indexOf('ａ');
		} else if (yunMu.contains("ｏ")) {
			index = yunMu.indexOf('ｏ');
		} else if (yunMu.contains("ｅ")) {
			index = yunMu.indexOf('ｅ');
		} else if (yunMu.contains("ｉ") && yunMu.contains("ｕ")) {
			index = Math.max(yunMu.lastIndexOf('ｉ'), yunMu.lastIndexOf('ｕ'));
		} else if (yunMu.contains("ｉ") && yunMu.contains("ü")) {
			index = Math.max(yunMu.lastIndexOf('ｉ'), yunMu.lastIndexOf('ｕ'));
		} else if (yunMu.contains("ｉ")) {
			index = yunMu.indexOf('ｉ');
		} else if (yunMu.contains("ｕ")) {
			index = yunMu.indexOf('ｕ');
		} else if (yunMu.contains("ü")) {
			index = yunMu.indexOf('ü');
		}
		if (index >= 0) {
			char zhuYin = yunMu.charAt(index);
			zhuYin = aoeiuv.get(zhuYin).get(yinDiaoNum);
			return yunMu.substring(0, index) + zhuYin + yunMu.substring(index + 1);
		}
		return yunMu;
	}
}
