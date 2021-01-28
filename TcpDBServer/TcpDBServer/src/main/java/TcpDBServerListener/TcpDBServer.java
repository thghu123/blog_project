package TcpDBServerListener;

import TcpUtil.Packet;
import TcpUtil.Protocol;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpDBServer implements ApplicationListener<ApplicationStartedEvent> {

    private static final int PORT = 5002;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent event) {

        ServerSocket serverSocket = null;
        Socket socket = null;
        InputStream is = null;
        OutputStream os = null;


        try {
            serverSocket = new ServerSocket();
            serverSocket.bind(new InetSocketAddress(PORT));

            while (true) {
                System.out.println("[TCP Connection waiting]");
                socket = serverSocket.accept();

                InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                System.out.println("[TCP Connected] :" + isa.getHostName());

                is = socket.getInputStream();
                os = socket.getOutputStream();

                Protocol protocol = new Protocol();

                Packet recvPacket = protocol.receivePacket(is);
                if (recvPacket == null) break;
                System.out.println(recvPacket);
                recvPacket.sendPacket(os);
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {
            try {

                if(is!=null)is.close();
                if(os!=null)os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            if(socket != null) socket.close();
            if(serverSocket != null) serverSocket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}





