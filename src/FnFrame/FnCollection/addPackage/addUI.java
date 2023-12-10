package FnFrame.FnCollection.addPackage;

import FnFrame.ButtonClickListener.CommonButtonClickListener;

import javax.swing.*;
import java.awt.*;

/**
 * 用于添加数据的用户界面
 */
public class addUI extends JFrame {
    protected static ButtonGroup radioButtonGroup;
    protected static JRadioButton athletesRadioButton;
    protected static JRadioButton refereeRadioButton;
    protected static JTextField selectField;
    protected JTextArea dataTextArea;

    /**
     * 打开数据添加窗口
     */
    public void addUIWindow() {
        setTitle("数据添加");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //创建addUI面板
        JPanel addUIPanel = new JPanel();
        addUIPanel.setLayout(new BorderLayout());

        //构建第一行按钮面板
        JPanel buttonPanel = createButtonPanel();
        addUIPanel.add(buttonPanel, BorderLayout.NORTH);

        //构建第二行单选框和搜索框面板
        JPanel optionPanel = createOptionPanel();
        addUIPanel.add(optionPanel, BorderLayout.CENTER);

        //构建第三行数据展示面板
        JPanel dataPanel = createDataPanel();
        addUIPanel.add(dataPanel, BorderLayout.SOUTH);

        add(addUIPanel);

        setVisible(true);
    }

    /**
     * 创建包含单选框和搜索功能的面板
     *
     * @return 创建的选项面板
     */
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

        //添加按钮点击事件监听器
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
        updateButton.addActionListener(CommonButtonClickListener.createUpdateButtonListener());
        selectButton.addActionListener(CommonButtonClickListener.createSelectButtonListener());
        backupButton.addActionListener(CommonButtonClickListener.createBackUpButtonListener());

        return buttonPanel;
    }

    /**
     * 创建包含单选框和搜索功能的面板
     *
     * @return 创建的选项面板
     */
    private JPanel createOptionPanel() {

        JPanel optionPanel = new JPanel();

        radioButtonGroup = new ButtonGroup();
        athletesRadioButton = new JRadioButton("Athletes");
        refereeRadioButton = new JRadioButton("Referees");
        selectField = new JTextField(20);
        JButton selectButton = new JButton("搜索");

        //添加搜索按钮点击事件监听器
        selectButton.addActionListener(e -> refreshData());

        //将单选框和搜索框添加到面板
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

    /**
     * 刷新数据展示区域
     */
    private JPanel createDataPanel() {

        JPanel dataPanel = new JPanel();

        dataTextArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(dataTextArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));
        dataPanel.add(scrollPane);
        return dataPanel;
    }

    /**
     * 刷新数据展示区域
     */
    private JPanel createAddDataPanel() {

        JPanel addDataPanel = new JPanel();
        JLabel dataLabel = new JLabel("添加数据:");

        JTextField dataField = new JTextField(20);
        JButton addButton = new JButton("添加数据");

        addButton.addActionListener(e -> {
            //获取选中的单选框（Athletes或Referees）
            String selectedTable = athletesRadioButton.isSelected() ? "Athletes" : "Referees";
            //获取要添加的数据
            String dataToAdd = dataField.getText();
            //进行数据库插入操作
            //验证输入数据格式
            if (validateInput(selectedTable, dataToAdd)) {
                //进行数据库插入操作
                insertData(selectedTable, dataToAdd);
                //刷新数据展示区域
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

    /**
     * 刷新数据展示区域
     */
    private void refreshData() {
        addFn addFnInstance = new addFn();
        addFnInstance.refreshData(dataTextArea);
    }

    /**
     * 进行数据库插入操作
     *
     * @param selectedTable 选中的表
     * @param dataToAdd     要添加的数据
     */
    private void insertData(String selectedTable, String dataToAdd) {
        addFn addFnInstance = new addFn();
        addFnInstance.insertData(selectedTable, dataToAdd);
    }

    /**
     * 验证输入数据格式
     *
     * @param selectedTable 选中的表
     * @param dataToAdd     要添加的数据
     * @return 验证结果
     */
    private boolean validateInput(String selectedTable, String dataToAdd) {
        addFn addFnInstance = new addFn();
        return addFnInstance.validateInput(selectedTable, dataToAdd);
    }
}
