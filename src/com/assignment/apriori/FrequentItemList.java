package com.assignment.apriori;

import java.util.Iterator;
import java.util.Map;

public class FrequentItemList {

	public static Map<String, Float> generateFrequentItemList(
			float minSupportCount, Map<String, Float> candidateList) {

		for (Iterator<Map.Entry<String, Float>> it = candidateList.entrySet()
				.iterator(); it.hasNext();) {

			if (it.next().getValue() < minSupportCount) {
				it.remove();
			}
		}

		return candidateList;
	}
}
