package com.dbGather.TcpDBGather;

import DAO.ProtocolDAO;
import VO_util.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf message;
    private byte[] str = null;

    public EchoClientHandler() {
        message = Unpooled.buffer(TcpDBGatherScheduler.MESSAGE_SIZE);
        this.str = "test".getBytes(StandardCharsets.UTF_8);
        message.writeBytes(str);
    }

    public EchoClientHandler(byte[] msg) {
        message = Unpooled.buffer(TcpDBGatherScheduler.MESSAGE_SIZE);
        message.writeBytes(msg);
    }

    public EchoClientHandler(Packet packet) {
        message = Unpooled.buffer(TcpDBGatherScheduler.MESSAGE_SIZE);
        message.writeBytes(packet.toBytes());
        System.out.println("send : "+packet.toString());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush(message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf inByteBufMsg = (ByteBuf) msg;
        ProtocolDAO protocol = new ProtocolDAO();
        Packet packet = protocol.receivePacket(inByteBufMsg);
        System.out.println("Echo : "+packet.toString());

        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }


}
