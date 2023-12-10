package FnFrame.FnCollection.deletePackage;

import util.JDBCUtils;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 删除数据的功能逻辑
 */
public class deleteFn extends JFrame {

    /**
     * 删除数据的方法
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    protected void deleteData(JTextArea dataTextArea){
        //获取选中的单选框（Athletes或Referees）
        String selectedTable = deleteUI.athletesRadioButton.isSelected() ? "Athletes" : "Referees";

        //获取要删除的数据
        String dataToDelete = deleteUI.selectField.getText().trim();

        //验证是否输入了要删除的数据
        if (!dataToDelete.isEmpty()) {
            //弹出确认框，提示确认删除
            int confirmResult = JOptionPane.showConfirmDialog(
                    this, "确定要删除选定的数据吗？", "确认删除",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (confirmResult == JOptionPane.YES_OPTION) {
                //确认删除，执行删除操作
                deleteData(selectedTable, dataToDelete);

                //更新IDs
                updateIDs(selectedTable);

                //刷新数据展示区域
                refreshData(dataTextArea);
            }
            //如果选择了"NO"，什么都不做
        } else {
            //如果没有输入要删除的数据，弹出提示框,this表示当前窗口
            JOptionPane.showMessageDialog(
                    this, "请选择要删除的数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    /**
     * 删除数据库中的数据。
     *
     * @param tableName    数据库表名
     * @param dataToDelete 要删除的数据
     */
    private void deleteData(String tableName, String dataToDelete) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            //获取数据库连接
            con = JDBCUtils.getConnection();

            //构建删除语句
            String sql = "DELETE FROM " + tableName + " WHERE name = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, dataToDelete);

            //执行删除操作
            int rowsDeleted = pstmt.executeUpdate();
            System.out.println("Rows Deleted: " + rowsDeleted);

            //如果删除成功更新新的ID
            if(rowsDeleted > 0){
                updateIDs(tableName);
            }

            //清空选中的数据
            String selectedData = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            //关闭连接资源
            JDBCUtils.close(con, pstmt, null);
        }
    }

    /**
     * 更新数据库中的ID。
     *
     * @param tableName 数据库表名
     */
    private void updateIDs(String tableName) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCUtils.getConnection();

            //构建更新语句，重新排序 ID
            String sql = "ALTER TABLE " + tableName + " DROP id";
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();

            sql = "ALTER TABLE " + tableName + " ADD id INT PRIMARY KEY AUTO_INCREMENT FIRST";
            pstmt = con.prepareStatement(sql);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.close(con, pstmt, null);
        }
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
            //获取数据库连接
            con = JDBCUtils.getConnection();

            //获取选中的单选框（Athletes或Referees）
            String selectedTable = deleteUI.athletesRadioButton.isSelected() ? "Athletes" : "Referees";

            //构建查询语句
            String sql = "SELECT * FROM " + selectedTable;

            //如果有搜索条件，则添加搜索条件
            String searchText = deleteUI.selectField.getText().trim();
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

                    //拼接字符串
                    result.append(id).append(", ").append(name).append(", ").append(gender)
                            .append(", ").append(birthday).append(", ").append(profession)
                            .append(", ").append(enterDate).append(", ").append(club).append("\n");
                } else {
                    //如果是Referees表，获取其他字段
                    String judgeField = rs.getString("judgeField");
                    int judgeTimes = rs.getInt("judgeTimes");

                    //拼接字符串
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
