import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;


@ChannelHandler.Sharable //여러 채널에서 핸들러를 공유할 수 있음을 의미함
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    //채널을 읽을 때 동작할 코드를 정의합니다.
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception{
        //받은 메세지 출력
        ByteBuf in = (ByteBuf) msg;
        System.out.println("server received : "+ in.toString());
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
