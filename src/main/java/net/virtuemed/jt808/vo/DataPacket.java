package net.virtuemed.jt808.vo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.util.ReferenceCountUtil;
import lombok.Data;
import net.virtuemed.jt808.util.BCD;
import net.virtuemed.jt808.config.JT808Const;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:
 * @Version: 1.0
 */
@Data
public class DataPacket {

    protected Header header = new Header(); //消息头
    protected ByteBuf byteBuf; //消息体

    public DataPacket() {
    }

    public DataPacket(ByteBuf byteBuf) {
        this.byteBuf = byteBuf;
    }

    public void parse() {
        try{
            this.parseHead();
            //验证包体长度
            if (this.header.getMsgBodyLength() != this.byteBuf.readableBytes()) {
                throw new RuntimeException("包体长度有误");
            }
            this.parseBody();
        }finally {
            ReferenceCountUtil.safeRelease(this.byteBuf);
        }
    }

    protected void parseHead() {
        header.setMsgId(byteBuf.readShort());
        header.setMsgBodyProps(byteBuf.readShort());
        header.setTerminalPhone(BCD.BCDtoString(readBytes(6)));
        header.setFlowId(byteBuf.readShort());
        if (header.hasSubPackage()) {
            //TODO 处理分包
            byteBuf.readInt();
        }
    }

    /**
     * 请求报文重写
     */
    protected void parseBody() {

    }

    /**
     * 响应报文重写 并调用父类
     * @return
     */
    public ByteBuf toByteBufMsg() {
        ByteBuf bb = ByteBufAllocator.DEFAULT.heapBuffer();//在JT808Encoder escape()方法处回收
        bb.writeInt(0);//先占4字节用来写msgId和msgBodyProps，JT808Encoder中覆盖回来
        bb.writeBytes(BCD.toBcdBytes(StringUtils.leftPad(this.header.getTerminalPhone(), 12, "0")));
        bb.writeShort(this.header.getFlowId());
        //TODO 处理分包
        return bb;
    }

    /**
     * 从ByteBuf中read固定长度的数组,相当于ByteBuf.readBytes(byte[] dst)的简单封装
     * @param length
     * @return
     */
    public byte[] readBytes(int length) {
        byte[] bytes = new byte[length];
        this.byteBuf.readBytes(bytes);
        return bytes;
    }

    /**
     * 从ByteBuf中读出固定长度的数组 ，根据808默认字符集构建字符串
     * @param length
     * @return
     */
    public String readString(int length) {
       return new String(readBytes(length),JT808Const.DEFAULT_CHARSET);
    }

    @Data
    public static class Header {
        private short msgId;// 消息ID 2字节
        private short msgBodyProps;//消息体属性 2字节
        private String terminalPhone; // 终端手机号 6字节
        private short flowId;// 流水号 2字节

        //获取包体长度
        public short getMsgBodyLength() {
            return (short) (msgBodyProps & 0x3ff);
        }

        //获取加密类型 3bits
        public byte getEncryptionType() {
            return (byte) ((msgBodyProps & 0x1c00) >> 10);
        }

        //是否分包
        public boolean hasSubPackage() {
            return ((msgBodyProps & 0x2000) >> 13) == 1;
        }
    }
}
