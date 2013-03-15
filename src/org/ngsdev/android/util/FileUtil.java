package org.ngsdev.android.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;

public class FileUtil {
  public static void copy(File origin, File dest) throws IOException {
    Log20.i(String.format("Copying %s to %s", origin, dest));
    InputStream in = new FileInputStream(origin);
    OutputStream out = new FileOutputStream(dest);
    BufferedInputStream bin = new BufferedInputStream(in);
    BufferedOutputStream bout = new BufferedOutputStream(out, in.available());
    byte buf[] = new byte[1024];
    int c;
    while ((c = bin.read(buf, 0, buf.length)) != -1) {
      bout.write(buf, 0, c);
    }
    bin.close();
    bout.close();
    Log20.i("Copied");
  }

  public static byte[] getBytesFromFile(File file) throws IOException {
    Log20.d("file: %s", file);
    InputStream is = new FileInputStream(file);
    byte[] bytes = getBytesFromInputStream(is);
    is.close();
    return bytes;
  }

  public static byte[] getBytesFromAssetName(Context context, String name)
      throws IOException {
    InputStream is = context.getResources().getAssets().open(name);
    byte[] bytes = getBytesFromInputStream(is);
    is.close();
    return bytes;
  }

  public static byte[] getBytesFromResourceId(Context context, int id)
      throws IOException {
    InputStream is = context.getResources().openRawResource(id);
    byte[] bytes = getBytesFromInputStream(is);
    is.close();
    return bytes;
  }

  public static byte[] getBytesFromInputStream(InputStream is)
      throws IOException {
    long length = is.available();
    if (length > Integer.MAX_VALUE) {
      throw new IOException("Data is too large");
    }
    byte[] bytes = new byte[(int) length];
    int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
        && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
      offset += numRead;
    }
    if (offset < bytes.length) {
      throw new IOException("Could not completely read file");
    }
    return bytes;
  }

  public static void writeBytesToFile(File file, byte[] byteArray)
      throws IOException {
    FileOutputStream fos = new FileOutputStream(file);
    fos.write(byteArray);
    fos.close();
  }

  public static void emptyDir(File dir) {
    if (dir == null || dir.isFile())
      return;
    File[] files = dir.listFiles();
    if (files == null)
      return;
    for (File file : files) {
      Log20.i("Unlinking: %s", file);
      file.delete();
    }
  }
}
