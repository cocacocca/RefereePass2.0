package FnFrame.FnCollection.updatePackage;

import FnFrame.ButtonClickListener.CommonButtonClickListener;

import javax.swing.*;
import java.awt.*;

/**
 * 用于更新数据的用户界面
 */
public class updateUI extends JFrame{
    protected static JTextField dataField;
    protected static ButtonGroup radioButtonGroup;
    protected static JRadioButton athletesRadioButton;
    protected static JRadioButton refereeRadioButton;
    protected static JTextField selectField;
    protected static JTextArea dataTextArea;

    /**
     * 打开更新数据窗口
     */
    public void updateUIWindow() {
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

    /**
     * 创建包含单选框和搜索功能的面板
     *
     * @return 创建的选项面板
     */
    private JPanel CreateOptionPanel() {

        JPanel optionPanel = new JPanel();

        radioButtonGroup = new ButtonGroup();
        athletesRadioButton = new JRadioButton("Athletes");
        refereeRadioButton = new JRadioButton("Referees");
        selectField = new JTextField(20);
        JButton selectButton = new JButton("搜索");

        //添加搜索按钮点击事件监听器
        selectButton.addActionListener(e -> refreshData(dataTextArea));

        //将单选框和搜索框添加到面板
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

    /**
     * 创建包含各种操作按钮的面板
     *
     * @return 创建的按钮面板
     */
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

        //添加按钮点击事件监听器
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
        addButton.addActionListener(CommonButtonClickListener.createAddButtonListener());
        selectButton.addActionListener(CommonButtonClickListener.createSelectButtonListener());
        backupButton.addActionListener(CommonButtonClickListener.createBackUpButtonListener());

        return buttonPanel;
    }

    /**
     * 创建包含显示数据的面板
     *
     * @return 创建的数据面板
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
     * 创建包含更新数据的面板
     *
     * @return 创建的更新数据面板
     */
    private JPanel createUpdateDataPanel() {

        JPanel updateDataPanel = new JPanel();
        JLabel dataLabel = new JLabel("更新数据:");

        dataField = new JTextField(20);

        JButton updateButton = new JButton("更新数据");

        updateButton.addActionListener(e -> updateData(dataTextArea));

        updateDataPanel.add(dataLabel);
        updateDataPanel.add(dataField);
        updateDataPanel.add(updateButton);

        return updateDataPanel;
    }

    /**
     * 更新数据
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    private void updateData(JTextArea dataTextArea) {
        updateFn updateFnInstance = new updateFn();
        updateFnInstance.updateData(dataTextArea);
    }

    /**
     * 刷新数据
     *
     * @param dataTextArea 用于显示数据的文本区域
     */
    private void refreshData(JTextArea dataTextArea) {
        updateFn updateFnInstance = new updateFn();
        updateFnInstance.refreshData(dataTextArea);
    }
}
