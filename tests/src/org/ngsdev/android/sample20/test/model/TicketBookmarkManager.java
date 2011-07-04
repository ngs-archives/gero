package org.ngsdev.android.sample20.test.model;

import org.kroz.activerecord.ActiveRecordBase;

import android.content.Context;

public class TicketBookmarkManager extends TicketManager {

	public TicketBookmarkManager(Context context) {
		super(context);
	}

	@Override
	public Class<? extends ActiveRecordBase> getEntityClass() {
		return BookmarkedTicket.class;
	}

}
