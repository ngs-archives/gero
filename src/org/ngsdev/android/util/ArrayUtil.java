package org.ngsdev.android.util;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Iterator;

public class ArrayUtil {

	public static String join(ArrayList<?> param, String sparator) {
		Iterator<?> it = param.iterator();
		StringBuilder sb = new StringBuilder();
		while (it.hasNext()) {
			Object i = it.next();
			sb.append(i);
			if (it.hasNext())
				sb.append(sparator);
		}
		return sb.toString();
	}

	public static String join(ArrayList<?> param) {
		return join(param, ",");
	}

	public static ArrayList<Integer> toIntArrayList(String str) {
		String[] ar = str.split(",");
		ArrayList<Integer> ls = new ArrayList<Integer>();
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] != "")
				ls.add(Integer.parseInt(ar[i]));
		}
		return ls;
	}

	public static ArrayList<String> toStringArrayList(String str) {
		String[] ar = str.split(",");
		ArrayList<String> ls = new ArrayList<String>();
		for (int i = 0; i < ar.length; i++) {
			if (ar[i] != "")
				ls.add(ar[i]);
		}
		return ls;
	}

	public static ArrayList<Integer> toIntArrayList(long[] longs) {
		ArrayList<Integer> ls = new ArrayList<Integer>();
		for (int i = 0; i < longs.length; i++) {
			ls.add(Integer.valueOf(Long.valueOf(longs[i]).intValue()));
		}
		return ls;
	}

	public static boolean isArrayWithAnyItems(Object val) {
		return val != null && AbstractCollection.class.isInstance(val)
				&& ((AbstractCollection<?>) val).size() > 0;
	}

}
