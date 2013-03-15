package org.ngsdev.android.adapter;

import java.util.ArrayList;

import org.ngsdev.android.layout.MoreButton;
import org.ngsdev.android.net.URLRequest;
import org.ngsdev.android.util.Log20;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public abstract class URLRequestAdapter extends
    ArrayAdapter<URLRequestAdapter.Item> implements URLRequest.RequestListener {

  public static interface CreationListener {
    abstract void onCreationCancel(URLRequestAdapter adapter);

    abstract void onCreationComplete(URLRequestAdapter adapter);

    abstract void onCreationFail(URLRequestAdapter adapter, Error error);

    abstract void onCreationProgress(URLRequestAdapter adapter, Double progress);

    abstract void onCreationStart(URLRequestAdapter adapter);
  }

  public static interface Item {
    abstract long getId();

    abstract Uri getUri();
  }

  public static interface ItemView {
    abstract void setItem(Item item);
  }

  public static class MoreItem implements Item {
    public long getId() {
      return 0xffffff;
    }

    public Uri getUri() {
      return null;
    }
  }

  public static final int  DEFAULT_COUNT = 10;
  private CreationListener creationListener;
  private boolean          loaded        = false;
  private boolean          loading       = false;
  private boolean          loadingMore   = false;
  private View             moreButton;
  private URLRequest       request;

  public URLRequestAdapter(Context c) {
    super(c, android.R.id.empty);
  }

  public URLRequestAdapter(Context c, URLRequest request) {
    super(c, android.R.id.empty);
    this.setRequest(request);
  }

  public abstract void createDataSource();

  public Context getContext() {
    return this.getRequest().getContext();
  }

  @Override
  public int getCount() {
    return this.getItems().size() + (this.hasMore() ? 1 : 0);
  }

  public int getCountPerRequest() {
    return DEFAULT_COUNT;
  }

  public CreationListener getCreationListener() {
    return creationListener;
  }

  @Override
  public Item getItem(int position) {
    if (position == this.getCount() - 1 && this.hasMore())
      return new MoreItem();
    return this.getItems().get(position);
  }

  @Override
  public long getItemId(int position) {
    return this.getItem(position).getId();
  }

  public abstract ArrayList<? extends Item> getItems();

  public boolean getLoaded() {
    return loaded;
  }

  public boolean getLoading() {
    return loading;
  }

  public boolean getLoadingMore() {
    return loadingMore;
  }

  public View getMoreButton() {
    if (moreButton == null)
      moreButton = View.inflate(this.getContext(),
          org.ngsdev.android.R.layout.more_button, null);
    MoreButton mb = (MoreButton) moreButton;
    mb.setText(this.getContext().getString(
        org.ngsdev.android.R.string.load_more, this.getNextCount()));
    mb.setLoading(this.getLoading());
    return moreButton;
  }

  public int getNextCount() {
    int t = this.getTotal();
    int c = this.getCount();
    int p = this.getCountPerRequest();
    int r = t - c;
    if (r > p)
      r = p;
    return r;
  }

  @Override
  public int getPosition(Item item) {
    for (int i = 0; i < this.getCount(); i++) {
      if (item.getId() == this.getItemId(i))
        return i;
    }
    return -1;
  }

  public URLRequest getRequest() {
    return request;
  }

  public abstract int getTotal();

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View v = null;
    if (this.hasMore() && position == this.getCount() - 1)
      return this.getMoreButton();
    Item item = this.getItem(position);
    v = convertView != null && ItemView.class.isInstance(v) ? convertView
        : this.getViewForItem(item);
    if (ItemView.class.isInstance(v))
      ((ItemView) v).setItem(item);
    return v;
  }

  public abstract View getViewForItem(Item item);

  public boolean hasMore() {
    return this.getItems().size() < this.getTotal();
  }

  public boolean load() {
    return this.load(false);
  }

  public boolean load(boolean more) {
    if (loading)
      return false;
    loading = true;
    loadingMore = more;
    if (more && moreButton != null && MoreButton.class.isInstance(moreButton)) {
      ((MoreButton) moreButton).setLoading(true);
    }
    return this.getRequest().send(URLRequestAdapter.this);
  }

  public void onRequestCancel(URLRequest request) {
    this.getCreationListener().onCreationCancel(this);
  }

  public void onRequestFail(URLRequest request, Error error) {
    this.getCreationListener().onCreationFail(this, error);
  }

  public void onRequestFinishLoad(URLRequest request) {
    this.createDataSource();
    this.getCreationListener().onCreationComplete(this);
    loading = false;
    loadingMore = false;
    Log20.d("%d", this.getCount());
    this.notifyDataSetChanged();
  }

  public void onRequestProgress(URLRequest request, Double progress) {
    this.getCreationListener().onCreationProgress(this, progress);
  }

  public void onRequestStart(URLRequest request) {
    this.getCreationListener().onCreationStart(this);
  }

  public URLRequestAdapter setCreationListener(CreationListener creationListener) {
    this.creationListener = creationListener;
    return this;
  }

  public void setRequest(URLRequest request) {
    this.request = request;
  }
}
