import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class MyFrame extends JFrame implements ActionListener{
    Player player;
    LevelManager levelManager;
    TeamManager teamManager;
    BattleManager battleManager;
    JButton button1;
    JButton button2;   
    JPanel currentlySelected = null; 
    Enemy targetEnemy;
    JFrame frame;

    MyFrame(Player player,LevelManager levelManager, TeamManager teamManager, BattleManager battleManager){
        this.player = player;
        this.levelManager = levelManager;
        this.teamManager = teamManager;
        this.battleManager = battleManager;
    }

    public void BattleFrame(){

        frame = new JFrame("Custom GUI");
        frame.setSize(1000, 700);
        frame.getContentPane().setBackground(new Color(0x123456));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);

        // Top Panel
        JPanel topPanel = new JPanel();
        topPanel.setBackground(new Color(102, 0, 255)); // Purple
        topPanel.setBounds(0, 0, 1000, 80);
        topPanel.setLayout(null);
        frame.add(topPanel);

        // Text Fields on Top
        JLabel textFieldTurn = new JLabel();
        textFieldTurn.setBounds(20, 20, 200, 40);
        textFieldTurn.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textFieldTurn.setForeground(Color.WHITE);
        textFieldTurn.setText("Turn: "+battleManager.getTurn());
        battleManager.addTurnListener(newTurn -> textFieldTurn.setText("Turn: "+battleManager.getTurn()));
        textFieldTurn.setHorizontalAlignment(textFieldTurn.CENTER);
        topPanel.add(textFieldTurn);

        JLabel textFieldLevel = new JLabel();
        textFieldLevel.setBounds(240, 20, 200, 40);
        textFieldLevel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        textFieldLevel.setForeground(Color.WHITE);
        textFieldLevel.setText(battleManager.getLevelName());
        battleManager.addLevelNameListener(newName -> textFieldLevel.setText(newName));
        textFieldLevel.setHorizontalAlignment(textFieldLevel.CENTER);
        topPanel.add(textFieldLevel);

        // Center Panel
        JPanel centerPanel = new JPanel();
        centerPanel.setBounds(240, 100, 520, 400);
        centerPanel.setBackground(new Color(102, 0, 255));
        centerPanel.setLayout(null);
        frame.add(centerPanel);

        // Text Field Container in Center Panel
        JPanel textContainer = new JPanel();
        textContainer.setBounds(100, 50, 300, 350);
        textContainer.setBackground(new Color(153, 102, 255, 150)); // Light purple
        textContainer.setLayout(new BorderLayout());
        centerPanel.add(textContainer);

        // Create the text area
        JTextArea terminal = new JTextArea();
        terminal.setEditable(false); // users can't type in it
        terminal.setLineWrap(true);
        terminal.setWrapStyleWord(true);
        terminal.setBackground(new Color(102, 0, 255));
        terminal.setForeground(Color.WHITE);
        terminal.setFont(new Font("Serif", Font.ITALIC, 16));
        terminal.setLineWrap(true);
        terminal.setWrapStyleWord(true);
        battleManager.addMessageListener(newMessage -> terminal.append("\n"+newMessage)); // a listener to show messages in the terminal

        // Add a scroll pane
        JScrollPane scrollPane = new JScrollPane(terminal);
        textContainer.add(scrollPane, BorderLayout.CENTER);


        // Left and Right Side Panels
        JPanel leftPanel = new JPanel();
        leftPanel.setBounds(20, 100, 200, 400);
        leftPanel.setBackground(new Color(102, 0, 255));
        leftPanel.setLayout(null);
        frame.add(leftPanel);

        JPanel rightPanel = new JPanel();
        rightPanel.setBounds(780, 100, 200, 400);
        rightPanel.setBackground(new Color(102, 0, 255));
        rightPanel.setLayout(null);
        frame.add(rightPanel);

        // player.addMessageListener(Message -> terminal.append("\n"+Message));  // a listenre for player messages
        // Small Image Containers on Left & Right
        // disablePlayerControls(false); // Disable player controls at the start of a new level
        battleManager.addLevelListener(e -> {
            rightPanel.removeAll(); // Clear old enemy panels including glass panels
            rightPanel.revalidate();
            rightPanel.repaint();
            for (int i = 0; i < 3; i++) {

                Enemy currentEnemy = levelManager.getCurrentLevel().getEnemies()[i];
                Entity currentTeammate = teamManager.getCompanyArray()[i];

                currentEnemy.addMessageListener(Message -> terminal.append("\n"+Message)); // a listener for each of the enemies
                currentTeammate.addMessageListener(Message -> terminal.append("\n"+Message)); // a listener for each teammate
                

                //get the image and resize it
                JLabel imageleft = new JLabel(currentTeammate.getImage());
                JPanel imagePanelLeft = new JPanel();

                //create label for each imagebox
                JLabel leftLabel = new JLabel();
                leftLabel.setText("Name: "+currentTeammate.getName() +"HP: "+currentTeammate.getHp());
                currentTeammate.addListeners(newHP -> leftLabel.setText("Name: "+currentTeammate.getName() +"HP: "+newHP));
                leftLabel.setBounds(0, 0 ,100,20);
                imagePanelLeft.setBounds(40, 20 + (i * 120), 120, 120);
                leftPanel.add(imagePanelLeft);
                imagePanelLeft.add(leftLabel);
                imagePanelLeft.add(imageleft);


                JLabel imageright = new JLabel(currentEnemy.getImage());       
                JPanel imagePanelRight = new JPanel();
                imagePanelRight.putClientProperty("target", currentEnemy);
                imagePanelRight.addMouseListener(new MouseAdapter(){
                    @Override
                    public void mouseClicked(MouseEvent e){

                    
                        if (currentlySelected != null) {
                            currentlySelected.setBorder(BorderFactory.createLineBorder(Color.white));
                        }

                        // Set border on the newly selected panel
                        JPanel clickedPanel = (JPanel) e.getSource();
                        clickedPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                        currentlySelected = clickedPanel;

                        targetEnemy = (Enemy) ((JPanel) e.getSource()).getClientProperty("target");
                        disablePlayerControls(true);
                    }
                });
                JLabel rightLabel = new JLabel();
                rightLabel.setText("Name: "+currentEnemy.getName() +"HP: "+currentEnemy.getHp());
                currentEnemy.addListeners(newHP -> {
                    if (newHP <= 0 ){
                        rightLabel.setText("Name: "+currentEnemy.getName() +"HP: "+newHP);
                        updateEnemyIconDead(rightLabel, imagePanelRight);
                    }
                    else{
                        rightLabel.setText("Name: "+currentEnemy.getName() +"HP: "+newHP);
                        updateEnemyIconAlive(rightLabel, imagePanelRight);

                    }
                    rightLabel.setText("Name: "+currentEnemy.getName() +"HP: "+newHP);
                });
                
                rightLabel.setBounds(0,0,100,20);
                imagePanelRight.setBounds(40, 20 + (i * 120), 120, 120);
                rightPanel.add(imagePanelRight);
                imagePanelRight.add(rightLabel);
                imagePanelRight.add(imageright);
            }
        });


        // Bottom Panel
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(102, 0, 255));
        bottomPanel.setBounds(0, 530, 1000, 100);
        bottomPanel.setLayout(null);
        frame.add(bottomPanel);

        // Buttons in Bottom Panel
        button1 = new JButton();
        button1.setBounds(100, 30, 120, 50);
        button1.setBackground(Color.BLACK);
        button1.setText("Attack");
        button1.addActionListener(this);
        button1.setEnabled(false);
        bottomPanel.add(button1);

        button2 = new JButton();
        button2.setBounds(280, 30, 120, 50);
        button2.setBackground(Color.BLACK);
        button2.setText("Inventory");
        button2.addActionListener(this);
        bottomPanel.add(button2);

        // Show the frame
        frame.setVisible(true);
    }
    public void showEnemies(){

    }
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getSource()==button1){
            player.deal_damage(targetEnemy);
            disablePlayerControls(false);
            battleManager.currentState = GameState.CHARACTER_TURN;
            battleManager.advanceBattle();
        }
        else if(e.getSource()==button2){
             new InventoryWiondow(player).inventoryWiondow();
        }
    }

    public void disablePlayerControls(boolean enable){
        if (!enable){
            button1.setEnabled(false);
            currentlySelected.setBorder(BorderFactory.createLineBorder(Color.white));
            currentlySelected = null;
            
        }
        else{
            button1.setEnabled(true);
        }
    }

    public void showEndScreen(){
        GameComplete end = new GameComplete();
        frame.dispose();
        end.endWindow();
    }
    public void updateEnemyIconDead(JLabel rightLabel, JPanel imagePanelRight){

        Component[] com = imagePanelRight.getComponents();
        for (Component com1 : com) {
            com1.setEnabled(false);
        }
        imagePanelRight.setEnabled(false);
        JPanel glass = new JPanel();
        glass.setOpaque(false); // Fully transparent
        glass.addMouseListener(new MouseAdapter() {}); // Consume clicks
        glass.setBounds(imagePanelRight.getBounds());
        imagePanelRight.getParent().add(glass, 0); // Add above panel
        imagePanelRight.getParent().repaint();
        imagePanelRight.setBackground(Color.LIGHT_GRAY);
        imagePanelRight.setBorder(BorderFactory.createLineBorder(Color.RED));
    }
    public void updateEnemyIconAlive(JLabel rightLabel, JPanel imagePanelRight){
        for (Component comp : imagePanelRight.getComponents()) {
            comp.setEnabled(true);
        }
        imagePanelRight.setEnabled(true);
    
        // Remove the glass pane (look for a transparent panel above it)
        Container parent = imagePanelRight.getParent();
        Component[] comps = parent.getComponents();
        
        for (Component comp : comps) {
            if (comp instanceof JPanel && comp != imagePanelRight) {
                JPanel panel = (JPanel) comp;
                if (!panel.isOpaque() && panel.getMouseListeners().length > 0) {
                    parent.remove(panel); 
                    break;
                }
            }
        }
    
        
        parent.revalidate();
        parent.repaint();
    }
}

class InventoryWiondow extends JFrame implements ActionListener{
    Player player;
    public static Item selectedItem;
    static JPanel currentlySelected = null;
    JFrame frame;
    JButton button1;
    JButton button2;

    public InventoryWiondow(Player player){
        this.player = player;
    }


    public void inventoryWiondow(){
        frame = new JFrame();
        frame.setSize(700,700);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLUE);
        frame.setLayout(null);
        frame.setResizable(false);

        ArrayList<Item> items = player.inventory.items;

        for (int i = 0; i < items.size(); i++){
            Item currenItem = items.get(i);

            JPanel itemBox = new JPanel();
            itemBox.setBounds(0, 0 + (i * 100),300,100);
            itemBox.setBackground(Color.BLACK);
            itemBox.setBorder(BorderFactory.createLineBorder(Color.WHITE));
            itemBox.setLayout(new FlowLayout());
            frame.add(itemBox);
            JLabel icon = new JLabel(items.get(i).getImage());
            icon.setBounds(0,0 + (i * 100),100,100);
            itemBox.add(icon);
            JLabel label = new JLabel();
            label.setText(items.get(i).name);
            label.setBounds(100,0 + (i * 100),150,20);
            itemBox.add(label);
            // JButton button = new JButton(items.get(i).name);
            // button.setBounds(200, 0 + (i * 100), 80,40);
            // button.addActionListener(this);
            // itemBox.add(button);
            itemBox.putClientProperty("item", currenItem);
            itemBox.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e){
                    
                    if (currentlySelected != null) {
                        currentlySelected.setBorder(BorderFactory.createLineBorder(Color.white));
                    }

                    // Set border on the newly selected panel
                    JPanel clickedPanel = (JPanel) e.getSource();
                    clickedPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));
                    currentlySelected = clickedPanel;

                    selectedItem = (Item) ((JPanel) e.getSource()).getClientProperty("item");
                    System.out.println("You selected: " + selectedItem);
                    
                }
            });

        }
        button1 = new JButton("Use item");
        button1.setBounds(0,500,80,40);
        button1.addActionListener(this);
        frame.add(button1);
        button2 = new JButton("go back");
        button2.setBounds(100,500,80,40);
        button2.addActionListener(this);
        frame.add(button2);
        frame.setVisible(true);
    }
    @Override
    public void actionPerformed(ActionEvent e){
        if (e.getSource()==button1){
            player.use_item(selectedItem);
            frame.dispose();
        }
        else{
            frame.dispose();
        }
    }

}

class GameComplete extends JFrame{
    JFrame frame;


    public void endWindow(){
        frame = new JFrame();
        frame.setSize(400,400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(new Color(0x123456));
        frame.setLayout(null);
        frame.setResizable(false);

        JLabel label = new JLabel();
        label.setBounds(50,50,400,200);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Serif", Font.ITALIC, 18));
        label.setText("Game Over!  Thank you for playing!");
        frame.add(label);

        frame.setVisible(true);
    }
}

class MainMenu extends JFrame {
    MyFrame myFrame;
    BattleManager battleManager;
    TeamManager teamManager;
    LevelManager levelManager;
    Character[] characters;
    ArrayList<Character> team = new ArrayList<>();
    Character selectedCharacter;

    public MainMenu(BattleManager battleManager, LevelManager levelManager, TeamManager teamManager, Character[] characters){
        this.battleManager = battleManager;
        this.levelManager = levelManager;
        this.teamManager = teamManager;
        this.characters = characters;
    }

    public void menuWindow() throws ArrayItemLimitException{
        setTitle("Game Main Menu");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setResizable(false);

        // Top panel
        JPanel topPanel = new JPanel();
        topPanel.setLayout(null);
        topPanel.setBackground(new Color(140, 82, 255));
        topPanel.setBounds(0, 0, 900, 80);
        add(topPanel);

        // Top left Label
        JPanel topLeftPanel = new JPanel();
        JLabel topLeftLabel = new JLabel(); 
        topLeftPanel.setBounds(20, 20, 150, 40);
        topLeftPanel.setBackground(Color.BLACK);
        topLeftLabel.setForeground(Color.WHITE);
        topLeftLabel.setText("Choose 2 Characters");
        topLeftPanel.add(topLeftLabel);
        topPanel.add(topLeftPanel);

        // Top middle label container
        JPanel midLabelPanel = new JPanel();
        JLabel midLabel = new JLabel();
        midLabelPanel.setBounds(300, 20, 300, 40);
        midLabelPanel.setBackground(Color.BLACK);
        midLabel.setForeground(Color.WHITE);
        midLabel.setText("Welcome! Prepare for Battle!");
        midLabelPanel.add(midLabel);
        topPanel.add(midLabelPanel);

        // Top right Button
        JButton goButton = new JButton();
        goButton.setBounds(750, 20, 120, 40);
        goButton.setBackground(Color.BLACK);
        goButton.setText("Start Battle!");
        goButton.setForeground(Color.WHITE);
        goButton.addActionListener(e -> {
            teamManager.setTeam(getTeam());
            myFrame.BattleFrame();
            battleManager.startBattle();
            dispose();
        });
        topPanel.add(goButton);

        // Left section (character images and descriptions)
        JPanel leftSection = new JPanel();
        leftSection.setLayout(null);
        leftSection.setBounds(0, 80, 450, 500);
        leftSection.setBackground(new Color(140, 82, 255));
        add(leftSection);

  

        for (int i = 0; i < 4; i++) {
            Character currentCharacter = characters[i];
            JLabel characterIcon = new JLabel(currentCharacter.getImage());
            JPanel imgBox = new JPanel();
            imgBox.setBounds(20, 20 + i * 110, 100, 100);
            imgBox.setBackground(Color.decode("#8C52FF"));
            imgBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            imgBox.putClientProperty("character", currentCharacter);

            imgBox.addMouseListener(new MouseAdapter() {

                @Override
                public void mouseClicked(MouseEvent e){
                    
                    JPanel clickedPanel = (JPanel) e.getSource();
                    clickedPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW, 3));   
                        
                    selectedCharacter = (Character) ((JPanel) e.getSource()).getClientProperty("character");
                    team.add(selectedCharacter);
                    
                    if ( team.size() > 2){
                        try {
                            throw new ArrayItemLimitException();    
                        } catch (ArrayItemLimitException ex) {
                            System.out.println(ex.getMessage());
                            team.remove(selectedCharacter);
                            clickedPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));   
                        }
                    }
                    
                }
            
            });
            imgBox.add(characterIcon);


            leftSection.add(imgBox);

            JPanel descBox = new JPanel();
            JLabel description = new JLabel();

            JTextArea descriptionArea = new JTextArea();
            descriptionArea.setText(currentCharacter.getDescription());
            descriptionArea.setLineWrap(true);         
            descriptionArea.setWrapStyleWord(true);      
            descriptionArea.setEditable(false);          
            descriptionArea.setOpaque(false);
            descriptionArea.setPreferredSize(new Dimension(280, 60));


            description.setText(currentCharacter.getDescription());
            description.setBounds(0,0, 200,60);
            descBox.add(descriptionArea);
            descBox.setBounds(110, 20 + i * 110, 300, 60);
            descBox.setBackground(Color.decode("#8C52FF"));
            descBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            leftSection.add(descBox);
        }

        // Bottom button in left section
        JButton selectButton = new JButton("Select");
        selectButton.setBounds(150, 430, 120, 40);
        selectButton.addActionListener(e -> {
            leftSection.setEnabled(false);
            for (Component c : leftSection.getComponents()){
                c.setEnabled(false);
            }
            JPanel glass = new JPanel();
            glass.setOpaque(false); // Fully transparent
            glass.addMouseListener(new MouseAdapter() {}); // Consume clicks
            glass.setBounds(leftSection.getBounds());
            leftSection.getParent().add(glass, 0); // Add above panel
            leftSection.getParent().repaint();
            leftSection.setBackground(Color.LIGHT_GRAY);
            leftSection.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        });
        leftSection.add(selectButton);

        // Right section (available levels list)
        JPanel rightSection = new JPanel();
        rightSection.setLayout(null);
        rightSection.setBounds(450, 80, 450, 500);
        rightSection.setBackground(new Color(140, 82, 255));
        add(rightSection);

        ArrayList<Level> levels = levelManager.getLevels();
        for (int i = 0; i < 3; i++) {
            Level currentLevel = levels.get(i);

            JLabel levelLabel = new JLabel();
            levelLabel.setText(currentLevel.getName());
            levelLabel.setForeground(Color.WHITE);


            JPanel levelBox = new JPanel();
            levelBox.setBounds(100, 20 + i * 80, 250, 50);
            levelBox.setBackground(Color.decode("#8C52FF"));
            levelBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            levelBox.add(levelLabel);
            rightSection.add(levelBox);
        }
        setVisible(true);
    }
    public Character[] getTeam(){
        return team.toArray(new Character[0]);
    }

}

