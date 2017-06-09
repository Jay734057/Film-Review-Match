----------------------------------------------------------------------
The Match of Titles and Reviews in JAVA

COMP90049 Knowledge Technologies

Project 1: Which films are good?

Authors: Jianyu Zhu 734057
----------------------------------------------------------------------

CONTENTS

  1. List of Files
  2. Running the JAVA project in Eclipse
  3. Export File
  4. Microsoft Excel Compatibility

======================================================================

1. List of Files

  Program files:    	
    	TitleReviewMatch folder - The whole JAVA project file.
    	lucene-spellchecker-3.6.1.jar - External JAR for string distance calculation.
    	commons-io-2.4.jar - External JAR for file reading.
    	jxl.jar - External JAR for excel reading and writing.
  Documentation:
    	Readme.txt - This document
    	Readme.html - An HTML version of this document.

======================================================================

2. Running the JAVA project in Eclipse
  1.Unzip the jianyuz-revs.tgz file and copy the unzipped folder into the TitleReviewMatch folder
  2.Copy the film_titles.txt file to the TitleReviewMatch folder
  3.Open Eclipse and import the project by selecting the TitleReviewMatch folder.
  4.Add external JARs(commons-io-2.4.jar, lucene-spellchecker-3.6.1.jar, jxl.jar) to the project.
  5.Click the run buttom.
  6.The output excel file is located int the TitleReviewMatch folder.
      
======================================================================
3. Export File

  An Excel form file located in the TitleReviewMatch folder and named "MatchResult.xls".
  It contains: File name, match results for Ngram distance and local edit distance.

======================================================================
4. Microsoft Excel Compatibility

  While jxl.jar has a high degree of compatibility with Microsoft Excel, there are a few          differences between the products.

  jxl.jar only supports  Microsoft Excel97 and above.

  jxl.jar currently does not support Macros(can keep it from template).
