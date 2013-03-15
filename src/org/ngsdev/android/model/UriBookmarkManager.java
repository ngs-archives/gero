package org.ngsdev.android.model;

import org.kroz.activerecord.ActiveRecordBase;

import android.content.Context;

public class UriBookmarkManager extends UriManager {
  public UriBookmarkManager(Context context) {
    super(context);
  }

  @Override
  public Class<? extends ActiveRecordBase> getEntityClass() {
    return BookmarkedUri.class;
  }

}
