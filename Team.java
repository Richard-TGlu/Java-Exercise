public class Team {
    private int team_id;
    private String name;
    private String city;
    private int foundationYear;

    public Team(){
        this.team_id = 0;
        this.name = "Team";
        this.city = "all";
        this.foundationYear = 0;
    }

    public int getTeamId() {
        return team_id;
    }
    public void setTeamId(int team_id) {
        this.team_id = team_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public int getFoundationYear() {
        return foundationYear;
    }
    public void setFoundationYear(int foundationYear) {
        this.foundationYear = foundationYear;
    }
    
    
    @Override
    public String toString() {
        return getCity() + "  " + getName();
    }
}
