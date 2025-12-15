

class Level {
    private int levelNumber;
    private String name;
    private boolean completed;
    private Enemy[] enemies;
    private Item reward;

    public Level(int levelNumber, String name, Enemy[] enemies, Item reward) {
        this.levelNumber = levelNumber;
        this.name = name;
        this.completed = false; 
        this.enemies = enemies;
        this.reward = reward;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public String getName() {
        return name;
    }

    public boolean isCompleted() {
        return completed;
    }

    public Enemy[] getEnemies(){
        return enemies;
    }

    public Item getReward(){
        return reward;
    }

    public void completeLevel() {
        this.completed = true;
        System.out.println("Level " + levelNumber + " (" + name + ") completed!");
    }
}
