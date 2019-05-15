package net.virtuemed.jt808.vo.resp;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.virtuemed.jt808.util.JT808Const;
import net.virtuemed.jt808.vo.DataPacket;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:通用响应包
 * @Version: 1.0
 */
@Data
public class CommonResp extends DataPacket {

    public static final byte SUCCESS = 0;//成功/确认
    public static final byte FAILURE = 1;//失败
    public static final byte MSG_ERROR = 2;//消息有误
    public static final byte UNSUPPORTED = 3;//不支持
    public static final byte ALARM_PROCESS_ACK = 4;//报警处理确认

    private short replyFlowId; //应答流水号 2字节
    private short replyId; //应答 ID  2字节
    private byte result;    //结果 1字节

    public CommonResp() {
        this.getHeader().setMsgId(JT808Const.SERVER_RESP_COMMON);
    }

    @Override
    public ByteBuf toByteBufMsg() {
        ByteBuf bb = super.toByteBufMsg();
        bb.writeShort(replyFlowId);
        bb.writeShort(replyId);
        bb.writeByte(result);
        return bb;
    }

    public static CommonResp success(DataPacket msg, short flowId) {
        CommonResp resp = new CommonResp();
        resp.getHeader().setTerminalPhone(msg.getHeader().getTerminalPhone());
        resp.getHeader().setFlowId(flowId);
        resp.setReplyFlowId(msg.getHeader().getFlowId());
        resp.setReplyId(msg.getHeader().getMsgId());
        resp.setResult(SUCCESS);
        return resp;
    }
}
