package FnFrame.FnCollection.updatePackage;

import util.JDBCUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 更新数据的功能逻辑
 */
public class updateFn extends JFrame {

    /**
     * 更新数据的方法
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    protected void updateData(JTextArea dataTextArea) {

        //获取选中的单选框（Athletes或Referees）
        String selectedTable = updateUI.athletesRadioButton.isSelected() ? "Athletes" : "Referees";
        //获取要更新的数据
        String dataToUpdate = updateUI.dataField.getText();

        //验证输入数据格式
        if (validateInput(selectedTable, dataToUpdate)) {

            //然后执行数据库更新操作
            updateData(selectedTable, dataToUpdate);

            //刷新数据展示区域
            refreshData(dataTextArea);
        } else {
            JOptionPane.showMessageDialog(this, "输入数据格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 更新数据库中的数据。
     *
     * @param tableName    数据库表名
     * @param dataToUpdate 要更新的数据
     */
    private void updateData(String tableName, String dataToUpdate) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            //获取数据库连接
            con = JDBCUtils.getConnection();

            //根据选择的表进行不同的处理
            if ("Athletes".equals(tableName) && validateInput(tableName, dataToUpdate)) {

                //如果是 Athletes 表，解析 data 为 id、name、gender、birthday、profession、enterDate、club
                String[] athleteData = dataToUpdate.split(",");

                //使用预编译语句更新数据
                String sql = "UPDATE Athletes SET name=?, gender=?, birthday=?, profession=?, enterDate=?, club=? WHERE name=?";
                pstmt = con.prepareStatement(sql);

                //设置更新的值
                pstmt.setString(1, athleteData[0].trim());  // name
                pstmt.setString(2, athleteData[1].trim());  // gender
                pstmt.setString(3, athleteData[2].trim());  // birthday
                pstmt.setString(4, athleteData[3].trim());  // profession
                pstmt.setString(5, athleteData[4].trim());  // enterDate
                pstmt.setString(6, athleteData[5].trim());  // club
                pstmt.setString(7, athleteData[0].trim());  // WHERE name


            } else if ("Referees".equals(tableName) && validateInput(tableName, dataToUpdate)) {

                //如果是 Referees 表，解析 data 为 id、name、gender、birthday、judgeField、judgeTimes
                String[] refereeData = dataToUpdate.split(",");

                //使用预编译语句更新数据
                String sql = "UPDATE Referees SET name=?, gender=?, birthday=?, judgeField=?, judgeTimes=? WHERE name=?";
                pstmt = con.prepareStatement(sql);

                //设置更新的值
                pstmt.setString(1, refereeData[0].trim());  // name
                pstmt.setString(2, refereeData[1].trim());  // gender
                pstmt.setString(3, refereeData[2].trim());  // birthday
                pstmt.setString(4, refereeData[3].trim());  // judgeField
                pstmt.setString(5, refereeData[4].trim());  // judgeTimes
                pstmt.setString(6, refereeData[5].trim());  // WHERE name

            } else {
                //输入数据格式不符合要求，弹出错误提示
                JOptionPane.showMessageDialog(this, "输入数据格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }

            // 执行更新操作
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //关闭连接资源
            JDBCUtils.close(con, pstmt, null);
        }
    }


    /**
     * 验证输入数据的格式是否正确。
     *
     * @param tableName 数据库表名
     * @param data      要验证的数据
     * @return 如果数据格式正确，则返回 true；否则返回 false
     */
    private boolean validateInput(String tableName, String data) {
        // 移除输入数据的前导和尾随空格
        data = data.trim();

        // 使用逗号拆分输入数据
        String[] fields = data.split(",");

        // 根据表名验证字段数量
        if ("Athletes".equals(tableName) && fields.length == 6) {
            // 如果是 Athletes 表，且有 6 个字段，返回 true
            return true;
        } else if ("Referees".equals(tableName) && fields.length == 5) {
            // 如果是 Referees 表，且有 5 个字段，返回 true
            return true;
        }

        // 如果字段数量不正确，返回 false
        return false;
    }

    /**
     * 刷新数据展示区域的方法
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    protected void refreshData(JTextArea dataTextArea) {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 获取数据库连接
            con = JDBCUtils.getConnection();

            // 获取选中的单选框（Athletes或Referees）
            String selectedTable = updateUI.athletesRadioButton.isSelected() ? "Athletes" : "Referees";

            // 构建查询语句
            String sql = "SELECT * FROM " + selectedTable;

            // 如果有搜索条件，则添加搜索条件
            String searchText = updateUI.selectField.getText().trim();
            if (!searchText.isEmpty()) {
                sql += " WHERE name LIKE '%" + searchText + "%'";
            }

            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            // 构建字符串来存储结果
            StringBuilder result = new StringBuilder();

            // 遍历结果集，构建展示数据的字符串
            while (rs.next()) {
                // 根据具体的表结构获取字段值
                String id = rs.getString("id");
                String name = rs.getString("name");
                String gender = rs.getString("gender");
                String birthday = rs.getString("birthday");

                // 如果是Athletes表，还需获取其他字段
                if ("Athletes".equals(selectedTable)) {
                    String profession = rs.getString("profession");
                    String enterDate = rs.getString("enterDate");
                    String club = rs.getString("club");

                    result.append(id).append(", ").append(name).append(", ").append(gender)
                            .append(", ").append(birthday).append(", ").append(profession)
                            .append(", ").append(enterDate).append(", ").append(club).append("\n");
                } else {
                    // 如果是Referees表，获取其他字段
                    String judgeField = rs.getString("judgeField");
                    int judgeTimes = rs.getInt("judgeTimes");

                    result.append(id).append(", ").append(name).append(", ").append(gender)
                            .append(", ").append(birthday).append(", ").append(judgeField)
                            .append(", ").append(judgeTimes).append("\n");
                }
            }

            // 将结果显示在文本区域
            dataTextArea.setText(result.toString());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接资源
            JDBCUtils.close(con, pstmt, rs);
        }
    }
}
