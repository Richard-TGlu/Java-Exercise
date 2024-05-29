import java.sql.*;
import java.util.ArrayList;

public class PlayerDAO {
    private ConnectionPool connectionPool;

    public PlayerDAO() {
        connectionPool = new ConnectionPool();
    }

    public void addPlayer(int team_id, String name, int number, Player.Position position, int age, int height, int weight) {
        String sql = "INSERT INTO Players (team_id, name, number, position, age, height, weight) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
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
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public void deletePlayer(int player_id) {
        String sql = "DELETE FROM Players WHERE player_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public ArrayList<Player> getTeamPlayers(int team_id) {
        ArrayList<Player> playerList = new ArrayList<>();
        String sql = "SELECT * FROM Players WHERE team_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
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
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return playerList;
    }
    
    public Player getPlayerByID(int player_id) {
        Player player = null;
        String sql = "SELECT * FROM Players WHERE player_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, player_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                player = new Player();
                player.setPlayerID(rs.getInt("player_id"));
                player.setTeamID(rs.getInt("team_id"));
                player.setName(rs.getString("name"));
                player.setNumber(rs.getInt("number"));
                String positionCode = rs.getString("position");
                player.setPosition(positionCode);
                player.setAge(rs.getInt("age"));
                player.setHeight(rs.getInt("height"));
                player.setWeight(rs.getInt("weight"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return player;
    }
}
