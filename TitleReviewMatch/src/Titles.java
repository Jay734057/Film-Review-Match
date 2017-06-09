/**
 * @File:   Titles.java, the main class in the project£¬object to 
 * 			represent each title,including the original title, the
 * 			 normalized one and the one of valued content
 *
 * @Desc:   In this class, there are methods to get the different
 * 			content of the title
 *
 * @Author: Jianyu ZHU   734057
 * @LoginID:jianyuz 			
 */

public class Titles {
	String title;// the original title
	String normalizedTitle;// title processed by normalize function
	String ValuedContent;// the valued content in title
	//String soundexFormat;// soundex format title

	public Titles(String t) {
		title = t;
		normalizedTitle = StringProcess.normalize(title);
		ValuedContent = StringProcess.ExtractValued(title);
//		if (ValuedContent.isEmpty())
//			soundexFormat = new String();
//		else
//			soundexFormat = StringProcess.Soundex(ValuedContent);
	}

	String getVal() {
		return ValuedContent;
	}

	String getNor() {
		return normalizedTitle;
	}

//	String getSoundex() {
//		return soundexFormat;
//	}

	public String toString() {
		return title;
	}

}
