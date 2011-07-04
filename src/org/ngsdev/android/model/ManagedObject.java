package org.ngsdev.android.model;

import java.sql.Timestamp;
import java.util.Date;

import org.kroz.activerecord.ActiveRecordBase;
import org.ngsdev.android.util.MD5Digest;

import android.net.Uri;

public abstract class ManagedObject extends ActiveRecordBase {
	public Timestamp timestamp = null;
	public String code = "";

	public String getCode() {
		return this.code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}

	public Date getTimestamp() {
		return new Date(timestamp.getTime());
	}

	public void setTimestamp(Date time) {
		this.timestamp = new Timestamp(time.getTime());
	}

	public static String toCode(Uri uri) {
		return new MD5Digest(uri.toString()).toString();
	}
}
