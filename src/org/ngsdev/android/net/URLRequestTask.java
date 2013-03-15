/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.ngsdev.android.util.Log20;

import android.os.AsyncTask;

public class URLRequestTask extends AsyncTask<Void, Double, Error> {
	URLRequest request = null;
	HttpClient client = null;
	public URLRequestTask(URLRequest request) {
		this.request = request;
	}
	@Override
	protected Error doInBackground(Void... arg) {
		try {
			URLCache cache = request.getCache();
			if (request.cachePolicy != URLCachePolicy.NO_CACHE
					&& cache.exists()) {
				try {
					if (request.response != null) {
						request.response.processResponse(cache.getCachedData());
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					Log20.e(e);
				}
			}
			HttpResponse res = client.execute(request.getHttpRequest());
			HttpEntity ent = res.getEntity();
			int cd = res.getStatusLine().getStatusCode();
			if (cd >= 400 && cd < 500) {
				return new Error(String.format("%d HTTP ERROR", cd));
			}
			InputStream is = ent.getContent();
			long len = ent.getContentLength();
			boolean unknown = len <= 0;
			double dataLen = Long.valueOf(len).doubleValue();
			File tmp = cache.getTempFile();
			FileOutputStream fos = new FileOutputStream(tmp);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			double par = 0;
			byte[] buf = new byte[128];
			int ch = -1;
			int count = 0;
			while ((ch = is.read(buf)) != -1 && !this.isCancelled()) {
				baos.write(buf, 0, ch);
				count += ch;
				fos.write(buf, 0, ch);
				if (!unknown) {
					par = Integer.valueOf(count).doubleValue() / dataLen;
					Double[] pub = {par};
					this.publishProgress(pub);
				}
			}
			byte[] bytes = baos.toByteArray();
			if (!this.isCancelled()) {
				if (request.response != null)
					request.response.processResponse(bytes);
				cache.store();
			}
			fos.close();
			baos.close();
			is.close();
		} catch (Exception e) {
			Log20.e(e);
			return new Error(e.getMessage(), e.getCause());
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		URLRequest.RequestListener ln = request.getListener();
		if (ln != null)
			ln.onRequestCancel(request);
	}

	@Override
	protected void onPostExecute(Error result) {
		super.onPostExecute(result);
		if(this.isCancelled())
			return;
		if (result != null)
			Log20.e(result.getMessage());
		URLRequest.RequestListener ln = request.getListener();
		if (ln != null) {
			if (result == null)
				ln.onRequestFinishLoad(request);
			else
				ln.onRequestFail(request, result);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if(this.isCancelled())
			return;
		URLRequest.RequestListener ln = request.getListener();
		if (ln != null)
			ln.onRequestStart(request);
	}

	@Override
	protected void onProgressUpdate(Double... values) {
		super.onProgressUpdate(values);
		if(this.isCancelled())
			return;
		Double val = values[0];
		URLRequest.RequestListener ln = request.getListener();
		if (ln != null)
			ln.onRequestProgress(request, val);
	}

}
