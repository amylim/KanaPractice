package com.chasingkytes.kana.practice;

import java.util.Random;

/**
 * Objects that the user will interact with in the game. It is a bubble with 
 * a single Japanese character written on it. It has an x and y position
 * and can also be assigned a color. 
 * @author Amy Lim
 */
public class KanaBubble {

	final static int BLUE = 0;
	final static int GREEN = 1;
	final static int RED = 2;
	
	final static int HIRAGANA_MODE = 0;
	final static int KATAKANA_MODE = 1;
	final static int ROMAJI_MODE = 2;
	
	float x, y;
	String katakana;
	String hiragana;
	int kanaIndex;
	int color;
	final float radius = 50;
	
	final static String[] hiraganaList = {"あ", "い", "う", "え", "お", 
			"か", "き", "く", "け", "こ", 
			"さ", "し", "す", "せ", "そ", 
			"た", "ち", "つ", "て", "と", 
			"な", "に", "ぬ", "ね", "の", 
			"は", "ひ", "ふ", "へ", "ほ", 
			"ま", "み", "む", "め", "も", 
			"や", "ゆ", "よ", "わ", "ん", 
			"ら", "り", "る", "れ", "ろ", 
			"が", "ぎ", "ぐ", "げ", "ご", 
			"ざ", "じ", "ず", "ぜ", "ぞ", 
			"だ", "ぢ", "づ", "で", "ど", 
			"ば", "び", "ぶ", "べ", "ぼ", 
			"ぱ", "ぴ", "ぷ", "ぺ", "ぽ"};
	final static String[] katakanaList = {"ア", "イ", "ウ", "エ", "オ", 
		"カ", "キ", "ク", "ケ", "コ", 
		"サ", "シ", "ス", "セ", "ソ", 
		"タ", "チ", "ツ", "テ", "ト", 
		"ナ", "ニ", "ヌ", "ネ", "ノ", 
		"ハ", "ヒ", "フ", "ヘ", "ホ", 
		"マ", "ミ", "ム", "メ", "モ", 
		"ヤ", "ユ", "ヨ", "ワ", "ン", 
		"ラ", "リ", "ル", "レ", "ロ", 
		"ガ", "ギ", "グ", "ゲ", "ゴ", 
		"ザ", "ジ", "ズ", "ゼ", "ゾ", 
		"ダ", "ヂ", "ヅ", "デ", "ド", 
		"バ", "ビ", "ブ", "ベ", "ボ", 
		"パ", "ピ", "プ", "ペ", "ポ"};
	final static String[] romajiList = {"a", "i", "u", "e", "o", 
		"ka", "ki", "ku", "ke", "ko", 
		"sa", "shi", "su", "se", "so", 
		"ta", "chi", "tsu", "te", "to", 
		"na", "ni", "nu", "ne", "no", 
		"ha", "hi", "fu", "he", "ho", 
		"ma", "mi", "mu", "me", "mo", 
		"ya", "yu", "yo", "wa", "n", 
		"ra", "ri", "ru", "re", "ro", 
		"ga", "gi", "gu", "ge", "go", 
		"za", "ji", "zu", "ze", "zo", 
		"da", "di", "du", "de", "do",
		"ba", "bi", "bu", "be", "bo", 
		"pa", "pi", "pu", "pe", "po" };
	
	/**
	 * Creates a KanaBubble. Default constructor for KanaBubble 
	 */
	public KanaBubble() {
		x = 0;
		y = 0;
		setKana(0);	
		color = GREEN;
	}
	
	/**
	 * KanaBubble constructor
	 * @param index - <i>Integer that represents the index of the Kana in hiraganaList, katakanaList, and romajiList. Acceptable values are 0 to 70</i>
	 */
	public KanaBubble(int index) {
		x = 0;
		y = 0;
		setKana(index);	
		color = GREEN;
	}
	
	/**
	 * Obtains the x position of the KanaBubble
	 * @return float - <i>x position of KanaBubble</i>
	 */
	public float getX() {
		return this.x;
	}
	
	/**
	 * Obtains the y position of the KanaBubble
	 * @return float - <i>y position of the KanaBubble</i>
	 */
	public float getY() {
		return this.y;
	}
	
	/**
	 * Obtains the radius of the KanaBubble
	 * @return float - <i>radius of KanaBubble</i>
	 */
	public float getRadius() {
		return this.radius;
	}
	
	/**
	 * Obtains the Kana based off of the mode
	 * @param mode - <i>Valid modes are HIRAGANA_MODE, KATAKANA_MODE, and ROMAJI_MODE</i>
	 * @return String - <i>Kana based off the mode. If an invalid mode is placed in the parameter, it will return an empty string</i>
	 */
	public String getKana(int mode) {
		switch(mode) {
		case HIRAGANA_MODE:
			return getHiragana();
		case KATAKANA_MODE:
			return getKatakana();
		case ROMAJI_MODE:
			return getRomaji(kanaIndex);
		default:
			return "";
		}
	}
	
	/**
	 * Obtains the Hiragana from the KanaBubble
	 * @return String - <i>Hiragana</i>
	 */
	public String getHiragana() {
		return this.hiragana;
	}
	
	/**
	 * Obtains the Katakana from the KanaBubble
	 * @return String - <i>Katakana</i>
	 */
	public String getKatakana() {
		return this.katakana;
	}
	
	/**
	 * Obtains the kana index
	 * @return int - </i>kana index</i>
	 */
	public int getKanaIndex() {
		return this.kanaIndex;
	}
	
	/**
	 * Obtains the color of the KanaBubble
	 * @return int - <i>BLUE, GREEN, or RED</i>
	 */
	public int getColor() {
		return this.color;
	}
	
	/**
	 * Obtains the Romaji based off of the index
	 * @param index - <i>index of the Romaji in romajiList</i>
	 * @return String - <i>Romaji</i>
	 */
	public static String getRomaji(int index) {
		return romajiList[index];
	}
	
	/**
	 * Obtains the <i>kanaIndex</i> based off of the string input and the mode it is in
	 * @param str - <i>String of a kana</i>
	 * @param mode - <i>constants HIRAGANA_MODE or KATAKANA_MODE</i>
	 * @return int - <i>index of the str param</i>
	 */
	public int findKanaIndex(String str, int mode) {
		int index = 0;
		if(mode == HIRAGANA_MODE)
			while(index < 70 && !str.equals(hiraganaList[index]))
				index++;
		else if(mode == KATAKANA_MODE)
			while(index < 70 && !str.equals(katakanaList[index]))
				index++;
		return index;
	}
	
	/**
	 * Sets a random Kana for the KanaBubble.
	 */
	public void randomizeKana() {
		Random rand = new Random();
		kanaIndex = rand.nextInt(70);
		hiragana = hiraganaList[kanaIndex];
		katakana = katakanaList[kanaIndex];	
	}

	/**
	 * Sets the x-position and y-position of the KanaBubble.
	 * @param x
	 * @param y
	 */
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
//	public void setKana(String str, int mode) {
//		int index = findKanaIndex(str, mode);
//		if(index < 70) {
//			this.kanaIndex = index;
//			this.hiragana = hiraganaList[kanaIndex];
//			this.katakana = katakanaList[kanaIndex];
//		} else {
//			setKana(0);
//		}
//	}
	
	/**
	 * Sets the Kana of the KanaBubble based off of the index that it is given. The index must be between 0 and 70.
	 * @param index - <i>integer between 0 and 70</i>
	 */
	public void setKana(int index) {
		if(index < 70) {
			this.kanaIndex = index;
			this.hiragana = hiraganaList[kanaIndex];
			this.katakana = katakanaList[kanaIndex];
		} else if (hiragana == null) {
			setKana(0);
		}
	}
	
	/**
	 * Sets the color of the KanaBubble.
	 * @param color - <i>GREEN, BLUE, or RED</i>
	 */
	public void setColor(int color) {
		this.color = color;
	}
	
//	public boolean isInBubble(float x, float y) {
//		float minX = this.x;
//		float minY = this.y;
//		float maxX = this.x + this.radius*2;
//		float maxY = this.y + this.radius*2;
//		if(x >= minX && x <= maxX && y >= minY && y <= maxY)
//			return true;
//		return false;
//	}	
}
