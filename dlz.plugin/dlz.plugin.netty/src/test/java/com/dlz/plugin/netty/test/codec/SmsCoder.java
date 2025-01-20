package com.dlz.plugin.netty.test.codec;

import com.dlz.comm.exception.BussinessException;
import com.dlz.plugin.netty.base.codec.ICoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 默认编码器
 * 传递数据编码成utf-8
 *
 * @author dingkui
 */
@Slf4j
public class SmsCoder implements ICoder<String, String> {
    public final Charset GBK=Charset.forName("GBK");

    private List<Byte> buffer=new ArrayList<>();

    private byte read(ByteBuf in){
        if (in.readerIndex() > in.writerIndex() - 1) {
            throw new BussinessException("当前读取结束，下次再读取");
        }
        return in.readByte();
    }
    @Override
    public String getOut(List<Byte> outBytes){
        byte[] out=new  byte[outBytes.size()];
        for (int i = 0; i < outBytes.size(); i++) {
            out[i]=outBytes.get(i);
        }
        return new String(out,GBK);
    }
    @Override
    public boolean decode(ByteBuf in, List<Byte> buf) throws Exception {
        //标记开始读取位置
        in.markReaderIndex();
        try{
            int index = findEndOfLine(in);
            if(index<0){
                while(in.isReadable()){
                    byte b=in.readByte();
                    buf.add(b);
                }
                return false;
            }
            int i=in.readerIndex();
            while(in.isReadable() && i++<index){
                byte b=in.readByte();
                buf.add(b);
            }
            if(in.getByte(index+1 ) == '\r'){
                in.readByte();
            }
            in.readByte();
            return true;
        }catch (BussinessException e){
            buf.clear();
            return false;
        }
    }

//
//    public String decode(ByteBuf in) throws Exception {
//        //标记开始读取位置
//        in.markReaderIndex();
//        try{
//            while (true) {
//                byte b = read(in);
//                if (b == '\r') {
//                    b = read(in);
//                    if (b == '\n') {
//                        break;
//                    }
//                }else{
//                    buffer.add(b);
//                }
//            }
//            byte[] bytes = new byte[buffer.size()];
//            for (int i = 0; i < buffer.size(); i++) {
//                bytes[i] = buffer.get(i);
//            }
//            String re=new String(bytes, GBK);
//            buffer.clear();
//            return re;
//        }catch (BussinessException e){
////            log.warn(e.getMsg());
//            return null;
//        }
//    }

    @Override
    public void writeObj(ByteBufOutputStream writer, String msg) throws Exception {
        writer.buffer().markWriterIndex();
        writer.write(msg.getBytes(GBK));
        writer.flush();
    }

    @Override
    public String getInfo(String in) {
        return in;
    }

    @Override
    public String mkOut(String result) {
        if (result == null) {
            return null;
        }
        return result+"\r\n";
    }
}
