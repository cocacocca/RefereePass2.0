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

public class updateFn extends JFrame {
    private JTextField dataField;
    private ButtonGroup radioButtonGroup;
    private JRadioButton athletesRadioButton;
    private JRadioButton refereeRadioButton;
    private JTextField selectField;
    private JTextArea dataTextArea;

    public void updateFnWindow() {
        setTitle("修改数据");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //创建updateFn面板
        JPanel updateFnPanel = new JPanel();
        updateFnPanel.setLayout(new BorderLayout());

        //构建第一行按钮面板
        JPanel buttonPanel = createButtonPanel();
        updateFnPanel.add(buttonPanel, BorderLayout.NORTH);

        //构建第二行单选框和搜索框面板
        JPanel optionPanel = CreateOptionPanel();
        updateFnPanel.add(optionPanel, BorderLayout.CENTER);

        //构建第三行数据展示面板
        JPanel dataPanel = createDataPanel();
        updateFnPanel.add(dataPanel, BorderLayout.SOUTH);

        add(updateFnPanel);

        setVisible(true);
    }

    private JPanel CreateOptionPanel() {
        JPanel optionPanel = new JPanel();
        radioButtonGroup = new ButtonGroup();
        athletesRadioButton = new JRadioButton("Athletes");
        refereeRadioButton = new JRadioButton("Referees");
        selectField = new JTextField(20);
        JButton selectButton = new JButton("搜索");

        // 添加搜索按钮点击事件监听器
        selectButton.addActionListener(e -> refreshData());

        // 将单选框和搜索框添加到面板
        radioButtonGroup.add(athletesRadioButton);
        radioButtonGroup.add(refereeRadioButton);
        optionPanel.add(athletesRadioButton);
        optionPanel.add(refereeRadioButton);
        optionPanel.add(selectField);
        optionPanel.add(selectButton);

        //创建添加数据的面板
        JPanel addDataPanel = createUpdateDataPanel();
        optionPanel.add(addDataPanel);

        return optionPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("删除数据");
        JButton addButton = new JButton("增加数据");
        JButton selectButton = new JButton("查询数据");
        JButton backupButton = new JButton("备份数据");

        buttonPanel.add(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(backupButton);

        // 添加按钮点击事件监听器
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
        addButton.addActionListener(CommonButtonClickListener.createAddButtonListener());
        selectButton.addActionListener(CommonButtonClickListener.createSelectButtonListener());
        backupButton.addActionListener(CommonButtonClickListener.createBackUpButtonListener());


        return buttonPanel;
    }

    private JPanel createDataPanel() {
        JPanel dataPanel = new JPanel();
        dataTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(dataTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        dataPanel.add(scrollPane);
        return dataPanel;
    }

    private JPanel createUpdateDataPanel() {
        JPanel updateDataPanel = new JPanel();
        JLabel dataLabel = new JLabel("更新数据:");

        dataField = new JTextField(20);

        JButton updateButton = new JButton("更新数据");

        updateButton.addActionListener(e -> updateData());

        updateDataPanel.add(dataLabel);
        updateDataPanel.add(dataField);
        updateDataPanel.add(updateButton);

        return updateDataPanel;
    }

    private void updateData() {

        // 获取选中的单选框（Athletes或Referees）
        String selectedTable = athletesRadioButton.isSelected() ? "Athletes" : "Referees";
        // 获取要更新的数据
        String dataToUpdate = dataField.getText();

        // 验证输入数据格式
        if (validateInput(selectedTable, dataToUpdate)) {

            // 然后执行数据库更新操作
            updateData(selectedTable, dataToUpdate);

            // 刷新数据展示区域
            refreshData();
        } else {
            JOptionPane.showMessageDialog(this, "输入数据格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateData(String tableName, String dataToUpdate) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 获取数据库连接
            con = JDBCUtils.getConnection();

            // 根据选择的表进行不同的处理
            if ("Athletes".equals(tableName) && validateInput(tableName, dataToUpdate)) {
                // 如果是 Athletes 表，解析 data 为 id、name、gender、birthday、profession、enterDate、club
                String[] athleteData = dataToUpdate.split(",");

                // 使用预编译语句更新数据
                String sql = "UPDATE Athletes SET name=?, gender=?, birthday=?, profession=?, enterDate=?, club=? WHERE name=?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, athleteData[0].trim());  // name
                pstmt.setString(2, athleteData[1].trim());  // gender
                pstmt.setString(3, athleteData[2].trim());  // birthday
                pstmt.setString(4, athleteData[3].trim());  // profession
                pstmt.setString(5, athleteData[4].trim());  // enterDate
                pstmt.setString(6, athleteData[5].trim());  // club
                pstmt.setString(7, athleteData[0].trim());  // WHERE name


            } else if ("Referees".equals(tableName) && validateInput(tableName, dataToUpdate)) {
                // 如果是 Referees 表，解析 data 为 id、name、gender、birthday、judgeField、judgeTimes
                String[] refereeData = dataToUpdate.split(",");

                // 使用预编译语句更新数据
                String sql = "UPDATE Referees SET name=?, gender=?, birthday=?, judgeField=?, judgeTimes=? WHERE name=?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, refereeData[0].trim());  // name
                pstmt.setString(2, refereeData[1].trim());  // gender
                pstmt.setString(3, refereeData[2].trim());  // birthday
                pstmt.setString(4, refereeData[3].trim());  // judgeField
                pstmt.setString(5, refereeData[4].trim());  // judgeTimes
                pstmt.setString(6, refereeData[5].trim());  // WHERE name

            } else {
                // 输入数据格式不符合要求，弹出错误提示
                JOptionPane.showMessageDialog(this, "输入数据格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }

            // 执行更新操作
            int rowsUpdated = pstmt.executeUpdate();
            System.out.println("Rows Updated: " + rowsUpdated);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭连接资源
            JDBCUtils.close(con, pstmt, null);
        }
    }


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
