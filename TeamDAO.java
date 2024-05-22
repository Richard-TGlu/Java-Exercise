import java.sql.*;
import java.util.ArrayList;

public class TeamDAO {
    private static final String url = "jdbc:mysql://34.80.246.158:3306/basketball?serverTimezone=UTC";
    private static final String user = "CSEbasketball";     
    private static final String password = "junqi525";
    
    //增加球隊
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

    //刪除球隊
    public void deleteTeam(String teamName){
        TeamDAO teamDAO = new TeamDAO();
        int team_id = teamDAO.getTeamIdByName(teamName);
        try (Connection conn = DriverManager.getConnection(url, user, password);
            PreparedStatement stmt = conn.prepareStatement("DELETE FROM Teams Where team_id = ?")) { 
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
    
    //透過team_id來搜尋球隊
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
    
    //返回一個所有球隊資料的ArrayList
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

    //透過Team Name獲取team_id
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
    ublic static void main(String[] args) {
        TeamDAO teamDAO = new TeamDAO();
        ArrayList<Team> teamlist = teamDAO.getAllTeams();
        for (Team team : teamlist) {
            System.out.println(team.toString());
        }

    }
}
