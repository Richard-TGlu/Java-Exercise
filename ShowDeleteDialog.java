import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class ShowDeleteDialog {
    
    public ShowDeleteDialog(){
    }

    //deleteTeam Function
    public void Team(ArrayList<Team> teams) {
        JDialog dialog = new JDialog((Frame) null, "Delete Team", true);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Delete the team you want:");
        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        titleLabel.setFont(LabelFont);
        inputPanel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<Team> model = new DefaultListModel<>();
        for (Team team : teams) {
            model.addElement(team);
        }
        JList<Team> teamJList = new JList<>(model);
        teamJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        teamJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Team) {
                    Team team = (Team) value;
                    setText(team.toString());
                }
                return this;
            }
        });
        inputPanel.add(new JScrollPane(teamJList), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            Team selectedTeam = teamJList.getSelectedValue();
            if (selectedTeam != null) {
                int response = JOptionPane.showConfirmDialog(dialog,
                        "Are you sure you want to delete the team: " + selectedTeam.toString() + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            TeamDAO teamDAO = new TeamDAO();
                            teamDAO.deleteTeam(selectedTeam.getTeamId());
                            return null;
                        }
                        @Override
                        protected void done() {
                            model.removeElement(selectedTeam);
                            JOptionPane.showMessageDialog(dialog, "Team deleted successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                        }
                    }.execute();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a team to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    public void Player(ArrayList<Player> players, int team_id) {
        JDialog dialog = new JDialog((Frame) null, "Delete Player", true);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Delete the player you want:");
        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        titleLabel.setFont(LabelFont);
        inputPanel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<Player> model = new DefaultListModel<>();
        for (Player player : players) {
            model.addElement(player);
        }
        JList<Player> playerJList = new JList<>(model);
        playerJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        playerJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Player) {
                    Player player = (Player) value;
                    setText(player.getName() + "  " + player.getNumber());
                }
                return this;
            }
        });
        inputPanel.add(new JScrollPane(playerJList), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            Player selectedPlayer = playerJList.getSelectedValue();
            if (selectedPlayer != null) {
                int response = JOptionPane.showConfirmDialog(dialog,
                        "Are you sure you want to delete the player: " + selectedPlayer.getName() + "  " + selectedPlayer.getNumber() + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            PlayerDAO playerDAO = new PlayerDAO();
                            playerDAO.deletePlayer(selectedPlayer.getPlayerID());
                            return null;
                        }
                        @Override
                        protected void done() {
                            model.removeElement(selectedPlayer);
                            JOptionPane.showMessageDialog(dialog, "Player deleted successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                        }
                    }.execute();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a player to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
    
    // delete game Function
    public void Game(ArrayList<Game> games) {
        JDialog dialog = new JDialog((Frame) null, "Delete Game", true);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Delete the game you want:");
        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        titleLabel.setFont(LabelFont);
        inputPanel.add(titleLabel, BorderLayout.NORTH);

        DefaultListModel<Game> model = new DefaultListModel<>();
        for (Game game : games) {
            model.addElement(game);
        }
        JList<Game> gameList = new JList<>(model);
        gameList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        gameList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Game) {
                    Game game = (Game) value;
                    setText(game.toString());
                }
                return this;
            }
        });
        inputPanel.add(new JScrollPane(gameList), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            Game selectedGame = gameList.getSelectedValue();
            if (selectedGame != null) {
                int response = JOptionPane.showConfirmDialog(dialog,
                        "Are you sure you want to delete the game: " + selectedGame.getGameDate() + "?",
                        "Confirm Delete",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.PLAIN_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                    new SwingWorker<Void, Void>() {
                        @Override
                        protected Void doInBackground() throws Exception {
                            GameDAO gameDAO = new GameDAO();
                            gameDAO.deleteGame(selectedGame.getGameId());
                            return null;
                        }

                        @Override
                        protected void done() {
                            model.removeElement(selectedGame);
                            JOptionPane.showMessageDialog(dialog, "Game deleted successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                            dialog.dispose();
                        }
                    }.execute();
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "Please select a game to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });
        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }
}
