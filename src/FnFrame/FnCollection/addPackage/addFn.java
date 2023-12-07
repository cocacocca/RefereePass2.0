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

public class addFn extends JFrame {
    private ButtonGroup radioButtonGroup;
    private JRadioButton athletesRadioButton;
    private JRadioButton refereeRadioButton;
    private JTextField selectField;
    private JTextArea dataTextArea;

    public void addFnWindow() {
        setTitle("数据添加");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 创建addFn面板
        JPanel addFnPanel = new JPanel();
        addFnPanel.setLayout(new BorderLayout());

        //构建第一行按钮面板
        JPanel buttonPanel = createButtonPanel();
        addFnPanel.add(buttonPanel, BorderLayout.NORTH);

        // 构建第二行单选框和搜索框面板
        JPanel optionPanel = createOptionPanel();
        addFnPanel.add(optionPanel, BorderLayout.CENTER);

        // 构建第三行数据展示面板
        JPanel dataPanel = createDataPanel();
        addFnPanel.add(dataPanel, BorderLayout.SOUTH);

        add(addFnPanel);

        setVisible(true);
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        JButton deleteButton = new JButton("删除数据");
        JButton updateButton = new JButton("修改数据");
        JButton selectButton = new JButton("查询数据");
        JButton backupButton = new JButton("备份数据");

        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(backupButton);

        // 添加按钮点击事件监听器
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
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
        JPanel addDataPanel = createAddDataPanel();
        optionPanel.add(addDataPanel);

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

    private JPanel createAddDataPanel() {
        JPanel addDataPanel = new JPanel();
        JLabel dataLabel = new JLabel("添加数据:");
        JTextField dataField = new JTextField(20);
        JButton addButton = new JButton("添加数据");

        addButton.addActionListener(e -> {
            // 获取选中的单选框（Athletes或Referees）
            String selectedTable = athletesRadioButton.isSelected() ? "Athletes" : "Referees";
            // 获取要添加的数据
            String dataToAdd = dataField.getText();
            // 进行数据库插入操作
            // 验证输入数据格式
            if (validateInput(selectedTable, dataToAdd)) {
                // 进行数据库插入操作
                insertData(selectedTable, dataToAdd);
                // 刷新数据展示区域
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "输入数据格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        addDataPanel.add(dataLabel);
        addDataPanel.add(dataField);
        addDataPanel.add(addButton);

        return addDataPanel;
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

    private void insertData(String tableName, String data) {
        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 移除输入数据的前导和尾随空格
            data = data.trim();

            // 获取数据库连接
            con = JDBCUtils.getConnection();

            // 根据选择的表进行不同的处理
            if ("Athletes".equals(tableName) && validateInput(tableName, data)) {
                // 如果是 Athletes 表，解析 data 为 name、gender、birthday、profession、enterDate、club
                String[] athleteData = data.split(",");

                // 使用预编译语句插入数据，不包括 id 字段
                String sql = "INSERT INTO Athletes (name, gender, birthday, profession, enterDate, club) " +
                        "VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                // 设置插入的值
                for (int i = 0; i < athleteData.length; i++) {
                    // 如果字段为空，设置为 NULL
                    if (athleteData[i].isEmpty()) {
                        pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(i + 1, athleteData[i].trim());
                    }
                }
            } else if ("Referees".equals(tableName) && validateInput(tableName, data)) {
                // 如果是 Referees 表，解析 data 为 name、gender、birthday、judgeField、judgeTimes
                String[] refereeData = data.split(",");

                // 使用预编译语句插入数据，不包括 id 字段
                String sql = "INSERT INTO Referees (name, gender, birthday, judgeField, judgeTimes) " +
                        "VALUES (?, ?, ?, ?, ?)";
                pstmt = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

                // 设置插入的值
                for (int i = 0; i < refereeData.length; i++) {
                    // 如果字段为空，设置为 NULL
                    if (refereeData[i].isEmpty()) {
                        pstmt.setNull(i + 1, java.sql.Types.VARCHAR);
                    } else {
                        pstmt.setString(i + 1, refereeData[i].trim());
                    }
                }
            } else {
                // 输入数据格式不符合要求，弹出错误提示
                JOptionPane.showMessageDialog(this, "输入数据格式错误！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // 执行插入操作
            pstmt.executeUpdate();

            // 获取自动生成的 ID
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                int generatedId = generatedKeys.getInt(1);
                System.out.println("Inserted ID: " + generatedId);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            // 关闭连接资源
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
