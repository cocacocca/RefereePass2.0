package FnFrame.FnCollection.backupPackage;

import FnFrame.ButtonClickListener.CommonButtonClickListener;

import javax.swing.*;
import java.awt.*;

/**
 * 用于备份数据的用户界面
 */
public class backupUI extends JFrame {
    protected static JRadioButton athletesRadioButton;

    /**
     * 打开备份数据窗口
     */
    public void backupUIWindow(){
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

    /**
     * 创建包含单选框和备份功能的面板
     *
     * @return 创建的选项面板
     */
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

        //添加按钮点击事件监听器
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
        updateButton.addActionListener(CommonButtonClickListener.createUpdateButtonListener());
        selectButton.addActionListener(CommonButtonClickListener.createSelectButtonListener());
        addButton.addActionListener(CommonButtonClickListener.createAddButtonListener());

        return buttonPanel;
    }

    /**
     * 创建包含单选框和备份功能的面板
     *
     * @return 创建的选项面板
     */
    private JPanel createOptionPanel() {

        JPanel optionPanel = new JPanel();

        ButtonGroup radioButtonGroup = new ButtonGroup();
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

    /**
     * 备份数据
     */
    private void backupData() {
        backupFn backupFnInstance = new backupFn();
        backupFnInstance.backupData();
    }
}
