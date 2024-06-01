import java.sql.Date;

public class Game {
    private int game_id;
    private int team1_id;
    private int team2_id;
    private TeamDAO teamDAO;
    private Team team1;
    private Team team2;
    private int team1_score;
    private int team2_score;
    private Date game_date;

    public Game(){
        teamDAO = new TeamDAO();
    }

    public int getGameId() {
        return game_id;
    }
    public void setGameId(int game_id) {
        this.game_id = game_id;
    }

    public int getTeam1Id() {
        return team1_id;
    }
    public void setTeam1Id(int team1_id) {
        this.team1_id = team1_id;
        this.team1 = teamDAO.getTeamByID(team1_id);
    }

    public int getTeam2Id() {
        return team2_id;
    }
    public void setTeam2Id(int team2_id) {
        this.team2_id = team2_id;
        this.team2 = teamDAO.getTeamByID(team2_id);
    }
    public int getTeam1Score() {
        return team1_score;
    }

    public void setTeam1Score(int team1_score) {
        this.team1_score = team1_score;
    }
    public int getTeam2Score() {
        return team2_score;
    }
    public void setTeam2Score(int team2_score) {
        this.team2_score = team2_score;
    }

    public Date getGameDate() {
        return game_date;
    }
    public void setGameDate(Date game_date) {
        this.game_date = game_date;
    }

    @Override
    public String toString() {
        return game_date + "  " + team1.getName() + " v.s. " + team2.getName();
    }
}
