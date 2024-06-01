import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManagePlayer extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel playerListPanel;
    private JPanel loadingPanel;
    private ShowCreateDialog showCreateDialog;
    private ShowDeleteDialog showDeleteDialog;
    private Team team;
    private ArrayList<Player> players;
    private PlayerDAO playerDAO;

    public ManagePlayer(Team team, ManageTeam manageTeam) {
        this.team = team;
        cardLayout = new CardLayout();
        setLayout(cardLayout);
        mainPanel = new JPanel(new BorderLayout());
        loadingPanel = new JPanel(new BorderLayout());
        showCreateDialog = new ShowCreateDialog();
        showDeleteDialog = new ShowDeleteDialog();

        initializeUI(manageTeam);

        new LoadPlayersWorker().execute();
    }

    private void initializeUI(ManageTeam manageTeam){
        // Title Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JPanel TeamListPanel = new JPanel();
        TeamListPanel.setLayout(new BorderLayout());

        JButton backToTeamList = new JButton("Team List");
        backToTeamList.setFont(new Font("Serif", Font.BOLD, 20));
        backToTeamList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageTeam.showTeamList();
            }
        });
        JLabel teamNameLabel = new JLabel(team.getName(), JLabel.CENTER);
        teamNameLabel.setFont(new Font("Serif", Font.BOLD, 40));
        teamNameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JPanel CityAndYear = new JPanel();
        CityAndYear.setLayout(new GridLayout(2, 1, 0, 0));
        JLabel CityLabel = new JLabel("City: " + team.getCity());
        CityLabel.setFont(new Font("Serif", Font.BOLD, 18));
        CityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        JLabel YearLabel = new JLabel("Foundation Year: " + team.getFoundationYear());
        YearLabel.setFont(new Font("Serif", Font.BOLD, 18));
        YearLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        CityAndYear.add(CityLabel);
        CityAndYear.add(YearLabel);
        TeamListPanel.add(backToTeamList, BorderLayout.EAST);
        TeamListPanel.add(CityAndYear, BorderLayout.WEST);
        titlePanel.add(TeamListPanel, BorderLayout.SOUTH);
        titlePanel.add(teamNameLabel, BorderLayout.CENTER);

        // Player List Panel
        playerListPanel = new JPanel(new BorderLayout());

        // Function Panel
        JPanel FunctionPanel = new JPanel();
        FunctionPanel.setLayout(new BorderLayout());

        JPanel createPlayer = new JPanel();
        createPlayer.setLayout(new BorderLayout());
        createPlayer.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 0));
        JButton createPlayerButton = new JButton("Create Player");
        createPlayerButton.setFont(new Font("Serif", Font.PLAIN, 30));
        createPlayer.add(createPlayerButton, BorderLayout.CENTER);
        createPlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateDialog.Player(team);
                refreshPlayerList();
            }
        });
        JPanel deletePlayer = new JPanel();
        deletePlayer.setLayout(new BorderLayout());
        deletePlayer.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton deletePlayerButton = new JButton("Delete Player");
        deletePlayerButton.setFont(new Font("Serif", Font.PLAIN, 16));
        deletePlayer.add(deletePlayerButton, BorderLayout.CENTER);
        deletePlayerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDeleteDialog.Player(players, team.getTeamId());
                refreshPlayerList();
            }
        });
        FunctionPanel.add(createPlayer, BorderLayout.CENTER);
        FunctionPanel.add(deletePlayer, BorderLayout.EAST);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(playerListPanel, BorderLayout.CENTER);
        mainPanel.add(FunctionPanel, BorderLayout.SOUTH);

        // 加載中的消息
        JLabel loadingText = new JLabel("Loading Player Data...", JLabel.CENTER);
        loadingText.setFont(new Font("Serif", Font.BOLD, 30));
        loadingPanel.add(loadingText, BorderLayout.CENTER);

        add(mainPanel, "playerList");
        add(loadingPanel, "loading");

        cardLayout.show(this, "loading");
    }

    private class LoadPlayersWorker extends SwingWorker<ArrayList<Player>, Void> {
        @Override
        protected ArrayList<Player> doInBackground() throws Exception {
            playerDAO = new PlayerDAO();
            return playerDAO.getTeamPlayers(team.getTeamId());
        }

        @Override
        protected void done() {
            try {
                players = get();
                displayPlayers();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void displayPlayers() {
        playerListPanel.removeAll();
        playerListPanel.add(tablePanel(), BorderLayout.CENTER);
        cardLayout.show(this, "playerList");
        revalidate();
        repaint();
    }

    // player list table
    private JPanel tablePanel() {
        String[] columnNames = {"Name", "Number", "Position", "Age", "Height", "Weight"};
        Object[][] data = new Object[players.size()][6];

        for (int i = 0; i < players.size(); i++) {
            Player player = players.get(i);
            data[i][0] = player.getName();
            data[i][1] = player.getNumber();
            data[i][2] = player.getPosition();
            data[i][3] = player.getAge();
            data[i][4] = player.getHeight() + " cm";
            data[i][5] = player.getWeight() + " kg";
        }
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
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
