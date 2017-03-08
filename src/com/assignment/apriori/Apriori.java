package com.assignment.apriori;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Apriori {

	// This will Store the details for each and every Candidate List
	public static Map<Integer, Map<String, Float>> candidateList = new HashMap<Integer, Map<String, Float>>();

	public static Map<Integer, Map<String, Float>> frequentItemSet = new HashMap<Integer, Map<String, Float>>();

	public static void main(String ar[]) {

		try {

			int k = 1;
			float minSupportCount;
			float minConfidence;

			List<String> _fileList = new ArrayList<String>();
			_fileList.add("data1");
			_fileList.add("data2");
			_fileList.add("data3");

			String filename = "";

			Scanner in = new Scanner(System.in);

			System.out.println("Enter the file name to find the rules:(E.g. data1 or data2 or data 3)\n");
			filename = in.nextLine();

			while (!_fileList.contains(filename)) {
				filename = getFileName(in, "Please enter correct file name:(E.g. data1 or data2 or data 3)\n");
			}

			System.out.println("Enter the Minimum Support Count between 0 & 1");
			minSupportCount = in.nextFloat();

			while (minSupportCount < 0.0 || minSupportCount > 1.0) {
				minSupportCount = getCount(in, "Please enter the value between 0 & 1");
			}

			System.out.println("Enter the Minimum Confidence between 0 & 1");
			minConfidence = in.nextFloat();

			while (minConfidence < 0.0 || minConfidence > 1.0) {
				minConfidence = getCount(in, "Please enter the value between 0 & 1");
			}

			// Read the file in appropriate format
			// /Users/bharatjain/Desktop/data1
			FileReader.readFile("/Users/bharatjain/Desktop/" + filename);

			// Read Column vice data (Column)
			Map<String, List<String>> _Columnlist = FileReader.columnList;

			// Find Candidate List
			Map<String, Float> _cList = FirstCandidateList.findCandidateList(_Columnlist);
			candidateList.put(k, _cList);
			// System.out.print(_cList+" : ");

			// Generate Frequent Item Set
			Map<String, Float> _fList = FrequentItemList.generateFrequentItemList(minSupportCount,
					candidateList.get(k));
			frequentItemSet.put(k, _fList);
			// System.out.println(_fList);

			/* GENERATE CANDIDATE ITEM SETS & FREQUENT ITEM SETS */

			for (int K = 2; !frequentItemSet.get(K - 1).isEmpty(); K++) {

				// Generate Unique List
				GenerateFrequentItemSet.generateUniqueSets(frequentItemSet.get(K - 1));
				List<String> _data = GenerateFrequentItemSet.uniqueList;

				// Get Combination of itemset based on K = 2,3,...
				String[] uniquelist = _data.toArray(new String[_data.size()]);
				GenerateFrequentItemSet.printCombination(uniquelist, uniquelist.length, K);

				// Add it to Candidate List
				Map<String, Float> _candidateList = NextCandidateList
						.getCandidateList(GenerateFrequentItemSet.combination, K);
				candidateList.put(++k, _candidateList);
				// System.out.println(_candidateList+" : ");

				// Generate Frequent Item Set
				Map<String, Float> frequentItemList = FrequentItemList.generateFrequentItemList(minSupportCount,
						candidateList.get(k));
				frequentItemSet.put(k, frequentItemList);

				// System.out.println(frequentItemList);

				GenerateFrequentItemSet.combination.clear();
				GenerateFrequentItemSet.uniqueList.clear();

			}

			/* GENERATE RULES BASED OF FREQUENT ITEM SETS */

			// Generate Subset for each and every frequent item set & generate
			// rules

			GenerateRules.generateListToCalculateConfidence();

			for (int i = 2; i < frequentItemSet.size(); i++) {
				// Get the frequent item set with 2,3,4,... subsets
				for (Map.Entry<String, Float> _freq : frequentItemSet.get(i).entrySet()) {
					// Generate Subsets

					for (int j = i - 1; j >= 1; j--) {
						GenerateRules.getSubSets(_freq.getKey().split(","), _freq.getKey().split(",").length, j);
					}

					List<String> combination = GenerateRules.combination;

					GenerateRules.generateRules(combination, _freq, minConfidence);

					GenerateRules.combination.clear();
				}

			}

			System.out.println("Summary:");
			System.out.println("Total rows in the original set:" + (FileReader.transaction.size() - 1));
			System.out.println("Total rules discovered:" + GenerateRules._rules.size());
			System.out.println("The selected measures: Support=" + minSupportCount + " Confidence=" + minConfidence);
			System.out.println("-------------------------------------------------------\n");
			System.out.println("Rules:\n");
			int ruleCount=1;
			for (RuleModel _model : GenerateRules._rules) {

				System.out.println("Rule#"+ruleCount+++": (Support="+_model.getSupportCount()+", Confidence="+_model.getConfidence()+")");
				System.out.println(_model.getLhs());
				System.out.println("------>"+_model.getRhs());
				System.out.println("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String getFileName(Scanner in, String message) {

		System.out.println(message);
		return in.nextLine();
	}

	static float getCount(Scanner in, String message) {

		System.out.println(message);
		return in.nextFloat();
	}
}
