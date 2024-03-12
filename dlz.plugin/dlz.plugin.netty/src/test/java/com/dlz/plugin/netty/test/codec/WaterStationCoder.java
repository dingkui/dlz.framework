package com.dlz.plugin.netty.test.codec;

import com.dlz.comm.exception.BussinessException;
import com.dlz.comm.util.ExceptionUtils;
import com.dlz.plugin.netty.base.codec.ICoder;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * 默认编码器
 * 传递数据编码成utf-8
 *
 * @author dingkui
 */
@Slf4j
public class WaterStationCoder implements ICoder<TestTranseDto, TestTranseDto> {

    private StringBuffer buffer=new StringBuffer();

    private byte read(ByteBuf in){
        if (in.readerIndex() > in.writerIndex() - 1) {
//            log.warn("IndexOutOfBoundsException：readerIndex({}) + length({}) exceeds writerIndex({})",in.readerIndex(),1,in.writerIndex());
            throw new BussinessException("当前读取结束，下次再读取");
        }
        return in.readByte();
    }
    @Override
    public TestTranseDto getOut(List<Byte> outBytes){
        StringBuffer buffer=new StringBuffer();
        for (int i = 0; i < outBytes.size(); i++) {
            byte b = outBytes.get(i);
            buffer.append((char)b);
        }
        TestTranseDto dto = new TestTranseDto();
        //包头 字符 2 固定为##
        dto.setHead(buffer.substring(0,2));
        //数据段长度 十进制整数 4 数据段的 ASCII 字符数，如：长 255，则写为“0255”
        dto.setLength(buffer.substring(2,6));
        //数据段 字符 0<n<1024 变长的数据，详见 6.5 章节的表 3《数据段结构组成表》
        Integer integer = Integer.valueOf(dto.getLength());
        if(buffer.length()!=integer+12){
            log.warn("消息有误：bufferLenth:{},msgLength:{},msgs:{}",buffer.length(),integer,buffer);
            buffer.setLength(0);
            return null;
        }
        dto.setData(buffer.substring(6,integer+6));
        dto.setCrc(buffer.substring(integer+6,integer+10));
        dto.setCrc(buffer.substring(integer+10,integer+12));
        return dto;
    }

    @Override
    public boolean decode(ByteBuf in, List<Byte> buf) throws Exception {
        //标记开始读取位置
        in.markReaderIndex();
        try{
            int i = findEndOfLine(in);
            if(i>0){
                byte[] bytes=new byte[in.readableBytes()];
                in.readBytes(bytes);
                for (int j = 0; j < bytes.length; j++) {
                    buf.add(bytes[j]);
                }
                return false;
            }
            byte[] bytes=new byte[i-in.readerIndex()];
            in.readBytes(bytes);
            for (int j = 0; j < bytes.length; j++) {
                buf.add(bytes[j]);
            }
            return true;
        }catch (BussinessException e){
//            log.warn(e.getMsg());
            buf.clear();
            return false;
        }
    }

//    @Override
//    public TestTranseDto decode(ByteBuf in) throws Exception {
//        //标记开始读取位置
//        in.markReaderIndex();
//        TestTranseDto dto = new TestTranseDto();
//        try{
//            while (true) {
//                byte b = read(in);
//                buffer.append((char)b);
//
//                if (b == '#') {
//                    b = read(in);
//                    if (b == '#') {
//                        int size = buffer.length();
//                        if(size >1){
//                            log.warn("非法消息："+buffer);
//                        }
//                        buffer.setLength(0);
//                        buffer.append("##");
//                    }
//                }
//
//                if (b == '\r') {
//                    b = read(in);
//                    if (b == '\n') {
//                        buffer.append("\n");
//                        break;
//                    }
//                }
//            }
//
//            //包头 字符 2 固定为##
//            dto.setHead(buffer.substring(0,2));
//            //数据段长度 十进制整数 4 数据段的 ASCII 字符数，如：长 255，则写为“0255”
//            dto.setLength(buffer.substring(2,6));
//            //数据段 字符 0<n<1024 变长的数据，详见 6.5 章节的表 3《数据段结构组成表》
//            Integer integer = Integer.valueOf(dto.getLength());
//            if(buffer.length()!=integer+12){
//                log.warn("消息有误：bufferLenth:{},msgLength:{},msgs:{}",buffer.length(),integer,buffer);
//                buffer.setLength(0);
//                return null;
//            }
//            dto.setData(buffer.substring(6,integer+6));
//            dto.setCrc(buffer.substring(integer+6,integer+10));
//            dto.setCrc(buffer.substring(integer+10,integer+12));
//            buffer.setLength(0);
//            //包尾 字符 2 固定为<CR><LF>（回车，换行）
//            return dto;
//        }catch (BussinessException e){
////            log.warn(e.getMsg());
//            return null;
//        }
//    }

    @Override
    public void writeObj(ByteBufOutputStream writer, TestTranseDto msg) throws Exception {
        writer.buffer().markWriterIndex();
        writer.write(msg.getHead().getBytes());
        writer.write(msg.getLength().getBytes());
        writer.write(msg.getData().getBytes());
        writer.write(msg.getCrc().getBytes());
        writer.write(msg.getEnd().getBytes());
        writer.flush();
    }

    @Override
    public String getInfo(TestTranseDto transeDto) {
        return transeDto.getData();
    }

    @Override
    public TestTranseDto mkOut(String result) {
        if (result == null) {
            return null;
        }
        TestTranseDto out = new TestTranseDto();
        out.setHead("##");
        byte[] bytes = result.getBytes();
        String len = String.valueOf(result.length());
        while (len.length() < 4) {
            len = "0" + len;
        }
        out.setLength(len);
        out.setData(result);
        String crc = getCRC(bytes);
        out.setCrc(crc);
        out.setEnd("\r\n");
        return out;
    }


    /**
     * 计算CRC16校验码
     *
     * @param puchMsg
     * @return
     */
    private static String getCRC(byte[] puchMsg) {
        int crc_reg = 0xFFFF;
        int usDataLen = puchMsg.length;
        int i, j, check;
        for (i = 0; i < usDataLen; i++) {
            crc_reg = (crc_reg >> 8) ^ puchMsg[i];
            for (j = 0; j < 8; j++) {
                check = crc_reg & 0x0001;
                crc_reg >>= 1;
                if (check == 0x0001) {
                    crc_reg ^= 0xA001;
                }
            }
        }
        String s = Integer.toHexString(crc_reg).toUpperCase();
        while(s.length()<4){
            s="0"+s;
        }
        return s;
    }
}
