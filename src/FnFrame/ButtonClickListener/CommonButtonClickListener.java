package FnFrame.ButtonClickListener;

import FnFrame.FnCollection.addPackage.addUI;
import FnFrame.FnCollection.backupPackage.backupUI;
import FnFrame.FnCollection.deletePackage.deleteUI;
import FnFrame.FnCollection.selectPackage.selectUI;
import FnFrame.FnCollection.updatePackage.updateUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 用于创建按钮点击事件监听器的类
 */
public class CommonButtonClickListener{
    public static ActionListener createDeleteButtonListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteUI deleteUIInstance = new deleteUI();
                deleteUIInstance.deleteUIWindow();
            }
        };
    }

    public static ActionListener createUpdateButtonListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUI updateUIInstance = new updateUI();
                updateUIInstance.updateUIWindow();
            }
        };
    }

    public static ActionListener createAddButtonListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addUI addUIInstance = new addUI();
                addUIInstance.addUIWindow();
            }
        };
    }

    public static ActionListener createSelectButtonListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectUI selectUIInstance = new selectUI();
                selectUIInstance.selectUIWindow();
            }
        };
    }

    public static ActionListener createBackUpButtonListener(){
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                backupUI backupUIInstance = new backupUI();
                backupUIInstance.backupUIWindow();
            }
        };
    }
}
