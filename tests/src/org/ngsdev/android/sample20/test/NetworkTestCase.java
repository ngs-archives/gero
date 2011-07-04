package org.ngsdev.android.sample20.test;

import org.ngsdev.android.sample20.Sample20;
import org.ngsdev.android.util.Log20;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import org.ngsdev.android.net.URLCache;
import org.ngsdev.android.net.URLRequest;
import org.ngsdev.android.net.URLRequestParams;
import org.ngsdev.android.net.impl.BitmapResponse;
import org.ngsdev.android.net.impl.JSONResponse;
import org.ngsdev.android.net.impl.TextResponse;
import org.ngsdev.android.net.impl.XMLResponse;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import android.app.Activity;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

public class NetworkTestCase extends ActivityInstrumentationTestCase2<Sample20> {

	protected Activity mActivity;
	protected int step;
	protected URLRequest req = null;
	protected URLRequestParams param = null;
	protected boolean cacheUsed;
	protected boolean networkEnabled = false;

	public NetworkTestCase() {
		super("org.ngsdev.android.sample20", Sample20.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		Log20.enable = true;
		mActivity = getActivity();
		URLCache.clearAll(mActivity);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	public void testAsyncResponse() throws Exception {
		if(!networkEnabled) return;
		final CountDownLatch lock = new CountDownLatch(1);
		final Runnable runnable = new Runnable() {
			public void run() {
				try {
					req = new URLRequest(mActivity, "http://ngsdev.org/CNAME",
							URLRequest.GET);
				} catch (URISyntaxException e) {
					fail(e.getMessage());
				}
				req.response = new TextResponse();
				param = new URLRequestParams();
				try {
					req.setURLRequestParams(param);
				} catch (Exception e) {
					fail(e.getMessage());
				}
				cacheUsed = req.send(new URLRequest.RequestListener() {
					public void onRequestCancel(URLRequest request) {
					}
					public void onRequestFail(URLRequest request, Error error) {
						fail(error.getMessage());
					}
					public void onRequestFinishLoad(URLRequest request) {
						Log20.d("finish");
						TextResponse res = (TextResponse) request.response;
						assertEquals(res.getResponseText(), "ngsdev.org\n");
						lock.countDown();
					}
					public void onRequestProgress(URLRequest request,
							Double progress) {
					}
					public void onRequestStart(URLRequest request) {
					}
				});
			}
		};

		mActivity.runOnUiThread(runnable);
		getInstrumentation().waitForIdleSync();
		lock.await();
		assertFalse(cacheUsed);

		mActivity.runOnUiThread(runnable);
		getInstrumentation().waitForIdleSync();
		lock.await();
		assertTrue(cacheUsed);
	}

	public void testCache() throws URISyntaxException, IOException,
			InterruptedException {
		if(!networkEnabled) return;
		final CountDownLatch lock = new CountDownLatch(1);
		URLCache cache = null;
		File cachef = null;
		req = new URLRequest(mActivity, "http://www.google.com/",
				URLRequest.GET);
		assertEquals(req.getRawCacheKey(), "[GET]http://www.google.com/");
		assertEquals(req.getCacheKey(), "5d90ba4c1a4d0a1fef5bdafccbe3943c");

		req = new URLRequest(mActivity, "http://www.google.com/search",
				URLRequest.POST);
		param = new URLRequestParams();
		param.setParameter("q", "python");
		param.setParameter("hl", "zh_cn");
		req.setURLRequestParams(param);
		assertEquals(req.getRawCacheKey(),
				"[POST]http://www.google.com/search?q=python&hl=zh_cn&");
		assertEquals(req.getCacheKey(), "4fac934fc2c7ced666e448ab2971c98b");

		req = new URLRequest(mActivity, "http://www.google.com/search",
				URLRequest.GET);
		req.response = new TextResponse();
		param = new URLRequestParams();
		param.setParameter("q", "java");
		param.setParameter("hl", "zh_cn");
		req.setURLRequestParams(param);
		assertEquals(req.getRawCacheKey(),
				"[GET]http://www.google.com/search?q=java&hl=zh_cn&");
		assertEquals(req.getCacheKey(), "03962c24b2e7a82025e525dfe2359f1a");

		cache = req.getCache();
		cachef = cache.getCacheFile();
		assertNotNull(cachef);
		assertFalse(cache.exists());
		assertEquals(
				cachef.toString(),
				"/data/data/org.ngsdev.android.sample20/cache/andr20/03962c24b2e7a82025e525dfe2359f1a");
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				req.send(new URLRequest.RequestListener() {
					public void onRequestCancel(URLRequest request) {
					}
					public void onRequestFail(URLRequest request, Error error) {
						fail(error.getMessage());
					}
					public void onRequestFinishLoad(URLRequest request) {
						lock.countDown();
					}
					public void onRequestProgress(URLRequest request,
							Double progress) {
					}
					public void onRequestStart(URLRequest request) {
					}
				});
			}
		});
		getInstrumentation().waitForIdleSync();
		lock.await();
	}

	public void testImageResponse() throws URISyntaxException,
			InterruptedException, UnsupportedEncodingException {
		if(!networkEnabled) return;
		final CountDownLatch lock = new CountDownLatch(1);
		req = new URLRequest(mActivity,
				"http://dummyimage.com/600x400/000/fff.png", URLRequest.GET);
		param = new URLRequestParams();
		req.response = new BitmapResponse();
		req.setURLRequestParams(param);
		mActivity.runOnUiThread(new Runnable() {
			public void run() {
				req.send(new URLRequest.RequestListener() {
					public void onRequestCancel(URLRequest request) {
						lock.countDown();
					}
					public void onRequestFail(URLRequest request, Error error) {
						fail(error.getMessage());
					}
					public void onRequestFinishLoad(URLRequest request) {
						lock.countDown();
					}
					public void onRequestProgress(URLRequest request,
							Double progress) {
						// TODO Auto-generated method stub

					}
					public void onRequestStart(URLRequest request) {
						// TODO Auto-generated method stub
					}
				});
			}
		});
		getInstrumentation().waitForIdleSync();
		lock.await();
		Bitmap bmp = ((BitmapResponse) req.response).getBitmap();
		assertNotNull(bmp);
		assertEquals(bmp.getHeight(), 400);
		assertEquals(bmp.getWidth(), 600);
	}

	public void testParams() {
		URLRequestParams param = null;
		param = new URLRequestParams();
		param.setBooleanParameter("bool", true);
		param.setDoubleParameter("double", 0.123);
		param.setIntParameter("int", 255);
		param.setParameter("str", "foobar");
		assertSame(param.getKeys()[0], "str");
		assertSame(param.getKeys()[1], "double");
		assertSame(param.getKeys()[2], "bool");
		assertSame(param.getKeys()[3], "int");
		assertEquals(param.toQueryString(),
				"str=foobar&double=0.123&bool=true&int=255&");
	}
	
	public void testJSONResponse() throws Exception {
		JSONResponse res = new JSONResponse();

		res.processResponse(new String("{ result: 1 }").getBytes());
		assertEquals( res.getJSONObject().getInt("result"), 1 );

		res.processResponse(new String("[1,2,3]").getBytes());
		assertEquals( res.getJSONArray().length(), 3 );
		assertEquals( res.getJSONArray().get(0), 1 );
		assertEquals( res.getJSONArray().get(1), 2 );
		assertEquals( res.getJSONArray().get(2), 3 );
	}
	
	public void testXMLResponse() throws Exception {
		XMLResponse res = new XMLResponse();
		
		res.processResponse(new String(
				"<root>" +
					"<item id='item1' />" +
					"<item id='item2'>aaa</item>" +
					"<item id='item3' />" +
					"<item id='item4' />" +
				"</root>"
				).getBytes());

		Document doc = res.getDocument();
		NodeList nodeList = doc.getElementsByTagName("item");
		assertEquals(nodeList.getLength(),4);
		assertEquals(doc.getElementById("item2").getFirstChild().getNodeValue(),"aaa");
		
	}
	
}
