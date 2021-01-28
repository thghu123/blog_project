package TcpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SysStatPacket implements Packet {

    private byte    packetType;
    private int     totalLen,
                    statistic,
                    strlen;
    private long    value;
    private String    name;

    public SysStatPacket(){
        this.packetType = PacketType.SYSSTAT;
        this.totalLen = 45;
        this.statistic = 0;
        this.strlen = 0;
        this.name = "";
        this.value = 0;
    }

    public SysStatPacket(int statistic, String name, long value){
        this.packetType = PacketType.SYSSTAT;
        this.totalLen = name.getBytes().length+1+4+4+4+8;

        this.statistic = statistic;
        this.name = name;

        this.strlen = name.getBytes().length;
        this.value = value;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(totalLen);
        buffer.put(packetType);
        buffer.putInt(totalLen);

        buffer.putInt(statistic);
        buffer.putLong(value);
        buffer.putInt(strlen);
        buffer.put(name.getBytes());

        return buffer.array();
    }

    @Override
    public String toString() {
        return
                "protocol Type: " + packetType +
                        ", total Length: " + totalLen +
                        ", statistic :" + statistic +
                        ", value :" + value +
                        ", name:" + name +
                        "\n";
    }

    public void sendPacket(OutputStream os) throws IOException {
        byte[] bytes = this.toBytes();
        os.write(bytes);
    }

    public void setPacket(InputStream is) throws IOException{

        byte[] totalLength = new byte[4];
        while (is.read(totalLength) == -1) break;
        this.totalLen = ByteBuffer.wrap(totalLength).getInt();

        byte[] statistic = new byte[4];
        while (is.read(statistic) == -1) break;
        this.statistic = ByteBuffer.wrap(statistic).getInt();

        byte[] value = new byte[8];
        while (is.read(value) == -1) break;
        this.value = ByteBuffer.wrap(value).getLong(0);

        byte[] strLength = new byte[4];
        while (is.read(strLength) == -1) break;
        int length = ByteBuffer.wrap(strLength).getInt(0);

        byte[] str = new byte[(int)length];
        while (is.read(str) == -1) break;

        this.name = new String(str);
        this.strlen = length;

    }

}