import java.sql.*;
import java.util.ArrayList;

public class GameDAO {
    private ConnectionPool connectionPool;

    public GameDAO() {
        connectionPool = new ConnectionPool();
    }

    public void addGame(int team1_id, int team2_id, Date game_date) {
        String sql = "INSERT INTO Games (team1_id, team2_id, team1_score, team2_score, game_date) VALUES (?, ?, 0, 0, ?)";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, team1_id);
            stmt.setInt(2, team2_id);
            stmt.setDate(3, game_date);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }


    public int searchGameId(int team1_id, int team2_id, Date game_date) {
        String sql = "SELECT game_id FROM Games WHERE team1_id = ? AND team2_id = ? AND game_date = ?";
        Connection conn = null;
        int game_id = -1;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, team1_id);
            stmt.setInt(2, team2_id);
            stmt.setDate(3, game_date);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                game_id = rs.getInt("game_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return game_id;
    }

    public void deleteGame(int game_id) {
        String sql = "DELETE FROM Games WHERE game_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, game_id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public Game getGameById(int gameId) {
        String sql = "SELECT * FROM Games WHERE game_id = ?";
        Game game = null;
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                game = new Game();
                game.setTeam1Id(rs.getInt("team1_id"));
                game.setTeam2Id(rs.getInt("team2_id"));
                game.setTeam1Score(rs.getInt("team1_score"));
                game.setTeam2Score(rs.getInt("team2_score"));
                game.setGameDate(rs.getDate("game_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return game;
    }

    public ArrayList<Game> getAllGames() {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games ORDER BY game_date DESC";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Game game = new Game();
                game.setGameId(rs.getInt("game_id"));
                game.setTeam1Id(rs.getInt("team1_id"));
                game.setTeam2Id(rs.getInt("team2_id"));
                game.setTeam1Score(rs.getInt("team1_score"));
                game.setTeam2Score(rs.getInt("team2_score"));
                game.setGameDate(rs.getDate("game_date"));
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return games;
    }

    public ArrayList<Game> getGamesByOneTeam(int team_id) {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games WHERE team1_id = ? OR team2_id = ? ORDER BY game_date DESC";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, team_id);
            stmt.setInt(2, team_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Game game = new Game();
                game.setGameId(rs.getInt("game_id"));
                game.setTeam1Id(rs.getInt("team1_id"));
                game.setTeam2Id(rs.getInt("team2_id"));
                game.setTeam1Score(rs.getInt("team1_score"));
                game.setTeam2Score(rs.getInt("team2_score"));
                game.setGameDate(rs.getDate("game_date"));
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return games;
    }

    public ArrayList<Game> getGamesByTwoTeam(int team1_id, int team2_id) {
        ArrayList<Game> games = new ArrayList<>();
        String sql = "SELECT * FROM Games WHERE (team1_id = ? AND team2_id = ?) OR (team1_id = ? AND team2_id = ?) ORDER BY game_date DESC";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, team1_id);
            stmt.setInt(2, team2_id);
            stmt.setInt(3, team2_id);
            stmt.setInt(4, team1_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Game game = new Game();
                game.setGameId(rs.getInt("game_id"));
                game.setTeam1Id(rs.getInt("team1_id"));
                game.setTeam2Id(rs.getInt("team2_id"));
                game.setTeam1Score(rs.getInt("team1_score"));
                game.setTeam2Score(rs.getInt("team2_score"));
                game.setGameDate(rs.getDate("game_date"));
                games.add(game);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return games;
    }
    
    public void updateGameScores(int gameId) {
        GameStatsDAO gameStatsDAO = new GameStatsDAO();
        int team1Score = gameStatsDAO.getTeamScoreByGame(gameId, getGameById(gameId).getTeam1Id());
        int team2Score = gameStatsDAO.getTeamScoreByGame(gameId, getGameById(gameId).getTeam2Id());

        String sql = "UPDATE Games SET team1_score = ?, team2_score = ? WHERE game_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, team1Score);
            stmt.setInt(2, team2Score);
            stmt.setInt(3, gameId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }
}
