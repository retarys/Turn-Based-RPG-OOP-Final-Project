class Character extends Entity{
    private String description;
    private TeamManager teamManager;
    private BattleManager battleManager;


    Character(String name, int hp, int atk, int dfn, String desc, String imagePath){
        super(name, hp, atk, dfn, imagePath);
        setDescription(desc);
    }

    
    public void ability(){};

    public Entity[] getTeam(){
        return teamManager.getCompanyArray();
    }
    public Entity getPlayer(){
        return this;
    }
    public Enemy[] getEnemies(){
        return battleManager.getEnemiesArray();
    }
    public String getDescription(){
        return this.description;
    }
    public BattleManager getBattleManager(){
        return this.battleManager;
    }
    public void setManagers(TeamManager t, BattleManager b){
        this.teamManager = t;
        this.battleManager = b;
    }
    private  void setDescription(String desc){
        this.description = desc;
    }
}

class Healer extends Character{

    public Healer(String name, int hp, int atk, int dfn, String desc, String imagePath){
        super(name, hp, atk, dfn, desc, imagePath);
    }

    @Override
    public void ability(){
        Entity[] team = getTeam();
        for (Entity teammate : team) {
            if (teammate.isAlive()) {
                teammate.setNewHp(30);
                teammate.notifyListeners();
                getBattleManager().notifyMessageListener(teammate.getName() + " gained 30 HP from Healer's ability!");
            }
        }
        getBattleManager().notifyMessageListener("Healer has used his ability!");
    
    }
}


class Paladin extends Character{

    public Paladin(String name, int hp, int atk, int dfn, String desc, String imagePath){
        super(name, hp, atk, dfn, desc, imagePath);
    }

    @Override
    public void ability(){
        Entity[] team = getTeam();
        for (Entity teammate : team) {
            if (teammate.isAlive()) {
                teammate.setNewDefense(30);
                getBattleManager().notifyMessageListener(teammate.getName() + "'s defense increased by 30 from Paladin's ability!");
            }
        }
        getBattleManager().notifyMessageListener("Paladin has used his ability!");
    }


}

class Mage extends Character{
    
    public Mage(String name, int hp, int atk, int dfn, String desc, String imagePath){
        super(name, hp, atk, dfn, desc, imagePath);
    }

    @Override
    public void ability(){
        Enemy[] enemies = getEnemies();
        for (Enemy enemy : enemies) {
            if (enemy.isAlive()) {
                enemy.take_damage(getDamage());
                getBattleManager().notifyMessageListener("Mage attacks " + enemy.getName() + " with ability!");
            }
        }
        getBattleManager().notifyMessageListener("Mage used ability");

    }

}

class Barbarian extends Character{

    public Barbarian(String name, int hp, int atk, int dfn, String desc, String imagePath){
        super(name, hp, atk, dfn, desc, imagePath);
    }

    @Override
    public void ability(){
        this.setNewHp(40);
        this.setNewDamage(50);
    }
}


