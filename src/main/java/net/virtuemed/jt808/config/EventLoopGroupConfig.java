package net.virtuemed.jt808.config;

import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.concurrent.DefaultEventExecutorGroup;
import io.netty.util.concurrent.EventExecutorGroup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: Zpsw
 * @Date: 2019-05-16
 * @Description:
 * @Version: 1.0
 */
@Configuration
public class EventLoopGroupConfig {

    @Value("${netty.threads.boss}")
    private int bossThreadsNum;

    @Value("${netty.threads.worker}")
    private int workerThreadsNum;

    @Value("${netty.threads.business}")
    private int businessThreadsNum;

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
