import TcpUtil.Packet;
import TcpUtil.Protocol;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


@ChannelHandler.Sharable //여러 채널에서 핸들러를 공유할 수 있음을 의미함
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    //채널을 읽을 때 동작할 코드를 정의합니다.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception{
        //받은 메세지 출력
        //받은 패킷을 맞는 타입에 맞게 추출해줘야한다.

        ByteBuf inByteBufMsg = (ByteBuf) msg;//들어온 값을 넘김
        //목적은 앞의 부분만 읽어와서 프로토콜에게 넘기고, 받아온 패킷 값을 toBytes로 전송, String 출력


        Protocol protocol = new Protocol();
        //Packet packet = protocol.receivePacket(inByteBufMsg);
        protocol.receivePacket(inByteBufMsg);

        //그걸 정제해서 값으로 남기고 싶기는 것 까지만 하고 싶다. 이를 패킷으로 남기고
        //to Byte 해서 이를 보여주기만 하자 -> 로그백 적용



        //다시 보내지는 동작 코드
        //System.out.println("server received : "+ inByteBufMsg.toString());
        ctx.write(msg);//메세지를 그대로 다시 write 한다

    }

    @Override
    //channelRead가 완전히 처리되었음을 핸들러에게 통보하는 메서드
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        //ctx.flush(); //context의 내용을 플러쉬합니다.


        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
        .addListener(ChannelFutureListener.CLOSE);


    }

    //예외 처리
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }






}
