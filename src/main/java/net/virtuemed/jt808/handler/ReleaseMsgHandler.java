package net.virtuemed.jt808.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;
import lombok.extern.slf4j.Slf4j;
import net.virtuemed.jt808.vo.DataPacket;
import org.springframework.stereotype.Component;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description: 释放数据包中的ByteBuf，位置必须处于pipe in中的最后一个
 * @Version: 1.0
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class ReleaseMsgHandler extends SimpleChannelInboundHandler<DataPacket> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataPacket msg) throws Exception {
        ReferenceCountUtil.safeRelease(msg.getByteBuf());
    }

}
