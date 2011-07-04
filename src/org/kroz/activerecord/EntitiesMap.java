package org.kroz.activerecord;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 
 * @author Vladimir Kroz
 * @author JEREMYOT
 * <p>This project based on and inspired by 'androidactiverecord' project written by JEREMYOT</p>
 */
class EntitiesMap {
	private Map<String, WeakReference<ActiveRecordBase>> map = new HashMap<String, WeakReference<ActiveRecordBase>>();
	WeakHashMap<ActiveRecordBase, String> _map = new WeakHashMap<ActiveRecordBase, String>(); 

	@SuppressWarnings("unchecked")
	<T extends ActiveRecordBase> T get(Class<T> c, long id) {
		String key = makeKey(c, id);
		WeakReference<ActiveRecordBase> i = map.get(key);
		if (i == null)
			return null;
		return (T) i.get();
	}

	void set(ActiveRecordBase e) {
		String key = makeKey(e.getClass(), e.getID());
		map.put(key, new WeakReference<ActiveRecordBase>(e));
	}

	private String makeKey(Class<? extends ActiveRecordBase> entityType, long id) {
		StringBuilder sb = new StringBuilder();
		sb	.append(entityType.getName())
			.append(id);
		return sb.toString();
	}
}
