package com.cab404.libph.util;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Simple serialization tool
 *
 * @author cab404
 */
public class JSONable {

	private static Map<Class, Map<Field, String>> forms_cached = new ConcurrentHashMap<>();
	private static final String NULL_NAME = "NOMINAL";

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	public @interface JSONField {
		String value() default NULL_NAME;
	}

	private Map<Field, String> generateForm() {
		ConcurrentHashMap<Field, String> form = new ConcurrentHashMap<>();

		for (Field field : getClass().getDeclaredFields())
			if (field.isAnnotationPresent(JSONField.class)) {
				String name = field.getAnnotation(JSONField.class).value();

				if (name.equals(NULL_NAME))
					form.put(field, field.getName());
				else
					form.put(field, name);

			}

		return form;
	}

	@SuppressWarnings("unchecked")
	private Object jsonize(Object val)
	throws IllegalAccessException {
		if (val == null)
			return null;

		if (val.getClass().isEnum()) {
			return ((Enum) val).name();
		}

		if (val instanceof JSONable)
			return ((JSONable) val).toJSON();

		if (val instanceof Boolean)
			return val;

		if (val instanceof CharSequence || val instanceof Number)
			return val;

		if (val instanceof Calendar)
			return Tabun.toSQLDate((Calendar) val);

		if (val instanceof Map.Entry) {
			JSONObject e = new JSONObject();
			e.put("k", jsonize(((Map.Entry) val).getKey()));
			e.put("v", jsonize(((Map.Entry) val).getValue()));
			return e;
		}

		if (val instanceof List) {
			List list = (List) val;
			JSONArray array = new JSONArray();
			for (Object object : list)
				array.add(jsonize(object));
			return array;
		}

		throw new RuntimeException("Not jsonable: " + val.getClass());
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSON()
	throws IllegalAccessException {
		Map<Field, String> form;
		if (!forms_cached.containsKey(getClass()))
			forms_cached.put(getClass(), generateForm());
		form = forms_cached.get(getClass());

		JSONObject json = new JSONObject();

		for (Map.Entry<Field, String> e : form.entrySet()) {
			Object val = e.getKey().get(this);
			String key = e.getValue();

			if (val == null)
				continue;
			if (val instanceof List && ((List) val).isEmpty())
				continue;

			json.put(key, jsonize(val));
		}

		return json;
	}

}
