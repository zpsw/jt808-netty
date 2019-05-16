package net.virtuemed.jt808.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import net.virtuemed.jt808.vo.DataPacket;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description: 为所有业务handler提供一些基础方法
 * @Version: 1.0
 */
@Slf4j
public abstract class BaseHandler<T> extends SimpleChannelInboundHandler<T> {

    //消息流水号
    private static final AttributeKey<Short> SERIAL_NUMBER = AttributeKey.newInstance("serialNumber");

    /**
     * 递增获取流水号
     * @return
     */
    public short getSerialNumber(Channel channel){
        Attribute<Short> flowIdAttr = channel.attr(SERIAL_NUMBER);
        Short flowId = flowIdAttr.get();
        if (flowId == null) {
            flowId = 0;
        } else {
            flowId++;
        }
        flowIdAttr.set(flowId);
        return flowId;
    }

    public void write(ChannelHandlerContext ctx, DataPacket msg) {
        ctx.writeAndFlush(msg).addListener(future -> {
            if (!future.isSuccess()) {
                log.error("发送失败", future.cause());
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.error("exceptionCaught",cause);
        ctx.close();
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            log.warn("客户端{}读取超时，自动断开", ctx.channel().remoteAddress());
            ctx.close();
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }


}
