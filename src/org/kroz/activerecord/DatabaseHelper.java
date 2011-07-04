package org.kroz.activerecord;

import java.io.File;

import android.content.Context;

public class DatabaseHelper {
	public static void dropAllTables(Database db) throws ActiveRecordException {
		if (db.isOpen()) {
			String[] tables;
			try {
				tables = db.getTables();
				for (String table : tables) {
					if (table.equalsIgnoreCase("android_metadata"))
						continue;
					else
						db.execute("drop table if exists " + table);
				}
			} catch (ActiveRecordException e) {
				e.printStackTrace();
			}
		} else
			throw new ActiveRecordException(ErrMsg.ERR_DB_IS_NOT_OPEN);
	}

	public static boolean dropDatabase(Context ctx, String dbName) {
		File f = ctx.getDatabasePath(dbName);
		return f.delete();
	}

}
