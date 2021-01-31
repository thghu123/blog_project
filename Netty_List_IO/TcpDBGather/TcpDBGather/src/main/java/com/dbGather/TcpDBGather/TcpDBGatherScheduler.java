package com.dbGather.TcpDBGather;

import ConnectionUtil.DBConnection;
import DAO.PacketDAO;
import VO_util.Packet;
import VO_util.PacketType;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

@EnableScheduling
@Component
public class TcpDBGatherScheduler {

    private static final String HOST = "127.0.0.1";
    private static final int PORT = 8081;
    static final int MESSAGE_SIZE = 256;
    private static byte whichPacket = PacketType.SYSSTAT;

    @Scheduled(fixedDelay = 2000)
    public static void TcpDBGather_s() {
        Connection conn = null;
        //Connection conn = null;
        PacketDAO packetDao = new PacketDAO();
        EventLoopGroup group = new NioEventLoopGroup();
        List<Packet> packet = new ArrayList<>();

        try {
            conn = DBConnection.getConnection();

            packet = packetDao.getAllPacketList(conn);

            int listSize = packet.size();
            for (int i = 0; i < listSize; i++) {
                Bootstrap b = new Bootstrap();
                int finalI = i;
                List<Packet> finalPacket = packet;
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .option(ChannelOption.TCP_NODELAY, true)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            protected void initChannel(SocketChannel sc) {
                                ChannelPipeline cp = sc.pipeline();
                                cp.addLast(new EchoClientHandler(finalPacket.get(finalI)));
                            }
                        });

                ChannelFuture cf = b.connect(HOST, PORT).sync();
                cf.channel().closeFuture().sync();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            group.shutdownGracefully();
        }

    }
}




