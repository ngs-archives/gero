package org.ngsdev.android.model;

import java.util.List;

import org.kroz.activerecord.ActiveRecordBase;
import org.kroz.activerecord.ActiveRecordException;
import org.kroz.activerecord.Database;
import org.kroz.activerecord.DatabaseBuilder;
import org.ngsdev.android.util.Log20;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class ObjectManager {

  private static ActiveRecordBase db        = null;
  public static String            dbName    = null;

  public static int               dbVersion = -1;

  public static void buildDB(String dbName, int dbVersion,
      Class<? extends ActiveRecordBase>... args) {
    DatabaseBuilder builder = new DatabaseBuilder(dbName);
    for (Class<? extends ActiveRecordBase> klass : args) {
      builder.addClass(klass);
    }
    Database.setBuilder(builder);
    ObjectManager.dbName = dbName;
    ObjectManager.dbVersion = dbVersion;
  }

  private Context           context = null;

  private SharedPreferences pref    = null;

  public ObjectManager(Context context) {
    this.context = context;
  }

  public ManagedObject add(ManagedObject src) throws ActiveRecordException {
    ManagedObject newObj = (ManagedObject) this.getDB().newEntity(
        this.getEntityClass());
    newObj.copyFrom(src);
    return newObj;
  }

  @SuppressWarnings("unchecked")
  public List<ManagedUri> all() throws ActiveRecordException {
    return (List<ManagedUri>) this.getDB().findAll(this.getEntityClass());
  }

  public void close() {
    if (db != null && db.isOpen()) {
      db.close();
      db = null;
    }
  }

  public void delete() throws ActiveRecordException {
    this.getDB().delete(this.getEntityClass(), null, null);
  }

  @SuppressWarnings("unchecked")
  public List<ManagedObject> find(boolean distinct, String whereClause,
      String[] whereArgs, String groupBy, String having, String orderBy,
      int limit) throws ActiveRecordException {
    return (List<ManagedObject>) this.getDB().find(this.getEntityClass(),
        distinct, whereClause, whereArgs, groupBy, having, orderBy,
        Integer.toString(limit));
  }

  @SuppressWarnings("unchecked")
  public List<ManagedObject> find(String whereClause, String[] whereArgs)
      throws ActiveRecordException {
    return (List<ManagedObject>) this.getDB().find(this.getEntityClass(),
        whereClause, whereArgs);
  }

  public Context getContext() {
    return context;
  }

  public ActiveRecordBase getDB() throws ActiveRecordException {
    if (db != null) {
      if (db.isOpen())
        return db;
      db.close();
    }
    db = ActiveRecordBase.open(this.getContext(), dbName, dbVersion);
    return db;
  }

  public abstract Class<? extends ActiveRecordBase> getEntityClass();

  public String getMaxPrefKey() {
    return null;
  }

  public int getMaxstorable() {
    String key = this.getMaxPrefKey();
    if (key == null || key.equals(""))
      return 0;
    return this.getSharedPreferences().getInt(key, 20);
  }

  public String getPrefName() {
    return "android20prefs";
  }

  private SharedPreferences getSharedPreferences() {
    if (pref == null)
      pref = context.getSharedPreferences(this.getPrefName(),
          Context.MODE_PRIVATE);
    return pref;
  }

  public void setMaxStorable(int max) {
    SharedPreferences.Editor e = this.getSharedPreferences().edit();
    e.putInt(this.getMaxPrefKey(), max);
    e.commit();
    try {
      this.trimToMax();
    } catch (ActiveRecordException e1) {
      Log20.e(e1);
    }
  }

  public int size() {
    try {
      return this.all().size();
    } catch (ActiveRecordException e) {
      Log20.e(e);
    }
    return 0;
  }

  public void trimToMax() throws ActiveRecordException {
    int max = this.getMaxstorable();
    int limit = this.size() - max;
    if (max <= 0 || limit <= 0)
      return;
    List<ManagedObject> list = this.find(false, null, null, null, null,
        "timestamp ASC", limit);
    for (ManagedObject item : list) {
      item.delete();
    }
  }

}
