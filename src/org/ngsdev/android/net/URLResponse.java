/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net;

import org.apache.http.HttpResponse;

public interface URLResponse {
  abstract void processResponse(HttpResponse res, byte[] byteArray) throws Exception;

}
