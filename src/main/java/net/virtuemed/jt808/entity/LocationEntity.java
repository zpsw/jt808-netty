package net.virtuemed.jt808.entity;

import lombok.Data;
import net.virtuemed.jt808.vo.req.LocationMsg;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:
 * @Version: 1.0
 */
@Data
@Entity
@Table(name = "location_log")
public class LocationEntity extends AbstractPersistable<Long> {

    private String terminalPhone; // 终端手机号
    private int alarm;
    private int statusField;
    private float latitude;
    private float longitude;
    private short elevation;
    private short speed;
    private short direction;
    private String time;

    public static LocationEntity parseFromLocationMsg(LocationMsg msg) {
        LocationEntity location = new LocationEntity();
        location.setTerminalPhone(msg.getHeader().getTerminalPhone());
        location.setAlarm(msg.getAlarm());
        location.setStatusField(msg.getStatusField());
        location.setLatitude(msg.getLatitude());
        location.setLongitude(msg.getLongitude());
        location.setElevation(msg.getElevation());
        location.setSpeed(msg.getSpeed());
        location.setDirection(msg.getDirection());
        location.setTime(msg.getTime());
        return location;
    }
}
