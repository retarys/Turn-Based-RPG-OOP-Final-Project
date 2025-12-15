import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public abstract class Entity implements Combatant, EntityStatus{
    private String name;
    private int healthPoints; 
    private int baseDamage;
    private int defense;
    ImageIcon image;
    private Weapon weapon;
    GenerateRandom random = new GenerateRandom();
    private List<BattleListener> listeners = new ArrayList<>();
    private List<MessageListeners> messageListeners = new ArrayList<>();

    
    Entity(String name, int hp, int atk, int dfn, String imagePath){
        setName(name);
        setHp(hp);
        setDamage(atk);
        setDefense(dfn);
        loadImage(imagePath);
    }




    @Override   
    public void take_damage(int damage){
        int final_damage = Math.max(1, (int) (damage * ((double) damage / (damage + this.defense))));
        this.healthPoints -= final_damage;
        notifyMessageListener(this.name + " took " + final_damage + " damage!");
        notifyMessageListener("--------------------------");
        notifyListeners();
    }

    @Override
    public void deal_damage(Entity enemy) {
        boolean crit = random.critChance();
        final double CRIT_MULT = 1.5;

        int damage;
        if (this.weapon == null) {
            damage = this.baseDamage;
        } else {
            damage = this.baseDamage + this.weapon.getAttack();
        }

        if (crit) {
            damage = (int) (damage * CRIT_MULT);
            notifyMessageListener(this.name + " Dealt a Critical Hit!");
        } else {
            notifyMessageListener(this.name + " attacks " + enemy.getName());
        }

        enemy.take_damage(damage);
        notifyListeners();
    }

    final protected void loadImage(String imagePath) {
        try {
            File imageFile = new File(imagePath);
            if (!imageFile.exists()) {
                throw new Exception("Image not found: " + imagePath);
            }
            this.image = (new ImageIcon(imagePath));
        } catch (Exception e) {
            System.err.println("Failed to load image at " + imagePath + ": " + e.getMessage());
            // Fallback to default placeholder
            this.image = (new ImageIcon("Images/defaultpicture.jpg"));
        }
    }

    @Override
    public boolean isAlive(){
        return healthPoints > 0;
    }

    //getters
    @Override
    public String getName(){
        return this.name;
    }
    @Override
    public int getHp(){
        return this.healthPoints;
    }
    @Override
    public int getDamage(){
        return this.baseDamage;
    }
    @Override
    public ImageIcon getImage(){
        return this.image;
    }


    //setters
    final public void setName(String name){
        this.name = name;
    }
    final public void setHp(int hp){
        this.healthPoints = hp;
    }
    final public void setDefense(int dfn){
        this.defense = dfn;
    }
    final public void setDamage(int dmg){
        this.baseDamage = dmg;
    }
    final public void setWeapon(Weapon w){
        this.weapon = w;
    }



    final public void setNewHp(int hp){
        this.healthPoints += hp;
    }
    final public void setNewDefense(int dfn){
        this.defense += dfn;
    }
    final public void setNewDamage(int dmg){
        this.baseDamage += dmg;
    }
    



    public void addListeners(BattleListener listener){
        listeners.add(listener);
    }
    public void addMessageListener(MessageListeners listener){
        messageListeners.add(listener);
    }

    
    protected void notifyListeners(){
        for (BattleListener listener : listeners){
            listener.onHealthChanged(healthPoints);
        }
    }
    protected void notifyMessageListener(String message){
        for (MessageListeners listener : messageListeners){
            listener.messageChanged(message);
        }
    }

    public interface MessageListeners {
        void messageChanged(String m);
    }
    public interface BattleListener {
        void onHealthChanged(int newHP);
    }


}

class Player extends Entity {
    Inventory inventory = new Inventory();

    public Player(String name, int hp, int atk, int dfn, String imagePath){
        super(name, hp, atk, dfn, imagePath);

    }




    public void use_item(Item item){
        if (item instanceof Potion){
            use_potion((Potion) item);
        }
        else if (item instanceof Weapon){
            equip_item((Weapon) item);
        }
    }

    public void equip_item(Weapon item){
        setWeapon(item);
        notifyMessageListener(item.name + " has been equiped!");
    }

    public void use_potion(Potion potion){
        int buff = potion.getBuff();
        int finalhp = this.getHp() + buff;
        this.setNewHp(finalhp);
        notifyMessageListener(this.getName() + " has used "+potion.name+"! and now has "+this.getHp());
        this.inventory.items.remove(this.inventory.items.indexOf(potion));
        notifyListeners();
    }
}



