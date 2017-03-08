package com.assignment.apriori.itemset;

import java.util.Iterator;
import java.util.Map;


/**
 * @author bharatjain
 * @machine Mac OS Sierra (10.12.3)
 */
public class FrequentItemList {

	
	/**
	 * @param minSupportCount
	 * @param candidateList
	 * @return
	 */
	// This function generates the frequent item list by eliminating the subsets whose Support Count < Minimum support count
	public static Map<String, Float> generateFrequentItemList(float minSupportCount, Map<String, Float> candidateList) {

		for (Iterator<Map.Entry<String, Float>> it = candidateList.entrySet().iterator(); it.hasNext();) {

			if (it.next().getValue() < minSupportCount) {
				it.remove();
			}
		}

		return candidateList;
	}
}
