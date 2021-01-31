package DAO;

import VO_util.Packet;
import VO_util.PacketType;
import VO_util.SysEventPacket;
import VO_util.SysStatPacket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PacketDAO {
    private String sql = null;
    static byte packetType = PacketType.SYSSTAT;

    public List<SysStatPacket> getAllSysStat(Connection conn) throws SQLException {
        List<SysStatPacket> sysStatList = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            sql = "SELECT STATISTIC#, name, value FROM v$sysstat";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int statistic = rs.getInt("STATISTIC#");
                String name = rs.getString("name");
                long value = rs.getLong("value");

                sysStatList.add(new SysStatPacket(statistic, name, value));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return sysStatList;
    }

    public List<SysEventPacket> getAllSysEvent(Connection conn) throws SQLException {
        List<SysEventPacket> sysEventList = new ArrayList<>();
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            sql = "SELECT en.EVENT#, se.event, se.TOTAL_WAITS, se.TOTAL_TIMEOUTS, se.time_waited FROM v$system_event se, v$event_name en WHERE se.EVENT = en.NAME AND se.EVENT_ID = en.EVENT_ID";
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                int eventNum = rs.getInt("EVENT#"); //이거 되면 위에거 바꾸자.
                String eventStr = rs.getString("event");
                long total_waits = rs.getLong("total_waits");
                long total_timeouts = rs.getLong("total_timeouts");
                Long time_waited = rs.getLong("time_waited");
                sysEventList.add(new SysEventPacket(eventNum, total_timeouts, time_waited, total_waits, eventStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return sysEventList;
    }

    public List<Packet> getAllPacketList(Connection conn) throws SQLException {

        List<Packet> packet = new ArrayList<>();

        if (packetType == PacketType.SYSSTAT) {

            List<SysStatPacket> allSysStatList = this.getAllSysStat(conn);

            int listSize = allSysStatList.size();
            for (int i = 0; i < listSize; i++) {
                packet.add(allSysStatList.get(i));
            }
            packetType = PacketType.SYSEVENT;
        }

        if (packetType == PacketType.SYSEVENT) {

            List<SysEventPacket> AllSysEventList = this.getAllSysEvent(conn);

            int listSize = AllSysEventList.size();
            for (int i = 0; i < listSize; i++) {
                packet.add(AllSysEventList.get(i));
            }
            packetType = PacketType.SYSSTAT;
        }
        return packet;
    }

}
