import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ShowCreateDialog extends JDialog{
    
    public ShowCreateDialog(){
    }

    public void Team() {
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

        Font labelFont = new Font("Serif", Font.PLAIN, 30);
        Font textFont = new Font("Serif", Font.PLAIN, 22);
        nameLabel.setFont(labelFont);
        cityLabel.setFont(labelFont);
        foundationYearLabel.setFont(labelFont);
        nameField.setFont(textFont);
        cityField.setFont(textFont);
        foundationYearField.setFont(textFont);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String city = cityField.getText();
                int foundationYear = Integer.parseInt(foundationYearField.getText());

                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        TeamDAO teamDAO = new TeamDAO();
                        teamDAO.addTeam(name, city, foundationYear);
                        return null;
                    }

                    @Override
                    protected void done() {
                        JOptionPane.showMessageDialog(dialog, "New team added successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    }
                }.execute();
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

    public void Player(Team currentTeam) {
        JDialog dialog = new JDialog((Frame) null, "Create Player", true);
        dialog.setSize(400, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(12, 1, 50, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("Player Name:");
        JTextField nameField = new JTextField();
        JLabel numberLabel = new JLabel("Player Number:");
        JTextField numberField = new JTextField();
        JLabel positionLabel = new JLabel("Player Position:");
        JPanel positionPanel = new JPanel();
        positionPanel.setLayout(new GridLayout(1, 5, 10, 10));
        JLabel ageLabel = new JLabel("Player Age:");
        JTextField ageField = new JTextField();
        JLabel heightLabel = new JLabel("Player Height(cm):");
        JTextField heightField = new JTextField();
        JLabel weightLabel = new JLabel("Player Weight(kg):");
        JTextField weightField = new JTextField();

        JRadioButton pgButton = new JRadioButton("PG");
        JRadioButton sgButton = new JRadioButton("SG");
        JRadioButton sfButton = new JRadioButton("SF");
        JRadioButton pfButton = new JRadioButton("PF");
        JRadioButton cButton = new JRadioButton("C");
        ButtonGroup positionGroup = new ButtonGroup();
        positionGroup.add(pgButton);
        positionGroup.add(sgButton);
        positionGroup.add(sfButton);
        positionGroup.add(pfButton);
        positionGroup.add(cButton);

        positionPanel.add(pgButton);
        positionPanel.add(sgButton);
        positionPanel.add(sfButton);
        positionPanel.add(pfButton);
        positionPanel.add(cButton);

        Font labelFont = new Font("Serif", Font.PLAIN, 30);
        Font textFont = new Font("Serif", Font.PLAIN, 22);
        Font buttonFont = new Font("Serif", Font.PLAIN, 18);
        nameLabel.setFont(labelFont);
        numberLabel.setFont(labelFont);
        positionLabel.setFont(labelFont);
        ageLabel.setFont(labelFont);
        heightLabel.setFont(labelFont);
        weightLabel.setFont(labelFont);

        nameField.setFont(textFont);
        numberField.setFont(textFont);
        ageField.setFont(textFont);
        heightField.setFont(textFont);
        weightField.setFont(textFont);

        pgButton.setFont(buttonFont);
        sgButton.setFont(buttonFont);
        sfButton.setFont(buttonFont);
        pfButton.setFont(buttonFont);
        cButton.setFont(buttonFont);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                int number = Integer.parseInt(numberField.getText());
                int age = Integer.parseInt(ageField.getText());
                int height = Integer.parseInt(heightField.getText());
                int weight = Integer.parseInt(weightField.getText());
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        PlayerDAO playerDAO = new PlayerDAO();
                        if (pgButton.isSelected()) {
                            playerDAO.addPlayer(currentTeam.getTeamId(), name, number, Player.Position.PG, age, height, weight);
                        } else if (sgButton.isSelected()) {
                            playerDAO.addPlayer(currentTeam.getTeamId(), name, number, Player.Position.SG, age, height, weight);
                        } else if (sfButton.isSelected()) {
                            playerDAO.addPlayer(currentTeam.getTeamId(), name, number, Player.Position.SF, age, height, weight);
                        } else if (pfButton.isSelected()) {
                            playerDAO.addPlayer(currentTeam.getTeamId(), name, number, Player.Position.PF, age, height, weight);
                        } else if (cButton.isSelected()) {
                            playerDAO.addPlayer(currentTeam.getTeamId(), name, number, Player.Position.C, age, height, weight);
                        }
                        return null;
                    }
                    @Override
                    protected void done() {
                        JOptionPane.showMessageDialog(dialog, "New player added successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    }
                }.execute();
            }
        });

        inputPanel.add(nameLabel);
        inputPanel.add(nameField);
        inputPanel.add(numberLabel);
        inputPanel.add(numberField);
        inputPanel.add(positionLabel);
        inputPanel.add(positionPanel);
        inputPanel.add(ageLabel);
        inputPanel.add(ageField);
        inputPanel.add(heightLabel);
        inputPanel.add(heightField);
        inputPanel.add(weightLabel);
        inputPanel.add(weightField);

        savePanel.add(saveButton);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(savePanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    public void Game(ArrayList<Team> teams) {
        JDialog dialog = new JDialog((Frame) null, "Record Game", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(10, 1, 0, 0));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());

        JLabel homeTeamLabel = new JLabel("Home Team:");
        JLabel awayTeamLabel = new JLabel("Away Team:");
        JLabel homeScoreLabel = new JLabel("Home Score:");
        JLabel awayScoreLabel = new JLabel("Away Score:");
        JLabel gameDateLabel = new JLabel("Game Date (YYYY-MM-DD):");

        JComboBox<Team> homeTeamComboBox = new JComboBox<>();
        JComboBox<Team> awayTeamComboBox = new JComboBox<>();
        DefaultComboBoxModel<Team> homeTeamModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<Team> awayTeamModel = new DefaultComboBoxModel<>();
        for (Team team : teams) {
            homeTeamModel.addElement(team);
            awayTeamModel.addElement(team);
        }
        homeTeamComboBox.setModel(homeTeamModel);
        awayTeamComboBox.setModel(awayTeamModel);
        homeTeamComboBox.setRenderer(new DefaultListCellRenderer() {
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
        awayTeamComboBox.setRenderer(new DefaultListCellRenderer() {
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

        JTextField homeScoreField = new JTextField();
        JTextField awayScoreField = new JTextField();
        JTextField gameDateField = new JTextField();

        Font labelFont = new Font("Serif", Font.PLAIN, 20);
        Font textFont = new Font("Serif", Font.PLAIN, 18);

        homeTeamLabel.setFont(labelFont);
        awayTeamLabel.setFont(labelFont);
        homeScoreLabel.setFont(labelFont);
        awayScoreLabel.setFont(labelFont);
        gameDateLabel.setFont(labelFont);

        homeTeamComboBox.setFont(textFont);
        awayTeamComboBox.setFont(textFont);
        homeScoreField.setFont(textFont);
        awayScoreField.setFont(textFont);
        gameDateField.setFont(textFont);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Serif", Font.PLAIN, 20));
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final Team homeTeam = (Team) homeTeamComboBox.getSelectedItem();
                final Team awayTeam = (Team) awayTeamComboBox.getSelectedItem();
                final String homeScoreText = homeScoreField.getText();
                final String awayScoreText = awayScoreField.getText();
                final String gameDateString = gameDateField.getText();
    
                if (homeTeam.equals(awayTeam)) {
                    JOptionPane.showMessageDialog(dialog, "Home Team and Away Team cannot be the same.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                    return;
                }
    
                new SwingWorker<Void, Void>() {
                    @Override
                    protected Void doInBackground() throws Exception {
                        int homeScore;
                        int awayScore;
                        java.sql.Date gameDate;
                        try {
                            homeScore = Integer.parseInt(homeScoreText);
                            awayScore = Integer.parseInt(awayScoreText);
                        } catch (NumberFormatException ex) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    JOptionPane.showMessageDialog(dialog, "Scores must be valid integers.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                                }
                            });
                            return null;
                        }
                        try {
                            gameDate = java.sql.Date.valueOf(gameDateString);
                        } catch (IllegalArgumentException ex) {
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    JOptionPane.showMessageDialog(dialog, "Game Date must be in the format YYYY-MM-DD.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
                                }
                            });
                            return null;
                        }
    
                        GameDAO gameDAO = new GameDAO();
                        gameDAO.addGame(homeTeam.getTeamId(), awayTeam.getTeamId(), homeScore, awayScore, gameDate);
                        return null;
                    }
    
                    @Override
                    protected void done() {
                        JOptionPane.showMessageDialog(dialog, "New game added successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        dialog.dispose();
                    }
                }.execute();
            }
        });

        inputPanel.add(homeTeamLabel);
        inputPanel.add(homeTeamComboBox);
        inputPanel.add(awayTeamLabel);
        inputPanel.add(awayTeamComboBox);
        inputPanel.add(homeScoreLabel);
        inputPanel.add(homeScoreField);
        inputPanel.add(awayScoreLabel);
        inputPanel.add(awayScoreField);
        inputPanel.add(gameDateLabel);
        inputPanel.add(gameDateField);

        savePanel.add(saveButton, BorderLayout.CENTER);

        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(savePanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
}