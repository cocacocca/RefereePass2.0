package util;

import java.sql.*;

/**
 * JDBC工具类
 */
public class JDBCUtils {
    public static final String driver = "com.mysql.cj.jdbc.Driver";
    public static final String url = "jdbc:mysql://gz-cynosdbmysql-grp-k38io6fz.sql.tencentcdb.com:29149/RefereePass";
    public static final String user = "root";
    public static final String password = "Coca915917282";

    //静态块，用于加载MySQL的JDBC驱动程序
    static {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //获取数据库连接方法
    public static Connection getConnection() {
        try {
            //调用DriverManager对象的getConnection()方法，获得一个Connection对象
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; //返回 null 表示连接失败
    }

    /**
    * 关闭连接资源
    * @param con   连接对象
    * @param pstmt 预编译对象
    * @param rs    结果集
    */
    public static void close(Connection con,PreparedStatement pstmt, ResultSet rs){
        try{
            if(rs != null){
                rs.close();
            }
            if(pstmt != null){
                pstmt.close();
            }
            if(con != null){
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}