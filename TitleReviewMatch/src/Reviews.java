/**		
 * @File:   Reviews£¬ object to represent each review file,including 
 * 			the file name and content.
 *
 * @Desc:   In this class, there are methods to set or get the titles
 *
 * @Author: Jianyu ZHU   734057
 * @LoginID:jianyuz 		
 */


import java.util.ArrayList;

public class Reviews {
	String fileName;
	String content;
	String titleForNgram;// title gotten by ngram analysis
	String titleForLocalDist;// title gotten by local edit distance analysis
	// String titleForGlobalDist;
	double NgramThreshold;
	double NgramThresholdForOne;
	double localDistThreshold;
	// double globalThreshold;

	public Reviews(String fileName, String review,
		ArrayList<Titles> listOfTitles) {
		this.fileName = fileName;
		content = review;
		// globalThreshold = 0;
		localDistThreshold = 0;
		NgramThresholdForOne = 0.8;
		NgramThreshold = 4;//6
		this.setNgramTitle(StringProcess.Ngram(listOfTitles, content,
			NgramThresholdForOne, NgramThreshold));
		this.setLocalDistTitle(StringProcess.LocalDist(listOfTitles,
			review, localDistThreshold));
	}

	public String getName() {
		return this.fileName;
	}

	public String toString() {
		// return the content of review
		return content;
	}

	void setNgramTitle(String string) {
		titleForNgram = string;
//		titleForNgram = new ArrayList<String>();
//		for (String s : string)
//			titleForNgram.add(s);
	}

	void setLocalDistTitle(String string) {
		titleForLocalDist = string; 
//		titleForLocalDist = new ArrayList<String>();
//		for (String s : string)
//			titleForLocalDist.add(s);
		
	}

	// void setGlobalDistTitle(String t){
	// this.titleForGlobalDist = t;
	// }

	String getNgramTitle() {
//		if(titleForNgram.isEmpty())
//			return "No match";
//		String output = new String("");
//		for (String s : titleForNgram)
//			output = output + "\n" + s;
//		return output;
		return this.titleForNgram;
	}

	String getLocalDistTitle() {
//		if(titleForLocalDist.isEmpty())
//			return "No match";
//		String output = new String(titleForLocalDist.get(0));
//		for (String s : titleForLocalDist)
//			output = output + "\n" + s;
//		return output;
		return this.titleForLocalDist;
	}

	// String getGlobalDistTitle(){
	// return this.titleForGlobalDist;
	// }

}
