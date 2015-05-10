package com.exositesample;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;


public class HttpMethod {

	final static String API_URL = "http://m2.exosite.com/onep:v1/stack/alias";
	final static String CIK = "your key";
	
	public static String postData(String gps) {

		String URI = API_URL;
		
		HttpPost httpPost = new HttpPost(URI);
		httpPost.addHeader("X-Exosite-CIK", CIK);
		httpPost.addHeader("Accept", "application/x-www-form-urlencoded; charset=utf-8");
		HttpClient client = new DefaultHttpClient();
		ArrayList<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(new BasicNameValuePair("gps", gps));
		
		try {
			httpPost.setEntity(new UrlEncodedFormEntity(pairList, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//Execute and get the response.
		try {
		    HttpResponse response = client.execute(httpPost);
		    int StatusCode = response.getStatusLine().getStatusCode();
		    
		    if (StatusCode == HttpStatus.SC_NO_CONTENT) { //Status:204
		        return "ok";
		    }else {
		    	return "";
		    }
		} catch (ClientProtocolException e) {
		    // writing exception to log
		    e.printStackTrace();
		    return "";
		} catch (IOException e) {
		    // writing exception to log
		    e.printStackTrace();
		    return "";
		}
		finally {
			client.getConnectionManager().shutdown();
		}
		
	}
	
	public static String getData(String date) {

		String URI = API_URL + "?" + date ;
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(URI);
		httpGet.addHeader("X-Exosite-CIK", CIK);
		httpGet.addHeader("Accept", "application/x-www-form-urlencoded; charset=utf-8");
		String strResult = "";
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) {
				strResult = EntityUtils.toString(response.getEntity(),"utf-8");
			} else {
				strResult = "";
				Log.e("HttpMethod", "網路錯誤");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return strResult;
	}
	
}
