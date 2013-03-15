package org.ngsdev.android.net.impl;

import org.apache.http.HttpResponse;
import org.ngsdev.android.net.URLResponse;

public class TextResponse implements URLResponse {

  private String text = null;

  public void processResponse(HttpResponse res, byte[] byteArray)
      throws Exception {
    this.text = new String(byteArray);
  }

  public String getResponseText() {
    return this.text;
  }

}
