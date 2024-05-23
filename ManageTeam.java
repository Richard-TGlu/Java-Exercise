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
    private TeamDAO teamDAO;

    public ManageTeam(MainMenu mainMenu) {
        this.mainMenu = mainMenu;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        teamListPanel = new JPanel(new BorderLayout());
        //Title Panel
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

        JLabel titleLabel = new JLabel("所有球隊列表", JLabel.CENTER);
        titleLabel.setFont(new Font("標楷體", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 0, 0));

        //TeamList Panel
        JPanel teamsButtonPanel = new JPanel();
        teamsButtonPanel.setLayout(new GridLayout(10, 2, 10, 50));
        teamsButtonPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 20, 50));
        
        teamDAO = new TeamDAO();
        teams = teamDAO.getAllTeams();
        Font buttonFont = new Font("Serif", Font.PLAIN,30);

        for (Team team : teams) {
            ManagePlayer playerListPanel = new ManagePlayer(team, this);
            add(playerListPanel, team.getName());
            JButton teamButton = new JButton(team.getCity() + "  " + team.getName());
            teamButton.setFont(buttonFont);
            teamsButtonPanel.add(teamButton);
            
            teamButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    showPlayerList(team);
                }
            });
        }

        //Function Panel
        JPanel FunctionPanel = new JPanel();
        FunctionPanel.setLayout(new BorderLayout());
        
        //set create team button
        JPanel createTeam = new JPanel();
        createTeam.setLayout(new BorderLayout());
        createTeam.setBorder(BorderFactory.createEmptyBorder(20, 100, 20, 0));
        JButton createTeamButton = new JButton("Create Team");
        createTeamButton.setFont(new Font("Serif", Font.PLAIN, 30));
        createTeam.add(createTeamButton, BorderLayout.CENTER);
        createTeamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showCreateTeamDialog();
            }
        });

        //set delete team button
        JPanel deleteTeam = new JPanel();
        deleteTeam.setLayout(new BorderLayout());
        deleteTeam.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JButton deleteTeamButton = new JButton("Delete Team");
        deleteTeamButton.setFont(new Font("Serif", Font.PLAIN, 16));
        deleteTeam.add(deleteTeamButton, BorderLayout.CENTER);
        deleteTeamButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showDeleteTeamDialog();
            }
        });
        
        FunctionPanel.add(createTeam, BorderLayout.CENTER);
        FunctionPanel.add(deleteTeam, BorderLayout.EAST);
        MenuPanel.add(backToMenu, BorderLayout.EAST);
        titlePanel.add(MenuPanel, BorderLayout.SOUTH);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        teamListPanel.add(titlePanel, BorderLayout.NORTH);
        teamListPanel.add(new JScrollPane(teamsButtonPanel), BorderLayout.CENTER);
        teamListPanel.add(FunctionPanel, BorderLayout.SOUTH);

        add(teamListPanel, "teamList");

        showTeamList();
    }

    //CreateTeam Function
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
    
    //deleteTeam Function
    private void showDeleteTeamDialog() {
        JDialog dialog = new JDialog((Frame) null, "Delete Team", true);

        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(null);
        dialog.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        //inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JPanel savePanel = new JPanel();
        savePanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Delete the team you want:");
        Font LabelFont = new Font("Serif", Font.PLAIN, 30);
        titleLabel.setFont(LabelFont);
        inputPanel.add(titleLabel, BorderLayout.NORTH);

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
        inputPanel.add(new JScrollPane(teamJList), BorderLayout.CENTER);

        JButton deleteButton = new JButton("Delete");
        deleteButton.addActionListener(e -> {
            Team selectedTeam = teamJList.getSelectedValue();
            if (selectedTeam != null) {
                int response = JOptionPane.showConfirmDialog(dialog,
                    "Are you sure you want to delete the team: " + selectedTeam.getCity() + " " + selectedTeam.getName() + "?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.PLAIN_MESSAGE);
                if (response == JOptionPane.YES_OPTION) {
                teamDAO.deleteTeam(selectedTeam.getTeamId());
                model.removeElement(selectedTeam);
                JOptionPane.showMessageDialog(dialog, "Team deleted successfully.", "Information", JOptionPane.INFORMATION_MESSAGE);
                }
                dialog.dispose();
                refreshTeamList();
            } else {
            JOptionPane.showMessageDialog(dialog, "Please select a team to delete.", "Warning", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.add(deleteButton, BorderLayout.SOUTH);
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void refreshTeamList() {
        removeAll();
        revalidate();
        repaint();
        ManageTeam manageTeam = new ManageTeam(mainMenu);
        add(manageTeam);
    }

    public void showTeamList() {
        cardLayout.show(this, "teamList");
    }
    public void showPlayerList(Team team){
        cardLayout.show(this, team.getName());
    }
}
