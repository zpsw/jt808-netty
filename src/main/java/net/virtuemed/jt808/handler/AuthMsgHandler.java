package net.virtuemed.jt808.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import net.virtuemed.jt808.config.ChannelManager;
import net.virtuemed.jt808.vo.req.AuthMsg;
import net.virtuemed.jt808.vo.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description: 鉴权消息->CommonResp
 * @Version: 1.0
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class AuthMsgHandler extends BaseHandler<AuthMsg> {

    @Autowired
    private ChannelManager channelManager;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, AuthMsg msg) throws Exception {
        log.debug(msg.toString());
        channelManager.add(msg.getHeader().getTerminalPhone(), ctx.channel());
        CommonResp resp = CommonResp.success(msg, getSerialNumber(ctx.channel()));
        write(ctx,resp);
    }
}
