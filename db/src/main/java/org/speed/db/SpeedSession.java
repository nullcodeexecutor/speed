package org.speed.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by coder on 15/6/5.
 */
public interface SpeedSession {

    Connection getConnection() throws SQLException;

    Map<String, Object> selectOne(String sql, Object... args);

    List<Map<String, Object>> selectList(String sql, Object... args);

    int execute(String sql, Object... args);


}
