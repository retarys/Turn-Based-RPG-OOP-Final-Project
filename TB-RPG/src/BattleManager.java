import java.util.ArrayList;
import java.util.List;
import javax.swing.Timer;



public class BattleManager {
    Player player;
    private Enemy[] enemies;
    private Character[] team;
    int turn = 1;
    LevelManager levelManager;
    TeamManager teamManager;
    GenerateRandom rand = new GenerateRandom();
    private final List<Turnlistener> turnListeners = new ArrayList<>();
    private final List<BattleMessagesListener> messageListeners = new ArrayList<>();
    private final List<EnemyAliveListener> enemyListeners = new ArrayList<>();
    private final List<LevelCompleteListener> levelListeners = new ArrayList<>();
    private final List<LevelNameListener> levelNameListeners = new ArrayList<>();

    public GameState currentState = GameState.START_LEVEL;
    MyFrame frame;


    BattleManager(Player player, LevelManager levelManager, TeamManager teamManager){
        this.player = player;
        this.levelManager = levelManager;
        this.teamManager = teamManager;
    }

    public void startBattle(){
        advanceBattle();   
    }


    public void advanceBattle(){
        switch (currentState) {
            case START_LEVEL:
                Level level = levelManager.getCurrentLevel();
                if (level != null) {
                    notifyLevelListeners(true);
                    notifyLevelNameListeners(level.getName());
                    notifyMessageListener("You are now playing - (" + level.getName() + ")");
                    notifyMessageListener("Battle has started!");
                    getEnemies();
                    getTeam();
                    currentState = GameState.PLAYER_TURN;
                    advanceBattle();
                } else {
                    currentState = GameState.GAME_COMPLETE;
                    advanceBattle();
                }
                break;
            case PLAYER_TURN:
                if (player.isAlive()) {
                    notifyMessageListener("\n--- Turn " + turn + " ---");
                    notifyMessageListener("------- It's your turn! -------");
                    playerTurn();
                } else {
                    currentState = GameState.CHECK_WINNER;
                    advanceBattle();
                }
                break;
            case CHARACTER_TURN:
                if (charactersAlive()){
                    startNextTurnWithDelay(1500, () -> {
                        characterTurn();
                    });
                }
                currentState = GameState.ENEMY_TURN;
                startNextTurnWithDelay(2000, () -> {
                    advanceBattle();
                });
                break;
            case ENEMY_TURN:
                if (enemiesAlive()){
                    startNextTurnWithDelay(1500, () -> {
                        enemyTurn();
                    });
                }
                currentState = GameState.CHECK_WINNER;
                startNextTurnWithDelay(2000, () -> {
                    advanceBattle();
                });
                break;
            case CHECK_WINNER:
                checkWinner();
                break;
            case GAME_COMPLETE:
                notifyMessageListener("You have completed all Levels!");
                frame.showEndScreen();
                break;
        }
    }


    private void startNextTurnWithDelay(int delayMillis, Runnable nextTurnAction) {
        Timer timer = new Timer(delayMillis, e -> {
            nextTurnAction.run();  // Execute next turn logic
        });
        timer.setRepeats(false); // One-time execution
        timer.start();
    }



    //Listeners
    public interface Turnlistener {
        void turnChanged(int newTurn);
    }
    public interface LevelNameListener {
        void levelChanged(String newLevelName);
    }
    public interface BattleMessagesListener {
        void messageChanged(String newMessage);
    }
    public interface EnemyAliveListener {
        void enemyAlive(boolean isAlive);
    }
    public interface LevelCompleteListener {
        void onLevelCompleted(boolean  isComplete);
    }

    //Add Listeners
    public void addTurnListener(Turnlistener l) {
        turnListeners.add(l);
    }
    public void addMessageListener(BattleMessagesListener m){
        messageListeners.add(m);
    }
    public void addEnemyListener(EnemyAliveListener e){
        enemyListeners.add(e);
    }
    public void addLevelListener(LevelCompleteListener e){
        levelListeners.add(e);
    }
    public void addLevelNameListener(LevelNameListener n){
        levelNameListeners.add(n);
    }
    

    //Notifiers
    public void notifyTurnListener(){
        for (Turnlistener t : turnListeners){
            t.turnChanged(turn);
        }
    }
    public void notifyMessageListener(String newMessage){
        for (BattleMessagesListener m : messageListeners){
            m.messageChanged(newMessage);
        }
    }
    public void notifyEnemyListener(){
        for (EnemyAliveListener e : enemyListeners){
            e.enemyAlive(enemiesAlive());
        }
    }
    public void notifyLevelListeners(boolean isComplete){
        for (LevelCompleteListener e : levelListeners){
            e.onLevelCompleted(isComplete);
        }
    }
    public void notifyLevelNameListeners(String newName){
        for (LevelNameListener e : levelNameListeners){
            e.levelChanged(newName);
        }
    }


    public int getTurn(){
        return this.turn;
    }
    public String getLevelName(){
        return levelManager.getCurrentLevel().getName();
    }

    public void getEnemies(){
        this.enemies = levelManager.getCurrentLevel().getEnemies();
    }
    public Enemy[] getEnemiesArray(){
        return this.enemies;
    }
    public void getTeam(){
        this.team = teamManager.getTeam();
    }
    

    private void playerTurn(){
        // frame.button1.setEnabled(true);
        // frame.button2.setEnabled(true);    
    }

    private void enemyTurn(){
        notifyMessageListener("------- Enemies' Turn! -------");
        for (Enemy enemy : enemies){
            if (enemy.isAlive()){
                if (rand.randChoice()){
                    enemy.deal_damage(player);
                }
                else{
                    enemy.deal_damage(team[rand.getRandomTarget(team)]);
                }
            }
        }
    }

    private boolean enemiesAlive(){
        for (Enemy enemy : enemies){
            if (enemy.isAlive()) return true;
        }
        return false;
    }

    private boolean charactersAlive(){
        for (Character character : team){
            if (character.isAlive()) return true;
        }
        return false;
    }

    private void characterTurn(){
        notifyMessageListener("------- Characters Turn! -------");
        for (Character character : team){
            if (character.isAlive()){
                if (rand.randChoice()){
                    character.ability();
                }
                else{
                    character.deal_damage(enemies[rand.getRandomTarget(enemies)]);
                }
            }
        }
    }

    private void checkWinner(){
        if (!player.isAlive()){
            notifyMessageListener("You have been defeated");
            frame.showEndScreen();

        }
        else if(!enemiesAlive()) {
            player.inventory.add_item(levelManager.getReward());
            notifyMessageListener("You have recivied - ("+ levelManager.getReward().name+") check your inventory");
            levelManager.completeCurrentLevel();
            notifyMessageListener("You have won!");
            currentState = GameState.START_LEVEL;
            advanceBattle();
        }
        else if(!enemiesAlive() && levelManager.getCurrentLevel()==null){
            currentState = GameState.GAME_COMPLETE;
            advanceBattle();
        }
        else {
            turn++;
            notifyTurnListener();
            getEnemies();
            currentState = GameState.PLAYER_TURN;
            advanceBattle();
        }
    }

}
