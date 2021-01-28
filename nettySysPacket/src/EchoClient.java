import TcpUtil.Packet;
import TcpUtil.Protocol;
import TcpUtil.SysEventPacket;
import TcpUtil.SysStatPacket;
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

        int statistic = 1;
        String name = "Lock Holder";
        long value = 4;

        int eventNum = 2;
        long total_timeouts = 0,
                time_waited = 3,
                total_waits = 7;
        String eventStr = "EVENT : Process is Locked";

        //값이 많기 때문에 한번의 접속마다 하나씩 처리? 배열로 처리하면?
        try {

            //while
            //위 부분에서 protocol을 이용해 패킷을 생성하고 toByte 실행
            //이후 Echo Clienthander를 이용할 떄 toString으로 값을 넣어주자.
            //리스트로 넘겨주는 가 byte로 넘겨주는 가?

            while (true) {
                Packet packet = null;

                System.out.println("will you send? (sysstat:1, sysEvent:2 quit:3)");
                int i = sc.nextInt();

                //protocol에서 하는 작업 -> 클래스로 나눠서 테스트
                if (i == 1) {//sysstatPacket
                    packet = new SysStatPacket(statistic, name, value);
                } else if (i == 2) {//sysEventPacket Send
                    packet = new SysEventPacket(eventNum, total_timeouts, time_waited, total_waits, eventStr);
                } else {
                    break;
                }

                Bootstrap b = new Bootstrap();

                Packet finalPacket = packet; //이유?

                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            protected void initChannel(SocketChannel sc) {
                                ChannelPipeline cp = sc.pipeline();  //pipeLine? 이벤트 처리와 순서를 담는 객체
                                cp.addLast(new EchoClientHandler(finalPacket.toBytes())); //채널 파이프 라인에 이벤트 핸들러를 하나 추가한다.
                            }
                        });

                ChannelFuture cf = b.connect(HOST, PORT).sync();//IP와 PORT
                cf.channel().closeFuture().sync();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            group.shutdownGracefully();
        }

    }


}
