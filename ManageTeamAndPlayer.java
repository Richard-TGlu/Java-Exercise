import javax.swing.*;
import java.awt.*;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ManageTeamAndPlayer extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel teamPanel;
    private ArrayList<String[]> teamsList;
    private MenuInterface menuInterface;
    private HashMap<String, List<String[]>> teamPlayers = new HashMap<>();

    // teams
    public ManageTeamAndPlayer(MenuInterface menuInterface) {
        if (menuInterface == null) {
            throw new IllegalArgumentException("MenuInterface cannot be null");
        }

        this.menuInterface = menuInterface;
        setTitle("NBA Teams and Players");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        teamsList = new ArrayList<>();
        teamsList.add(new String[] { "Lakers", "Los Angeles", "1947" });
        teamsList.add(new String[] { "Celtics", "Boston", "1946" });
        teamsList.add(new String[] { "Nets", "Brooklyn", "1967" });

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        teamPanel = createTeamPanel();
        cardPanel.add(teamPanel, "Teams");

        for (String[] team : teamsList) {
            cardPanel.add(createPlayersPanel(team[0]), team[0] + " Players");
        }

        add(cardPanel, BorderLayout.CENTER);

        initializeUI();
    }

    private void initializeUI() {
        JButton backButton = new JButton("Back to Main Menu");
        backButton.addActionListener(e -> backToMenu());
        add(backButton, BorderLayout.SOUTH);
    }

    private void backToMenu() {
        this.setVisible(false);
        menuInterface.setVisible(true);
    }

    private JPanel createTeamPanel() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        for (String[] team : teamsList) {
            JButton teamButton = new JButton(team[0]);
            teamButton.addActionListener(e -> cardLayout.show(cardPanel, team[0] + " Players"));
            panel.add(teamButton);
        }

        JButton addButton = new JButton("Add Team");
        addButton.addActionListener(e -> {
            JPanel addPanel = createAddDataPanel();
            cardPanel.add(addPanel, "AddData");
            cardLayout.show(cardPanel, "AddData");
        });
        panel.add(addButton);

        return panel;
    }

    // jump to players
    private JPanel createPlayersPanel(String teamName) {
        JPanel panel = new JPanel(new BorderLayout());
        List<String[]> playersData = teamPlayers.getOrDefault(teamName, new ArrayList<>());
        String[] columnNames = { "Player", "Number", "Position", "Age", "Height", "Weight(lbs)" };
        String[][] data = new String[playersData.size()][columnNames.length];
        for (int i = 0; i < playersData.size(); i++) {
            data[i] = playersData.get(i);
        }
        /*
         * if (teamName.equals("Lakers")) {
         * data = new String[][] {
         * { "LeBron James", "23", "SF", "39", "6'7", "210 lbs" },
         * { "Anthony Davis", "3", "PF-C", "31", "6'10", "253 lbs" }
         * };
         * } else if (teamName.equals("Celtics")) {
         * data = new String[][] {
         * { "Jayson Tatum", "0", "SF", "26", "6'8", "210 lbs" }
         * };
         * } else if (teamName.equals("Nets")) {
         * data = new String[][] {
         * { "Ben Simmons", "10", "PG-PF", "27", "6'10", "240 lbs" },
         * };
         * }
         */

        JTable table = new JTable(data, columnNames);

        table.setFillsViewportHeight(true);
        table.setGridColor(Color.GRAY);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(teamName);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(nameLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton backButton = new JButton("Back to Teams");
        backButton.addActionListener(e -> cardLayout.show(cardPanel, "Teams"));

        JButton nextButton = new JButton("Next");
        nextButton.addActionListener(e -> showNextTeamPanel(teamName));
        JButton previousButton = new JButton("Previous");
        previousButton.addActionListener(e -> showPreviousTeamPanel(teamName));

        buttonPanel.add(backButton);
        buttonPanel.add(previousButton);
        buttonPanel.add(nextButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(headerPanel, BorderLayout.NORTH);

        JButton createPlayerButton = new JButton("Create Player");
        createPlayerButton.addActionListener(e -> {
            JPanel addPlayerPanel = createAddPlayerPanel(teamName);
            cardPanel.add(addPlayerPanel, teamName + " AddPlayer");
            cardLayout.show(cardPanel, teamName + " AddPlayer");
        });

        buttonPanel.add(backButton);
        buttonPanel.add(createPlayerButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    public void saveTeamsToFile(List<String[]> teamsList, String filename) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (String[] team : teamsList) {
                writer.write(String.join(",", team) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // create team
    private JPanel createAddDataPanel() {
        JPanel panel = new JPanel(new GridLayout(4, 2));

        JTextField nameField = new JTextField();
        JTextField cityField = new JTextField();
        JTextField yearField = new JTextField();

        panel.add(new JLabel("Name:"));
        panel.add(nameField);
        panel.add(new JLabel("City:"));
        panel.add(cityField);
        panel.add(new JLabel("Foundation Year:"));
        panel.add(yearField);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            String[] newTeam = { nameField.getText(), cityField.getText(), yearField.getText() };
            teamsList.add(newTeam);
            teamPlayers.putIfAbsent(newTeam[0], new ArrayList<>());
            saveTeamsToFile(teamsList, "teams.txt");
            cardPanel.remove(teamPanel);
            teamPanel = createTeamPanel();
            cardPanel.add(teamPanel, "Teams");
            cardLayout.show(cardPanel, "Teams");
        });

        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> cardLayout.show(cardPanel, "Teams"));

        panel.add(saveButton);
        panel.add(quitButton);

        return panel;
    }

    // creat players
    private JPanel createAddPlayerPanel(String teamName) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(2, 2, 2, 2);

        // Player Name
        JTextField playerNameField = new JTextField(10);
        panel.add(new JLabel("Player Name"), gbc);
        panel.add(playerNameField, gbc);

        // Position with Checkboxes
        panel.add(new JLabel("Position"), gbc);
        JCheckBox pgButton = new JCheckBox("PG");
        JCheckBox sgButton = new JCheckBox("SG");
        JCheckBox sfButton = new JCheckBox("SF");
        JCheckBox pfButton = new JCheckBox("PF");
        JCheckBox cButton = new JCheckBox("C");

        JPanel positionPanel = new JPanel(new GridLayout(0, 1));
        positionPanel.add(pgButton);
        positionPanel.add(sgButton);
        positionPanel.add(sfButton);
        positionPanel.add(pfButton);
        positionPanel.add(cButton);
        panel.add(positionPanel, gbc);

        // Height
        JTextField heightField = new JTextField(10);
        panel.add(new JLabel("Height"), gbc);
        panel.add(heightField, gbc);

        // Number
        JTextField numberField = new JTextField(10);
        panel.add(new JLabel("Number"), gbc);
        panel.add(numberField, gbc);

        // Age
        JTextField ageField = new JTextField(10);
        panel.add(new JLabel("Age"), gbc);
        panel.add(ageField, gbc);

        // Weight
        JTextField weightField = new JTextField(10);
        panel.add(new JLabel("Weight(lbs)"), gbc);
        panel.add(weightField, gbc);

        // Save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            StringBuilder position = new StringBuilder();
            if (pgButton.isSelected())
                position.append("PG ");
            if (sgButton.isSelected())
                position.append("SG ");
            if (sfButton.isSelected())
                position.append("SF ");
            if (pfButton.isSelected())
                position.append("PF ");
            if (cButton.isSelected())
                position.append("C ");

            String[] newPlayer = {
                    playerNameField.getText(),
                    numberField.getText(),
                    position.toString().trim(), // Removes trailing space
                    ageField.getText(),
                    heightField.getText(),
                    weightField.getText()
            };

            teamPlayers.putIfAbsent(teamName, new ArrayList<>());
            teamPlayers.get(teamName).add(newPlayer);

            cardPanel.remove(teamPanel);
            teamPanel = createPlayersPanel(teamName); // Assuming this method now uses `teamPlayers`
            cardPanel.add(teamPanel, teamName);
            cardLayout.show(cardPanel, teamName);
        });
        panel.add(saveButton, gbc);

        // Quit button
        JButton quitButton = new JButton("Quit");
        quitButton.addActionListener(e -> cardLayout.show(cardPanel, "Teams"));
        panel.add(quitButton, gbc);

        return panel;
    }

    // for the circle of teams
    private void showNextTeamPanel(String currentTeam) {
        int index = findTeamIndex(currentTeam);
        index = (index + 1) % teamsList.size();
        String nextTeam = teamsList.get(index)[0];
        cardLayout.show(cardPanel, nextTeam + " Players");
    }

    private void showPreviousTeamPanel(String currentTeam) {
        int index = findTeamIndex(currentTeam);
        index = (index - 1 + teamsList.size()) % teamsList.size();
        String prevTeam = teamsList.get(index)[0];
        cardLayout.show(cardPanel, prevTeam + " Players");
    }

    private int findTeamIndex(String teamName) {
        for (int i = 0; i < teamsList.size(); i++) {
            if (teamsList.get(i)[0].equals(teamName)) {
                return i;
            }
        }
        return -1;
    }

}
