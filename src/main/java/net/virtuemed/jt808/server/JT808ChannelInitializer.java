package net.virtuemed.jt808.server;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import net.virtuemed.jt808.codec.JT808Decoder;
import net.virtuemed.jt808.codec.JT808Encoder;
import net.virtuemed.jt808.handler.*;
import net.virtuemed.jt808.util.JT808Const;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:
 * @Version: 1.0
 */
@Component
public class JT808ChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Value("${netty.read-timeout}")
    private int readTimeOut;

    @Value("${netty.threads.business}")
    private int businessThreadsNum;

    @Autowired
    private AuthMsgHandler authMsgHandler;
    @Autowired
    private HeartBeatMsgHandler heartBeatMsgHandler;
    @Autowired
    private LocationMsgHandler locationMsgHandler;
    @Autowired
    private LogOutMsgHandler logOutMsgHandler;
    @Autowired
    private RegisterMsgHandler registerMsgHandler;

    private EventExecutorGroup businessGroup;

    @PostConstruct
    public void initEventExecutorGroup() {
        businessGroup = new DefaultEventExecutorGroup(businessThreadsNum);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(
                new IdleStateHandler(readTimeOut, 0, 0, TimeUnit.MINUTES));
        // 1024表示单条消息的最大长度，解码器在查找分隔符的时候，达到该长度还没找到的话会抛异常
        pipeline.addLast(
                new DelimiterBasedFrameDecoder(1024, Unpooled.copiedBuffer(new byte[]{JT808Const.PKG_DELIMITER}),
                        Unpooled.copiedBuffer(new byte[]{JT808Const.PKG_DELIMITER, JT808Const.PKG_DELIMITER})));
        pipeline.addLast(new JT808Decoder());
        pipeline.addLast(new JT808Encoder());
        pipeline.addLast(authMsgHandler);
        pipeline.addLast(heartBeatMsgHandler);
        pipeline.addLast(locationMsgHandler);
        pipeline.addLast(logOutMsgHandler);
        pipeline.addLast(registerMsgHandler);

    }

}
