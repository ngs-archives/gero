package org.ngsdev.android.view;

import java.net.URISyntaxException;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import org.ngsdev.android.net.URLRequest;
import org.ngsdev.android.net.impl.BitmapResponse;

/**
 * Displays an arbitrary image, such as an icon. The ImageView class can load
 * images from various sources (such as resources or content providers), takes
 * care of computing its measurement from the image so that it can be used in
 * any layout manager, and provides various display options such as scaling and
 * tinting.
 * 
 * @attr ref android.R.styleable#ImageView_adjustViewBounds
 * @attr ref android.R.styleable#ImageView_src
 * @attr ref android.R.styleable#ImageView_maxWidth
 * @attr ref android.R.styleable#ImageView_maxHeight
 * @attr ref android.R.styleable#ImageView_tint
 * @attr ref android.R.styleable#ImageView_scaleType
 */
public class URLImageView extends FrameLayout
		implements
			URLRequest.RequestListener {

	private URLRequest.RequestListener listener;
	public ProgressBar mProgressBar = null;
	public ImageView mImageView = null;
	private String URLPath;
	private URLRequest request;
	private boolean loading;

	public URLImageView(Context context) {
		this(context, null);
	}
	public URLImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	public URLImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
		mProgressBar = new ProgressBar(context);
		mImageView = new ImageView(context, attrs);

		final int WC = LayoutParams.WRAP_CONTENT;
		final int FP = LayoutParams.FILL_PARENT;
		LayoutParams lp = new LayoutParams(WC, WC);
		lp.gravity = Gravity.CENTER;
		this.addView(mProgressBar, lp);
		this.addView(mImageView, new LayoutParams(FP, FP));
		setLoading(false);
	}

	/**
	 * Controls how the image should be resized or moved to match the size of
	 * this ImageView.
	 * 
	 * @param scaleType
	 *            The desired scaling mode.
	 * 
	 * @attr ref android.R.styleable#ImageView_scaleType
	 */
	public void setScaleType(ImageView.ScaleType scaleType) {
		mImageView.setScaleType(scaleType);
	}

	public void onRequestCancel(URLRequest request) {
		URLRequest.RequestListener ln = this.getListener();
		setLoading(false);
		if (ln != null)
			ln.onRequestCancel(request);
		request = null;
	}

	public void onRequestFail(URLRequest request, Error error) {
		URLRequest.RequestListener ln = this.getListener();
		setLoading(false);
		if (ln != null)
			ln.onRequestFail(request, error);
		request = null;
	}

	public void onRequestFinishLoad(URLRequest request) {
		BitmapResponse res = (BitmapResponse) request.response;
		mImageView.setImageBitmap(res.getBitmap());
		setLoading(false);
		URLRequest.RequestListener ln = this.getListener();
		if (ln != null)
			ln.onRequestFinishLoad(request);
		request = null;
	}

	public void onRequestProgress(URLRequest request, Double progress) {
		URLRequest.RequestListener ln = this.getListener();
		if (ln != null)
			ln.onRequestProgress(request, progress);
	}

	public void onRequestStart(URLRequest request) {
		URLRequest.RequestListener ln = this.getListener();
		if (ln != null)
			ln.onRequestStart(request);
	}
	public void setListener(URLRequest.RequestListener listener) {
		this.listener = listener;
	}

	public URLRequest.RequestListener getListener() {
		return listener;
	}

	public void cancel() {
		if (request != null) {
			request.abort();
			request = null;
		}
		this.mImageView.setImageBitmap(null);
	}

	public void setURLPath(String urlPath) throws URISyntaxException {
		this.cancel();
		URLPath = urlPath;
		request = new URLRequest(this.getContext(), urlPath);
		request.response = new BitmapResponse();
		setLoading(true);
		request.send(this);
	}

	public String getURLPath() {
		return URLPath;
	}
	public void setLoading(boolean loading) {
		mImageView.setVisibility(loading ? GONE : VISIBLE);
		mProgressBar.setVisibility(loading ? VISIBLE : GONE);
		this.loading = loading;
	}
	public boolean isLoading() {
		return loading;
	}

}
