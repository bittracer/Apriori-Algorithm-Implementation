package com.assignment.apriori;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Apriori {

	// This will Store the details for each and every Candidate List
	public static Map<Integer, Map<String, Float>> candidateList = new HashMap<Integer, Map<String, Float>>();

	public static Map<Integer, Map<String, Float>> frequentItemSet = new HashMap<Integer, Map<String, Float>>();

	public static void main(String ar[]) {

		try {

			int k = 1;
			float minSupportCount = 0.2f;
			float minConfidence = 0.2f;
			// Read the file in appropriate format
			FileReader.readFile1("/Users/bharatjain/Desktop/data1");

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

			// Generate Subset for each and every frequent item set & generate rules
			
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
			
			for(RuleModel _model:GenerateRules._rules){
				
				System.out.print("Confidence  : "+_model.getConfidence());
				System.out.println("     Support  : "+_model.getSupportCount());
				System.out.print(_model.getLhs()+"--->");
				System.out.println(_model.getRhs());
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
