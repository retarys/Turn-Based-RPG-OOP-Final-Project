
import javax.swing.ImageIcon;

public interface EntityStatus {
    boolean isAlive();
    String getName();
    int getHp();
    int getDamage();
    ImageIcon getImage();
}
