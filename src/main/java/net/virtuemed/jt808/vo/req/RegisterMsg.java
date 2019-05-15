package net.virtuemed.jt808.vo.req;

import io.netty.buffer.ByteBuf;
import lombok.Data;
import net.virtuemed.jt808.vo.DataPacket;
/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:终端注册包
 * @Version: 1.0
 */
@Data
public class RegisterMsg extends DataPacket {

    private short provinceId;//省域ID 2字节
    private short cityId;//市县ID 2字节
    private String manufacturerId;//制造商ID 5字节
    private String terminalType;//终端型号 8字节
    private String terminalId;//终端ID 7字节
    private byte licensePlateColor;//车牌颜色 1字节
    private String licensePlate;//车牌号 剩余字节

    public RegisterMsg(ByteBuf byteBuf) {
        super(byteBuf);
    }

    @Override
    public void parseBody() {
        ByteBuf bb = this.byteBuf;
        this.setProvinceId(bb.readShort());
        this.setCityId(bb.readShort());
        this.setManufacturerId(readString(5));
        this.setTerminalType(readString(8));
        this.setTerminalId(readString(7));
        this.setLicensePlateColor(bb.readByte());
        this.setLicensePlate(readString(bb.readableBytes()));
    }
}
