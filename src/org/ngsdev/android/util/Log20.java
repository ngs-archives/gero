package org.ngsdev.android.util;

import android.util.Log;

public class Log20 {
  public static String  Tag    = "Android20";
  public static boolean enable = false;

  public static void d(String msg) {
    if (enable)
      Log.d(Tag, msg);
  }

  public static void d(String format, Object... args) {
    if (enable)
      Log.d(Tag, String.format(format, args));
  }

  public static void v(String msg) {
    if (enable)
      Log.v(Tag, msg);
  }

  public static void w(String msg) {
    if (enable)
      Log.w(Tag, msg);
  }

  public static void i(String msg) {
    if (enable)
      Log.i(Tag, msg);
  }

  public static void i(String format, Object... args) {
    if (enable)
      Log.i(Tag, String.format(format, args));
  }

  public static void e(String msg) {
    Log.e(Tag, msg);
  }

  public static void e(String msg, Throwable tr) {
    if (enable)
      Log.e(Tag, msg, tr);
  }

  public static void e(Throwable tr) {
    if (enable)
      Log.e(Tag, tr.getMessage(), tr);
  }
}
