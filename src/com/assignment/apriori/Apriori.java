package com.assignment.apriori;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import com.assignment.apriori.itemset.FirstCandidateList;
import com.assignment.apriori.itemset.FrequentItemList;
import com.assignment.apriori.itemset.GenerateFrequentItemSet;
import com.assignment.apriori.itemset.NextCandidateList;
import com.assignment.apriori.model.RuleModel;

/**
 * @author bharatjain
 * @machine Mac OS Sierra (10.12.3)
 */
public class Apriori {

	// This will Store the details for each and every Candidate Item set
	public static Map<Integer, Map<String, Float>> candidateList = new HashMap<Integer, Map<String, Float>>();

	// This will Store the details for each and every Frequent Item set
	public static Map<Integer, Map<String, Float>> frequentItemSet = new HashMap<Integer, Map<String, Float>>();

	static String filename = "";
	static int k = 1;
	static float minSupportCount;
	static float minConfidence;
	static List<String> _fileList = new ArrayList<String>();

	static Scanner in = new Scanner(System.in);

	/**
	 * @param ar
	 */
	public static void main(String ar[]) {

		try {

			// Add the file names to the list
			_fileList.add("data1");
			_fileList.add("data2");
			_fileList.add("data3");

			// Get User Input
			getInputFromUser();

			// Read the file in appropriate format
			FileReader.readFile(filename);

			// Read Column vice data (Column)
			Map<String, List<String>> _Columnlist = FileReader.columnList;

			// Find 1st Candidate List
			Map<String, Float> _cList = FirstCandidateList.generateFirstCandidateList(_Columnlist);
			candidateList.put(k, _cList);

			// Generate Frequent Item Set
			Map<String, Float> _fList = FrequentItemList.generateFrequentItemList(minSupportCount,
					candidateList.get(k));
			frequentItemSet.put(k, _fList);

			/*
			 * GENERATE CANDIDATE ITEM SETS & FREQUENT ITEM SETS FROM K=2 TO N
			 */
			for (int K = 2; !frequentItemSet.get(K - 1).isEmpty(); K++) {

				// Generate Unique List
				GenerateFrequentItemSet.generateUniqueSets(frequentItemSet.get(K - 1));
				List<String> _data = GenerateFrequentItemSet.uniqueList;

				// Get Combination of itemset based on K = 2,3,...
				String[] uniquelist = _data.toArray(new String[_data.size()]);
				GenerateFrequentItemSet.getCombination(uniquelist, uniquelist.length, K);

				// Add it to Candidate List
				Map<String, Float> _candidateList = NextCandidateList
						.getCandidateList(GenerateFrequentItemSet.combination, K);
				candidateList.put(++k, _candidateList);

				// Generate Frequent Item Set
				Map<String, Float> frequentItemList = FrequentItemList.generateFrequentItemList(minSupportCount,
						candidateList.get(k));
				frequentItemSet.put(k, frequentItemList);

				// Clear the list to store the details for next iteration
				GenerateFrequentItemSet.combination.clear();
				GenerateFrequentItemSet.uniqueList.clear();

			}

			/* GENERATE RULES BASED OF FREQUENT ITEM SETS */

			GenerateRules.generateListToCalculateConfidence();

			// Generate Subset & Rules for each and every frequent item set
			// where supportCount >= minSupport && confidence >= minimum confidence
			for (int i = 2; i < frequentItemSet.size(); i++) {
				// Get the frequent item set with 2,3,4,... subsets
				for (Map.Entry<String, Float> _freq : frequentItemSet.get(i).entrySet()) {
					// Generate Subsets

					for (int j = i - 1; j >= 1; j--) {
						GenerateRules.generateSubSets(_freq.getKey().split(","), _freq.getKey().split(",").length, j);
					}

					List<String> combination = GenerateRules.combination;

					// Generate the rules for each and every frequent item set
					// combination
					GenerateRules.generateRules(combination, _freq, minConfidence);

					// Clear the List
					GenerateRules.combination.clear();
				}

			}

			// Write Data to files
			writeDataToFile();

		} catch (Exception e) {
			Logger.getGlobal().info(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 */
	// Output the data to files
	static void writeDataToFile() throws FileNotFoundException, UnsupportedEncodingException {

		// Create the file to store the output (E.g. File name will be like
		// Rules_For_data1.txt)
		File file = new File("Rules_For_" + filename + ".txt");
		PrintWriter writer = new PrintWriter(file, "UTF-8");

		// Write data to file
		writer.println("Summary:");
		writer.println("Total rows in the original set:" + (FileReader.transaction.size() - 1));
		writer.println("Total rules discovered:" + GenerateRules._rules.size());
		writer.println("The selected measures: Support=" + minSupportCount + " Confidence=" + minConfidence);
		writer.println("-------------------------------------------------------\n");
		writer.println("Rules:\n");

		int ruleCount = 1;

		// Write data to file
		for (RuleModel _model : GenerateRules._rules) {

			writer.println("Rule#" + ruleCount++ + ": (Support=" + _model.getSupportCount() + ", Confidence="
					+ _model.getConfidence() + ")");
			writer.println(_model.getLhs());
			writer.println("------>" + _model.getRhs());
			writer.println("\n");
		}
		System.out.println("Data is generated in file named:Rules_For_"+filename+".txt");

		writer.close();
	}

	/**
	 * No Param
	 */
	// Reads the data from users input
	static void getInputFromUser() {

		System.out.println("Enter the file name to find the rules:(E.g. data1 or data2 or data3)\n");
		filename = in.nextLine();

		while (!_fileList.contains(filename)) {
			filename = getFileName(in, "Please enter correct file name:(E.g. data1 or data2 or data 3)\n");
		}

		System.out.println("Enter the Minimum Support Rate between 0 & 1");
		minSupportCount = in.nextFloat();

		while (minSupportCount < 0.0 || minSupportCount > 1.0) {
			minSupportCount = getCount(in, "Please enter the value between 0 & 1");
		}

		System.out.println("Enter the Minimum Confidence Rate between 0 & 1");
		minConfidence = in.nextFloat();

		while (minConfidence < 0.0 || minConfidence > 1.0) {
			minConfidence = getCount(in, "Please enter the value between 0 & 1");
		}

	}

	/**
	 * @param Scanner
	 * @param message
	 * @return String
	 */
	// Recursive file name function call if input file name is incorrect
	static String getFileName(Scanner in, String message) {

		System.out.println(message);
		return in.nextLine();
	}

	
	/**
	 * @param Scanner
	 * @param message
	 * @return String
	 */
	// Recursive Support Count & Confidence rate function call if input values
	// are in incorrect format
	static float getCount(Scanner in, String message) {

		System.out.println(message);
		return in.nextFloat();
	}
}
