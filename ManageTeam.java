import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageTeam extends JPanel {
    
    private MainMenu mainMenu;
    private ArrayList<Team> teams;

    public ManageTeam(MainMenu mainMenu) {
        setLayout(new BorderLayout());

        // 設定標題
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JPanel MenuPanel = new JPanel();
        MenuPanel.setLayout(new BorderLayout());
        
        JButton backToMenu = new JButton("Menu");
        backToMenu.setFont(new Font("Serif", Font.BOLD, 20));
        backToMenu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                mainMenu.showMenu();
            }
        });
        JLabel titleLabel = new JLabel("球隊列表", JLabel.CENTER);
        titleLabel.setFont(new Font("標楷體", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        // 創建球隊按鈕面板
        JPanel teamsButtonPanel = new JPanel();
        teamsButtonPanel.setLayout(new GridLayout(10, 2, 10, 50));
        teamsButtonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 20, 50));
        
        //設置創建球隊按鈕
        JPanel createTeam = new JPanel();
        createTeam.setLayout(new BorderLayout());
        createTeam.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        JButton createTeamButton = new JButton("Create Team");
        createTeamButton.setFont(new Font("Serif", Font.PLAIN, 30));
        createTeam.add(createTeamButton, BorderLayout.CENTER);
        createTeamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateTeamDialog();
            }
        });

        // 獲取所有球隊並創建按鈕
        TeamDAO teamDAO = new TeamDAO();
        teams = teamDAO.getAllTeams();
        Font buttonFont = new Font("Serif", Font.PLAIN,30);

        for (Team team : teams) {
            JButton teamButton = new JButton(team.getCity() + "  " + team.getName());
            teamButton.setFont(buttonFont);
            teamsButtonPanel.add(teamButton);
        }

        MenuPanel.add(backToMenu, BorderLayout.WEST);
        titlePanel.add(MenuPanel, BorderLayout.SOUTH);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        // 設定框架的Layout
        add(titlePanel, BorderLayout.NORTH);
        add(new JScrollPane(teamsButtonPanel), BorderLayout.CENTER);
        add(createTeam, BorderLayout.SOUTH);
    }

    //CreateTeam頁面
    private void showCreateTeamDialog() {
        JDialog dialog = new JDialog((Frame) null, "Create Team", true);
        
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(6, 1, 50, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());
        
        JLabel nameLabel = new JLabel("Team Name:");
        JTextField nameField = new JTextField();
        JLabel cityLabel = new JLabel("Team City:");
        JTextField cityField = new JTextField();
        JLabel foundationYearLabel = new JLabel("Foundation Year:");
        JTextField foundationYearField = new JTextField();
        
        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        Font TextFont = new Font("Serif", Font.PLAIN, 22);
        nameLabel.setFont(LabelFont);
        cityLabel.setFont(LabelFont);
        foundationYearLabel.setFont(LabelFont);
        nameField.setFont(TextFont);
        cityField.setFont(TextFont);
        foundationYearField.setFont(TextFont);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String city = cityField.getText();
                int foundationYear = Integer.parseInt(foundationYearField.getText());

                TeamDAO teamDAO = new TeamDAO();
                teamDAO.addTeam(name, city, foundationYear);
                dialog.dispose();

                // 更新球隊列表
                refreshTeamList();
            }
        });

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(cityLabel);
        inputPanel.add(cityField);
        inputPanel.add(foundationYearLabel);
        inputPanel.add(foundationYearField);
        
        savePanel.add(saveButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(savePanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void refreshTeamList() {
        removeAll();
        revalidate();
        repaint();
        // 重新初始化介面
        new ManageTeam(mainMenu);
    }
}
