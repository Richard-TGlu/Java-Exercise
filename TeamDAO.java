import java.sql.*;
import java.util.ArrayList;

public class TeamDAO {
    private ConnectionPool connectionPool;

    public TeamDAO() {
        connectionPool = new ConnectionPool();
    }

    public void addTeam(String teamName, String teamCity, int foundationYear) {
        String sql = "INSERT INTO Teams (name, city, foundation_year) VALUES (?, ?, ?)";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, teamName);
            stmt.setString(2, teamCity);
            stmt.setInt(3, foundationYear);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public void deleteTeam(int team_id) {
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement updatePlayerStmt = conn.prepareStatement("UPDATE Players SET team_id = NULL WHERE team_id = ?");
            updatePlayerStmt.setInt(1, team_id);
            updatePlayerStmt.executeUpdate();

            PreparedStatement updateGamesStmt1 = conn.prepareStatement("UPDATE Games SET team1_id = NULL WHERE team1_id = ?");
            updateGamesStmt1.setInt(1, team_id);
            updateGamesStmt1.executeUpdate();

            PreparedStatement updateGamesStmt2 = conn.prepareStatement("UPDATE Games SET team2_id = NULL WHERE team2_id = ?");
            updateGamesStmt2.setInt(1, team_id);
            updateGamesStmt2.executeUpdate();

            PreparedStatement deleteTeamStmt = conn.prepareStatement("DELETE FROM Teams WHERE team_id = ?");
            deleteTeamStmt.setInt(1, team_id);
            deleteTeamStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public Team getTeamInfo(int team_id) {
        Team team = null;
        String sql = "SELECT * FROM Teams WHERE team_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
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
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return team;
    }

    public ArrayList<Team> getAllTeams() {
        ArrayList<Team> teamList = new ArrayList<>();
        String sql = "SELECT * FROM Teams";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
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
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return teamList;
    }

    public Team getTeamByID(int team_id) {
        Team team = null;
        String sql = "SELECT * FROM Teams WHERE team_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
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
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return team;
    }
}
