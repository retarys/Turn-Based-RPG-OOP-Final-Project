
import java.util.ArrayList;

public class LevelManager {
    private ArrayList<Level> levels = new ArrayList<>();
    private int currentLevelIndex;
    
    
    public void addLevel(Level level){
        this.levels.add(level);
    }
    
    public Level getCurrentLevel(){
        if (currentLevelIndex < levels.size()){
            return levels.get(currentLevelIndex);
        }
        else
        return null;
    }
    
    public LevelManager(){
        this.currentLevelIndex = 0;  
    }
    
    public Item getReward(){
        return getCurrentLevel().getReward();
    }
    
    
    public ArrayList<Level> getLevels(){
        return this.levels;
    }
    
    public Enemy[] getEnemies(){
        return levels.get(currentLevelIndex).getEnemies();
    }
    public boolean canAccsessLevel(int levelNumber){
        return levelNumber <= currentLevelIndex + 1;
    }
    public boolean completeCurrentLevel(){
        if (currentLevelIndex < levels.size()){
            levels.get(currentLevelIndex).completeLevel();
            currentLevelIndex++;
            return true;
        }
        return false;
    }

}
