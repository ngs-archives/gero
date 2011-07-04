package org.kroz.activerecord.utils;

import android.util.Log;

/**
 * Extended android logger with 'printf'-style logging and controlled
 * indentation. Use it similar to standard Android Log class. To start or stop
 * indentations of log messages call method indents().
 * <p/>
 * Log messages priorities
 * <ul>
 * <li>VERBOSE 2 (0x00000002)
 * <li>DEBUG 3 (0x00000003)
 * <li>INFO 4 (0x00000004)
 * <li>WARN 5 (0x00000005)
 * <li>ERROR 6 (0x00000006)
 * <li>ASSERT 7 (0x00000007)
 * </ul>
 * 
 * @author Vladimir Kroz
 * 
 */
public class Logg {

	static int _startIdentLevel = -1;

	/**
	 * Switch which allows to turn logger on and off. When set to false - logger
	 * is disabled for all subsequent calls until this attributes is set to true
	 * again
	 */
	public static boolean enabled = true;
	public static boolean useIdents = false;

	/**
	 * @deprecated new recommended approach - set/unset attribute useIdents directly. 
	 * 
	 * @param flag
	 *            true to start indentation, false - to stop it
	 */
	public static void indents(boolean flag) {
		useIdents = flag;
		_startIdentLevel = Math.max(
				Thread.currentThread().getStackTrace().length - 1, 0);
	}

	// /**
	// * What a Terrible Failure: Report a condition that should never happen.
	// * The error will always be logged at level ASSERT with the call stack.
	// * Depending on system configuration, a report may be added to the
	// * {@link android.os.DropBoxManager} and/or the process may be terminated
	// * immediately with an error dialog.
	// * @param tag Used to identify the source of a log message.
	// * @param msg The message you would like logged.
	// */
	// public static int wtf(String tag, String msg) {
	// return wtf(tag, msg, null);
	// }
	//
	// /**
	// * What a Terrible Failure: Report an exception that should never happen.
	// * Similar to {@link #wtf(String, String)}, with an exception to log.
	// * @param tag Used to identify the source of a log message.
	// * @param tr An exception to log.
	// */
	// public static int wtf(String tag, Throwable tr) {
	// return wtf(tag, tr.getMessage(), tr);
	// }
	//
	// /**
	// * What a Terrible Failure: Report an exception that should never happen.
	// * Similar to {@link #wtf(String, Throwable)}, with a message as well.
	// * @param tag Used to identify the source of a log message.
	// * @param msg The message you would like logged.
	// * @param tr An exception to log. May be null.
	// */
	// public static int wtf(String tag, Throwable tr, String format, Object...
	// args) {
	// if (_useIdents)
	// format = getIdent() + format;
	// format = format.replaceAll("%t", Long.toString(Logg.t()));
	// return Log.wtf(tag, String.format(format, args), tr);
	// }

	/** Send a DEBUG log message. */
	public static int d(String tag, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return println(Log.DEBUG, tag, format, args);
		} else {
			return 0;
		}
	}

	/** Send a DEBUG log message and log the exception. */
	public static int d(String tag, Throwable tr, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return Log.d(tag, String.format(format, args), tr);
		} else {
			return 0;
		}
	}

	/** Send a ERROR log message. */
	public static int e(String tag, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return Log.e(tag, String.format(format, args));
		} else {
			return 0;
		}
	}

	/** Send a ERROR log message and log the exception. */
	public static int e(String tag, Throwable tr, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return Log.e(tag, String.format(format, args), tr);
		} else {
			return 0;
		}
	}

	/** Send a INFO log message. */
	public static int i(String tag, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return Log.i(tag, String.format(format, args));
		} else {
			return 0;
		}
	}

	/** Send a INFO log message and log the exception. */
	public static int i(String tag, Throwable tr, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return Log.i(tag, String.format(format, args), tr);
		} else {
			return 0;
		}
	}

	/**
	 * Checks to see whether or not a log for the specified tag is loggable at
	 * the specified level.
	 */
	public static boolean isLoggable(String tag, int level) {
		return Log.isLoggable(tag, level);
	}

	/** Low-level logging call. */
	public static int println(int priority, String tag, String format,
			Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			return Log.println(priority, tag, String.format(format, args));
		} else {
			return 0;
		}
	}

	/** Send a VERBOSE log message. */
	public static int v(String tag, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return println(Log.VERBOSE, tag, format, args);
		} else {
			return 0;
		}
	}

	/** Send a WARN log message. */
	public static int w(String tag, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return Log.w(tag, String.format(format, args));
		} else {
			return 0;
		}
	}

	/** Send a WARN log message and log the exception. */
	public static int w(String tag, Throwable tr, String format, Object... args) {
		if (Logg.enabled) {
			if (useIdents)
				format = getIdent() + format;
			format = format.replaceAll("%t", Long.toString(Logg.t()));
			return Log.w(tag, String.format(format, args), tr);
		} else {
			return 0;
		}
	}

	/** Handy function to get a loggable stack trace from a Throwable */
	public static String getStackTraceString(Throwable tr) {
		return Log.getStackTraceString(tr);
	}

	private static String getIdent() {
		if (useIdents) {
			int currentIdentLevel = Thread.currentThread().getStackTrace().length - 1;
			currentIdentLevel = Math.min(idents.length - 1,
					Math.max(0, currentIdentLevel - _startIdentLevel));
			return idents[currentIdentLevel];
		} else {
			return "";
		}
	}

	static long t() {
		return Thread.currentThread().getId();
	}

	static String[] idents = { "", " ", "  ", "   ", "    ", "     ", "      ",
			"       ", "        ", "         ", "          ", "           ",
			"            ", "             ", "              ",
			"               " };

}
