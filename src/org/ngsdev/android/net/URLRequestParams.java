package org.ngsdev.android.net;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.ngsdev.android.util.ArrayUtil;

public class URLRequestParams {

  private HashMap<String, Object> parameters;

  public URLRequestParams() {
  }

  public void clear() {
    this.parameters = null;
  }

  public String[] getKeys() {
    ArrayList<String> list = new ArrayList<String>();

    if (this.parameters == null)
      return (String[]) list.toArray(new String[0]);

    Iterator<Entry<String, Object>> iter = parameters.entrySet().iterator();
    while (iter.hasNext()) {
      Entry<String, Object> me = iter.next();
      String key = me.getKey();
      if (key instanceof String)
        list.add(me.getKey());
    }
    return (String[]) list.toArray(new String[list.size()]);
  }

  public String toQueryString() {
    StringBuffer buf = new StringBuffer("");
    for (String key : this.getKeys()) {
      try {
        String value = this.getParameter(key).toString();
        value = URLEncoder.encode(value, "UTF-8");
        buf.append(String.format("%s=%s&", key, value));
      } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
      }
    }
    return buf.toString();
  }

  public Object getParameter(final String name) {
    Object param = null;
    if (this.parameters != null) {
      param = this.parameters.get(name);
    }
    return param;
  }

  public boolean isParameterSet(final String name) {
    return getParameter(name) != null;
  }

  public boolean isParameterSetLocally(final String name) {
    return this.parameters != null && this.parameters.get(name) != null;
  }

  public boolean isStringParameter(final String name) {
    return isParameterSet(name) && this.getParameter(name) instanceof String;
  }

  public boolean isBooleanParameter(final String name) {
    return isParameterSet(name) && this.getParameter(name) instanceof Boolean;
  }

  public boolean isDoubleParameter(final String name) {
    return isParameterSet(name) && this.getParameter(name) instanceof Double;
  }

  public boolean isIntParameter(final String name) {
    return isParameterSet(name) && this.getParameter(name) instanceof Integer;
  }

  public boolean isByteArrayParameter(final String name) {
    return isParameterSet(name) && this.getParameter(name) instanceof byte[];
  }

  public HttpEntity getEntity(final String name)
      throws UnsupportedEncodingException {
    Object val = this.getParameter(name);
    if (this.isStringParameter(name) || this.isDoubleParameter(name)
        || this.isIntParameter(name) || this.isBooleanParameter(name)

    )
      return new StringEntity(name, val.toString());
    if (this.isByteArrayParameter(name))
      return new ByteArrayEntity((byte[]) val);
    return null;
  }

  public boolean removeParameter(String name) {
    if (this.parameters == null) {
      return false;
    }
    if (this.parameters.containsKey(name)) {
      this.parameters.remove(name);
      return true;
    } else {
      return false;
    }
  }

  public URLRequestParams setParameter(String name, ArrayList<?> values) {
    return this.setParameter(name, ArrayUtil.join(values));
  }

  public URLRequestParams setParameter(final String name, final int value) {
    return this.setParameter(name, Integer.valueOf(value));
  }

  public URLRequestParams setParameter(final String name, final Object value) {
    if (this.parameters == null) {
      this.parameters = new HashMap<String, Object>();
    }
    this.parameters.put(name, value);
    return this;
  }

  public void setConditionParameter(String name, boolean cond,
      Object trueValue, Object falseValue) {
    this.setParameter(name, cond ? trueValue : falseValue);
  }

  public void setFlagParameter(String name, boolean bool) {
    if (bool)
      this.setParameter(name, 1);
    else
      this.removeParameter(name);
  }

  public void setBooleanParameter(String name, boolean value) {
    this.setParameter(name, Boolean.valueOf(value));
  }

  public void setDoubleParameter(String name, double value) {
    this.setParameter(name, Double.valueOf(value));
  }

  public void setIntParameter(String name, int value) {
    this.setParameter(name, Integer.valueOf(value));
  }

  public void setParameters(final String[] names, final Object value) {
    for (int i = 0; i < names.length; i++) {
      setParameter(names[i], value);
    }
  }
}
