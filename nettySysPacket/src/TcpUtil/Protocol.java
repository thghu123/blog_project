package TcpUtil;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.sctp.SctpOutboundByteStreamHandler;

import java.io.IOException;
import java.io.InputStream;

public class Protocol {

    public Packet receivePacket(ByteBuf inByteBufMsg) throws IOException {
        Packet packet = null;

        int size = 1;
        byte bytePacketType;
        inByteBufMsg.getByte(size);
        bytePacketType = inByteBufMsg.getByte(0);

        switch(bytePacketType) {
            case PacketType.SYSSTAT:
                //System.out.println("this is SYSSTAT Packit");
                packet = new SysStatPacket();

                break;

            case PacketType.SYSEVENT:
                //System.out.println("this is SYSEVENT Packit");
                packet = new SysEventPacket();
                //packet.setPacket(is);

                break;

            default:
                break;
        }

        packet.setPacket(inByteBufMsg);

        return packet;

    }/*
    public Packet receivePacket(InputStream is) throws IOException {
        Packet packet = null;
        int packetType = is.read();

        switch(packetType) {
            case PacketType.SYSSTAT:

                //packet = new NumPacker();
                //packet.setPacket(is);

                break;

            case PacketType.SYSEVENT:

                //packet = new StrPacker();
                //packet.setPacket(is);

                break;

            default:
                break;
        }

        return packet;

    }*/




}


