package org.ngsdev.android.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
public class MD5Digest {
	private byte[] _digest;
	public MD5Digest(File file) throws IOException {
		MessageDigest md = getMDInstance();
		FileInputStream in = new FileInputStream(file);
		byte[] dat = new byte[256];
		int len;
		while ((len = in.read(dat)) >= 0) {
			md.update(dat, 0, len);
		}
		in.close();
		_digest = md.digest();
	}

	public MD5Digest(String str) {
		MessageDigest md = getMDInstance();
		byte[] dat = str.getBytes();
		md.update(dat);
		_digest = md.digest();
	}

	public MD5Digest(byte[] bytes) {
		MessageDigest md = getMDInstance();
		md.update(bytes);
		_digest = md.digest();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < _digest.length; i++) {
			int d = _digest[i];
			if (d < 0)
				d += 256;
			if (d < 16)
				buf.append("0");
			buf.append(Integer.toString(d, 16));
		}
		return buf.toString();
	}

	public byte[] getDigest() {
		return _digest;
	}

	private MessageDigest getMDInstance() {
		try {
			return MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

}
