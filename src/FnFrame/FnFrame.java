package FnFrame;

import javax.swing.*;
import java.awt.*;

import FnFrame.ButtonClickListener.CommonButtonClickListener;

public class FnFrame {

    /**
     * 打开功能选择窗口
     */
    public void openFunctionWindow() {
        JFrame functionFrame = new JFrame("功能选择");
        functionFrame.setSize(500, 150);
        functionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        functionFrame.setLocationRelativeTo(null);

        //添加功能选择组件
        //添加按钮
        JButton addButton = new JButton("添加数据");
        //删除按钮
        JButton deleteButton = new JButton("删除数据");
        //修改按钮
        JButton updateButton = new JButton("修改数据");
        //查询按钮
        JButton selectButton = new JButton("查询数据");
        //备份按钮
        JButton backupButton = new JButton("备份数据");

        addButton.addActionListener(CommonButtonClickListener.createAddButtonListener());
        deleteButton.addActionListener(CommonButtonClickListener.createDeleteButtonListener());
        updateButton.addActionListener(CommonButtonClickListener.createUpdateButtonListener());
        selectButton.addActionListener(CommonButtonClickListener.createSelectButtonListener());
        backupButton.addActionListener(CommonButtonClickListener.createBackUpButtonListener());

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(selectButton);
        buttonPanel.add(backupButton);

        functionFrame.add(buttonPanel);

        functionFrame.setVisible(true);
    }
}