/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net.impl;

import org.apache.http.HttpResponse;
import org.ngsdev.android.net.URLResponse;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitmapResponse implements URLResponse {

  private Bitmap bitmap = null;

  public Bitmap getBitmap() {
    return bitmap;
  }

  @Override
  public void processResponse(HttpResponse res, byte[] byteArray)
      throws Exception {
    bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
  }

}
