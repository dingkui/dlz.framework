package com.dlz.comm.util.jackson;

import com.dlz.comm.util.DateUtil;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * java 8 时间默认序列化
 *
 * @author dk
 */
public class DlzJavaTimeModule extends SimpleModule {
	public static final DlzJavaTimeModule INSTANCE = new DlzJavaTimeModule();

	private DlzJavaTimeModule() {
		super(PackageVersion.VERSION);
		this.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtil.DATETIME.formatter));
		this.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateUtil.DATE.formatter));
		this.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtil.TIME.formatter));
		this.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtil.DATETIME.formatter));
		this.addSerializer(LocalDate.class, new LocalDateSerializer(DateUtil.DATE.formatter));
		this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtil.TIME.formatter));
	}

}
