/**
 * @File:   RevStringProcess， 
 *
 * @Desc:   class to implement all the matching processes and methods 
 * 			including string preprocessing, local and global edit 
 * 			distances calculation and Ngram calculation. 
 *
 * @Author: Jianyu ZHU   734057
 * @LoginID:jianyuz 			
 */


import java.text.Normalizer;
import java.util.ArrayList;
import java.util.regex.Pattern;
import org.apache.lucene.search.spell.NGramDistance;

public class StringProcess {

	public static String normalize(String title) {
		// replace all accented words or letters,then remove all
		// punctuation and finally cast all letters to lower case
		title = deAccent(title);
		title = title.replaceAll("[^a-zA-Z0-9 ]", "").toLowerCase();
		return title;
	}

	public static String deAccent(String str) {
		// transfer all the accented letters to corresponding English letters
		String nfdNormalizedString = Normalizer.normalize(str,
			Normalizer.Form.NFD);
		Pattern pattern = Pattern
			.compile("\\p{InCombiningDiacriticalMarks}+");
		return pattern.matcher(nfdNormalizedString).replaceAll("");
	}

	public static String ExtractValued(String plain) {
		// remove all the words without useful information in matches
		plain = normalize(plain);
		plain = " " + plain;// in order to remove the useless words in the first
							// place in string
		for (int i = 0; i < 3; i++) {
			plain = plain.replaceAll(
				"(\\sthe\\s|\\sbe\\s|\\sto\\s|\\sof\\s|\\sand\\s|\\sa\\s|\\sin\\s|\\sthat\\s|\\shave\\s|\\si\\s|\\sit\\s|\\sfor\\s|\\snot\\s|\\son\\s|\\swith\\s|\\she\\s)",
				" ");
		}
		// remove the useless words in the last place in string
		plain = plain + " ";
		plain = plain.replaceAll(
			"(\\sthe\\s|\\sbe\\s|\\sto\\s|\\sof\\s|\\sand\\s|\\sa\\s|\\sin\\s|\\sthat\\s|\\shave\\s|\\si\\s|\\sit\\s|\\sfor\\s|\\snot\\s|\\son\\s|\\swith\\s|\\she\\s)",
			" ");
		// remove the space in the head or end of the string
		plain = plain.replaceAll("\\s+$|^\\s+", "");
		return plain;
	}

	/*-----------------------soundex transfer-----------------------------
	public static String Soundex(String plain) {
		// transfer all words to soundex form
		String[] soundex_tmp = plain.split("\\W+");
		//transfer word by word
		for (int k = 0; k < soundex_tmp.length; k++) {
			//transfer the string to char[], then transfer to
			//soundex format one by one
			char[] soundexChar = soundex_tmp[k].toCharArray();
			for (int i = 1; i < soundexChar.length; i++)
				switch (soundexChar[i]) {
				case 'a':
				case 'e':
				case 'h':
				case 'i':
				case 'o':
				case 'u':
				case 'w':
				case 'y':
					soundexChar[i] = '0';
					break;
				case 'b':
				case 'f':
				case 'p':
				case 'v':
					soundexChar[i] = '1';
					break;
				case 'c':
				case 'g':
				case 'j':
				case 'k':
				case 'q':
				case 's':
				case 'x':
				case 'z':
					soundexChar[i] = '2';
					break;
				case 'd':
				case 't':
					soundexChar[i] = '3';
					break;
				case 'l':
					soundexChar[i] = '4';
					break;
				case 'm':
				case 'n':
					soundexChar[i] = '5';
					break;
				case 'r':
					soundexChar[i] = '6';
					break;
				}
			//keep the first letter
			String soundexForOne = "" + soundexChar[0];
			for (int i = 1; i < soundexChar.length; i++)
				//remove '0's and repeated letters
				if (soundexChar[i] != soundexChar[i - 1]
					&& soundexChar[i] != '0')
					soundexForOne += soundexChar[i];
			//appending "0000" to maintain the size of 4 in soundex string
			soundexForOne += "0000";
			soundex_tmp[k] = soundexForOne.substring(0, 4);
		}
		String output = soundex_tmp[0];
		//put words back into a single string
		for (int i = 1; i < soundex_tmp.length; i++)
			output = output + " " + soundex_tmp[i];
		return output;
	}
	
	*/

	public static String Ngram(ArrayList<Titles> listOfTitles,
		String review, double thresholdForOne, double threshold) {
		String matchedTitle = new String();
		double NgramMark = 0;
		// split the valued content of review into single words
		String[] wordsInReview = ExtractValued(review).split("\\W+");
		// use NGramDistance object to calculate the Ngram
		NGramDistance a = new NGramDistance();
		for (int k = 0; k < listOfTitles.size(); k++) {
			Titles title = listOfTitles.get(k);
			// split the valued content of title into single words
			// calculate the ngram for each word in title
			String[] wordsInTitle = title.getVal().split("\\W+");
			int numOfWordsInTitle = wordsInTitle.length;
			double Ngram_tmp = 0;
			for (int i = 0; i < numOfWordsInTitle; i++) {
				float NgramForOne = 0;
				for (int j = 0; j < wordsInReview.length; j++) {
					float NgramForOne_tmp = a.getDistance(wordsInTitle[i],
						wordsInReview[j]);
					if (NgramForOne_tmp > thresholdForOne) // 0.8
						NgramForOne ++;
				}
				Ngram_tmp += NgramForOne;
			}
			// if the total ngram of the words in title is larger than
			// threshold, believe this title is a valued candidate.
			if (Ngram_tmp >= threshold) {
				if (Ngram_tmp > NgramMark) {
					NgramMark = Ngram_tmp;
					matchedTitle = listOfTitles.get(k).toString();
				}
				if (Ngram_tmp == NgramMark && matchedTitle
					.length() < listOfTitles.get(k).toString().length()) {
					matchedTitle = listOfTitles.get(k).toString();
				}
			}
		}
		return matchedTitle;
	}

	public static String LocalDist(ArrayList<Titles> listOfTitles,
		String review, double threshold) {
		double localDistMark = 0;
		String matchedTitle = new String();
		// extract the valued content in review
		String valuedContent = ExtractValued(review);
		for (int k = 0; k < listOfTitles.size(); k++) {
			Titles title = listOfTitles.get(k);
			// get valued content in title
			String valuedTitle = title.getVal();
			double localDis_tmp = 0;
			// calculate the local edit distance matrix for each title
			int[][] localEditDist = new int[valuedTitle.length()
				+ 1][valuedContent.length() + 1];
			for (int i = 0; i <= valuedTitle.length(); i++)
				localEditDist[i][0] = 0;
			for (int j = 1; j <= valuedContent.length(); j++)
				localEditDist[0][j] = 0;
			for (int i = 1; i <= valuedTitle.length(); i++)
				for (int j = 1; j <= valuedContent.length(); j++) {
					int deletion = localEditDist[i - 1][j] - 1;
					int insertion = localEditDist[i][j - 1] - 1;
					int matchOrReplace = localEditDist[i - 1][j - 1]
						+ equal(valuedTitle.charAt(i - 1),
							valuedContent.charAt(j - 1));
					localEditDist[i][j] = Math.max(
						Math.max(deletion, insertion),
						Math.max(matchOrReplace, 0));
				}
			// find the max local edit mark
			for (int i = 0; i <= valuedTitle.length(); i++)
				for (int j = 0; j <= valuedContent.length(); j++)
					if (localEditDist[i][j] > localDis_tmp)
						localDis_tmp = localEditDist[i][j];
			// if the edit distance of the title is larger than
			// threshold, believe this title is a valued candidate.
			if (localDis_tmp >= threshold) {
				if (localDis_tmp > localDistMark) {
					localDistMark = localDis_tmp;
					matchedTitle = listOfTitles.get(k).toString();
				}
				if (localDis_tmp == localDistMark && matchedTitle
					.length() < listOfTitles.get(k).toString().length()) {
					matchedTitle = listOfTitles.get(k).toString();
				}
			}
		}
		return matchedTitle;
	}

	static int equal(int i, int j) {
		if (i == j)
			return 1;
		else
			return -1;
	}

/*------------------------Global Edit Dist Match Method------------------------
		public static String GlobalDist(ArrayList<Titles> listOfTitles,
			String review, double threshold) {
			double globalDistMark = 0;
			String matchedTitle = new String();
			// LevensteinDistance a = new LevensteinDistance();
			String[] wordsInReview = Soundex(ExtractValued(review))
				.split("\\W+");
			// String[] wordsInReview = ExtractValued(review).split("\\W+");
			for (int k = 0; k < listOfTitles.size(); k++) {
				String title = listOfTitles.get(k).getSoundex();
				int numOfWordsInTitle = title.split("\\W+").length;
				double globalDist = 0;// dist for each word
				for (int i = 0; i < wordsInReview.length - numOfWordsInTitle
					+ 1; i++) {
					// cut into substrings with the same number of words in title
					String subString = wordsInReview[i];
					for (int j = 1; j < numOfWordsInTitle; j++) {
						subString += " " + wordsInReview[i + j];
					}
					// make global edit dist matrix
					int[][] globalEditDist = new int[title.length()
						+ 1][subString.length() + 1];
	
					for (int p = 0; p <= title.length(); p++)
						globalEditDist[p][0] = 0;
					for (int q = 1; q <= subString.length(); q++)
						globalEditDist[0][q] = 0;
					for (int p = 1; p <= title.length(); p++)
						for (int q = 1; q <= subString.length(); q++) {
							int deletion = globalEditDist[p - 1][q] - 1;
							int insertion = globalEditDist[p][q - 1] - 1;
							int matchOrReplace = globalEditDist[p - 1][q - 1]
								+ equal(title.charAt(p - 1),
									subString.charAt(q - 1));
							globalEditDist[p][q] = Math.max(
								Math.max(deletion, insertion), matchOrReplace);
						}
					// find the largest global edit dist in the matrix
					double globalDist_tmp = 0;// dist for substring
					for (int x = 0; x <= title.length(); x++)
						for (int y = 0; y <= subString.length(); y++)
							if (globalEditDist[x][y] > globalDist_tmp)
								globalDist_tmp = globalEditDist[x][y];
	
					if (globalDist_tmp > globalDist)
						globalDist = globalDist_tmp;
				}
				// same marks, select the longer one
				// if(globalDist == globalDistMark
				// && listOfTitles.get(k).toString().length() >
				// matchedTitle.length()
				// && globalDist > threshold){
				// matchedTitle = listOfTitles.get(k).toString();
				// }
				// greater marks, select the better one
				if (globalDist > globalDistMark && globalDist > threshold) {
					globalDistMark = globalDist;
					matchedTitle = listOfTitles.get(k).toString();
				}
			}
			return matchedTitle;
		}
--------------------------------exactMatch-------------------------------------
		public static ArrayList<Titles> exactMatch(
			ArrayList<Titles> listOfTitles, String review) {
			ArrayList<Titles> matchedTitles = new ArrayList<Titles>();
			for (int k = 0; k < listOfTitles.size(); k++) {
				Titles title = listOfTitles.get(k);
				if (review.contains(title.toString()))
					matchedTitles.add(title);
			}
			return matchedTitles;
		}
*/

}
