package LoginFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import FnFrame.FnFrame;
public class LoginFrame extends JFrame {
    private final JTextField usernameField;
    private final JPasswordField passwordField;

    public LoginFrame() {
        //设定窗体属性
        setTitle("裁判通");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        //创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        //创建登录面板
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(3, 2));

        //用户名标签和文本框
        JLabel usernameLabel = new JLabel("用户名");
        usernameField = new JTextField();
        //添加进面板中
        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);

        //密码标签和密码框
        JLabel passwordLabel = new JLabel("密码");
        passwordField = new JPasswordField();
        //添加进面板中
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);

        //创建登录按钮
        JButton loginButton = new JButton("登录");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        //创建登录按钮面板
        JPanel loginButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        loginButtonPanel.add(loginButton);

        //创建退出按钮
        JButton exitButton = new JButton("退出");
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        //创建退出按钮面板
        JPanel exitButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        exitButtonPanel.add(exitButton);

        //添加登录面板和登录按钮面板到主面板的左侧,登录面板的底部
        mainPanel.add(loginPanel, BorderLayout.PAGE_START);
        mainPanel.add(loginButtonPanel, BorderLayout.WEST);
        mainPanel.add(exitButtonPanel, BorderLayout.EAST);

        //将主面板添加到窗体
        add(mainPanel);

        //显示窗体
        setVisible(true);
    }


    /**
     * 登录方法，用于处理用户输入的用户名和密码，并进行登录验证
     */
    private void login() {
        int loginPassCode;

        //接收输入
        String username = usernameField.getText();
        char[] passwordChars = passwordField.getPassword();
        String password = new String(passwordChars);

        //调用checkLogin检查输入
        loginPassCode = checkLogin(username,password);

        if (loginPassCode == 1) {
            //创建FnFrame对象以便使用其方法
            FnFrame fnFrame = new FnFrame();
            //打开功能选择窗口
            fnFrame.openFunctionWindow();
        }
        else {
            //弹出窗口提示
            int option = JOptionPane.showConfirmDialog(this, "登录失败，是否重新输入?", "登录失败", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                clearFields();
            }
        }
    }

    /**
     * 检查登录信息的方法，用于验证输入的用户名和密码是否正确
     *
     * @param username 用户名
     * @param password 密码
     * @return 登录验证结果，1表示成功，0表示失败
     */
    private int checkLogin(String username, String password) {
        //设定一个状态值
        int detectLoginStatus;
        //判定输入
        if ("cocacocca".equals(username) && "coca915917282".equals(password)) {
            JOptionPane.showMessageDialog(this, "登录成功！");//显示Dialog
            detectLoginStatus = 1;
            return detectLoginStatus;
        } else {
            JOptionPane.showMessageDialog(this, "登录失败，请检查用户名和密码！");
            return 0;
        }
    }

    /**
     * 清除登录失败时的输入，将用户名和密码字段置为空
     */
    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}