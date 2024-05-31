import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ManageGame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;
    private JPanel homeTeamPanel;
    private JPanel awayTeamPanel;
    private JPanel playerListPanel;
    private List<GameStats> gameStat;

    public ManageGame(String homeTeam, String awayTeam) {
        super("Manage Game");
        gameStat = new ArrayList<>();
        initializeUI(homeTeam, awayTeam);
        setSize(1000, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
        pack();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeUI(String homeTeam, String awayTeam) {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        homeTeamPanel = createTeamPanel(homeTeam, "Home Team Stats");
        cards.add(homeTeamPanel, "Home");

        awayTeamPanel = createTeamPanel(awayTeam, "Away Team Stats");
        cards.add(awayTeamPanel, "Away");

        getContentPane().add(cards, BorderLayout.CENTER);

        JButton switchButton = new JButton("Switch Team");
        switchButton.addActionListener(e -> cardLayout.next(cards));
        getContentPane().add(switchButton, BorderLayout.SOUTH);
    }

    private JPanel createTeamPanel(String teamName, String label) {
        JPanel teamPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel(label + ": " + teamName);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 50));
        teamPanel.add(titleLabel, BorderLayout.NORTH);

        teamPanel.add(createPlayerStatsTable(), BorderLayout.CENTER);

        return teamPanel;
    }

    private JPanel createPlayerStatsTable() {

        String[] columnNames = {
                "Name", "PlayingTime", "Points", "Assists", "Steals", "Blocks", "Rebounds",
                "Turnovers", "FGM", "FGA", "ThreePM", "ThreePA", "FTM", "FTA", "Foul"
        };
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
            @Override
            public boolean isCellEditable(int row, int column) {
                return true;
                // 讓所有格子裡的數據可以更改

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

    public void showGameView(String viewName) {
        cardLayout.show(cards, viewName);
    }
}
