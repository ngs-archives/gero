/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net;

public class URLRequestQueue {
  private static URLRequestQueue _instance;

  public static URLRequestQueue getInstance() {
    if (_instance == null) {
      _instance = new URLRequestQueue();
    }
    return _instance;
  }
}
