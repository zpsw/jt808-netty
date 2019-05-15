package net.virtuemed.jt808.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:
 * @Version: 1.0
 */

@Slf4j
@Component
public class NettyTcpServer {

    @Value("${netty.port}")
    private int port;

    @Value("${netty.threads.boss}")
    private int bossThreadsNum;

    @Value("${netty.threads.worker}")
    private int workerThreadsNum;

    @Value("${netty.threads.business}")
    private int businessThreadsNum;

    @Autowired
    private JT808ChannelInitializer jt808ChannelInitializer;

    private volatile boolean isStarted = false;


    public synchronized void start() {
        if (this.isStarted) {
            throw new IllegalStateException("TCP服务正在运行中，监听端口:" + port);
        }
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup(), workerGroup())
                    .channel(NioServerSocketChannel.class)
                    .childHandler(jt808ChannelInitializer)
                    .option(ChannelOption.SO_BACKLOG, 1024) //服务端可连接队列数,对应TCP/IP协议listen函数中backlog参数
                    .childOption(ChannelOption.TCP_NODELAY, true)//立即写出
                    .childOption(ChannelOption.SO_KEEPALIVE, true);//长连接
            ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.SIMPLE);//内存泄漏检测 开发推荐PARANOID 线上SIMPLE
            this.isStarted = true;
            log.info("TCP服务启动完毕,port={}", this.port);
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error("TCP服务启动出错:{}", e.getMessage());
        }
    }

    /**
     * 负责TCP连接建立操作 绝对不能阻塞
     * @return
     */
    @Bean(name = "bossGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossThreadsNum);
    }

    /**
     * 负责Socket读写操作 绝对不能阻塞
     * @return
     */
    @Bean(name = "workerGroup", destroyMethod = "shutdownGracefully")
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerThreadsNum);
    }

    /**
     * Handler中出现IO操作(如数据库操作，网络操作)使用这个
     * @return
     */
    @Bean(name = "businessGroup",destroyMethod = "shutdownGracefully")
    public EventExecutorGroup businessGroup() {
       return new DefaultEventExecutorGroup(businessThreadsNum);
    }

}
