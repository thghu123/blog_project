package DAO;

import VO_util.Packet;
import VO_util.PacketType;
import VO_util.SysEventPacket;
import VO_util.SysStatPacket;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class ProtocolDAO {

    public Packet receivePacket(ByteBuf inByteBufMsg) throws IOException {
        Packet packet = null;

        int size = 1;
        byte bytePacketType;
        inByteBufMsg.getByte(size);
        bytePacketType = inByteBufMsg.getByte(0);

        switch(bytePacketType) {
            case PacketType.SYSSTAT:
                packet = new SysStatPacket();

                break;

            case PacketType.SYSEVENT:
                packet = new SysEventPacket();
                break;

            default:
                break;
        }
        packet.setPacket(inByteBufMsg);

        return packet;

    }

}


