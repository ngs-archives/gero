package org.ngsdev.android.layout;

import org.ngsdev.android.util.Log20;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MoreButton extends LinearLayout {

  private TextView    textView;
  private ProgressBar progressBar;

  public MoreButton(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MoreButton(Context context) {
    super(context);
  }

  private void initElements() {
    if (textView == null)
      textView = (TextView) this
          .findViewById(org.ngsdev.android.R.id.text_view);
    if (progressBar == null)
      progressBar = (ProgressBar) this
          .findViewById(org.ngsdev.android.R.id.progress_bar);
  }

  public void setText(CharSequence text) {
    initElements();
    Log20.d((String) text);
    textView.setText(text);
  }

  public void setText(int resid) {
    initElements();
    textView.setText(resid);
  }

  public void setLoading(boolean loading) {
    initElements();
    if (loading)
      this.setText(org.ngsdev.android.R.string.loading);
    this.progressBar.setVisibility(loading ? VISIBLE : GONE);
  }
}
