import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PlayerCareerData extends JPanel{

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel playerListPanel;
    private JPanel loadingPanel;
    private Team team;
    private ArrayList<Player> players;
    private PlayerDAO playerDAO;
    private GameStatsDAO gameStatsDAO;

    public PlayerCareerData(Team team, AllTeamList allTeamList){
        this.team = team;
        cardLayout = new CardLayout();
        setLayout(cardLayout);

        mainPanel = new JPanel(new BorderLayout());
        loadingPanel = new JPanel(new BorderLayout());

        initializeUI(allTeamList);

    }
    
    public void initializeUI(AllTeamList allTeamList){
        // Title Panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        JPanel TeamInfo = new JPanel(new BorderLayout());
        JButton backToTeamList = new JButton("Team List");
        backToTeamList.setFont(new Font("Serif", Font.BOLD, 20));
        backToTeamList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                allTeamList.showTeamList();
            }
        });
        JLabel titleLabel = new JLabel("Players Career Data", JLabel.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 40));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JLabel teamNamLabel = new JLabel(team.toString());
        teamNamLabel.setFont(new Font("Serif", Font.BOLD, 28));
        teamNamLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        TeamInfo.add(backToTeamList, BorderLayout.EAST);
        TeamInfo.add(teamNamLabel, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(TeamInfo, BorderLayout.SOUTH);

        JPanel panel = new JPanel(new BorderLayout());

        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(panel, BorderLayout.CENTER);
        
        
        add(mainPanel, "playList");
    }
}