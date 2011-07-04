package org.ngsdev.android.model;

import org.kroz.activerecord.ActiveRecordBase;

import android.content.Context;

public class UriHistoryManager extends UriManager {
	public UriHistoryManager(Context context) {
		super(context);
	}

	public static final String MAX_PREF_KEY = "maxUriHistory";

	@Override
	public String getMaxPrefKey() {
		return MAX_PREF_KEY;
	}

	@Override
	public Class<? extends ActiveRecordBase> getEntityClass() {
		return HistoryUri.class;
	}

}
