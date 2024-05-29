import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;   // 主面板
    private JPanel menuPanel;   // 菜單面板
    private JPanel loadingPanel; // 加載中的面板

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
        JButton button3 = new JButton("Search or Analyze Players Career Data");

        Font buttonFont = new Font("Serif", Font.PLAIN, 32);
        button1.setFont(buttonFont);
        button2.setFont(buttonFont);
        button3.setFont(buttonFont);

        menuPanel.add(titleLabel, BorderLayout.NORTH);
        

        // 設定按鈕點擊事件
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoadingScreen();
                new LoadManageTeamDataWorker().execute();
            }
        });

        button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLoadingScreen();
                new LoadSearchGamesDataWorker().execute();
            }
        });

        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "功能3");
            }
        });

        // function button
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 30));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 80, 100, 80));
        buttonPanel.add(button1);
        buttonPanel.add(button2);
        buttonPanel.add(button3);

    
        menuPanel.add(buttonPanel, BorderLayout.CENTER);

        // 創建功能頁面面板
        JPanel functionPanel3 = new JPanel(new BorderLayout());
        functionPanel3.add(new JLabel("這是功能3頁面", JLabel.CENTER), BorderLayout.CENTER);

        //loading Panel
        loadingPanel = new JPanel(new BorderLayout());
        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        JLabel loading = new JLabel("Loading...", JLabel.CENTER);
        loading.setFont(LabelFont);
        loadingPanel.add(loading, BorderLayout.CENTER);


        mainPanel.add(menuPanel, "主菜單");
        mainPanel.add(functionPanel3, "功能3");
        mainPanel.add(loadingPanel, "加載中");

        cardLayout.show(mainPanel, "主菜單");

        add(mainPanel);
    }

    private void showLoadingScreen() {
        cardLayout.show(mainPanel, "加載中");
    }

    private class LoadManageTeamDataWorker extends SwingWorker<ManageTeam, Void> {
        @Override
        protected ManageTeam doInBackground() throws Exception {
            return new ManageTeam(MainMenu.this);
        }

        @Override
        protected void done() {
            try {
                ManageTeam manageTeam = get();
                mainPanel.add(manageTeam, "功能1");
                cardLayout.show(mainPanel, "功能1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class LoadSearchGamesDataWorker extends SwingWorker<SearchGames, Void> {
        @Override
        protected SearchGames doInBackground() throws Exception {
            return new SearchGames(MainMenu.this, new Team(), new Team());
        }

        @Override
        protected void done() {
            try {
                SearchGames searchGames = get();
                mainPanel.add(searchGames, "功能2");
                cardLayout.show(mainPanel, "功能2");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
