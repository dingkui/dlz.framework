package com.dlz.plugin.netty.base.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.ArrayList;
import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder { 
	private ICoder coder;
	List<Byte> outBuf = new ArrayList<>();

	public MessageDecoder(ICoder coder){
		this.coder=coder;
	}
	
    @Override  
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    	boolean complete = coder.decode(in,outBuf);
    	if(complete && outBuf.size()>0){
    		out.add(coder.getOut(outBuf));
			outBuf.clear();
    	}
		if(!complete && outBuf.size()>0){
			StringBuilder sb=new StringBuilder("decode not complete:");
			for (int i = 0; i < outBuf.size(); i++) {
				byte b =  outBuf.get(i);
				sb.append(b);
			}
			System.out.println(sb);
		}
    }  
}  