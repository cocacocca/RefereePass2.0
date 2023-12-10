package FnFrame.FnCollection.selectPackage;

import FnFrame.ButtonClickListener.CommonButtonClickListener;

import javax.swing.*;
import java.awt.*;

/**
 * 查询数据的用户界面
 */
public class selectUI extends selectFn{
    protected static ButtonGroup radioButtonGroup;
    protected static JRadioButton athletesRadioButton;
    protected static JRadioButton refereeRadioButton;
    protected static JTextField selectField;
    private JTextArea dataTextArea;

    /**
     * 打开数据查询窗口
     */
    public void selectUIWindow() {
        setTitle("数据查询");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //创建selectFn面板
        JPanel selectFnPanel = new JPanel();
        selectFnPanel.setLayout(new BorderLayout());

        //构建第一行按钮面板
        JPanel buttonPanel = createButtonPanel();
        selectFnPanel.add(buttonPanel, BorderLayout.NORTH);

        //构建第二行单选框和搜索框面板
        JPanel optionPanel = createOptionPanel();
        selectFnPanel.add(optionPanel, BorderLayout.CENTER);

        //构建第三行数据展示面板
        JPanel dataPanel = createDataPanel();
        selectFnPanel.add(dataPanel, BorderLayout.SOUTH);

        add(selectFnPanel);

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
        JButton addButton = new JButton("添加数据");
        JButton backupButton = new JButton("备份数据");

        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(addButton);
        buttonPanel.add(backupButton);

        //添加按钮点击事件监听器
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
        updateButton.addActionListener(CommonButtonClickListener.createUpdateButtonListener());
        addButton.addActionListener(CommonButtonClickListener.createAddButtonListener());
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
        selectButton.addActionListener(e -> refreshData(dataTextArea));

        //将单选框和搜索框添加到面板
        radioButtonGroup.add(athletesRadioButton);
        radioButtonGroup.add(refereeRadioButton);
        optionPanel.add(athletesRadioButton);
        optionPanel.add(refereeRadioButton);
        optionPanel.add(selectField);
        optionPanel.add(selectButton);

        return optionPanel;
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
}
