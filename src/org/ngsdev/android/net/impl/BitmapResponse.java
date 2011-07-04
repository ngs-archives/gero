/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net.impl;

import org.ngsdev.android.net.URLResponse;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;

public class BitmapResponse implements URLResponse {

	private Bitmap bitmap = null;
	
	public void processResponse(byte[] byteArray) throws Exception {
		bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	}
	public Bitmap getBitmap() {
		return bitmap;
	}

}
