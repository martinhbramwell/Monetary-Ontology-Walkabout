package net.justtrade.rest.util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

public class JSONHelper {
	
	public static JSONObject put(JSONObject _json, String _key, String _val)
	{
		if ((_val == null)  ||  (_key == null))  return _json;
		try {
			_json.put(_key, _val);
		} catch (JSONException jsonex) {
			// eat exception because values won't be null
		}
		return _json;
	}

}
