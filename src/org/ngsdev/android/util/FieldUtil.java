package org.ngsdev.android.util;

public class FieldUtil {
  public static boolean isStringWithAnyText(Object val) {
    return val != null && String.class.isInstance(val) && !val.equals("");
  }
}
