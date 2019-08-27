package net.virtuemed.jt808.vo.resp;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.virtuemed.jt808.config.JT808Const;
import net.virtuemed.jt808.vo.DataPacket;
import org.apache.commons.lang3.StringUtils;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:注册响应包
 * @Version: 1.0
 */
@Data
public class RegisterResp extends DataPacket {

    public static final byte SUCCESS = 0;//成功
    public static final byte VEHICLE_ALREADY_REGISTER = 1;//车辆已被注册
    public static final byte NOT_IN_DB = 2;//数据库无该车辆
    public static final byte TERMINAL_ALREADY_REGISTER = 3;//终端已被注册

    private short replyFlowId; //应答流水号 2字节
    private byte result;    //结果 1字节
    private String authCode; //鉴权码

    public RegisterResp() {
        this.getHeader().setMsgId(JT808Const.SERVER_RESP_REGISTER);
    }

    @Override
    public ByteBuf toByteBufMsg() {
        ByteBuf bb = super.toByteBufMsg();
        bb.writeShort(replyFlowId);
        bb.writeByte(result);
        if (result == SUCCESS && StringUtils.isNotBlank(authCode)) {//成功才写入鉴权码
            bb.writeBytes(authCode.getBytes(JT808Const.DEFAULT_CHARSET));
        }
        return bb;
    }

    public static RegisterResp success(DataPacket msg, short flowId) {
        RegisterResp resp = new RegisterResp();
        resp.getHeader().setTerminalPhone(msg.getHeader().getTerminalPhone());
        resp.getHeader().setFlowId(flowId);
        resp.setReplyFlowId(msg.getHeader().getFlowId());
        resp.setResult(SUCCESS);
        resp.setAuthCode("SUCCESS");
        return resp;
    }
}
