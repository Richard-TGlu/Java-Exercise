import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageGame extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel playerListPanel;
    private JPanel loadingPanel;
    private Game game;
    private ArrayList<Player> players;
    private Team team;
    private PlayerDAO playerDAO;
    private ArrayList<GameStats> gameStat;
    private GameStatsDAO gameStatsDAO;
    private GameDAO gameDAO;

    public ManageGame(Game game, SearchGames searchGames) {
        this.game = game;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        mainPanel = new JPanel(new BorderLayout());
        loadingPanel = new JPanel(new BorderLayout());

        initializeUI(searchGames);

        new LoadPlayersWorker().execute();
    }

    private void initializeUI(SearchGames searchGames) {
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JPanel TeamListPanel = new JPanel();
        TeamListPanel.setLayout(new BorderLayout());

        JButton backToTeamList = new JButton("Game List");
        backToTeamList.setFont(new Font("Serif", Font.BOLD, 20));
        backToTeamList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchGames.showGameList();
            }
        });
        JLabel teamNameLabel = new JLabel(team.getName(), JLabel.CENTER);
        teamNameLabel.setFont(new Font("Serif", Font.BOLD, 40));
        teamNameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JPanel Date = new JPanel();
        Date.setLayout(new GridLayout(1, 1, 0, 0));
        JLabel DateLabel = new JLabel("Game Date:" + game.getGameDate());
        DateLabel.setFont(new Font("Serif", Font.BOLD, 18));
        DateLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        TeamListPanel.add(backToTeamList, BorderLayout.EAST);
        TeamListPanel.add(DateLabel, BorderLayout.WEST);
        titlePanel.add(TeamListPanel, BorderLayout.SOUTH);
        titlePanel.add(teamNameLabel, BorderLayout.CENTER);

        // 這個是用來儲存修改過的資料的Button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                refreshPlayerList();
            }
        });

        // Player List Panel
        playerListPanel = new JPanel(new BorderLayout());

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(playerListPanel, BorderLayout.CENTER);

        JLabel loadingText = new JLabel("Loading Player Data...", JLabel.CENTER);
        loadingText.setFont(new Font("Serif", Font.BOLD, 30));
        loadingPanel.add(loadingText, BorderLayout.CENTER);

        add(mainPanel, "GameList");
        add(loadingPanel, "loading");

        cardLayout.show(this, "loading");
    }

    private class LoadPlayersWorker extends SwingWorker<ArrayList<GameStats>, Void> {
        @Override
        protected ArrayList<GameStats> doInBackground() throws Exception {
            gameStatsDAO = new GameStatsDAO();
            gameDAO = new GameDAO();
            // 這個條件我不知道要給什麼
            if(){
                return gameStatsDAO.getGameStatsByGameAndTeam(game.getGameId(), game.getTeam1Id());
            }
            else{
                return gameStatsDAO.getGameStatsByGameAndTeam(game.getGameId(), game.getTeam2Id());
            }

        }

        @Override
        protected void done() {
            try {
                gameStat = get();
                displayPlayers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayPlayers() {
        playerListPanel.removeAll();
        playerListPanel.add(tablePanel(), BorderLayout.CENTER);
        cardLayout.show(this, "GameList");
        revalidate();
        repaint();
    }

    // player list table
    private JPanel tablePanel() {
        String[] columnNames = { "Name", "PlayingTime", "Points", "Assists", "Steals", "Blocks", "Rebounds",
                "Turnovers", "FGM", "FGA", "ThreePM", "ThreePA", "FTM", "FTA", "Foul" };
        Object[][] data = new Object[gameStat.size()][15];
        PlayerDAO playerDAO = new PlayerDAO();

        for (int i = 0; i < gameStat.size(); i++) {
            GameStats gameStats = gameStat.get(i);
            Player player = playerDAO.getPlayerByID(gameStats.getPlayerId());
            data[i][0] = player.getName();
            data[i][1] = gameStats.getPlayingTime();
            data[i][2] = gameStats.getPoints();
            data[i][3] = gameStats.getAssists();
            data[i][4] = gameStats.getSteals();
            data[i][5] = gameStats.getBlocks();
            data[i][6] = gameStats.getRebounds();
            data[i][7] = gameStats.getTurnovers();
            data[i][8] = gameStats.getFGM();
            data[i][9] = gameStats.getFGA();
            data[i][10] = gameStats.getThreePM();
            data[i][11] = gameStats.getThreePA();
            data[i][12] = gameStats.getFTM();
            data[i][13] = gameStats.getFTA();
            data[i][14] = gameStats.getFoul();
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            // 這個是讓所有格子裡的數據可以更改
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
            }

            @Override
            public void setValueAt(Object value, int row, int column) {
                super.setValueAt(value, row, column);
                // 下面需要你寫一行代碼來更新數據庫

            }
        };

        JTable playerTable = new JTable(tableModel);
        playerTable.setFont(new Font("Serif", Font.PLAIN, 20));
        playerTable.setRowHeight(40);
        TableColumnModel columnModel = playerTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(150);
        JTableHeader tableHeader = playerTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 50));
        tableHeader.setFont(new Font("Serif", Font.BOLD, 28));

        JScrollPane scrollPane = new JScrollPane(playerTable);

        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void refreshPlayerList() {
        new LoadPlayersWorker().execute();
    }

    public Team getTeam() {
        return team;
    }

}
