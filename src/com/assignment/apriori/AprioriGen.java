package com.assignment.apriori;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AprioriGen {

	static final List<String> uniqueList = new ArrayList<String>();
	public static List<String> combination = new ArrayList<String>();

//
//	public static void main(String[] args) {
//		String arr[] = { "arun", "tushar", "bharat" };
//		int itemsets = 2;
//		int n = arr.length;
//		printCombination(arr, n, itemsets);
//	}

	public static void combinationUtil(String arr[], int size, int itemSetCount, int index, String data[], int i) {
		// Current combination is ready to be printed, print it
		if (index == itemSetCount) {

			String temp = "";
			for (int j = 0; j < itemSetCount; j++) {
				temp += data[j];
				if(j<itemSetCount-1){
					temp += ",";
				}
			}
			combination.add(temp);
			return;
		}

		// When no more elements are there to put in data[]
		if (i >= size)
			return;

		// current is included, put next at next location
		data[index] = arr[i];
		combinationUtil(arr, size, itemSetCount, index + 1, data, i + 1);

		// current is excluded, replace it with next (Note that
		// i+1 is passed, but index is not changed)
		combinationUtil(arr, size, itemSetCount, index, data, i + 1);
	}

	static void printCombination(String arr[], int n, int itemsets) {
		// A temporary array to store all combination one by one
		String data[] = new String[itemsets];

		// Print all combination using temprary array 'data[]'
		combinationUtil(arr, n, itemsets, 0, data, 0);
	}

	public static void generateUniqueSets(Map<String, Float> candidateList) {

		for (Map.Entry<String, Float> entry : candidateList.entrySet()) {
			String[] _split = entry.getKey().split(",");
			for (String _spl : _split) {
				if (!uniqueList.contains(_spl)) {
					uniqueList.add(_spl);
				}
			}
		}
	}

}
