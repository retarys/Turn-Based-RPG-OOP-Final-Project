class TeamManager{
    private Character[] team;
    private Entity player;

    TeamManager(Character[] team, Entity player){
        setTeam(team);
        addPlayer(player);
    }

    public void addPlayer(Entity player){
        this.player = player;
    }
    public Character[] getTeam(){
        return this.team;
    }
    public Entity getPlayer(){
        return this.player;
    }
    public void setTeam(Character[] team){
        this.team = team;
    }


    // public ArrayList<Entity> getCompany(){
    //     ArrayList<Entity> company = new ArrayList();
    //     company.add(player);
    //     for (int i = 0; i<team.length; i++){
    //         company.add(team[i]);
    //     }
    //     return company;
    // }
    public Entity[] getCompanyArray(){
        Entity[] company = new Entity[team.length + 1];
        company[0] = player;
        System.arraycopy(team, 0, company, 1, team.length);
        return company;
    }
}