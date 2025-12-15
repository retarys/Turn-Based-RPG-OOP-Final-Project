
import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;




class Inventory implements ItemAdder<Item>{
    protected ArrayList<Item> items = new ArrayList<>();
    
    @Override
    public void add_item(Item item){
        this.items.add(item);
    }

    public ArrayList<Item> show_inventory(){
        return this.items;
    }
}



public class Item{
    String name;
    ImageIcon image;

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
    public ImageIcon getImage(){
        return this.image;
    }

}

class Weapon extends Item{
    int attackPower;

    public Weapon(String name, int atk, String imagePath){
        this.name = name;
        this.attackPower = atk;
        loadImage(imagePath);
    }

    public int getAttack(){
        return this.attackPower;
    }
}

class Potion extends Item{
    private int buffFactor;

    public Potion(String name,int buf,  String imagePath){
        this.name = name;
        this.buffFactor = buf;
        loadImage(imagePath);
    }

    public int getBuff(){
        return this.buffFactor;
    }
}