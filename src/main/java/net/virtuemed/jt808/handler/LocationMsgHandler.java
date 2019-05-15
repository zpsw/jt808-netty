package net.virtuemed.jt808.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;
import net.virtuemed.jt808.entity.LocationEntity;
import net.virtuemed.jt808.repository.LocationRepository;
import net.virtuemed.jt808.vo.req.LocationMsg;
import net.virtuemed.jt808.vo.resp.CommonResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description: 位置消息->CommonResp
 * @Version: 1.0
 */

@Slf4j
@Component
@ChannelHandler.Sharable
public class LocationMsgHandler extends BaseHandler<LocationMsg> {
    @Autowired
    private LocationRepository locationRespository;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LocationMsg msg) throws Exception {
        log.debug(msg.toString());
        locationRespository.save(LocationEntity.parseFromLocationMsg(msg));
        CommonResp resp = CommonResp.success(msg, getSerialNumber(ctx.channel()));
        writeAndFlush(ctx,resp);
    }
}
