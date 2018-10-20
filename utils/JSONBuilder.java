package indi.fs.common.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class JSONBuilder {
	private boolean error;
	private String message;
	private Object body;

	public JSONBuilder(boolean error, String message, Object body) {
		this.error = error;
		this.message = message;
		this.body = body;
	}

	public String build() {
		JSONObject json = new JSONObject();
		try {
			json.put("error", error);
			json.put("message", message);
			json.put("body", body);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json.toString();
	}

	public static String successResponse(Object body) {
		JSONBuilder json = new JSONBuilder(false, null, body);
		return json.build().toString();
	}

	public static String errorResponse(String message) {
		JSONBuilder json = new JSONBuilder(true, message, null);
		return json.build();
	}

}
