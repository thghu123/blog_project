package TcpUtil;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SysStatPacket implements Packet {

    private byte packetType;
    private int totalLen,
            statistic,
            strlen;
    private long value;
    private String name;

    public SysStatPacket() {
        this.packetType = PacketType.SYSSTAT;
        this.totalLen = 45;
        this.statistic = 0;
        this.strlen = 0;
        this.name = "";
        this.value = 0;
    }

    public SysStatPacket(int statistic, String name, long value) {
        this.packetType = PacketType.SYSSTAT;
        this.totalLen = name.getBytes().length + 1 + 4 + 4 + 4 + 8;

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

    public void setPacket(ByteBuf byteBufMsg) throws IOException {

        //한번에 받아온 ByteBuf 한줄 처리
        this.packetType = byteBufMsg.getByte(0);
        this.totalLen = byteBufMsg.getInt(1);
        this.statistic = byteBufMsg.getInt(5);
        this.value = byteBufMsg.getLong(9);
        this.strlen = byteBufMsg.getInt(17);
        byte[] byteMsg = new byte[this.totalLen];
        for (int i = 21; i < (this.totalLen); i++) {
            byteMsg[i] = byteBufMsg.getByte(i);
        }
        this.name = new String(byteMsg);
        System.out.println(this.toString());

    }

}