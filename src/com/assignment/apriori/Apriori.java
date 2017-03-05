package com.assignment.apriori;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Apriori {

	public static void main(String ar[]) {

		try {

			// This will Store the details for each and every Candidate List
			final Map<Integer, Map<String, Float>> candidateList = new HashMap<Integer, Map<String, Float>>();
			
			final Map<Integer, Map<String, Float>> frequentItemSet = new HashMap<Integer, Map<String, Float>>();

			int k = 1;
			float minSupportCount = 0.2f;
			// Read the file in appropriate format
			FileReader.readFile1("/Users/bharatjain/Desktop/data1");

			// Read Column vice data (Column)
			Map<String, List<String>> _Columnlist = FileReader.columnList;
			
			// Find Candidate List
			Map<String, Float> _cList = FirstCandidateList.findCandidateList(_Columnlist);
			candidateList.put(k, _cList);
			System.out.print(_cList+"   :   ");


			// Generate Frequent Item Set
			Map<String, Float> _fList = FrequentItemList.generateFrequentItemList(minSupportCount, candidateList.get(k));
			frequentItemSet.put(k,_fList);
			System.out.println(_fList);

			
			for(int K=2;!frequentItemSet.get(K-1).isEmpty();K++){
				
				//Generate Unique List
				AprioriGen.generateUniqueSets(frequentItemSet.get(K-1));
				List<String> _data = AprioriGen.uniqueList;
				
				//Get Combination of itemset based on K = 2,3,...
				String[] uniquelist = _data.toArray(new String[_data.size()]);
				AprioriGen.printCombination(uniquelist, uniquelist.length,K);

				// Add it to Candidate List
				Map<String, Float> _candidateList = NextCandidateList.getCandidateList(AprioriGen.combination,K);
				candidateList.put(++k,_candidateList);
				System.out.print(_candidateList+"   :   ");

				
				// Generate Frequent Item Set
				Map<String, Float> frequentItemList = FrequentItemList.generateFrequentItemList(minSupportCount,candidateList.get(k));
				frequentItemSet.put(k,frequentItemList);

				System.out.println(frequentItemList);
				
				AprioriGen.combination.clear();
				AprioriGen.uniqueList.clear();

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
