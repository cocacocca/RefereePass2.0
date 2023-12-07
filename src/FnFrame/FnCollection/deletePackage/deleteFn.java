package FnFrame.FnCollection;

import FnFrame.ButtonClickListenerImplements.CommonButtonClickListener;
import util.JDBCUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class deleteFn extends JFrame {
    private ButtonGroup radioButtonGroup;
    private JRadioButton athletesRadioButton;
    private JRadioButton refereeRadioButton;
    private JTextField selectField;
    private JTextArea dataTextArea;

    public void deleteFnWindow(){
        setTitle("数据删除");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //创建deleteFn面板
        JPanel deleteFnPanel = new JPanel();
        deleteFnPanel.setLayout(new BorderLayout());

        //构建第一行按钮面板
        JPanel buttonPanel = createButtonPanel();
        deleteFnPanel.add(buttonPanel, BorderLayout.NORTH);

        //构建第二行单选框和搜索框面板
        JPanel optionPanel = createOptionPanel();
        deleteFnPanel.add(optionPanel, BorderLayout.CENTER);

        //构建第三行数据展示面板
        JPanel dataPanel = createDataPanel();
        deleteFnPanel.add(dataPanel, BorderLayout.SOUTH);

        add(deleteFnPanel);

        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton addButton = new JButton("添加数据");
        JButton updateButton = new JButton("修改数据");
        JButton selectButton = new JButton("查询数据");
        JButton backupButton = new JButton("备份数据");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(backupButton);

        // 添加按钮点击事件监听器
        addButton.addActionListener(CommonButtonClickListener.createAddButtonListener());
        updateButton.addActionListener(CommonButtonClickListener.createUpdateButtonListener());
        selectButton.addActionListener(CommonButtonClickListener.createSelectButtonListener());
        backupButton.addActionListener(CommonButtonClickListener.createBackUpButtonListener());


        return buttonPanel;
    }

    private JPanel createOptionPanel() {
        JPanel optionPanel = new JPanel();
        radioButtonGroup = new ButtonGroup();
        athletesRadioButton = new JRadioButton("Athletes");
        refereeRadioButton = new JRadioButton("Referees");
        selectField = new JTextField(20);
        JButton selectButton = new JButton("搜索");
        JButton deleteButton = new JButton("删除");

        // 添加搜索按钮点击事件监听器
        selectButton.addActionListener(e -> refreshData());

        deleteButton.addActionListener(e -> deleteData());

        // 将单选框和搜索框添加到面板
        radioButtonGroup.add(athletesRadioButton);
        radioButtonGroup.add(refereeRadioButton);
        optionPanel.add(athletesRadioButton);
        optionPanel.add(refereeRadioButton);
        optionPanel.add(selectField);
        optionPanel.add(selectButton);
        optionPanel.add(deleteButton);


        return optionPanel;
    }

    private JPanel createDataPanel() {
        JPanel dataPanel = new JPanel();
        dataTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(dataTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        dataPanel.add(scrollPane);
        return dataPanel;
    }

    private void deleteData(){
        String selectedTable = athletesRadioButton.isSelected() ? "Athletes" : "Referees";

        String dataToDelete = selectField.getText().trim();

        // 验证是否输入了要删除的数据
        if (!dataToDelete.isEmpty()) {
            // 执行删除操作
            deleteData(selectedTable, dataToDelete);

            updateIDs(selectedTable);

            // 刷新数据展示区域
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "请输入要删除的数据！", "提示", JOptionPane.INFORMATION_MESSAGE);
        }

    }

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

            // 清空选中的数据
            String selectedData = null;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭连接资源
            JDBCUtils.close(con, pstmt, null);
        }
    }

    private void updateIDs(String tableName) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = JDBCUtils.getConnection();

            // 构建更新语句，重新排序 ID
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

    private void refreshData() {
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            // 获取数据库连接
            con = JDBCUtils.getConnection();

            // 获取选中的单选框（Athletes或Referees）
            String selectedTable = athletesRadioButton.isSelected() ? "Athletes" : "Referees";

            // 构建查询语句
            String sql = "SELECT * FROM " + selectedTable;

            // 如果有搜索条件，则添加搜索条件
            String searchText = selectField.getText().trim();
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
