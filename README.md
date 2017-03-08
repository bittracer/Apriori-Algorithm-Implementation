# Apriori-Algorithm-Implementation

----- How to run the code ?
	
	Go inside "assign3" directory and run the following command:

	Command: java -jar Apriori.jar

	The command executes the code and ask the user input.
	Note: Data Files are already there in the same directory. So, just enter the file names (E.g. data1)

	Output: It will generate the text file in the same directory named "Rules_For_data1.txt" if "data1" file is selected and similarly so on.

----- What is this code all about ?
 
	This code is an implementatin of the association rule mining
	algorithm(Apriori) in Java.

----- Overview & Structure of the program code in Eclipse IDE:

--- Project Structure

	> src

		> package com.assignment.apriori;
			> Apriori.java (Contains main() function)
			> FileReader.java
			> GenerateRules.java

		> package com.assignment.apriori.itemset;
			> FirstCandidateList.java
			> FrequentItemList.java
			> GenerateFrequentItemSet.java
			> NextCandidateList.java

		> package com.assignment.apriori.model;
			> RuleModel.java

--- Methods included in the Java files & the use of it
	
	> package com.assignment.apriori;

			> Apriori.java
				main() 				- Responsible for initiating the algorithm & asking for user input
				writeDataToFile() 	- Prints the data to the file
				getInputFromUser() 	- get the inputs from the user with validation
				getFileName() 		- Recursive file name function call if input file name is incorrect
				getCount() 			- Recursive Support Count & Confidence rate function call if input values are in incorrect format

			> FileReader.java
				readFile() 			- Reads the input from the file

			> GenerateRules.java
				splitRecursiveCombination()			- Generates the Subset from Item set
				generateRules()						- This function generate the rules & store it
				getRemainingItemsFromItemSet()		- It gives the remaining item set using the rule S -> (I-S), where S & I are non-empty subsets
				generateListToCalculateConfidence() - Combines all the frequent item set in a Map
				getFormattedData()					- Format the data to display it in beautify & readable manner

	> package com.assignment.apriori.itemset;

			> FirstCandidateList.java
				generateFirstCandidateList()		- Generates the first candidate list
				findUniqueItemSet()					- This function calculates the unique item sets from Map & other frequent item sets

			> FrequentItemList.java
				generateFrequentItemList() 			- Frequent item list by eliminating the subsets whose Support_Count < Minimum_support_count

			> GenerateFrequentItemSet.java
				getKCombination()					- Joins the ItemSet
				generateUniqueSets()				- Creates the List where unique items are filtered

			> NextCandidateList.java
				getCandidateList()					- This function will generate the candidate list from K=2 to N

	> package com.assignment.apriori.model;

			> RuleModel.java 						- Model class to store the output
