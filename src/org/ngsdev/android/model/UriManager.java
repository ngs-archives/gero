package org.ngsdev.android.model;

import java.util.Date;
import java.util.List;

import android.content.Context;
import android.net.Uri;

import org.kroz.activerecord.*;

public abstract class UriManager extends ObjectManager {
	

	public UriManager(Context context) {
		super(context);
	}

	public ManagedUri add(Uri uri) throws ActiveRecordException {
		ManagedUri newUri = (ManagedUri) this.getDB().newEntity(this.getEntityClass());
		newUri.setUri(uri);
		newUri.setTimestamp(new Date());
		newUri.save();
		trimToMax();
		return newUri;
	}

	public ManagedUri findUri(Uri uri) throws ActiveRecordException {
		String[] args = { ManagedUri.toCode(uri) };
		List<ManagedObject> list = this.find("code = ?", args);
		if(list.size()>0) 
			return (ManagedUri) list.get(0);
		return null;
	}

}
