package FnFrame.FnCollection.backupPackage;

import util.JDBCUtils;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 备份数据的功能逻辑
 */
public class backupFn extends JFrame {
    /**
     * 备份数据的方法
     */
    protected void backupData(){
        //获取用户选择的备份表
        String selectedTable = backupUI.athletesRadioButton.isSelected() ? "Athletes" : "Referees";

        //获取用户选择的备份路径
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择备份文件保存路径");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        //弹出文件选择器对话框
        int userSelection = fileChooser.showSaveDialog(this);

        //如果用户选择了保存路径，备份数据
        if(userSelection == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            String backupFilePath = selectedFile.getAbsolutePath();

            //调用备份方法
            backupData(selectedTable, backupFilePath);
        }
    }

    /**
     * 备份数据库中的数据。
     *
     * @param tableName    数据库表名
     * @param backupFilePath 备份文件路径
     */
    private void backupData(String tableName, String backupFilePath){
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        FileWriter writer = null;

        try{
            //获取数据库连接
            con = JDBCUtils.getConnection();

            //构建查询语句
            String sql = "SELECT * FROM " + tableName;
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            //创建备份文件
            File backupFile = new File(backupFilePath);

            //如果文件不存在就创建新文件
            if(!backupFile.exists()){
                backupFile.createNewFile();
            }

            //使用FileWriter写入文件
            writer = new FileWriter(backupFile);

            //遍历表并将数据写入备份文件
            while (rs.next()){
                StringBuilder rowData = new StringBuilder();
                for (int i = 1; i<= rs.getMetaData().getColumnCount(); i++){
                    rowData.append(rs.getString(i)).append(",");
                }

                //将rowData对象的字符串表示形式写入文件，并在末尾添加换行符
                writer.write(rowData.toString() + "\n");
            }

            //弹出提示框
            JOptionPane.showMessageDialog(this, "备份完成", "成功", JOptionPane.INFORMATION_MESSAGE);


        } catch (SQLException | IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "备份失败", "错误", JOptionPane.INFORMATION_MESSAGE);
        } finally {
            //关闭连接和文件写入器
            JDBCUtils.close(con, pstmt, rs);
            if (writer != null){
                try{
                    writer.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
