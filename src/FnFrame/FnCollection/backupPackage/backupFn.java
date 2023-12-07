package FnFrame.FnCollection;

import FnFrame.ButtonClickListenerImplements.CommonButtonClickListener;
import util.JDBCUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class backupFn extends JFrame {
    private ButtonGroup radioButtonGroup;
    private JRadioButton athletesRadioButton;
    private JRadioButton refereeRadioButton;

    public void backupFnWinodw(){
        setTitle("数据备份");
        setSize(800,600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //创建backupFn面板
        JPanel backupFnPanel = new JPanel();
        backupFnPanel.setLayout(new BorderLayout());

        //构建第一行按钮面板
        JPanel buttonPanel = createButtonPanel();
        backupFnPanel.add(buttonPanel, BorderLayout.NORTH);

        //构建第二行单选框和备份按钮
        JPanel dataPanel = createOptionPanel();
        backupFnPanel.add(dataPanel, BorderLayout.CENTER);

        add(backupFnPanel);

        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("删除数据");
        JButton updateButton = new JButton("修改数据");
        JButton selectButton = new JButton("查询数据");
        JButton addButton = new JButton("添加数据");

        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(addButton);

        // 添加按钮点击事件监听器
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
        updateButton.addActionListener(CommonButtonClickListener.createUpdateButtonListener());
        selectButton.addActionListener(CommonButtonClickListener.createSelectButtonListener());
        addButton.addActionListener(CommonButtonClickListener.createAddButtonListener());


        return buttonPanel;
    }

    private JPanel createOptionPanel() {
        JPanel optionPanel = new JPanel();
        radioButtonGroup = new ButtonGroup();
        athletesRadioButton = new JRadioButton("Athletes");
        refereeRadioButton = new JRadioButton("Referees");
        JButton backupButton = new JButton("备份");

        //添加备份按钮点击事件监听器
        backupButton.addActionListener(e -> backupData());

        // 将单选框和搜索框添加到面板
        radioButtonGroup.add(athletesRadioButton);
        radioButtonGroup.add(refereeRadioButton);
        optionPanel.add(athletesRadioButton);
        optionPanel.add(refereeRadioButton);
        optionPanel.add(backupButton);

        return optionPanel;
    }

    private void backupData(){
        String selectedTable = athletesRadioButton.isSelected() ? "Athletes" : "Referees";

        //获取用户选择的备份路径
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择备份文件保存路径");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int userSelection = fileChooser.showSaveDialog(this);

        if(userSelection == JFileChooser.APPROVE_OPTION){
            File selectedFile = fileChooser.getSelectedFile();
            String backupFilePath = selectedFile.getAbsolutePath();

            backupData(selectedTable, backupFilePath);
        }
    }

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

                writer.write(rowData.toString() + "\n");
            }

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
