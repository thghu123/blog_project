package VO_util;

import io.netty.buffer.ByteBuf;

import java.io.IOException;
import java.io.OutputStream;

public interface Packet {

    public byte[] toBytes();
    public void sendPacket(OutputStream os) throws IOException;
    public void setPacket(ByteBuf byteBufMsg) throws IOException;

}
