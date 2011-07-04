/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import org.ngsdev.android.util.*;
import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.params.HttpParams;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;

import android.content.Context;

public class URLRequest implements HttpUriRequest {
	public static interface RequestListener {
		abstract void onRequestCancel(URLRequest request);
		abstract void onRequestFail(URLRequest request, Error error);
		abstract void onRequestFinishLoad(URLRequest request);
		abstract void onRequestProgress(URLRequest request, Double progress);
		abstract void onRequestStart(URLRequest request);
	}
	public static final String GET = HttpGet.METHOD_NAME;
	public static final String POST = HttpPost.METHOD_NAME;
	private String cacheKey = null;
	public URLCachePolicy cachePolicy = URLCachePolicy.NETWORK;
	private Context context;
	private HttpRequestBase httpRequest = null;
	private RequestListener listener = null;
	private URLRequestParams URLRequestParams;
	public URLResponse response = null;

	private URLRequestTask task = null;
	public URLRequest(Context context) {
		this.context = context;
	}
	public URLRequest(Context context, String uri) throws URISyntaxException {
		this.context = context;
		createHttpRequest(new URI(uri), GET);
	}
	public URLRequest(Context context, String uri, String methodName)
			throws URISyntaxException {
		this.context = context;
		createHttpRequest(new URI(uri), methodName);
	}
	public URLRequest(Context context, URI uri) {
		this.context = context;
		createHttpRequest(uri, GET);
	}

	public URLRequest(Context context, URI uri, String methodName) {
		this.context = context;
		createHttpRequest(uri, methodName);
	}

	public void abort() throws UnsupportedOperationException {
		this.httpRequest.abort();
	}

	public void addHeader(Header arg0) {
		this.httpRequest.addHeader(arg0);
	}

	public void addHeader(String arg0, String arg1) {
		this.httpRequest.addHeader(arg0, arg1);
	}

	public boolean containsHeader(String arg0) {
		return this.httpRequest.containsHeader(arg0);
	}

	public void createHttpRequest(String uri, String methodName) throws URISyntaxException {
		this.createHttpRequest(new URI(uri), methodName);
	}

	public void createHttpRequest(URI uri, String methodName) {
		if (methodName.equals(GET))
			this.httpRequest = new HttpGet(uri);
		else if (methodName.equals(POST))
			this.httpRequest = new HttpPost(uri);
	}
	public void generateCacheKey() {
		this.cacheKey = new MD5Digest(getRawCacheKey()).toString();
	}

	public Header[] getAllHeaders() {
		return this.httpRequest.getAllHeaders();
	}

	public URLCache getCache() {
		return new URLCache(this.context, this);
	}

	public String getCacheKey() {
		if (this.cacheKey == null)
			generateCacheKey();
		return this.cacheKey;
	}

	public Context getContext() {
		return this.context;
	}

	public Header getFirstHeader(String arg0) {
		return this.httpRequest.getFirstHeader(arg0);
	}

	public Header[] getHeaders(String arg0) {
		return this.httpRequest.getHeaders(arg0);
	}

	public Header getLastHeader(String arg0) {
		return this.httpRequest.getLastHeader(arg0);
	}

	public RequestListener getListener() {
		return this.listener;
	}

	public String getMethod() {
		return this.httpRequest.getMethod();
	}

	public HttpParams getParams() {
		return this.httpRequest.getParams();
	}

	public ProtocolVersion getProtocolVersion() {
		return this.httpRequest.getProtocolVersion();
	}

	public String getRawCacheKey() {
		URI uri = getURI();
		String strUri = "";
		String strParam = "";
		if (uri != null)
			strUri = uri.toString();
		URLRequestParams params = this.getURLRequestParams();
		if (params != null)
			strParam = "?" + params.toQueryString();
		return String.format("[%s]%s%s", getMethod(), strUri, strParam);
	}

	public RequestLine getRequestLine() {
		return this.httpRequest.getRequestLine();
	}

	public URI getURI() {
		URI URI = this.httpRequest.getURI();
		URLRequestParams params = this.getURLRequestParams();
		if (!this.isPost() && params != null) {
			String strURI = URI.toString();
			try {
				URI = new URI(
						String.format("%s%s%s", strURI, strURI.contains("?")
								? ""
								: "?", params.toQueryString()));
			} catch (URISyntaxException e) {
				Log20.e(e);
			}
		}
		return URI;

	}

	public HeaderIterator headerIterator() {
		return this.httpRequest.headerIterator();
	}

	public HeaderIterator headerIterator(String arg0) {
		return this.httpRequest.headerIterator(arg0);
	}

	public boolean isAborted() {
		return this.httpRequest.isAborted();
	}

	public void removeHeader(Header arg0) {
		this.httpRequest.removeHeader(arg0);

	}

	public void removeHeaders(String arg0) {
		this.httpRequest.removeHeaders(arg0);

	}
	public boolean isPost() {
		return this.getMethod().equals(POST);
	}
	public boolean send(RequestListener listener) {
		this.listener = listener;
		boolean exists = this.getCache().exists()
				&& this.cachePolicy != URLCachePolicy.NO_CACHE;
		this.task = new URLRequestTask(URLRequest.this);
		this.task.execute();
		return exists;
	}

	public void setHeader(Header arg0) {
		this.httpRequest.setHeader(arg0);
	}

	public void setHeader(String arg0, String arg1) {
		this.httpRequest.setHeader(arg0, arg1);
	}

	public void setHeaders(Header[] arg0) {
		this.httpRequest.setHeaders(arg0);
	}

	public void setListener(RequestListener listener) {
		this.listener = listener;
	}
	public void setParams(HttpParams arg0) {
		this.httpRequest.setParams(arg0);
	}
	public void setURLRequestParams(URLRequestParams params)
			throws UnsupportedEncodingException {
		this.URLRequestParams = params;
		if (this.isPost()) {
			for (String key : params.getKeys()) {
				HttpEntity entity = params.getEntity(key);
				if (entity != null)
					this.setEntity(entity);
			}
		}
	}
	public void setEntity(HttpEntity entity) {
		if (this.isPost()) {
			((HttpPost) this.httpRequest).setEntity(entity);
		}
	}

	public URLRequestParams getURLRequestParams() {
		return URLRequestParams;
	}

}
