package net.virtuemed.jt808.vo.req;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.virtuemed.jt808.vo.DataPacket;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:鉴权包
 * @Version: 1.0
 */
@Data
public class AuthMsg extends DataPacket {

    private String authCode;//鉴权码

    public AuthMsg(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public void parseBody() {
        this.setAuthCode(readString(this.byteBuf.readableBytes()));
    }
}
