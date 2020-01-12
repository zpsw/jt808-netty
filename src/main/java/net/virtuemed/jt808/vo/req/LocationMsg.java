package net.virtuemed.jt808.vo.req;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.virtuemed.jt808.util.BCD;
import net.virtuemed.jt808.vo.DataPacket;
/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:位置上报包
 * @Version: 1.0
 */
@Data
public class LocationMsg extends DataPacket {

    public LocationMsg(ByteBuf byteBuf) {
        super(byteBuf);
    }

    private int alarm; //告警信息 4字节
    private int statusField;//状态 4字节
    private float latitude;//纬度 4字节
    private float longitude;//经度 4字节
    private short elevation;//海拔高度 2字节
    private short speed; //速度 2字节
    private short direction; //方向 2字节
    private String time; //时间 6字节BCD

    @Override
    public void parseBody() {
        ByteBuf bb = this.payload;
        this.setAlarm(bb.readInt());
        this.setStatusField(bb.readInt());
        this.setLatitude(bb.readUnsignedInt() * 1.0F / 1000000);
        this.setLongitude(bb.readUnsignedInt() * 1.0F / 1000000);
        this.setElevation(bb.readShort());
        this.setSpeed(bb.readShort());
        this.setDirection(bb.readShort());
        this.setTime(BCD.toBcdTimeString(readBytes(6)));
    }
}
