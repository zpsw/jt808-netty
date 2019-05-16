package net.virtuemed.jt808.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.EventExecutorGroup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Zpsw
 * @Date: 2019-05-16
 * @Description: Channel管理，header中的terminalPhone作为主键，实现参考DefaultChannelGroup
 * @Version: 1.0
 */
@Slf4j
@Component
public class ChannelManager {

    private static final AttributeKey<String> TERMINAL_PHONE = AttributeKey.newInstance("terminalPhone");

    @Autowired
    @Qualifier("businessGroup")
    private EventExecutorGroup businessGroup;

    private ChannelGroup channelGroup;

    private Map<String, ChannelId> channelIdMap = new ConcurrentHashMap<>();

    @PostConstruct
    public void initGroup() {
        channelGroup = new DefaultChannelGroup(businessGroup.next());
    }

    public boolean add(String terminalPhone, Channel channel) {
        boolean added = channelGroup.add(channel);
        if (added) {
            channel.attr(TERMINAL_PHONE).set(terminalPhone);//留给广播使用
            channelIdMap.put(terminalPhone, channel.id());
        }
        return added;
    }

    public void remove(String terminalPhone) {
        channelGroup.remove(channelIdMap.remove(terminalPhone));
    }

    public Channel get(String terminalPhone) {
        Channel channel = channelGroup.find(channelIdMap.get(terminalPhone));
        if (channel == null) {
            channelIdMap.remove(terminalPhone);
        }
        return channel;
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }
}
