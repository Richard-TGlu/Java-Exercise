public class GameStats {
    private int stat_id;
    private int player_id;
    private int game_id;
    
    private int playingTime;
    private int points;
    private int assists;
    private int rebounds;
    private int steals;
    private int blocks;
    private int turnovers;
    private int FGM; 
    private int FGA;
    private int threePM; 
    private int threePA; 
    private int FTM; 
    private int FTA; 
    private int foul;
    

    // Constructor
    public GameStats() {
    }

    // Getters and Setters
    public int getStatId() {
        return stat_id;
    }
    public void setStatId(int stat_id) {
        this.stat_id = stat_id;
    }

    public int getPlayerId() {
        return player_id;
    }
    public void setPlayerId(int player_id) {
        this.player_id = player_id;
    }

    public int getGameId() {
        return game_id;
    }
    public void setGameId(int game_id) {
        this.game_id = game_id;
    }

    public int getPlayingTime() {
        return playingTime;
    }
    public void setPlayingTime(int playingTime) {
        this.playingTime = playingTime;
    }

    public int getPoints() {
        return points;
    }
    public void setPoints(int points) {
        this.points = points;
    }

    public int getAssists() {
        return assists;
    }
    public void setAssists(int assists) {
        this.assists = assists;
    }

    public int getRebounds() {
        return rebounds;
    }
    public void setRebounds(int rebounds) {
        this.rebounds = rebounds;
    }

    public int getSteals() {
        return steals;
    }
    public void setSteals(int steals) {
        this.steals = steals;
    }

    public int getBlocks() {
        return blocks;
    }
    public void setBlocks(int blocks) {
        this.blocks = blocks;
    }

    public int getTurnovers() {
        return turnovers;
    }
    public void setTurnovers(int turnovers) {
        this.turnovers = turnovers;
    }

    public int getFGM() {
        return FGM;
    }
    public void setFGM(int FGM) {
        this.FGM = FGM;
    }

    public int getFGA() {
        return FGA;
    }
    public void setFGA(int FGA) {
        this.FGA = FGA;
    }

    public int getThreePM() {
        return threePM;
    }
    public void setThreePM(int threePM) {
        this.threePM = threePM;
    }

    public int getThreePA() {
        return threePA;
    }
    public void setThreePA(int threePA) {
        this.threePA = threePA;
    }

    public int getFTM() {
        return FTM;
    }
    public void setFTM(int FTM) {
        this.FTM = FTM;
    }

    public int getFTA() {
        return FTA;
    }
    public void setFTA(int FTA) {
        this.FTA = FTA;
    }

    public int getFoul() {
        return foul;
    }
    public void setFoul(int foul) {
        this.foul = foul;
    }
}
