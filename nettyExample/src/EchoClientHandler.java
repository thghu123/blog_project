import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf message;
    private byte[] str = null;

    public EchoClientHandler(){
        message = Unpooled.buffer(EchoClient.MESSAGE_SIZE); //바이트 배열 할당, 공간의 의미
        this.str = "test".getBytes(StandardCharsets.UTF_8);//바이트 배열에 메세지를 작성
        //toArray 구분과 함께 사용.
        message.writeBytes(str);
    }
    public EchoClientHandler(String str){
        message = Unpooled.buffer(EchoClient.MESSAGE_SIZE); //바이트 배열 할당, 공간의 의미
        this.str = str.getBytes(StandardCharsets.UTF_8);//바이트 배열에 메세지를 작성
        message.writeBytes(this.str);
    }




    @Override
    public void channelActive(ChannelHandlerContext ctx){
        ctx.writeAndFlush(message);
        //Byte타입으로 변환된 메세지를 쓴 후 플래쉬
    }
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)throws Exception{
        //받은 메세지를 ByteBuf형으로 캐스팅한다
        ByteBuf byteBufmsg = (ByteBuf)msg;
        //읽을 수 있는 길이를 가져온다
        int size = byteBufmsg.readableBytes();

        //읽을 수 있는 길이만큼 바이트 배열을 초기화합니다.
        byte [] byteMsg  = new byte[size];
        //for문을 돌며 가져온 바이트 값을 연결합니다.
        for(int i = 0; i<size; i++){
            byteMsg[i] = byteBufmsg.getByte(i);
        }
        
        //byteBufmsg.getBytes(size,bytesMsg);
        //위 코드로 대체해볼 것
        //같은 nio이기에 getInt Long 사용가능하다.

        String str = new String(byteMsg);
        System.out.println("echoFromServer: "+str);
        ctx.close();
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx)throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }






}
