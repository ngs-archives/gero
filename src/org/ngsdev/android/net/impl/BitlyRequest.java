package org.ngsdev.android.net.impl;

import java.net.URI;
import java.net.URLEncoder;

import org.ngsdev.android.net.URLRequest;
import org.ngsdev.android.net.URLRequestParams;

import android.content.Context;

public class BitlyRequest extends URLRequest {

  private static final String SHORTEN_API_URI = "http://api.bit.ly/v3/shorten";
  private static final String EXPAND_API_URI  = "http://api.bit.ly/v3/expand";

  private String              apiKey          = null;
  private String              login           = null;

  public BitlyRequest(Context context, String login, String apiKey) {
    super(context);
    this.setApiKey(apiKey);
    this.setLogin(login);
    this.response = new BitlyResponse();

  }

  public URLRequestParams getBaseParams() {
    URLRequestParams params = new URLRequestParams();
    params.setParameter("login", this.getLogin());
    params.setParameter("apiKey", this.getApiKey());
    params.setParameter("format", "json");
    return params;
  }

  public boolean shorten(String longUrl, URLRequest.RequestListener listener) {
    try {
      this.createHttpRequest(new URI(SHORTEN_API_URI), GET);
      URLRequestParams params = this.getBaseParams();
      params.setParameter("longUrl", URLEncoder.encode(longUrl, "UTF-8"));
      this.setURLRequestParams(params);
    } catch (Exception e) {
      return false;
    }
    return this.send(listener);
  }

  public boolean expand(String shortUrl, URLRequest.RequestListener listener) {
    try {
      this.createHttpRequest(new URI(EXPAND_API_URI), GET);
      URLRequestParams params = this.getBaseParams();
      params.setParameter("shortUrl", shortUrl);
      this.setURLRequestParams(params);
    } catch (Exception e) {
      return false;
    }
    return this.send(listener);
  }

  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  public String getApiKey() {
    return apiKey;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getLogin() {
    return login;
  }
}
