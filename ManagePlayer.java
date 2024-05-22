import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ManagePlayer extends JPanel{
    
    private Team team;
    private ManageTeam manageTeam;
    private ArrayList<Player> players;

    public ManagePlayer(Team team, ManageTeam manageTeam){
        this.team = team;
        this.manageTeam = manageTeam;
        PlayerDAO playerDAO = new PlayerDAO();
        this.players = playerDAO.getTeamPlayers(team.getTeamId());

        setLayout(new BorderLayout());

        //set title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new BorderLayout());
        JPanel TeamListPanel = new JPanel();
        TeamListPanel.setLayout(new BorderLayout());
        
        JButton backToTeamList = new JButton("Team List");
        backToTeamList.setFont(new Font("Serif", Font.BOLD, 20));
        backToTeamList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                manageTeam.showTeamList();;
            }
        });
        JLabel nameLabel = new JLabel(team.getName(), JLabel.CENTER);
        nameLabel.setFont(new Font("Serif", Font.BOLD, 40));
        nameLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        JPanel CityAndYear = new JPanel();
        CityAndYear.setLayout(new GridLayout(2,1,0,0));
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
        titlePanel.add(nameLabel, BorderLayout.CENTER);
        
        //set PlayerList
        JPanel playerListPanel = new JPanel();
        playerListPanel.setLayout(new BorderLayout());

        JPanel ListColumn = new JPanel();
        ListColumn.setLayout(new GridLayout(1, 6));
        String[] column = {"Name", "Number", "Position", "Age", "Height", "Weight"};
        Font columnFont = new Font("Serif", Font.PLAIN,18);
        for(int i=0 ; i<6 ; i++){
            JTextField columnField = new JTextField(column[i]);
            columnField.setFont(columnFont);
            columnField.setEditable(false);
            ListColumn.add(columnField);
        }

        JPanel table = tablePanel();
        


        playerListPanel.add(ListColumn, BorderLayout.NORTH);
        playerListPanel.add(table, BorderLayout.CENTER);
        add(titlePanel, BorderLayout.NORTH);
        add(playerListPanel, BorderLayout.CENTER);
    }

    
    JPanel tablePanel(){
        Font columnFont = new Font("Serif", Font.PLAIN,18);
        JPanel table = new JPanel();
        table.setLayout(new GridLayout(players.size(), 6));
        for(Player player : players){
            JTextField name = new JTextField(player.getName());
            JTextField number = new JTextField(String.valueOf(player.getNumber()));
            JTextField position = new JTextField(player.getPosition());
            JTextField age = new JTextField(String.valueOf(player.getAge()));
            JTextField height = new JTextField(String.valueOf(player.getHeight()) + " cm");
            JTextField weight = new JTextField(String.valueOf(player.getWeight() + " kg"));

            name.setFont(columnFont);
            number.setFont(columnFont);
            position.setFont(columnFont);
            age.setFont(columnFont);
            height.setFont(columnFont);
            weight.setFont(columnFont);
            
            name.setEditable(false);
            number.setEditable(false);
            position.setEditable(false);
            age.setEditable(false);
            height.setEditable(false);
            weight.setEditable(false);

            table.add(name);
            table.add(number);
            table.add(position);
            table.add(age);
            table.add(height);
            table.add(weight);
        }
        return table;
    }
}
