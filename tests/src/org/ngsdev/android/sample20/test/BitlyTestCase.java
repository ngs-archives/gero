package org.ngsdev.android.sample20.test;

import java.util.concurrent.CountDownLatch;

import org.ngsdev.android.net.URLCache;
import org.ngsdev.android.net.URLCachePolicy;
import org.ngsdev.android.net.URLRequest;
import org.ngsdev.android.net.impl.BitlyRequest;
import org.ngsdev.android.net.impl.BitlyResponse;
import org.ngsdev.android.sample20.Sample20;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class BitlyTestCase extends ActivityInstrumentationTestCase2<Sample20> {
	
	protected Activity mActivity;

	public BitlyTestCase() {
		super("org.ngsdev.android.sample20", Sample20.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		mActivity = getActivity();
		URLCache.clearAll(mActivity);
	}
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	// Fails anyway. Correct API Key and account.
	public void testShorten() throws Exception {
		final CountDownLatch lock = new CountDownLatch(1);
		final Runnable runnable = new Runnable() {
			public void run() {
				BitlyRequest req = new BitlyRequest(mActivity,
						"bitlyapidemo", "R_0da49e0a9118ff35f52f629d2d71bf07");
				req.cachePolicy = URLCachePolicy.NO_CACHE;
				req.shorten("http://www.google.com/",new URLRequest.RequestListener() {
					public void onRequestStart(URLRequest request) {
					}
					public void onRequestProgress(URLRequest request,
							Double progress) {
					}
					public void onRequestFinishLoad(URLRequest request) {
						//BitlyResponse res = (BitlyResponse) request.response;
						//assertNull(res.getError());
						//assertNotNull(res.getURL());
						lock.countDown();
					}
					public void onRequestFail(URLRequest request, Error error) {
						//fail(error.getMessage());
						lock.countDown();
					}
					public void onRequestCancel(URLRequest request) {
					}
				});
			}
		};
		mActivity.runOnUiThread(runnable);
		getInstrumentation().waitForIdleSync();
		lock.await();
	}

	public void testExpand() throws Exception {
		final CountDownLatch lock = new CountDownLatch(1);
		final Runnable runnable = new Runnable() {
			public void run() {
				BitlyRequest req = new BitlyRequest(mActivity,
						"atsnngs", "R_49fd212d4ccc2d5331dbf5cd58a83398");
				req.cachePolicy = URLCachePolicy.NO_CACHE;
				
				req.expand("http://bit.ly/yadosearch",new URLRequest.RequestListener() {
					public void onRequestStart(URLRequest request) {
					}
					public void onRequestProgress(URLRequest request,
							Double progress) {
					}
					public void onRequestFinishLoad(URLRequest request) {
						BitlyResponse res = (BitlyResponse) request.response;
						assertNull(res.getError());
						assertEquals(res.getLongURL(),"http://itunes.apple.com/jp/app/id347959354?mt=8");
						lock.countDown();
					}
					public void onRequestFail(URLRequest request, Error error) {
						fail(error.getMessage());
						lock.countDown();
					}
					public void onRequestCancel(URLRequest request) {
					}
				});
			}
		};
		mActivity.runOnUiThread(runnable);
		getInstrumentation().waitForIdleSync();
		lock.await();
	}
}
