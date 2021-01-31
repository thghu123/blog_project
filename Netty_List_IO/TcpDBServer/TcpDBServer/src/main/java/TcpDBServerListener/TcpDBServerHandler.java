package TcpDBServerListener;

import DAO.ProtocolDAO;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


@ChannelHandler.Sharable
public class TcpDBServerHandler extends ChannelInboundHandlerAdapter {

    //Read
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception{

        ByteBuf inByteBufMsg = (ByteBuf) msg;

        ProtocolDAO protocol = new ProtocolDAO();
        protocol.receivePacket(inByteBufMsg);

        ctx.write(msg);

    }

    //channelRead Complete -> alert to Event
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    //Exception
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
