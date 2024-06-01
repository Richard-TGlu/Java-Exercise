import java.sql.*;
import java.util.ArrayList;

public class GameStatsDAO {
    private ConnectionPool connectionPool;

    public GameStatsDAO() {
        connectionPool = new ConnectionPool();
    }

    public void addGameStats(int playerId, int gameId) {
        String sql = "INSERT INTO Game_stats (player_id, game_id, playing_time, points, assists, " + 
        "rebounds, steals, blocks, turnovers, FGM, FGA, threePM, threePA, FTM, FTA, foul) " + 
        "VALUES (?, ?, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerId);
            stmt.setInt(2, gameId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }

    public void updateGameStats(GameStats gameStats) {
        String sql = "UPDATE Game_stats SET player_id = ?, game_id = ?, playing_time = ?, points = ?, assists = ?, " + 
        "rebounds = ?, steals = ?, blocks = ?, turnovers = ?, FGM = ?, " + 
        "FGA = ?, threePM = ?, threePA = ?, FTM = ?, FTA = ?, foul = ? WHERE stat_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, gameStats.getPlayerId());
            stmt.setInt(2, gameStats.getGameId());
            stmt.setInt(3, gameStats.getPlayingTime());
            stmt.setInt(4, gameStats.getPoints());
            stmt.setInt(5, gameStats.getAssists());
            stmt.setInt(6, gameStats.getRebounds());
            stmt.setInt(7, gameStats.getSteals());
            stmt.setInt(8, gameStats.getBlocks());
            stmt.setInt(9, gameStats.getTurnovers());
            stmt.setInt(10, gameStats.getFGM());
            stmt.setInt(11, gameStats.getFGA());
            stmt.setInt(12, gameStats.getThreePM());
            stmt.setInt(13, gameStats.getThreePA());
            stmt.setInt(14, gameStats.getFTM());
            stmt.setInt(15, gameStats.getFTA());
            stmt.setInt(16, gameStats.getFoul());
            stmt.setInt(17, gameStats.getStatId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
    }
    
    public ArrayList<GameStats> getGameStatsByGameAndTeam(int game_id, int team_id) {
        ArrayList<GameStats> gameStatsList = new ArrayList<>();
        String sql = "SELECT gs.* FROM Game_stats gs " +
                     "JOIN Players p ON gs.player_id = p.player_id " +
                     "WHERE gs.game_id = ? AND p.team_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, game_id);
            stmt.setInt(2, team_id);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                GameStats gameStats = new GameStats();
                gameStats.setStatId(rs.getInt("stat_id"));
                gameStats.setPlayerId(rs.getInt("player_id"));
                gameStats.setGameId(rs.getInt("game_id"));
                gameStats.setPlayingTime(rs.getInt("playing_time"));
                gameStats.setPoints(rs.getInt("points"));
                gameStats.setAssists(rs.getInt("assists"));
                gameStats.setRebounds(rs.getInt("rebounds"));
                gameStats.setSteals(rs.getInt("steals"));
                gameStats.setBlocks(rs.getInt("blocks"));
                gameStats.setTurnovers(rs.getInt("turnovers"));
                gameStats.setFGM(rs.getInt("FGM"));
                gameStats.setFGA(rs.getInt("FGA"));
                gameStats.setThreePM(rs.getInt("threePM"));
                gameStats.setThreePA(rs.getInt("threePA"));
                gameStats.setFTM(rs.getInt("FTM"));
                gameStats.setFTA(rs.getInt("FTA"));
                gameStats.setFoul(rs.getInt("foul"));
                gameStatsList.add(gameStats);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return gameStatsList;
    }

    public int getTeamScoreByGame(int game_id, int team_id) {
        int teamScore = 0;
        String sql = "SELECT SUM(gs.points) AS team_score FROM Game_stats gs " +
                     "JOIN Players p ON gs.player_id = p.player_id " +
                     "WHERE gs.game_id = ? AND p.team_id = ?";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, game_id);
            stmt.setInt(2, team_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                teamScore = rs.getInt("team_score");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return teamScore;
    }

    public CareerStats getPlayerCareerStats(int playerId) {
        CareerStats careerStats = new CareerStats();
        String sql = "SELECT p.name AS player_name, COUNT(*) AS games_played, SUM(playing_time) AS total_playing_time, " +
                     "SUM(points) AS total_points, SUM(assists) AS total_assists, " +
                     "SUM(rebounds) AS total_rebounds, SUM(steals) AS total_steals, " +
                     "SUM(blocks) AS total_blocks, SUM(turnovers) AS total_turnovers, " +
                     "SUM(FGM) AS total_FGM, SUM(FGA) AS total_FGA, " +
                     "SUM(threePM) AS total_threePM, SUM(threePA) AS total_threePA, " +
                     "SUM(FTM) AS total_FTM, SUM(FTA) AS total_FTA, " +
                     "SUM(foul) AS total_foul " +
                     "FROM Game_stats gs " +
                     "JOIN Players p ON gs.player_id = p.player_id " +
                     "WHERE gs.player_id = ? AND gs.playing_time > 0";
        Connection conn = null;
        try {
            conn = connectionPool.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int gamesPlayed = rs.getInt("games_played");
                if (gamesPlayed > 0) {
                    careerStats.setPlayerName(rs.getString("player_name"));
                    careerStats.setPlayingTime(rs.getDouble("total_playing_time") / gamesPlayed);
                    careerStats.setPoints(rs.getDouble("total_points") / gamesPlayed);
                    careerStats.setAssists(rs.getDouble("total_assists") / gamesPlayed);
                    careerStats.setRebounds(rs.getDouble("total_rebounds") / gamesPlayed);
                    careerStats.setSteals(rs.getDouble("total_steals") / gamesPlayed);
                    careerStats.setBlocks(rs.getDouble("total_blocks") / gamesPlayed);
                    careerStats.setTurnovers(rs.getDouble("total_turnovers") / gamesPlayed);
                    careerStats.setFoul(rs.getDouble("total_foul") / gamesPlayed);

                    careerStats.setFGM(rs.getDouble("total_FGM") / gamesPlayed);
                    careerStats.setFGA(rs.getDouble("total_FGA") / gamesPlayed);
                    careerStats.setThreePM(rs.getDouble("total_threePM") / gamesPlayed);
                    careerStats.setThreePA(rs.getDouble("total_threePA") / gamesPlayed);
                    careerStats.setFTM(rs.getDouble("total_FTM") / gamesPlayed);
                    careerStats.setFTA(rs.getDouble("total_FTA") / gamesPlayed);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                connectionPool.releaseConnection(conn);
            }
        }
        return careerStats;
    }
}
