public class Player {
    private int player_id;
    private int team_id;
    private String name;
    private int number;
    private Position position;
    private int age;
    private int height;
    private int weight;
    
    
    public enum Position {PG,SG,SF,PF,C};

    public Player(){
    }

    public int getPlayerID(){
        return this.player_id;
    }
    public void setPlayerID(int player_id){
        this.player_id = player_id;
    }
    
    public int getTeamID(){
        return this.team_id;
    }
    public void setTeamID(int team_id){
        this.team_id = team_id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getNumber(){
        return this.number;
    }
    public void setNumber(int number){
        this.number = number;
    }

    public String getPosition(){
        if(position == Position.PG) return "PG";
        else if(position == Position.SG) return "SG";
        else if(position == Position.SF) return "SF";
        else if(position == Position.PF) return "PF";
        else return "C";
    }
    
    public void setPosition(String positionCode){
        switch (positionCode) {
            case "PG":
                this.position = Position.PG;
                break;
            case "SG":
                this.position = Position.SG;
                break;
            case "SF":
                this.position = Position.SF;
                break;
            case "PF":
                this.position = Position.PF;
                break;
            case "C":
                this.position = Position.C;
                break;
            default:
                break;
        }
    }

    public int getAge(){
        return this.age;
    }
    public void setAge(int age){
        this.age = age;
    }

    public int getHeight(){
        return this.height;
    }
    public void setHeight(int height){
        this.height = height;
    }

    public int getWeight(){
        return this.weight;
    }
    public void setWeight(int weight){
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Player{" +
                "player_id=" + player_id +
                ", team_id=" + team_id +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", position=" + position +
                ", age=" + age +
                ", height=" + height +
                "cm, weight=" + weight +
                "kg}";
}
}
