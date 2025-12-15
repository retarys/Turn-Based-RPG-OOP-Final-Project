public class App {
    public static void main(String[] args) throws Exception {
        
        Player player = new Player("Knight",100,60,100,"Images/Knight_0-ezgif.com-resize.gif");

        Healer healer = new Healer("Healer",100,60,100,"Heals the whole team at once, keeping everyone alive during tough fights.","Images/Priestess_0-ezgif.com-resize.gif");
        Paladin paladin = new Paladin("Paladin",100,60,150,"Raises the team's defense, helping everyone take less damage.","Images/Paladin_0-ezgif.com-resize.gif");
        Mage mage = new Mage("Mage",100,60,80,"Uses magic to hit all enemies in one turn with strong attacks.","Images/Witch_0-ezgif.com-resize.gif");
        Barbarian barbarian = new Barbarian("Barbarian",150,70,50,"Gets stronger by losing some of his own health to deal more damage.","Images/Berserker_0-ezgif.com-resize.gif");

        
        
        Weapon sword = new Weapon("Long Sword",50,"Images/sword.png");
        Potion heal = new Potion("Healing Potion",30,"Images/healin potion.png");
        Weapon sword2 = new Weapon("Great Diomond Sword",75,"Images/diamond sword.png");
        Potion heal2 = new Potion("Healing Potion",30,"Images/healin potion.png");
        

        player.inventory.add_item(sword);
        player.inventory.add_item(heal);
        player.inventory.add_item(heal2);
        
        Enemy goblin = new Enemy("Goblin",100,60,100,"Images/goblin-ezgif.com-rotate.gif");
        
        Enemy orc = new Enemy("Orc",100,60,100,"Images/goblin-ezgif.com-rotate.gif");

        Enemy bug = new Enemy("Bug",100,60,100,"Images/goblin-ezgif.com-rotate.gif");

        Enemy goblin2 = new Enemy("Goblin",100,60,100,"Images/goblin-ezgif.com-rotate.gif");
        Enemy goblin3 = new Enemy("Goblin",100,60,100,"Images/goblin-ezgif.com-rotate.gif");
        Enemy goblin4 = new Enemy("Goblin",100,60,100,"Images/goblin-ezgif.com-rotate.gif");

        Enemy goblin5 = new Enemy("Goblin",100,60,100,"Images/goblin-ezgif.com-rotate.gif");
        Enemy goblin6 = new Enemy("Goblin",100,60,100,"Images/goblin-ezgif.com-rotate.gif");
        Enemy goblin7 = new Enemy("Goblin",100,60,100,"Images/goblin-ezgif.com-rotate.gif");
        
        LevelManager levelManager = new LevelManager();
        Enemy[] enemies1 = {goblin,orc,bug};
        Enemy[] enemies2 = {goblin2,goblin3,goblin4};
        Enemy[] enemies3 = {goblin5,goblin6,goblin7};
        
        Level dungeon = new Level(2, "The Dungeon",enemies2, heal2);
        Level castle = new Level(3, "Castle",enemies3,sword2);
        Level forest = new Level(1,"Dark Forest",enemies1, sword2);

        levelManager.addLevel(forest);
        levelManager.addLevel(dungeon);
        levelManager.addLevel(castle);


        Character[] team = {mage,healer};
        TeamManager teamManager = new TeamManager(team,player);

        Character[] characters = {healer,mage,paladin,barbarian};

        BattleManager battle = new BattleManager(player,levelManager, teamManager);
        MyFrame NewFrame = new MyFrame(player,levelManager, teamManager, battle);

        for (Character character : characters) {
            character.setManagers(teamManager, battle);
        }

        battle.frame = NewFrame;


        // NewFrame.BattleFrame();
        // battle.startBattle();

        MainMenu menu = new MainMenu(battle, levelManager, teamManager, characters);
        menu.myFrame = NewFrame;
        menu.menuWindow();
        
    }
}
