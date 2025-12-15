
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GenerateRandom {
    private Random random = new Random();
    
    
    public boolean critChance(){
        boolean val = random.nextInt(10)==0;
        return val;
    }

    public boolean randChoice(){
        boolean val = random.nextBoolean();
        return val;
    }

    public int getRandomTarget(Enemy[] enemies){
        List<Entity> enemiesAlive = new ArrayList<>();
        for (Entity c : enemies){
            if (c.isAlive()){
                enemiesAlive.add(c);
            }
        }
        int rnd = random.nextInt(enemiesAlive.size());
        return rnd;
    }
    public int getRandomTarget(Character[] characters){
        List<Entity> charactersAlive = new ArrayList<>();
        for (Entity c : characters){
            if (c.isAlive()){
                charactersAlive.add(c);
            }
        }
        int rnd = random.nextInt(charactersAlive.size());
        return rnd;
    }
    public int getRandomAliveTarget(Entity[] entities){
        int randomIndex = random.nextInt(entities.length);
        if(entities[randomIndex].isAlive()){
            return randomIndex;
        }
        else{
            return getRandomAliveTarget(entities);  
        }
    }
}
