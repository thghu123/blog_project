import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

    private static final int PORT = 8081;


    public static void main(String[] args) {


        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        // nThead는 쓰레드의 특정 수를 의미. 해당 수를 사용하여 새로운 instance 생성
        EventLoopGroup childGroup = new NioEventLoopGroup();

        //Nio EventLoop는 IO 동작을 다루는 멀티 스레드 이벤트 루프
        //parent 그룹은 인커밍 커넥션, child는 엑세스한 커넥션의 트래픽을 처리
        try {
            ServerBootstrap sb = new ServerBootstrap();
            //helper class와 유사한 개념, 서버에서 Channel Setting 가능

            sb.group(parentGroup, childGroup)
            //Incomming 연결을 접근하기 위해 새로운 채널을 객체화하는 클래스 지정.
            .channel(NioServerSocketChannel.class)
            //.class는 클래스 자체에 대한 정보가 담겨는 반환 타입.
            //상세한 채널 구현을 위한 옵션 지정가능
            .option(ChannelOption.SO_BACKLOG, 100)
            .handler(new LoggingHandler(LogLevel.INFO))
            //새롭게 엑세스된 채널을 처리
            .childHandler(new ChannelInitializer<SocketChannel>() {
                //Initializer는 새로운 Channel의 환경 구성을 돕는다.
                protected void initChannel(SocketChannel sc) {
                    ChannelPipeline cp = sc.pipeline();
                    cp.addLast(new EchoServerHandler());
                }
            });


        //인커밍 커넥션을 액세스 하기 위해 바인드를 시작
            ChannelFuture cf = sb.bind(PORT).sync();
        //server socket이 닫힐 때까지 대기
            cf.channel().closeFuture().sync();


        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}
