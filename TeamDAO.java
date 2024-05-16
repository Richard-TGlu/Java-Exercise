import java.sql.*;
import java.util.ArrayList;

public class TeamDAO {
    private static final String url = "jdbc:mysql://localhost:3306/BasketBallDB?serverTimezone=UTC";
    private static final String user = "root"; 
    private static final String password = "junqi525";

    public void addTeam(String teamName, String teamCity, int foundationYear) {
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO teams (name, city, foundation_year) VALUES (?, ?, ?)")) { 
            stmt.setString(1, teamName);
            stmt.setString(2, teamCity);
            stmt.setInt(3, foundationYear);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteTeam(String teamName){
        TeamDAO teamDAO = new TeamDAO();
        int team_id = teamDAO.getTeamIdByName(teamName);
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM teams Where team_id = ?")) { 
            stmt.setInt(1, team_id);
            int rowsAffected = stmt.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("成功刪除了名字為 " + teamName + " 的球隊");
            } else {
                System.out.println("找不到名字為為 " + teamName + " 的球隊");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public Team getTeamInfo(int team_id) {
        Team team = null;
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM teams WHERE team_id = ?")) {
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
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM teams")) {
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
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM teams WHERE name = ?")) {
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
