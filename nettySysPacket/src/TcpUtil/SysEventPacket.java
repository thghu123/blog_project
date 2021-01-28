package TcpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

public class SysEventPacket implements Packet {

    private byte    packetType;
    private int     totalLen;

    private int     eventNum,
                    strlen;
    private long    total_waits,
                    total_timeouts,
                    time_waited;
    private String  eventStr;

    public SysEventPacket(){
        this.packetType = PacketType.SYSEVENT;
        this.totalLen = 45;

        this.eventNum = 0;
        this.total_timeouts = 0;
        this.total_waits = 0;
        this.time_waited = 0;
        this.eventStr = "";
    }

    public SysEventPacket(int eventNum,long total_timeouts, long time_waited, long total_waits, String eventStr){
        this.packetType = PacketType.SYSEVENT;
        this.totalLen = eventStr.getBytes().length + 1+4+4+4+8+8+8;

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

    public void setPacket(InputStream is) throws IOException{

        byte[] totalLength = new byte[4];
        while (is.read(totalLength) == -1) break;
        this.totalLen = ByteBuffer.wrap(totalLength).getInt();

        byte[] eventNum = new byte[4];
        while (is.read(eventNum) == -1) break;
        this.eventNum = ByteBuffer.wrap(eventNum).getInt();

        byte[] total_timeouts = new byte[8];
        while (is.read(total_timeouts) == -1) break;
        this.total_timeouts = ByteBuffer.wrap(total_timeouts).getLong(0);

        byte[] total_waits = new byte[8];
        while (is.read(total_waits) == -1) break;
        this.total_waits = ByteBuffer.wrap(total_waits).getLong(0);

        byte[] time_waited = new byte[8];
        while (is.read(time_waited) == -1) break;
        this.time_waited = ByteBuffer.wrap(time_waited).getLong(0);

        byte[] strLength = new byte[4];
        while (is.read(strLength) == -1) break;
        int length = ByteBuffer.wrap(strLength).getInt(0);

        byte[] str = new byte[(int)length];
        while (is.read(str) == -1) break;

        this.eventStr = new String(str);
        this.strlen = length;
    }

}