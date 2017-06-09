/**
 * @File:   Match.java, the main class in the project.
 *
 * @Desc:   In this class, we read all the titles into the title list
 * 			and create review objects for each review file. To report 
 * 			the match result, we use the excel to restore all the match
 * 			outcomes, making it easier to have the whole view of 
 * 			different matching methods.
 *
 * @Author: Jianyu ZHU   734057
 * @LoginID:jianyuz 
 */

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class Match {

	public static void main(String[] args) throws IOException {
		@SuppressWarnings("resource")
		// use scanner to read all the titles in UTF-8 format
		Scanner inString = new Scanner(new File("film_titles.txt"),
			"UTF-8");
		String line;
		ArrayList<Titles> listOfTitles = new ArrayList<Titles>();
		// read all titles
		while (inString.hasNextLine()) {
			line = inString.nextLine();
			listOfTitles.add(new Titles(line));
		}

		////////////////////// start ///////////////////////////////

		// create an excel for result export
		WritableWorkbook book = Workbook
			.createWorkbook(new File("MatchResult.xls"));
		WritableSheet wealthSheet = book.createSheet("MatchResult", 0);
		try {
			// setup labels in the excel
			wealthSheet.addCell(new Label(0, 0, "FileName"));
			wealthSheet.addCell(new Label(1, 0, "Title For Ngram"));
			wealthSheet.addCell(new Label(2, 0, "Title For Local Dist"));

			int turn = 0;
			// read all reviews by using File object
			ArrayList<Reviews> listOfReviews = new ArrayList<Reviews>();
			File folder = new File("revs/");
			File[] listOfFile = folder.listFiles();
			// for each review file find its best matched title
			for (File file : listOfFile) {
				if (file.isFile() && file.getName().endsWith(".txt")) {
					listOfReviews.add(new Reviews(file.getName(),
						FileUtils.readFileToString(file), listOfTitles));
					// print the match results in the screen
					System.out.println(file.getName());
					System.out.println("Title for Ngram: " + listOfReviews
						.get(listOfReviews.size() - 1).getNgramTitle());
					System.out.println("Title for Local Distance: "
						+ listOfReviews.get(listOfReviews.size() - 1)
							.getLocalDistTitle());
				}
				// store results in the excel sheet
				turn++;
				wealthSheet.addCell(new Label(0, turn, file.getName()));
				wealthSheet.addCell(new Label(1, turn, listOfReviews
					.get(listOfReviews.size() - 1).getNgramTitle()));
				wealthSheet.addCell(new Label(2, turn, listOfReviews
					.get(listOfReviews.size() - 1).getLocalDistTitle()));
				// if(turn >= 20)
				// break;
			}

			// write excel and close
			book.write();
			book.close();

		} catch (RowsExceededException e) {
			e.printStackTrace();
		} catch (WriteException e) {
			e.printStackTrace();
		}

	}
}
