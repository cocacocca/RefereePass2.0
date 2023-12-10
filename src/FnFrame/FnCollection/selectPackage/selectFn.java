package FnFrame.FnCollection.selectPackage;

import util.JDBCUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 查询数据的功能逻辑
 */
public class selectFn extends JFrame{
    /**
     * 查询数据的方法
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    protected void refreshData(JTextArea dataTextArea) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            //获取数据库连接
            con = JDBCUtils.getConnection();

            //获取选中的单选框（Athletes或Referees）
            String selectedTable = selectUI.athletesRadioButton.isSelected() ? "Athletes" : "Referees";

            //构建查询语句
            String sql = "SELECT * FROM " + selectedTable;

            //如果有搜索条件，则添加搜索条件
            String searchText = selectUI.selectField.getText().trim();
            if (!searchText.isEmpty()) {
                sql += " WHERE name LIKE '%" + searchText + "%'";
            }

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
            //关闭连接资源
            JDBCUtils.close(con, pstmt, rs);
        }
    }
}
