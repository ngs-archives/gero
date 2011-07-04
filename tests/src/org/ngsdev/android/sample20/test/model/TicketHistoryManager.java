package org.ngsdev.android.sample20.test.model;

import org.kroz.activerecord.ActiveRecordBase;

import android.content.Context;

public class TicketHistoryManager extends TicketManager {

	public static final String MAX_PREF_KEY = "maxTicketHistory";

	public TicketHistoryManager(Context context) {
		super(context);
	}

	@Override
	public Class<? extends ActiveRecordBase> getEntityClass() {
		return HistoryTicket.class;
	}

	@Override
	public String getMaxPrefKey() {
		return MAX_PREF_KEY;
	}

}
