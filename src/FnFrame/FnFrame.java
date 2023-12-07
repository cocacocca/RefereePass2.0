package FnFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import FnFrame.FnCollection.*;

public class FnFrame {

    public void openFunctionWindow() {
        JFrame functionFrame = new JFrame("功能选择");
        functionFrame.setSize(400, 200);
        functionFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        functionFrame.setLocationRelativeTo(null);

        //添加功能选择组件
        //添加按钮
        JButton addButton = new JButton("添加数据");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFn addFnInstance = new addFn();
                addFnInstance.addFnWindow();
            }
        });

        //删除按钮
        JButton deleteButton = new JButton("删除数据");
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFn deleteFnInstance = new deleteFn();
                deleteFnInstance.deleteFnWindow();
            }
        });

        //修改按钮
        JButton updateButton = new JButton("修改数据");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateFn updateFnInstance = new updateFn();
                updateFnInstance.updateFnWindow();
            }
        });

        //查询按钮
        JButton selectButton = new JButton("查询数据");
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFn selectFnInstance = new selectFn();
                selectFnInstance.selectFnWindow();
            }
        });

        //备份按钮
        JButton backupButton = new JButton("备份数据");
        backupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backupFn backupFnInstance = new backupFn();
                backupFnInstance.backupFnWinodw();
            }
        });

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