package org.kroz.activerecord;

/**
 * Generic exception wrapper 
 * @author Vladimir Kroz
 */
public class ActiveRecordException extends Exception {
	private static final long serialVersionUID = -1305233534054765602L;

	public ActiveRecordException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public ActiveRecordException(String detailMessage) {
		super(detailMessage);
	}

	public ActiveRecordException(Throwable throwable) {
		super(throwable);
	}

	public ActiveRecordException() {
		super();
	}
}
