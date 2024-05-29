import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class SearchGames extends JPanel {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel gameListPanel;
    private JPanel searchListPanel;
    private JPanel loadingPanel;
    private ShowCreateDialog showCreateDialog;
    private ShowDeleteDialog showDeleteDialog;
    private ArrayList<Game> games;
    private GameDAO gameDAO;
    private TeamDAO teamDAO;
    private ArrayList<Team> teams;
    private Team team1;
    private Team team2;
    private JComboBox<Team> team1ComboBox;
    private JComboBox<Team> team2ComboBox;

    public SearchGames(MainMenu mainMenu, Team team1, Team team2) {
        this.team1 = team1;
        this.team2 = team2;
        teamDAO = new TeamDAO();
        gameDAO = new GameDAO();
        
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        mainPanel = new JPanel(new BorderLayout());
        loadingPanel = new JPanel(new BorderLayout());
        showCreateDialog = new ShowCreateDialog();
        showDeleteDialog = new ShowDeleteDialog();

        // 初始化 UI
        initializeUI(mainMenu);

        // 加載數據
        new LoadTeamWorker().execute();
        new LoadGamesWorker().execute();
    }

    private void initializeUI(MainMenu mainMenu) {
        // Title Panel
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
        searchListPanel = new JPanel(new BorderLayout());

        JLabel titleLabel = new JLabel("比賽列表一覽", JLabel.CENTER);
        titleLabel.setFont(new Font("標楷體", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

        MenuPanel.add(backToMenu, BorderLayout.EAST);
        MenuPanel.add(searchListPanel, BorderLayout.CENTER);
        titlePanel.add(MenuPanel, BorderLayout.SOUTH);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // GameList Panel
        gameListPanel = new JPanel();
        gameListPanel.setLayout(new BorderLayout());

        // Function Panel
        JPanel FunctionPanel = new JPanel();
        FunctionPanel.setLayout(new BorderLayout());

        // Set create game button
        JPanel createGame = new JPanel();
        createGame.setLayout(new BorderLayout());
        createGame.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 0));
        JButton createGameButton = new JButton("Record Game");
        createGameButton.setFont(new Font("Serif", Font.PLAIN, 30));
        createGame.add(createGameButton, BorderLayout.CENTER);
        createGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateDialog.Game(teams);
                new LoadGamesWorker().execute();
            }
        });

        JPanel deleteGame = new JPanel();
        deleteGame.setLayout(new BorderLayout());
        deleteGame.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton deleteGameButton = new JButton("Delete Game");
        deleteGameButton.setFont(new Font("Serif", Font.PLAIN, 16));
        deleteGame.add(deleteGameButton, BorderLayout.CENTER);
        deleteGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDeleteDialog.Game(games);
                new LoadGamesWorker().execute();
            }
        });
        FunctionPanel.add(createGame, BorderLayout.CENTER);
        FunctionPanel.add(deleteGame, BorderLayout.EAST);

        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(gameListPanel, BorderLayout.CENTER);
        mainPanel.add(FunctionPanel, BorderLayout.SOUTH);

        // Loading Panel
        JLabel loadingText = new JLabel("Loading games...", JLabel.CENTER);
        loadingText.setFont(new Font("Serif", Font.BOLD, 30));
        loadingPanel.add(loadingText, BorderLayout.CENTER);

        add(mainPanel, "gameList");
        add(loadingPanel, "loading");
        cardLayout.show(this, "loading");
    }

    // search panel
    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel(new FlowLayout());

        JLabel gameSearchLabel = new JLabel("Game Search : ");
        JLabel vsLabel = new JLabel(" v.s. ");

        team1ComboBox = new JComboBox<>();
        team2ComboBox = new JComboBox<>();

        DefaultComboBoxModel<Team> team1Model = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<Team> team2Model = new DefaultComboBoxModel<>();
        team1Model.addElement(new Team());
        team2Model.addElement(new Team());
        for (Team team : teams) {
            team1Model.addElement(team);
            team2Model.addElement(team);
        }
        team1ComboBox.setModel(team1Model);
        team2ComboBox.setModel(team2Model);
        team1ComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Team) {
                    Team team = (Team) value;
                    setText(team.toString());
                }
                return this;
            }
        });
        team2ComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Team) {
                    Team team = (Team) value;
                    setText(team.toString());
                }
                return this;
            }
        });

        JButton searching = new JButton("Search");
        searching.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                team1 = (Team) team1ComboBox.getSelectedItem();
                team2 = (Team) team2ComboBox.getSelectedItem();
                new LoadGamesWorker().execute();
            }
        });

        searchPanel.add(gameSearchLabel);
        searchPanel.add(team1ComboBox);
        searchPanel.add(vsLabel);
        searchPanel.add(team2ComboBox);
        searchPanel.add(searching);
        return searchPanel;
    }

    // table panel
    private JPanel tablePanel() {
        String[] columnNames = {"Game Date", "Home Team", "Away Team", "Home Score", "Away Score"};
        Object[][] data = new Object[games.size()][5];
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            Team team1 = teamDAO.getTeamByID(game.getTeam1Id());
            Team team2 = teamDAO.getTeamByID(game.getTeam2Id());
            data[i][0] = game.getGameDate();
            if (game.getTeam1Score() > game.getTeam2Score()) {
                data[i][1] = team1.toString() + " (win)";
                data[i][2] = team2.toString() + " (lose)";
            } else {
                data[i][1] = team1.toString() + " (lose)";
                data[i][2] = team2.toString() + " (win)";
            }
            data[i][3] = game.getTeam1Score();
            data[i][4] = game.getTeam2Score();
        }

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable gamesTable = new JTable(tableModel);
        gamesTable.setFont(new Font("Serif", Font.PLAIN, 20));
        gamesTable.setRowHeight(40);
        TableColumnModel columnModel = gamesTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(50);
        JTableHeader tableHeader = gamesTable.getTableHeader();
        tableHeader.setPreferredSize(new Dimension(tableHeader.getWidth(), 50));
        tableHeader.setFont(new Font("Serif", Font.BOLD, 20));

        gamesTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    // Implement double-click behavior here
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(gamesTable);
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void updateGameListPanel() {
        gameListPanel.removeAll();
        JPanel gameInfoPanel = tablePanel();
        gameListPanel.add(new JScrollPane(gameInfoPanel), BorderLayout.CENTER);
        gameListPanel.revalidate();
        gameListPanel.repaint();
    }
    
    private class LoadGamesWorker extends SwingWorker<ArrayList<Game>, Void> {
        @Override
        protected ArrayList<Game> doInBackground() throws Exception {
            if (team1.getTeamId() == 0 && team2.getTeamId() == 0) {
                return gameDAO.getAllGames();
            } else if (team1.getTeamId() != 0 && team2.getTeamId() != 0) {
                return gameDAO.getGamesByTwoTeam(team1.getTeamId(), team2.getTeamId());
            } else if (team1.getTeamId() != 0) {
                return gameDAO.getGamesByOneTeam(team1.getTeamId());
            } else {
                return gameDAO.getGamesByOneTeam(team2.getTeamId());
            }
        }

        @Override
        protected void done() {
            try {
                games = get();
                updateGameListPanel();
                cardLayout.show(SearchGames.this, "gameList");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private class LoadTeamWorker extends SwingWorker<ArrayList<Team>, Void> {
        @Override
        protected ArrayList<Team> doInBackground() throws Exception {
            return teamDAO.getAllTeams();
        }

        @Override
        protected void done() {
            try {
                teams = get();
                searchListPanel.add(createSearchPanel(), BorderLayout.CENTER);
                searchListPanel.revalidate();
                searchListPanel.repaint();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
