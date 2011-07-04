package org.ngsdev.android.net.impl;

import org.json.JSONException;
import org.json.JSONObject;

public class BitlyResponse extends JSONResponse {

	private String longURL;
	private String URL;
	private String hash;
	private String globalHash;
	private String newHash;
	private String userHash;
	private Error error;

	@Override
	public void processResponse(byte[] byteArray) throws Exception {
		super.processResponse(byteArray);
		
		JSONObject root = this.getJSONObject();
		String statusCode = root.getString("status_code");
		String statusText = root.getString("status_txt");
		if(!statusCode.equals("200")) {
			this.error = new Error(statusText!=null?statusText:"UNKNOWN_ERROR");
			return;
		}
		JSONObject obj = this.getJSONObject().getJSONObject("data");
		if (obj.has("expand"))
			obj = obj.getJSONArray("expand").getJSONObject(0);

		try {
			this.longURL = obj.getString("long_url");
		} catch (JSONException e) {
		}
		try {
			this.URL = obj.getString("url");
		} catch (JSONException e) {
		}
		try {
			this.hash = obj.getString("hash");
		} catch (JSONException e) {
		}
		try {
			this.globalHash = obj.getString("global_hash");
		} catch (JSONException e) {
		}
		try {
			this.newHash = obj.getString("new_hash");
		} catch (JSONException e) {
		}
		try {
			this.userHash = obj.getString("user_hash");
		} catch (JSONException e) {
		}
		try {
			this.error = new Error(obj.getString("error"));
		} catch (JSONException e) {
		}

	}

	public String getLongURL() {
		return longURL;
	}

	public String getURL() {
		return URL;
	}

	public String getHash() {
		return hash;
	}

	public String getGlobalHash() {
		return globalHash;
	}

	public String getNewHash() {
		return newHash;
	}

	public String getUserHash() {
		return userHash;
	}

	public Error getError() {
		return error;
	}

}
