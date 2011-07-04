/** 
 * $Id$
 * $Author$
 * $Commiter$
 * $Date$
 */
package org.ngsdev.android.net.impl;

import java.io.CharArrayReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

public class XMLResponse extends TextResponse {
	private Document document;
	
	@Override
	public void processResponse(byte[] byteArray) throws Exception {
		super.processResponse(byteArray);
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Reader reader=new CharArrayReader(this.getResponseText().toCharArray());
		this.document = db.parse(new InputSource(reader));
	}

	public Document getDocument() {
		return this.document;
	}
}
