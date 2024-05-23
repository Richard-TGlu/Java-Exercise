import java.sql.*;
import java.util.ArrayList;

public class PlayerDAO {
    private static final String url = "jdbc:mysql://34.80.246.158:3306/basketball?serverTimezone=UTC";
    private static final String user = "CSEbasketball"; 
    private static final String password = "junqi525";

    public void addPlayer(String teamName, String name, int number, Player.Position position, int age, int height, int weight){
        try (Connection conn = DriverManager.getConnection(url, user, password)){
            TeamDAO teamDAO = new TeamDAO();
            int team_id = teamDAO.getTeamIdByName(teamName);
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO Players (team_id, name, number, position, age, height, weight) VALUES (?, ?, ?, ?, ?, ?, ?)");
            stmt.setInt(1, team_id);
            stmt.setString(2, name);
            stmt.setInt(3, number);
            stmt.setString(4, position.name());
            stmt.setInt(5, age);
            stmt.setInt(6, height);
            stmt.setInt(7, weight);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void deletePlayer(int player_id){
        try{
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement deleteTeamStmt = conn.prepareStatement("DELETE FROM Players Where player_id = ?");
            deleteTeamStmt.setInt(1, player_id);
            deleteTeamStmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public ArrayList<Player> getTeamPlayers(int team_id){
        ArrayList<Player> playerList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Players WHERE team_id = ?")) {
            stmt.setInt(1, team_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Player player = new Player();
                player.setPlayerID(rs.getInt("player_id"));
                player.setTeamID(rs.getInt("team_id"));
                player.setName(rs.getString("name"));
                player.setNumber(rs.getInt("number"));
                String positionCode = rs.getString("position");
                player.setPosition(positionCode);
                player.setAge(rs.getInt("age"));
                player.setHeight(rs.getInt("height"));
                player.setWeight(rs.getInt("weight"));
                playerList.add(player);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerList;
    }

    

    public static void main(String[] args){
        //TeamDAO teamDAO = new TeamDAO();
        //PlayerDAO playerDAO = new PlayerDAO();
        //playerDAO.deletePlayer(6);
    }
}
