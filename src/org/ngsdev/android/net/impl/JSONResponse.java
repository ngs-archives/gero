/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net.impl;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class JSONResponse extends TextResponse {
	private Object rootObject = null;

	@Override
	public void processResponse(byte[] byteArray) throws Exception {
		super.processResponse(byteArray);
		this.rootObject = new JSONTokener(this.getResponseText()).nextValue();
	}
	
	public JSONObject getJSONObject() {
		if(this.rootObject instanceof JSONObject)
			return (JSONObject) this.rootObject;
		return null;
	}
	
	public JSONArray getJSONArray() {
		if(this.rootObject instanceof JSONArray)
			return (JSONArray) this.rootObject;
		return null;
	}

}
