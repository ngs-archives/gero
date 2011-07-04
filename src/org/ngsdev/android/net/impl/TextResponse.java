package org.ngsdev.android.net.impl;

import org.ngsdev.android.net.URLResponse;

public class TextResponse implements URLResponse {
	
	private String text = null;

	public void processResponse(byte[] byteArray) throws Exception {
		this.text = new String(byteArray);
	}
	
	public String getResponseText() {
		return this.text;
	}

}
