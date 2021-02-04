package DAO;

import VO_util.Packet;
import VO_util.PacketType;
import VO_util.SysEventPacket;
import VO_util.SysStatPacket;
import io.netty.buffer.ByteBuf;

import java.io.IOException;

public class ProtocolDAO {

    public Packet receivePacket(ByteBuf inByteBufMsg) throws IOException {
        Packet packet = null;

        int size = 1;
        byte bytePacketType;
        inByteBufMsg.getByte(size);
        bytePacketType = inByteBufMsg.getByte(0);

        switch(bytePacketType) {
            case PacketType.SYSSTAT:
                packet = new SysStatPacket();

                break;

            case PacketType.SYSEVENT:
                packet = new SysEventPacket();
                break;

            default:
                break;
        }
        packet.setPacket(inByteBufMsg);

        return packet;

    }

    public Packet receiveListPacket(ByteBuf inByteBufMsg) throws IOException {
        Packet packet = null;

        int size = 1;
        byte bytePacketType;
        inByteBufMsg.getByte(size);
        bytePacketType = inByteBufMsg.getByte(0);
        //이후의 모든 타입이 같은 타입이다.
        //int 형인 totalLen을 받아오고 해당 값만큼 읽어내자.

        //int listSize = 4;
        int byteListSize;
        //inByteBufMsg.getInt(1);
        byteListSize = inByteBufMsg.getInt(1);

        int packetSize = 0;

        switch(bytePacketType) { //어떤 패킷인지 확인했으니 패킷의 양만큼 분해할 것. 해당 패킷의 totalLen을 받아서 진행한다.
            case PacketType.SYSSTAT:
                for(int i=0; i<byteListSize; i++) {
                    int totalLen = inByteBufMsg.getInt(packetSize+5);
                    //마지막에 i에 total len을 더해주면 된다.
                    //set에 필요한 인자값 : static, String name, value 값만 필요하다.
                    //이제 전체 길이가 나왔으니 하나씩 빼면된다.
                    int statistic = inByteBufMsg.getInt(packetSize+9);
                    long value = inByteBufMsg.getLong(packetSize+13);
                    int strlen = inByteBufMsg.getInt(packetSize+21);
                    byte[] byteMsg = new byte[strlen];
                    for (int j = packetSize+25; j < (strlen); packetSize++) {
                        byteMsg[packetSize] = inByteBufMsg.getByte(packetSize);
                    }
                    String name = new String(byteMsg);

                    //받은 값을 다 set으로 처리해주면 되잖아.

                    packet = new SysStatPacket(statistic, name, value, byteListSize);
                    System.out.println(packet.toString());
                    // 패킷을 잘라서 주면 알아서 이를 나누게 하자. 처음에 토탈렌 얻고
                    // 해당 부분을 잘라내서 패킷으로 전달해주는 함수, 통 리스트를 전달하는 부분.
                    /*packet.setPacket(inByteBufMsg);*/
                    packetSize = packetSize + totalLen;
                }
                break;

            case PacketType.SYSEVENT:
                /*
                    packet = new SysEventPacket();
                    packet.setPacket(inByteBufMsg);
                    */

                for(int i=0; i<byteListSize; i++) {
                    int totalLen = inByteBufMsg.getInt(packetSize+5);

                    int eventNum = inByteBufMsg.getInt(packetSize+9);
                    long total_timeouts = inByteBufMsg.getLong(packetSize+13);
                    long total_waits = inByteBufMsg.getLong(packetSize+21);
                    long time_waited = inByteBufMsg.getLong(packetSize+29);

                    int strlen = inByteBufMsg.getInt(packetSize+37);
                    byte[] byteMsg = new byte[strlen];
                    for (int j = packetSize+41; j < (strlen); packetSize++) {
                        byteMsg[packetSize] = inByteBufMsg.getByte(packetSize);
                    }
                    String eventStr = new String(byteMsg);

                    //받은 값을 다 set으로 처리해주면 되잖아.

                    packet = new SysEventPacket( eventNum,  total_timeouts, time_waited,total_waits, eventStr);
                    System.out.println(packet.toString());
                    // 패킷을 잘라서 주면 알아서 이를 나누게 하자. 처음에 토탈렌 얻고
                    // 해당 부분을 잘라내서 패킷으로 전달해주는 함수, 통 리스트를 전달하는 부분.
                    /*packet.setPacket(inByteBufMsg);*/
                    packetSize = packetSize + totalLen;
                }

                break;

            default:
                break;
        }
        packetSize = 0;

        return packet;

    }

}


