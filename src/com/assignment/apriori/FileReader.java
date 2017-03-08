package com.assignment.apriori;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileReader {

	static final Map<Integer, List<String>> transaction = new HashMap<Integer, List<String>>();

	static final Map<String, List<String>> columnList = new HashMap<String, List<String>>();

	static final Map<String, String> uniqueConbination = new HashMap<String, String>();

	public static void readFile(String string) throws IOException {

		FileInputStream fis = new FileInputStream(string);

		// Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));

		String line = null;
		int i = 0;
		while ((line = br.readLine()) != null) {

			String[] split = line.split(" +");

			List<String> _row = new ArrayList<String>();
			for (String _sp : split) {
				_row.add(_sp);
			}
			transaction.put(i++, _row);
		}

		for (int count = 0; count < transaction.get(0).size(); count++) {
			List<String> set = new ArrayList<String>();

			for (int column = 0; column < i; column++) {
				if (column != 0) {
					set.add(transaction.get(column).get(count));
				}
			}
			columnList.put(transaction.get(0).get(count), set);
		}

		br.close();

		for (Map.Entry<String, List<String>> _columnList : columnList.entrySet()) {
			for (String _list : _columnList.getValue()) {
				uniqueConbination.put(_list, _columnList.getKey());
			}
		}
		System.out.println(uniqueConbination);

	}
}
