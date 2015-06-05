package org.speed.db;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by coder on 15/6/5.
 */
public class SpeedSessionImpl implements SpeedSession {
    private DataSource dataSource;

    public Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public Map<String, Object> selectOne(String sql, Object... args) {
        List<Map<String, Object>> list = selectList(sql, args);
        if (list.isEmpty()) {
            return new HashMap<String, Object>();
        }
        return list.get(0);
    }

    public List<Map<String, Object>> selectList(String sql, Object... args) {
        Connection connection = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            dataSource.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            int incr = 1;
            for (Object arg : args) {
                pst.setObject(incr++, arg);
            }
            ResultSet rs = pst.executeQuery();
            ResultSetMetaData resultSetMetaData = pst.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            while (rs.next()) {
                Map<String, Object> data = new HashMap<String, Object>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    data.put(resultSetMetaData.getColumnName(columnIndex), rs.getObject(columnIndex));
                }
                list.add(data);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    public int execute(String sql, Object... args) {
        Connection connection = null;
        try {
            dataSource.getConnection();
            PreparedStatement pst = connection.prepareStatement(sql);
            int incr = 1;
            for (Object arg : args) {
                pst.setObject(incr++, arg);
            }
            return pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (null != connection) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }
}
