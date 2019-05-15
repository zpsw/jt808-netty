package net.virtuemed.jt808.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import net.virtuemed.jt808.vo.req.LogOutMsg;
import net.virtuemed.jt808.vo.resp.CommonResp;
import org.springframework.stereotype.Component;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description: 注销消息->CommonResp
 * @Version: 1.0
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class LogOutMsgHandler extends BaseHandler<LogOutMsg> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LogOutMsg msg) throws Exception {
        log.debug(msg.toString());
        CommonResp resp = CommonResp.success(msg, getSerialNumber(ctx.channel()));
        writeAndFlush(ctx,resp);
    }
}
