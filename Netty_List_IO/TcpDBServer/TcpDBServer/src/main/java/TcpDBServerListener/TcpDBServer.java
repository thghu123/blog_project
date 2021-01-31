package TcpDBServerListener;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

public class TcpDBServer implements ApplicationListener<ApplicationStartedEvent> {

    private static final int PORT = 8081;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        EventLoopGroup parentGroup = new NioEventLoopGroup(1);
        EventLoopGroup childGroup = new NioEventLoopGroup();

        //channel setting
        try {
            ServerBootstrap sb = new ServerBootstrap();

            sb.group(parentGroup, childGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        //Initializer는 새로운 Channel의 환경 구성을 돕는다.
                        protected void initChannel(SocketChannel sc) {
                            ChannelPipeline cp = sc.pipeline();
                            cp.addLast(new TcpDBServerHandler());
                        }
                    });

        //bind and close
            ChannelFuture cf = sb.bind(PORT).sync();
            cf.channel().closeFuture().sync();

            //Do

        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            parentGroup.shutdownGracefully();
            childGroup.shutdownGracefully();
        }
    }
}





