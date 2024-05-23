import java.sql.*;
import java.util.ArrayList;

public class TeamDAO {
    private static final String url = "jdbc:mysql://34.80.246.158:3306/basketball?serverTimezone=UTC";
    private static final String user = "CSEbasketball";     
    private static final String password = "junqi525";
    
   
    public void addTeam(String teamName, String teamCity, int foundationYear) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO Teams (name, city, foundation_year) VALUES (?, ?, ?)")) { 
            stmt.setString(1, teamName);
            stmt.setString(2, teamCity);
            stmt.setInt(3, foundationYear);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void deleteTeam(int team_id){
        try{
            Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement updatePlayerStmt = conn.prepareStatement("UPDATE Players SET team_id = NULL WHERE team_id = ?");
            updatePlayerStmt.setInt(1, team_id);
            updatePlayerStmt.executeUpdate();
            
            PreparedStatement updateGamesStmt1 = conn.prepareStatement("UPDATE Games SET team1_id = NULL WHERE team1_id = ?");
            updateGamesStmt1.setInt(1, team_id);
            updateGamesStmt1.executeUpdate();

            PreparedStatement updateGamesStmt2 = conn.prepareStatement("UPDATE Games SET team2_id = NULL WHERE team2_id = ?");
            updateGamesStmt2.setInt(1, team_id);
            updateGamesStmt2.executeUpdate();

            PreparedStatement deleteTeamStmt = conn.prepareStatement("DELETE FROM Teams Where team_id = ?");
            deleteTeamStmt.setInt(1, team_id);
            deleteTeamStmt.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
   
    public Team getTeamInfo(int team_id) {
        Team team = null;
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Teams WHERE team_id = ?")) {
            stmt.setInt(1, team_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                team = new Team();
                team.setTeamId(rs.getInt("team_id"));
                team.setName(rs.getString("name"));
                team.setCity(rs.getString("city"));
                team.setFoundationYear(rs.getInt("foundation_year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return team;
    }
    

    public ArrayList<Team> getAllTeams() {
        ArrayList<Team> teamList = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Teams")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Team team = new Team();
                team.setTeamId(rs.getInt("team_id"));
                team.setName(rs.getString("name"));
                team.setCity(rs.getString("city"));
                team.setFoundationYear(rs.getInt("foundation_year"));
                teamList.add(team);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return teamList;
    }

 
    public int getTeamIdByName(String teamName) {
        int team_id = -1;
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Teams WHERE name = ?")) {
            stmt.setString(1, teamName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                team_id = rs.getInt("team_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return team_id;
    }
}
