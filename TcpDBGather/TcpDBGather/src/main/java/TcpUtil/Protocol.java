package TcpUtil;

import java.io.IOException;
import java.io.InputStream;

public class Protocol {

    public Packet receivePacket(InputStream is) throws IOException {
        Packet packet = null;
        int packetType = is.read();

        switch(packetType) {
            case PacketType.SYSSTAT:

                //packet = new NumPacker();
                //packet.setPacket(is);

                break;

            case PacketType.SYSEVENT:

                //packet = new StrPacker();
                //packet.setPacket(is);

                break;

            default:
                break;
        }

        return packet;

    }




}


