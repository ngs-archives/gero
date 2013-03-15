package org.ngsdev.android.model;

import android.net.Uri;

public abstract class ManagedUri extends ManagedObject {

  public String url = "";

  public Uri getUri() {
    return Uri.parse(this.url);
  }

  public void setUri(Uri uri) {
    this.url = uri.toString();
    this.setCode(toCode(uri));
  }

}
