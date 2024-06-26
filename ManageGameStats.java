package basketball.app;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ManageGameStats extends JPanel {
    
    private CardLayout cardLayout;
    private JPanel homeStatsPanel;
    private JPanel awayStatsPanel;
    private JPanel loadingPanel;
    private ManageGame manageGame;
    private Team homeTeam;
    private Team awayTeam;
    private Game game;
    private ArrayList<GameStats> homeTeamStats;
    private ArrayList<GameStats> awayTeamStats;
    private TeamDAO teamDAO;
    private PlayerDAO playerDAO;
    private GameDAO gameDAO;
    private GameStatsDAO gameStatsDAO;

    public ManageGameStats(Game game, ManageGame manageGame) {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        this.manageGame = manageGame;
        this.game = game;
        teamDAO = new TeamDAO();
        playerDAO = new PlayerDAO();
        gameDAO = new GameDAO();
        gameStatsDAO = new GameStatsDAO();
        homeTeam = teamDAO.getTeamByID(game.getTeam1Id());
        awayTeam = teamDAO.getTeamByID(game.getTeam2Id());
        loadingPanel = new JPanel(new BorderLayout());

        initializeUI();

        new LoadGameStatsDataWorker(game.getGameId(), homeTeam.getTeamId(), 1).execute();
        new LoadGameStatsDataWorker(game.getGameId(), awayTeam.getTeamId(), 2).execute();
    }

    private void initializeUI() { 
        createTeamPanel(homeTeam);
        createTeamPanel(awayTeam);
        
        JLabel loadingText = new JLabel("Loading game stats...", JLabel.CENTER);
        loadingText.setFont(new Font("Serif", Font.BOLD, 30));
        loadingPanel.add(loadingText, BorderLayout.CENTER);
        add(loadingPanel, "loading");
        cardLayout.show(this, "loading");
    }

    private void createTeamPanel(Team team) {
        JPanel teamPanel = new JPanel(new BorderLayout());
        
        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(game.toString(), JLabel.CENTER);
        JLabel teamNameLabel = new JLabel();
        if (team == homeTeam) {
            teamNameLabel.setText("(HomeTeam) " + team.toString());
            teamNameLabel.setHorizontalAlignment(JLabel.CENTER);
        } else {
            teamNameLabel.setText("(AwayTeam) " + team.toString());
            teamNameLabel.setHorizontalAlignment(JLabel.CENTER);
        }
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        teamNameLabel.setFont(new Font("Serif", Font.BOLD, 32));
        //titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton anotherTeam = new JButton("Another Team");
        anotherTeam.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(team == homeTeam){
                    cardLayout.show(ManageGameStats.this, awayTeam.getName());
                }else{
                    cardLayout.show(ManageGameStats.this, homeTeam.getName());
                }
            }
        });
        JButton backToGameList = new JButton("Game List");
        backToGameList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageGame.showGameList();
            }
        });
        JButton compareButton = new JButton("比較兩隊數據");
            compareButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showComparisonDialog();
                }
            });
        
        buttonPanel.add(compareButton, BorderLayout.WEST);
        buttonPanel.add(anotherTeam, BorderLayout.SOUTH);
        buttonPanel.add(backToGameList, BorderLayout.EAST);
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(teamNameLabel, BorderLayout.CENTER);
        titlePanel.add(buttonPanel, BorderLayout.SOUTH);

        if(team == homeTeam){
            homeStatsPanel = new JPanel(new BorderLayout());
            teamPanel.add(homeStatsPanel, BorderLayout.CENTER);
        }else if(team == awayTeam){
            awayStatsPanel = new JPanel(new BorderLayout());
            teamPanel.add(awayStatsPanel, BorderLayout.CENTER);
        }
        teamPanel.add(titlePanel, BorderLayout.NORTH);
        
        add(teamPanel, team.getName());
    }

    private JPanel createPlayerStatsTable(ArrayList<GameStats> gameStats) {
        String[] columnNames = {
                "球員名稱", "上場時間(分鐘)", "得分", "助攻", "抄截", "阻攻", "籃板",
                "失誤", "命中", "三分命中", "罰球", "犯規" };
        Object[][] data = new Object[gameStats.size()][12];

        for (int i = 0; i < gameStats.size(); i++) {
            GameStats gameStat = gameStats.get(i);
            Player player = playerDAO.getPlayerByID(gameStat.getPlayerId());
            data[i][0] = player.getName();
            data[i][1] = String.valueOf(gameStat.getPlayingTime());
            data[i][2] = String.valueOf(gameStat.getPoints());
            data[i][3] = String.valueOf(gameStat.getAssists());
            data[i][4] = String.valueOf(gameStat.getSteals());
            data[i][5] = String.valueOf(gameStat.getBlocks());
            data[i][6] = String.valueOf(gameStat.getRebounds());
            data[i][7] = String.valueOf(gameStat.getTurnovers());
            data[i][8] = gameStat.getFGM() + "/" + gameStat.getFGA();
            data[i][9] = gameStat.getThreePM() + "/" + gameStat.getThreePA();
            data[i][10] = gameStat.getFTM() + "/" + gameStat.getFTA();
            data[i][11] = String.valueOf(gameStat.getFoul());
        }

        EditableTableModel tableModel = new EditableTableModel(data, columnNames);

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

        JButton editButton = new JButton("編輯模式");
        editButton.addActionListener(new ActionListener() {
            private boolean isEditing = false;

            @Override
            public void actionPerformed(ActionEvent e) {
                isEditing = !isEditing;
                tableModel.setEditable(isEditing);
                if (isEditing) {
                    editButton.setText("儲存");
                } else {
                    editButton.setText("編輯模式");
                    saveDataToDatabase(tableModel, gameStats);
                    gameDAO.updateGameScores(game.getGameId());
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(editButton);
        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        return tablePanel;
    }

    private void saveDataToDatabase(EditableTableModel tableModel, ArrayList<GameStats> gameStats) {
        // 只更新被修改的行
        Set<Integer> updatedRows = tableModel.getUpdatedRows();
        for (int i : updatedRows) {
            // 获取每一行的数据
            int playingTime = Integer.parseInt((String) tableModel.getValueAt(i, 1)); // 将 playingTime 转换为 int
            int points = Integer.parseInt((String) tableModel.getValueAt(i, 2));
            int assists = Integer.parseInt((String) tableModel.getValueAt(i, 3));
            int steals = Integer.parseInt((String) tableModel.getValueAt(i, 4));
            int blocks = Integer.parseInt((String) tableModel.getValueAt(i, 5));
            int rebounds = Integer.parseInt((String) tableModel.getValueAt(i, 6));
            int turnovers = Integer.parseInt((String) tableModel.getValueAt(i, 7));
            String fgmFga = (String) tableModel.getValueAt(i, 8);
            String[] fgmFgaParts = fgmFga.split("/");
            int fgm = Integer.parseInt(fgmFgaParts[0]);
            int fga = Integer.parseInt(fgmFgaParts[1]);
            String threePmPa = (String) tableModel.getValueAt(i, 9);
            String[] threePmPaParts = threePmPa.split("/");
            int threePm = Integer.parseInt(threePmPaParts[0]);
            int threePa = Integer.parseInt(threePmPaParts[1]);
            String ftmFta = (String) tableModel.getValueAt(i, 10);
            String[] ftmFtaParts = ftmFta.split("/");
            int ftm = Integer.parseInt(ftmFtaParts[0]);
            int fta = Integer.parseInt(ftmFtaParts[1]);
            int foul = Integer.parseInt((String) tableModel.getValueAt(i, 11));
    
            // 更新数据库中的记录
            gameStatsDAO.updateGameStats(new GameStats(
                gameStats.get(i).getStatId(), gameStats.get(i).getPlayerId(), gameStats.get(i).getGameId(), playingTime, points, assists,
                rebounds, steals, blocks, turnovers, fgm, fga, threePm, threePa, ftm, fta, foul));
        }
    }

    

    class EditableTableModel extends DefaultTableModel {
        private boolean isEditable = false;
        private Set<Integer> updatedRows = new HashSet<>();

        public EditableTableModel(Object[][] data, Object[] columnNames) {
            super(data, columnNames);
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return isEditable;
        }

        public void setEditable(boolean editable) {
            this.isEditable = editable;
        }

        @Override
        public void setValueAt(Object value, int row, int column) {
            super.setValueAt(value, row, column);
            if (isEditable) {
                updatedRows.add(row);
            }
        }

        public Set<Integer> getUpdatedRows() {
            return updatedRows;
        }
    }

    private class LoadGameStatsDataWorker extends SwingWorker<ArrayList<GameStats>, Void> {
        private int game_id;
        private int team_id;
        private int mode;
        
        public LoadGameStatsDataWorker(int game_id, int team_id, int mode) {
            this.game_id = game_id;
            this.team_id = team_id;
            this.mode = mode;
        }

        @Override
        protected ArrayList<GameStats> doInBackground() throws Exception {
            return gameStatsDAO.getGameStatsByGameAndTeam(game_id, team_id);
        }

        @Override
        protected void done() {
            try {
                if(mode == 1){
                    homeTeamStats = get();
                    displayStats(homeTeam);
                }else if(mode == 2){
                    awayTeamStats = get();
                    displayStats(awayTeam);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void displayStats(Team team){
        if(team == homeTeam){
            homeStatsPanel.removeAll();
            homeStatsPanel.add(createPlayerStatsTable(homeTeamStats), BorderLayout.CENTER);
            cardLayout.show(this, team.getName());
        }else{
            awayStatsPanel.removeAll();
            awayStatsPanel.add(createPlayerStatsTable(awayTeamStats), BorderLayout.CENTER);
        }
        revalidate();
        repaint();
    }

    public void showComparisonDialog() {
        JDialog comparisonDialog = new JDialog((Frame) null, "Comparison", true);
        comparisonDialog.setLayout(new BorderLayout());
    
        // Create panel for comparison data
        JPanel comparisonPanel = new JPanel(new GridLayout(10, 3, 10, 10));
        comparisonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    
        // Add headers
        comparisonPanel.add(createCenteredLabel("Stat"));
        comparisonPanel.add(createCenteredLabel(homeTeam.getName()));
        comparisonPanel.add(createCenteredLabel(awayTeam.getName()));
    
        // Add comparison data
        comparisonPanel.add(createCenteredLabel("得分"));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumPoints(homeTeamStats))));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumPoints(awayTeamStats))));
    
        comparisonPanel.add(createCenteredLabel("助攻"));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumAssists(homeTeamStats))));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumAssists(awayTeamStats))));
    
        comparisonPanel.add(createCenteredLabel("抄截"));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumSteals(homeTeamStats))));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumSteals(awayTeamStats))));
    
        comparisonPanel.add(createCenteredLabel("阻攻"));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumBlocks(homeTeamStats))));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumBlocks(awayTeamStats))));
    
        comparisonPanel.add(createCenteredLabel("籃板"));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumRebounds(homeTeamStats))));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumRebounds(awayTeamStats))));
    
        comparisonPanel.add(createCenteredLabel("失誤"));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumTurnovers(homeTeamStats))));
        comparisonPanel.add(createCenteredLabel(String.valueOf(sumTurnovers(awayTeamStats))));
    
        comparisonPanel.add(createCenteredLabel("兩分命中數"));
        comparisonPanel.add(createCenteredLabel(sumTwoPM(homeTeamStats)));
        comparisonPanel.add(createCenteredLabel(sumTwoPM(awayTeamStats)));
    
        comparisonPanel.add(createCenteredLabel("三分命中數"));
        comparisonPanel.add(createCenteredLabel(sumThreePMAndThreePA(homeTeamStats)));
        comparisonPanel.add(createCenteredLabel(sumThreePMAndThreePA(awayTeamStats)));
    
        comparisonPanel.add(createCenteredLabel("罰球命中數"));
        comparisonPanel.add(createCenteredLabel(sumFTMAndFTA(homeTeamStats)));
        comparisonPanel.add(createCenteredLabel(sumFTMAndFTA(awayTeamStats)));
    
        comparisonDialog.add(comparisonPanel, BorderLayout.CENTER);
    
        // Add a close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> comparisonDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        comparisonDialog.add(buttonPanel, BorderLayout.SOUTH);
    
        comparisonDialog.pack();
        comparisonDialog.setLocationRelativeTo(null);
        comparisonDialog.setVisible(true);
    }
    
    private JLabel createCenteredLabel(String text) {
        JLabel label = new JLabel(text, JLabel.CENTER);
        label.setHorizontalAlignment(JLabel.CENTER);
        label.setFont(new Font("Serif", Font.BOLD, 20));
        return label;
    }
    
    private int sumPoints(ArrayList<GameStats> stats) {
        return stats.stream().mapToInt(GameStats::getPoints).sum();
    }
    
    private int sumAssists(ArrayList<GameStats> stats) {
        return stats.stream().mapToInt(GameStats::getAssists).sum();
    }
    
    private int sumSteals(ArrayList<GameStats> stats) {
        return stats.stream().mapToInt(GameStats::getSteals).sum();
    }
    
    private int sumBlocks(ArrayList<GameStats> stats) {
        return stats.stream().mapToInt(GameStats::getBlocks).sum();
    }
    
    private int sumRebounds(ArrayList<GameStats> stats) {
        return stats.stream().mapToInt(GameStats::getRebounds).sum();
    }
    
    private int sumTurnovers(ArrayList<GameStats> stats) {
        return stats.stream().mapToInt(GameStats::getTurnovers).sum();
    }
      
    private String sumTwoPM(ArrayList<GameStats> stats) {
        int fgm = stats.stream().mapToInt(GameStats::getFGM).sum();
        int fga = stats.stream().mapToInt(GameStats::getFGA).sum();
        int threePm = stats.stream().mapToInt(GameStats::getThreePM).sum();
        int threePa = stats.stream().mapToInt(GameStats::getThreePA).sum();
        return (fgm - threePm) + "/" + (fga - threePa);
    }
    
    private String sumThreePMAndThreePA(ArrayList<GameStats> stats) {
        int threePm = stats.stream().mapToInt(GameStats::getThreePM).sum();
        int threePa = stats.stream().mapToInt(GameStats::getThreePA).sum();
        return threePm + "/" + threePa;
    }
    
    private String sumFTMAndFTA(ArrayList<GameStats> stats) {
        int ftm = stats.stream().mapToInt(GameStats::getFTM).sum();
        int fta = stats.stream().mapToInt(GameStats::getFTA).sum();
        return ftm + "/" + fta;
    }
}
    
