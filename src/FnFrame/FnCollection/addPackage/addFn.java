package FnFrame.FnCollection.addPackage;


import util.JDBCUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 添加数据的功能逻辑
 */
public class addFn extends JFrame {
    /**
     * 添加数据的方法
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    protected boolean validateInput(String tableName, String data) {
        //移除输入数据的前导和尾随空格
        data = data.trim();

        //使用逗号拆分输入数据
        String[] fields = data.split(",");

        //根据表名验证字段数量
        if ("Athletes".equals(tableName) && fields.length == 6) {
            //如果是 Athletes 表，且有 6 个字段，返回 true
            return true;
        } else if ("Referees".equals(tableName) && fields.length == 5) {
            //如果是 Referees 表，且有 5 个字段，返回 true
            return true;
        }

        //如果字段数量不正确，返回 false
        return false;
    }

    /**
     * 添加数据库中的数据。
     *
     * @param tableName 数据库表名
     * @param data      要添加的数据
     */
    protected void insertData(String tableName, String data) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            //移除输入数据的前导和尾随空格
            data = data.trim();

            //获取数据库连接
            con = JDBCUtils.getConnection();

            //根据选择的表进行不同的处理
            if ("Athletes".equals(tableName) && validateInput(tableName, data)) {
                //如果是Athletes表，解析data为name、gender、birthday、profession、enterDate、club
                String[] athleteData = data.split(",");

                //使用预编译语句插入数据，不包括id字段
                String sql = "INSERT INTO Athletes (name, gender, birthday, profession, enterDate, club) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                //设置插入的值
                for (int i = 0; i < athleteData.length; i++) {
                    //如果字段为空，设置为 NULL
                    if (athleteData[i].isEmpty()) {
                        pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(i + 1, athleteData[i].trim());
                    }
                }

            } else if ("Referees".equals(tableName) && validateInput(tableName, data)) {
                //如果是Referees表，解析data为name、gender、birthday、judgeField、judgeTimes
                String[] refereeData = data.split(",");

                //使用预编译语句插入数据，不包括 id 字段
                String sql = "INSERT INTO Referees (name, gender, birthday, judgeField, judgeTimes) " +
                        "VALUES (?, ?, ?, ?, ?)";
                pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                //设置插入的值
                for (int i = 0; i < refereeData.length; i++) {
                    //如果字段为空，设置为 NULL
                    if (refereeData[i].isEmpty()) {
                        pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(i + 1, refereeData[i].trim());
                    }
                }
            } else {
                //输入数据格式不符合要求，弹出错误提示
                JOptionPane.showMessageDialog(this, "输入数据格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //执行插入操作
            pstmt.executeUpdate();

            //获取自动生成的 ID
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Inserted ID: " + generatedId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //关闭连接资源
            JDBCUtils.close(con, pstmt, null);
        }
    }

    /**
     * 刷新数据
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    protected void refreshData(JTextArea dataTextArea) {
        JRadioButton athletesRadioButton = addUI.athletesRadioButton;
        JTextField selectField = addUI.selectField;


        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //获取数据库连接
            con = JDBCUtils.getConnection();

            //获取选中的单选框（Athletes或Referees）
            String selectedTable = athletesRadioButton.isSelected() ? "Athletes" : "Referees";

            //构建查询语句
            String sql = "SELECT * FROM " + selectedTable;

            //如果有搜索条件，则添加搜索条件
            String searchText = selectField.getText().trim();
            if (!searchText.isEmpty()) {
                sql += " WHERE name LIKE '%" + searchText + "%'";
            }

            //将sql语句打包执行
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            //构建字符串来存储结果
            StringBuilder result = new StringBuilder();

            //遍历结果集，构建展示数据的字符串
            while (rs.next()) {
                //根据具体的表结构获取字段值
                String id = rs.getString("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String birthday = rs.getString("birthday");

                //如果是Athletes表，还需获取其他字段
                if ("Athletes".equals(selectedTable)) {
                    String profession = rs.getString("profession");
                    String enterDate = rs.getString("enterDate");
                    String club = rs.getString("club");

                    result.append(id).append(", ").append(name).append(", ").append(gender)
                            .append(", ").append(birthday).append(", ").append(profession)
                            .append(", ").append(enterDate).append(", ").append(club).append("\n");
                } else {
                    //如果是Referees表，获取其他字段
                    String judgeField = rs.getString("judgeField");
                    int judgeTimes = rs.getInt("judgeTimes");

                    result.append(id).append(", ").append(name).append(", ").append(gender)
                            .append(", ").append(birthday).append(", ").append(judgeField)
                            .append(", ").append(judgeTimes).append("\n");
                }
            }

            //将结果显示在文本区域
            dataTextArea.setText(result.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接资源
            JDBCUtils.close(con, pstmt, rs);
        }
    }

}
