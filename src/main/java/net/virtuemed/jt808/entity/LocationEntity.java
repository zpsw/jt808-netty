package net.virtuemed.jt808.entity;

import lombok.Data;
import net.virtuemed.jt808.vo.req.LocationMsg;
import org.springframework.beans.BeanUtils;
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
    private Integer alarm;
    private Integer statusField;
    private Float latitude;
    private Float longitude;
    private Short elevation;
    private Short speed;
    private Short direction;
    private String time;

    public static LocationEntity parseFromLocationMsg(LocationMsg msg) {
        LocationEntity location = new LocationEntity();
        location.setTerminalPhone(msg.getHeader().getTerminalPhone());
        BeanUtils.copyProperties(msg, location);
        return location;
    }
}
