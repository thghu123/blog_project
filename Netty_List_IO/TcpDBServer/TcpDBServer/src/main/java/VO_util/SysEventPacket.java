package VO_util;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;


public class SysEventPacket implements Packet {

    private byte packetType;
    private int totalLen;

    private int eventNum,
            strlen;
    private long total_waits,
            total_timeouts,
            time_waited;
    private String eventStr;

    public SysEventPacket() {
        this.packetType = PacketType.SYSEVENT;
        this.totalLen = 45;

        this.eventNum = 0;
        this.total_timeouts = 0;
        this.total_waits = 0;
        this.time_waited = 0;
        this.eventStr = "";
    }

    public SysEventPacket(int eventNum, long total_timeouts, long time_waited, long total_waits, String eventStr) {
        this.packetType = PacketType.SYSEVENT;
        this.totalLen = eventStr.getBytes().length + 1 + 4 + 4 + 4 + 8 + 8 + 8;

        this.eventNum = eventNum;
        this.total_timeouts = total_timeouts;
        this.total_waits = time_waited;
        this.time_waited = total_waits;

        this.strlen = eventStr.getBytes().length;
        this.eventStr = eventStr;
    }

    @Override
    public byte[] toBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(totalLen);
        buffer.put(packetType);
        buffer.putInt(totalLen);

        buffer.putInt(eventNum);
        buffer.putLong(total_timeouts);
        buffer.putLong(total_waits);
        buffer.putLong(time_waited);
        buffer.putInt(strlen);
        buffer.put(eventStr.getBytes());

        return buffer.array();
    }

    @Override
    public String toString() {
        return
                "protocol Type: " + packetType +
                        ", total Length: " + totalLen +
                        ", eventNum:" + eventNum +
                        ", total_timeouts:" + total_timeouts +
                        ", total_waits:" + total_waits +
                        ", time_waited:" + time_waited +
                        ", eventStr:" + eventStr +
                        "\n";
    }

    public void sendPacket(OutputStream os) throws IOException {
        byte[] bytes = this.toBytes();
        os.write(bytes);
    }


    public void setPacket(ByteBuf byteBufMsg) throws IOException {

        ByteBuffer buffer = ByteBuffer.allocate(totalLen);
        buffer.put(packetType);
        buffer.putInt(totalLen);

        buffer.putInt(eventNum);
        buffer.putLong(total_timeouts);
        buffer.putLong(total_waits);
        buffer.putLong(time_waited);
        buffer.putInt(strlen);
        buffer.put(eventStr.getBytes());

        this.packetType = byteBufMsg.getByte(0);
        this.totalLen = byteBufMsg.getInt(1);
        this.eventNum = byteBufMsg.getInt(5);

        this.total_timeouts = byteBufMsg.getLong(9);
        this.total_waits = byteBufMsg.getLong(17);
        this.time_waited = byteBufMsg.getLong(25);

        this.strlen = byteBufMsg.getInt(33);
        byte[] byteMsg = new byte[this.totalLen];
        for (int i = 37; i < (this.totalLen); i++) {
            byteMsg[i] = byteBufMsg.getByte(i);
        }
        this.eventStr = new String(byteMsg);

        System.out.println(this.toString());
    }


}