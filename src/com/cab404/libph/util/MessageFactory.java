package com.cab404.libph.util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * Показывает на экране те самые зелёные и красные окошки с Табуна. Почти.
 *
 * @author cab404
 */
public class MessageFactory {
	private static JSONParser parser = new JSONParser();

	private static MessageListener impl = new MessageListener() {
		@Override public void show(JSONObject parsed) {}
	};

	public static void setListener(MessageListener impl) {
		MessageFactory.impl = impl;
	}

	public static JSONObject processJSONwithMessage(String json) {
		try {
			JSONObject parsed = (JSONObject) parser.parse(json);
			impl.show(parsed);

			return parsed;
		} catch (ParseException | VerifyError e) {
			e.printStackTrace();
			System.err.println(json);
		}

		return null;
	}

	/**
	 * Вся его работа - принимать строки о том,
	 * что нельзя давать комментариям больше одного минуса
	 * и заходить в Табун с неправильным паролем.
	 */
	public static interface MessageListener {
		public void show(JSONObject parsed);
	}
}
