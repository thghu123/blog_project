import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.Scanner;

public class EchoClient {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8081;
    static final int MESSAGE_SIZE = 256;

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        Scanner sc = new Scanner(System.in);
        try{

            //while
            //위 부분에서 protocol을 이용해 패킷을 생성하고 toByte 실행
            //이후 Echo Clienthander를 이용할 떄 toString으로 값을 넣어주자.
            while(true) {
                System.out.println("will you send? press Number (quit:0)");
                int i = sc.nextInt();
                if(i == 0){break;}

                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            protected void initChannel(SocketChannel sc) {
                                ChannelPipeline cp = sc.pipeline();  //pipeLine? 이벤트 처리와 순서를 담는 객체
                                cp.addLast(new EchoClientHandler()); //채널 파이프 라인에 이벤트 핸들러를 하나 추가한다.
                            }
                        });

                ChannelFuture cf = b.connect(HOST, PORT).sync();//IP와 PORT
                cf.channel().closeFuture().sync();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally {
            group.shutdownGracefully();
        }

    }


}
