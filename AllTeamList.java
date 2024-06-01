import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class AllTeamList extends JPanel{
    
    private MainMenu mainMenu;
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel teamListPanel;
    private JPanel loadingPanel;
    private ArrayList<Team> teams;
    private TeamDAO teamDAO;

    public AllTeamList(MainMenu mainMenu){
        this.mainMenu = mainMenu;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        teamDAO = new TeamDAO();

        mainPanel = new JPanel(new BorderLayout());
        loadingPanel = new JPanel(new BorderLayout());

        initializeUI();
        new LoadTeamsWorker().execute();

    }

    private void initializeUI(){
        //Title Panel
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

        JLabel titleLabel = new JLabel("所有球隊列表", JLabel.CENTER);
        titleLabel.setFont(new Font("標楷體", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        MenuPanel.add(backToMenu, BorderLayout.EAST);
        titlePanel.add(MenuPanel, BorderLayout.SOUTH);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        mainPanel.add(titlePanel, BorderLayout.NORTH);

        //teamList Panel
        teamListPanel = new JPanel(new BorderLayout());
        mainPanel.add(teamListPanel, BorderLayout.CENTER);

        // 加載中的消息
        JLabel loadingText = new JLabel("Loading Team Data...", JLabel.CENTER);
        loadingText.setFont(new Font("Serif", Font.BOLD, 30));
        loadingPanel.add(loadingText, BorderLayout.CENTER);

        add(mainPanel, "teamList");
        add(loadingPanel, "loading");

        cardLayout.show(this, "loading");
    }

    private class LoadTeamsWorker extends SwingWorker<ArrayList<Team>, Void> {
        @Override
        protected ArrayList<Team> doInBackground() throws Exception {
            return teamDAO.getAllTeams();
        }

        @Override
        protected void done() {
            try {
                teams = get();
                displayTeams();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayTeams() {
        teamListPanel.removeAll();
        //TeamList Panel
        JPanel teamsButtonPanel = new JPanel();
        teamsButtonPanel.setLayout(new GridLayout(10, 2, 10, 20));
        teamsButtonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 20, 50));

        Font buttonFont = new Font("Serif", Font.PLAIN, 30);
        for (Team team : teams) {
            JButton teamButton = new JButton(team.toString());
            teamButton.setFont(buttonFont);
            teamsButtonPanel.add(teamButton);
            teamButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showLoadingScreen();
                    new LoadPlayerCareerDataWorker(team).execute();
                }
            });
        }

        teamListPanel.add(new JScrollPane(teamsButtonPanel), BorderLayout.CENTER);
        showTeamList();
        revalidate();
        repaint();
    }

    private void showLoadingScreen() {
        JPanel loadingPanel = new JPanel(new BorderLayout());
        loadingPanel.add(new JLabel("Loading player data...", JLabel.CENTER), BorderLayout.CENTER);
        add(loadingPanel, "加載中");
        cardLayout.show(this, "加載中");
    }
    
    public void showTeamList() {
        cardLayout.show(this, "teamList");
    }

    private class LoadPlayerCareerDataWorker extends SwingWorker<PlayerCareerData, Void> {
        private Team team;

        public LoadPlayerCareerDataWorker(Team team) {
            this.team = team;
        }

        @Override
        protected PlayerCareerData doInBackground() throws Exception {
            return new PlayerCareerData(team, AllTeamList.this);
        }

        @Override
        protected void done() {
            try {
                PlayerCareerData playerCareerData = get();
                add(playerCareerData, team.getName());
                cardLayout.show(AllTeamList.this, team.getName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
