package marchsoft.modules.notice.netty.config;

import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Wangmingcan
 * @date 2021/2/21 16:35
 * @description
 */
@Slf4j
@Component
public class WebSocketUserUtil {

    /**
     * 1.7 HashMap线程不安全
     */
    private static ConcurrentHashMap<String, Channel> rooms = new ConcurrentHashMap<>();

    public static void put(String userId, Channel channel) {
        rooms.put(userId,channel);
    }

    public static Channel get(String userId) {
        return rooms.get(userId);
    }

    public static void remove(Channel channel){
        Set<String> set = rooms.keySet();
        for (String id : set) {
            if (channel.equals(rooms.get(id))) {
                rooms.remove(id);
            }
        }
    }
}
