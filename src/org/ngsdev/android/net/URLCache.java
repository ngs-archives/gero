/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net;

import java.io.*;

import android.content.Context;

import org.ngsdev.android.util.*;

public class URLCache {
	private Context context = null;
	private URLRequest request;
	public static String CACHE_DIR = "andr20";
	public static String EXT_IN_PROGRESS = ".dl";
	private File tempFile;

	public URLCache(Context context, URLRequest request) {
		this.context = context;
		this.request = request;
	}
	public File getCacheDir() {
		return URLCache.getCacheDir(this.context);
	}
	
	public static File getCacheDir(Context c) {
		File cdir = new File(String.format("%s/%s", c.getCacheDir(),
				CACHE_DIR));
		if ((cdir==null||!cdir.exists()) && !cdir.mkdirs())
			return null;
		return cdir;
	}
	
	public static void clearAll(Context c) {
		FileUtil.emptyDir(getCacheDir(c));
	}

	public File getCacheFile() throws IOException {
		File file = null;
		File cdir = getCacheDir();
		String fname = request.getCacheKey();
		file = new File(cdir, fname);
		return file;
	}

	public File getTempFile() throws IOException {
		if (tempFile != null)
			tempFile.delete();
		tempFile = File.createTempFile(request.getCacheKey(), EXT_IN_PROGRESS,
				this.getCacheDir());
		return tempFile;
	}

	public boolean store() {
		File cf;
		try {
			cf = getCacheFile();
			cf.createNewFile();
			FileUtil.copy(tempFile, cf);
			tempFile.delete();
			tempFile = null;
			return true;
		} catch (IOException e) {
			Log20.e(e);
		}
		return false;
	}

	public boolean exists() {
		try {
			return getCacheFile().exists();
		} catch (IOException e) {
			Log20.e(String.format("%s: %s", e, e.getMessage()));
		}
		return false;
	}
	public boolean clear() {
		File f = null;
		try {
			f = this.getCacheFile();
			if (f != null)
				return f.delete();
		} catch (IOException e) {
			Log20.e(e);
		}
		return false;
	}
	
	public byte[] getCachedData() throws IOException {
		return FileUtil.getBytesFromFile(this.getCacheFile());
	}
}
