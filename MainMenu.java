import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;   //主面板
    private JPanel menuPanel;   //菜單面板
    

    public MainMenu() {
        setTitle("BasketBall Data APP");
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 設定主面板和CardLayout
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        menuPanel = new JPanel(new BorderLayout());
        
        // 設定標題
        JLabel titleLabel = new JLabel("籃球比賽資料庫", JLabel.CENTER);
        titleLabel.setFont(new Font("標楷體", Font.BOLD, 80));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(80, 0, 50, 0));

        // 創建按鈕
        JButton button1 = new JButton("Create or Manage Teams and Players");
        JButton button2 = new JButton("Create or Search Games Data");
        JButton button3 = new JButton("Search or Analyze Players Carrer Data");

        Font buttonFont = new Font("Serif", Font.PLAIN, 32);
        button1.setFont(buttonFont);
        button2.setFont(buttonFont);
        button3.setFont(buttonFont);

        // 設定按鈕點擊事件
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "功能1");
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "功能2");
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "功能3");
            }
        });

        // 設定按鈕的Panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 100, 80));
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

        // 添加標題和按鈕到主菜單面板
        menuPanel.add(titleLabel, BorderLayout.NORTH);
        menuPanel.add(buttonPanel, BorderLayout.CENTER);

        // 創建功能頁面面板
        ManageTeam manageTeam = new ManageTeam(this);

        JPanel functionPanel2 = new JPanel(new BorderLayout());
        functionPanel2.add(new JLabel("這是功能2頁面", JLabel.CENTER), BorderLayout.CENTER);

        JPanel functionPanel3 = new JPanel(new BorderLayout());
        functionPanel3.add(new JLabel("這是功能3頁面", JLabel.CENTER), BorderLayout.CENTER);

        // 將所有面板添加到主面板中
        mainPanel.add(menuPanel, "主菜單");
        mainPanel.add(manageTeam, "功能1");
        mainPanel.add(functionPanel2, "功能2");
        mainPanel.add(functionPanel3, "功能3");

        // 顯示主菜單面板
        cardLayout.show(mainPanel, "主菜單");

        // 將主面板添加到框架中
        add(mainPanel);
    }

    public void showMenu() {
        cardLayout.show(mainPanel, "主菜單");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }
}
