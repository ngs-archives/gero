package org.kroz.activerecord;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Internal framework class. Utilize DatabaseBuilder to produce DDL statements
 * directly out of Java classes.
 * 
 * @author Vladimir Kroz
 * @author JEREMYOT
 * 
 * <p>This project based on and inspired by 'androidactiverecord' project written by JEREMYOT</p>
 */
class DatabaseOpenHelper extends SQLiteOpenHelper {

	DatabaseBuilder _builder;
	int _version;

	/**
	 * Constructor
	 * 
	 * @param ctx
	 * @param dbPath
	 * @param dbVersion
	 * @param builder
	 */
	public DatabaseOpenHelper(Context ctx, String dbPath, int dbVersion,
			DatabaseBuilder builder) {
		super(ctx, dbPath, null, dbVersion);
		_builder = builder;
		_version = dbVersion;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		for (String table : _builder.getTables()) {
			String sqlStr = null;
			try {
				sqlStr = _builder.getSQLCreate(table);
			} catch (ActiveRecordException e) {
				Log.e(this.getClass().getName(), e.getMessage(), e);
			}
			if (sqlStr != null)
				db.execSQL(sqlStr);
		}
		db.setVersion(_version);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		for (String table : _builder.getTables()) {
			String sqlStr = _builder.getSQLDrop(table);
			db.execSQL(sqlStr);
		}
		onCreate(db);
	}
}
