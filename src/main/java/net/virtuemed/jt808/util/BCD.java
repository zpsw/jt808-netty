package net.virtuemed.jt808.util;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Zpsw
 * @Date: 2019-05-15
 * @Description:
 * @Version: 1.0
 */
@Slf4j
public class BCD {

    public static byte[] DecimalToBCD(long num) {
        int digits = 0;
        long temp = num;
        while (temp != 0) {
            digits++;
            temp /= 10;
        }
        int byteLen = digits % 2 == 0 ? digits / 2 : (digits + 1) / 2;
        byte bcd[] = new byte[byteLen];
        for (int i = 0; i < digits; i++) {
            byte tmp = (byte) (num % 10);
            if (i % 2 == 0) {
                bcd[i / 2] = tmp;
            } else {
                bcd[i / 2] |= (byte) (tmp << 4);
            }
            num /= 10;
        }
        for (int i = 0; i < byteLen / 2; i++) {
            byte tmp = bcd[i];
            bcd[i] = bcd[byteLen - i - 1];
            bcd[byteLen - i - 1] = tmp;
        }
        return bcd;
    }

    public static long BCDToDecimal(byte[] bcd) {
        return Long.valueOf(BCD.BCDtoString(bcd));
    }

    public static String BCDtoString(byte bcd) {
        StringBuffer sb = new StringBuffer();

        byte high = (byte) (bcd & 0xf0);
        high >>>= (byte) 4;
        high = (byte) (high & 0x0f);
        byte low = (byte) (bcd & 0x0f);

        sb.append(high);
        sb.append(low);

        return sb.toString();
    }

    public static String BCDtoString(byte[] bcd) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < bcd.length; i++) {
            sb.append(BCDtoString(bcd[i]));
        }

        return sb.toString();
    }

    private static final String HEX = "0123456789ABCDEF";
    private static byte toByte(char c) {
        byte b = (byte) HEX.indexOf(c);
        return b;
    }

    public static byte[] toBcdBytes(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }
    public static String toBcdDateString(byte[] bs) {
        if (bs.length != 3 && bs.length != 4) {
            log.error("无效BCD日期");
            return "0000-00-00";
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        if (bs.length == 3) {
            sb.append("20");
        }else{
            sb.append(BCD.BCDtoString(bs[i++]));
        }
        sb.append(BCD.BCDtoString(bs[i++]));
        sb.append("-").append(BCD.BCDtoString(bs[i++]));
        sb.append("-").append(BCD.BCDtoString(bs[i++]));
        return sb.toString();
    }
    public static String toBcdTimeString(byte[] bs) {
        if (bs.length != 6 && bs.length != 7) {
            log.error("无效BCD时间");
            return "0000-00-00 00:00:00";
        }
        StringBuffer sb = new StringBuffer();
        int i = 0;
        if (bs.length == 6) {
            sb.append("20");
        }else{
            sb.append(BCD.BCDtoString(bs[i++]));
        }
        sb.append(BCD.BCDtoString(bs[i++]));
        sb.append("-").append(BCD.BCDtoString(bs[i++]));
        sb.append("-").append(BCD.BCDtoString(bs[i++]));
        sb.append(" ").append(BCD.BCDtoString(bs[i++]));
        sb.append(":").append(BCD.BCDtoString(bs[i++]));
        sb.append(":").append(BCD.BCDtoString(bs[i]));
        return sb.toString();
    }
}
