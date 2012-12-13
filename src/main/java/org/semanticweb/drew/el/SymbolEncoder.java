package org.semanticweb.drew.el;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SymbolEncoder<K> {

	private Map<Integer, K> int2K;

	private Map<K, Integer> K2int;

	int maxEncode;

	public SymbolEncoder() {
		maxEncode = -1;
		int2K = new HashMap<>();
		K2int = new HashMap<>();
	}

	public int encode(K K) {
		if (K2int.containsKey(K)) {
			return K2int.get(K);
		} else {
			maxEncode++;
			K2int.put(K, maxEncode);
			int2K.put(maxEncode, K);
			return maxEncode;
		}
	}

	public K decode(Integer value) {
		return int2K.get(value);
	}

	public void report() {
		for (Entry<K, Integer> e : K2int.entrySet()) {
			System.out.println(e.getKey() + " -> " + e.getValue());
		}
	}
}
