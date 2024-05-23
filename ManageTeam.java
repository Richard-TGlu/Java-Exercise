import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManageTeam extends JPanel {

    private MainMenu mainMenu;
    private CardLayout cardLayout;
    private JPanel teamListPanel;
    private ArrayList<Team> teams;

    public ManageTeam(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        teamListPanel = new JPanel(new BorderLayout());
        JPanel panel = new JPanel(new BorderLayout(5, 10));

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
        JLabel titleLabel = new JLabel("team list", JLabel.CENTER);
        titleLabel.setFont(new Font("words", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        JPanel teamsButtonPanel = new JPanel();
        teamsButtonPanel.setLayout(new GridLayout(10, 2, 10, 50));
        teamsButtonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 20, 50));

        JPanel createTeam = new JPanel();
        createTeam.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 20));
        createTeam.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 100));
        JButton createTeamButton = new JButton("Create Team");
        createTeamButton.setFont(new Font("Serif", Font.PLAIN, 30));
        createTeamButton.setPreferredSize(new Dimension(200, 40));
        createTeam.add(createTeamButton, BorderLayout.CENTER);
        createTeamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateTeamDialog();
            }
        });
        JButton deleteTeamButton = new JButton("Delete Team");
        deleteTeamButton.setFont(new Font("Serif", Font.PLAIN, 30));

        deleteTeamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                showDeleteTeamDialog();
            }
        });
        TeamDAO teamDAO = new TeamDAO();
        teams = teamDAO.getAllTeams();
        Font buttonFont = new Font("Serif", Font.PLAIN, 30);

        for (Team team : teams) {
            ManagePlayer playerListPanel = new ManagePlayer(team, this);
            add(playerListPanel, team.getName());
            JButton teamButton = new JButton(team.getCity() + "  " + team.getName());
            teamButton.setFont(buttonFont);
            teamsButtonPanel.add(teamButton);

            teamButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showPlayerList(team);
                }
            });
        }

        panel.add(createTeamButton, BorderLayout.CENTER);
        panel.add(deleteTeamButton, BorderLayout.EAST);

        MenuPanel.add(backToMenu, BorderLayout.EAST);
        titlePanel.add(MenuPanel, BorderLayout.SOUTH);
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        teamListPanel.add(titlePanel, BorderLayout.NORTH);
        teamListPanel.add(new JScrollPane(teamsButtonPanel), BorderLayout.CENTER);

        teamListPanel.add(panel, BorderLayout.SOUTH);

        add(teamListPanel, "teamList");

        showTeamList();
    }

    private void showCreateTeamDialog() {
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

        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        Font TextFont = new Font("Serif", Font.PLAIN, 22);
        nameLabel.setFont(LabelFont);
        cityLabel.setFont(LabelFont);
        foundationYearLabel.setFont(LabelFont);
        nameField.setFont(TextFont);
        cityField.setFont(TextFont);
        foundationYearField.setFont(TextFont);

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String city = cityField.getText();
                int foundationYear = Integer.parseInt(foundationYearField.getText());

                TeamDAO teamDAO = new TeamDAO();
                teamDAO.addTeam(name, city, foundationYear);
                dialog.dispose();

                refreshTeamList();
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

    private DefaultListModel<Team> createListModel(ArrayList<Team> teams) {
        DefaultListModel<Team> model = new DefaultListModel<>();
        for (Team team : teams) {
            model.addElement(team);
        }
        return model;
    }

    private void showDeleteTeamDialog() {
        JDialog dialog = new JDialog((Frame) null, "Delete Team", true);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(2, 1, 50, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());

        JLabel nameLabel = new JLabel("Delete the team you want:");
        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        nameLabel.setFont(LabelFont);
        inputPanel.add(nameLabel);

        TeamDAO teamDAO = new TeamDAO();
        ArrayList<Team> teams = teamDAO.getAllTeams();
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
                    setText(team.getCity() + " " + team.getName());
                }
                return this;
            }
        });
        JScrollPane scrollPane = new JScrollPane(teamJList);
        teamlistPanel.add(scrollPane, BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            ensure();
        });

        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.add(inputPanel, BorderLayout.CENTER);

        dialog.setVisible(true);
    }

    private void ensure() {
        JDialog confirmdialog = new JDialog();
        confirmdialog.setTitle("Confirm Deletion");
        confirmdialog.setSize(400, 400);
        confirmdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        confirmdialog.setLocationRelativeTo(null);
        confirmdialog.setModal(true);

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Ensure to delete it", JLabel.CENTER);
        titleLabel.setFont(new Font("words", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JPanel downPanel = new JPanel(new FlowLayout());
        JButton yesButton = new JButton("Yes");
        yesButton.setFont(new Font("Serif", Font.PLAIN, 30));
        yesButton.setPreferredSize(new Dimension(200, 40));
        JButton noButton = new JButton("No");
        noButton.setFont(new Font("Serif", Font.PLAIN, 30));
        noButton.setPreferredSize(new Dimension(200, 40));

        yesButton.addActionListener(ev -> {
            confirmdialog.dispose();
        });
        noButton.addActionListener(ev -> {
            confirmdialog.dispose();
        });

        downPanel.add(yesButton);
        downPanel.add(noButton);

        confirmdialog.add(titlePanel, BorderLayout.CENTER);
        confirmdialog.add(downPanel, BorderLayout.SOUTH);

        confirmdialog.setVisible(true);
    }

    private void refreshTeamList() {
        removeAll();
        revalidate();
        repaint();

        new ManageTeam(mainMenu);
    }

    public void showTeamList() {
        cardLayout.show(this, "teamList");
    }

    public void showPlayerList(Team team) {
        cardLayout.show(this, team.getName());
    }

}
