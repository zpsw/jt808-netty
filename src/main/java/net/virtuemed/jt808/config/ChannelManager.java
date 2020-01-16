package net.virtuemed.jt808.config;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Zpsw
 * @Date: 2019-05-16
 * @Description: 使用ChannelGroup管理Channel, 维护terminalPhone->ChannelId->Channel 一对一映射关系
 * @Version: 1.0
 */
@Slf4j
@Component
public class ChannelManager {

    private static final AttributeKey<String> TERMINAL_PHONE = AttributeKey.newInstance("terminalPhone");

    private ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Map<String, ChannelId> channelIdMap = new ConcurrentHashMap<>();

    private ChannelFutureListener remover = future -> {
        String phone = future.channel().attr(TERMINAL_PHONE).get();
        if (channelIdMap.get(phone) == future.channel().id()) {
            channelIdMap.remove(phone);
        }
    };

    public boolean add(String terminalPhone, Channel channel) {
        boolean added = channelGroup.add(channel);
        if (added) {
            if (channelIdMap.containsKey(terminalPhone)) {//替换
                Channel old = get(terminalPhone);
                old.closeFuture().removeListener(remover);
                old.close();
            }
            channel.attr(TERMINAL_PHONE).set(terminalPhone);
            channel.closeFuture().addListener(remover);
            channelIdMap.put(terminalPhone, channel.id());
        }
        return added;
    }

    public boolean remove(String terminalPhone) {
        return channelGroup.remove(channelIdMap.remove(terminalPhone));
    }

    public Channel get(String terminalPhone) {
        return channelGroup.find(channelIdMap.get(terminalPhone));
    }

    public ChannelGroup getChannelGroup() {
        return channelGroup;
    }
}
