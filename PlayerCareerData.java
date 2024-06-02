import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlayerCareerData extends JPanel{

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel careerDataPanel;
    private JPanel loadingPanel;
    private Team team;
    private ArrayList<Player> players;
    private PlayerDAO playerDAO;
    private GameStatsDAO gameStatsDAO;

    public PlayerCareerData(Team team, AllTeamList allTeamList){
        this.team = team;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        playerDAO = new PlayerDAO();
        gameStatsDAO = new GameStatsDAO();
        mainPanel = new JPanel(new BorderLayout());
        loadingPanel = new JPanel(new BorderLayout());

        initializeUI(allTeamList);

        new LoadPlayersWorker().execute();
    }
    
    public void initializeUI(AllTeamList allTeamList){
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JPanel TeamInfo = new JPanel(new BorderLayout());
        JButton backToTeamList = new JButton("Team List");
        backToTeamList.setFont(new Font("Serif", Font.BOLD, 20));
        backToTeamList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                allTeamList.showTeamList();
            }
        });
        JLabel titleLabel = new JLabel("Players Career Data", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel teamNamLabel = new JLabel(team.toString());
        teamNamLabel.setFont(new Font("Serif", Font.BOLD, 28));
        teamNamLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        TeamInfo.add(backToTeamList, BorderLayout.EAST);
        TeamInfo.add(teamNamLabel, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(TeamInfo, BorderLayout.SOUTH);

        careerDataPanel = new JPanel(new BorderLayout());

        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(careerDataPanel, BorderLayout.CENTER);

        JLabel loadingText = new JLabel("Loading player career data...", JLabel.CENTER);
        loadingText.setFont(new Font("Serif", Font.BOLD, 30));
        loadingPanel.add(loadingText, BorderLayout.CENTER);
        
        add(mainPanel, "careerData");
        add(loadingPanel, "loading");
        cardLayout.show(this, "loading");
    }

    private JPanel careerDataTable() {
        String[] columnNames = {
                "球員名稱", "上場時間(分鐘)", "得分", "助攻", "抄截", "阻攻", "籃板",
                "失誤", "命中率", "三分命中率", "罰球率"};
        Object[][] data = new Object[players.size()][11];

        for (int i = 0; i < players.size(); i++) {
            CareerStats careerStats = gameStatsDAO.getPlayerCareerStats(players.get(i).getPlayerID());
            data[i][0] = careerStats.getPlayerName();
            data[i][1] = String.format("%.1f",careerStats.getPlayingTime());
            data[i][2] = String.format("%.1f",careerStats.getPoints());
            data[i][3] = String.format("%.1f",careerStats.getAssists());
            data[i][4] = String.format("%.1f",careerStats.getSteals());
            data[i][5] = String.format("%.1f",careerStats.getBlocks());
            data[i][6] = String.format("%.1f",careerStats.getRebounds());
            data[i][7] = String.format("%.1f",careerStats.getTurnovers());
            data[i][8] = String.format("%.2f%%", (careerStats.getFGM() / careerStats.getFGA()) * 100);
            data[i][9] = String.format("%.2f%%", (careerStats.getThreePM() / careerStats.getThreePA()) * 100);
            data[i][10] = String.format("%.2f%%", (careerStats.getFTM() / careerStats.getFTA()) * 100);
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);

        JTable statsTable = new JTable(tableModel);
        statsTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        statsTable.setFont(new Font("Serif", Font.PLAIN, 20));
        statsTable.setRowHeight(40);
        TableColumnModel columnModel = statsTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(200);
        for (int i = 1; i < columnModel.getColumnCount(); i++) {
            columnModel.getColumn(i).setPreferredWidth(100); // 设置每列的首选宽度
        }
        JTableHeader tableHeader = statsTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 50));
        tableHeader.setFont(new Font("Serif", Font.BOLD, 16));

        JScrollPane scrollPane = new JScrollPane(statsTable);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private class LoadPlayersWorker extends SwingWorker<ArrayList<Player>, Void> {
        @Override
        protected ArrayList<Player> doInBackground() throws Exception {
            return playerDAO.getTeamPlayers(team.getTeamId());
        }

        @Override
        protected void done() {
            try {
                players = get();
                displayStats(team);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayStats(Team team){
        careerDataPanel.removeAll();
        careerDataPanel.add(careerDataTable(), BorderLayout.CENTER);
        cardLayout.show(this, "careerData");
        revalidate();
        repaint();
    }
}
