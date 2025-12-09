package com.dlz.plugin.netty.base.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.util.ByteProcessor;

import java.util.List;

public interface ICoder<IN,OUT> {

	IN getOut(List<Byte> outBytes);

	boolean decode(ByteBuf in, List<Byte> outBytes) throws Exception;

//	IN decode(ByteBuf in) throws Exception;
	
	void writeObj(ByteBufOutputStream writer, OUT m) throws Exception;

	String getInfo(IN in);

	OUT mkOut(String result);

	default  int findEndOfLine(ByteBuf buffer) {
		int n = buffer.forEachByte(ByteProcessor.FIND_LF);
		if(n>0 && buffer.getByte(n -1 ) == '\r'){
			n--;
		}
		return n;
	}
}
