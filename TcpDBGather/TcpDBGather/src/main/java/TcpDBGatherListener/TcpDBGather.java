package TcpDBGatherListener;

import ConnectionUtil.DBConnection;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class TcpDBGather implements ApplicationListener<ApplicationStartedEvent>  {

    public void onApplicationEvent(ApplicationStartedEvent event) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            String sql = "SELECT STATISTIC#, name, value FROM v$sysstat";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int statistic = rs.getInt("STATISTIC#");
                String name = rs.getString("name");
                long value = rs.getLong("value");
                System.out.println(statistic+"\t"+name+"\t"+value);
            }

            /*
            sql = "SELECT en.EVENT#, se.event, se.TOTAL_WAITS, se.TOTAL_TIMEOUTS, se.time_waited FROM v$system_event se, v$event_name en WHERE se.EVENT = en.NAME AND se.EVENT_ID = en.EVENT_ID";

            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                int eventNum = rs.getInt("EVENT#"); //이거 되면 위에거 바꾸자.
                String eventStr = rs.getString("event");
                long total_waits = rs.getLong("total_waits");
                long total_timeouts = rs.getLong("total_timeouts");
                Long time_waited = rs.getLong("time_waited");
                System.out.println(eventNum+"\t"+eventStr+"\t"+total_waits+"\t"+total_timeouts+"\t"+time_waited+"\t");
            }*/

        }catch(Exception e) {
            e.printStackTrace();
        }
        finally {
            DBConnection.dbclose(conn, pstmt, rs);
        }


    }
}