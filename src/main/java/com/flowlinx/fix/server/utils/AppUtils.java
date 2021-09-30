package com.flowlinx.fix.server.utils;

import com.flowlinx.fix.server.domain.FixEntity;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.MessageParseError;
import quickfix.field.MsgType;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AppUtils {

	private static final Map<String, String> map = new HashMap<>();

	static  {
		final Field[] declaredFields = MsgType.class.getDeclaredFields();
		for (Field field : declaredFields) {
			if (java.lang.reflect.Modifier.isStatic(field.getModifiers() ) ) {
				field.setAccessible( true );
				try {
					map.put( field.get( MsgType.class ).toString(), field.getName() );
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<T> getParameterizedClassByIndex(Class<?> clazz, int index) {

		Class<T> typeClass = null;
		final Type[] arguments = ( (ParameterizedType) clazz.getGenericSuperclass() ).getActualTypeArguments();

		if( arguments != null && arguments.length >= index) {
			typeClass = (Class<T>) arguments[ index ];
		}

		return typeClass;
	}

	public static MsgType getMsgType(String text){

		MsgType msgType = null;

		try {
			msgType = Message.identifyType( text );
		} catch (MessageParseError messageParseError) {
		}

		return msgType;
	}

	public static <E extends FixEntity> List<E> getContent(List<E> list){
		return list.stream().map(m -> {
			m.setMsgType( map.get( AppUtils.getMsgType( m.getText() ).getValue() ) );
			return m;
		}).collect(Collectors.toList());
	}

	public static String getString(Message message, Integer key){
		String value = null;
		try {
			value = message.getString( key );
		} catch (FieldNotFound fieldNotFound) {}

		return value;
	}

	public static String getString(Message.Header header, Integer key){
		String value = null;
		try {
			value = header.getString( key );
		} catch (FieldNotFound fieldNotFound) {}

		return value;
	}


}