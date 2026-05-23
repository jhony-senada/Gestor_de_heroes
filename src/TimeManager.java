import javax.swing.Timer;
public class TimeManager {
    private Timer missionTimer;
    private Timer recoveryTimer;
    private Timer clockTimer;
    private MissionPQ missionPQ;
    private MissionGenerator missionGen;
    private HeroLinkedList heroRoster;

    private Runnable updateUICallback;
    
    private int gameHours = 10;
    private int gameMinutes = 55;

    public TimeManager(MissionPQ queue, MissionGenerator generator, HeroLinkedList roster, Runnable updateUICallback){
        this.missionPQ = queue;
        this.missionGen = generator;
        this.heroRoster = roster;
        this.updateUICallback = updateUICallback;
        setupTimers();
    }
    private void setupTimers(){
        missionTimer = new Timer(10000, e->{
            Mission nuevaMission=missionGen.generateMission();
            missionPQ.enqueue(nuevaMission);
            if (updateUICallback != null) {
                updateUICallback.run();
            }
        });
        recoveryTimer = new Timer(15000, e -> {
            boolean someoneRecovered = false;
            
            // Recorremos nuestra lista enlazada personalizada
            for (int i = 0; i < heroRoster.size(); i++) {
                Hero hero = heroRoster.get(i);
                if (!hero.isAvailable()) {
                    hero.setAvailable(true);
                    someoneRecovered = true;
                }
            }
            // Si alguien se recuperó, actualizamos la UI (ej. para volver a habilitar botones)
            if (someoneRecovered && updateUICallback != null) {
                updateUICallback.run();
            }
        });

        clockTimer = new Timer(5000, e -> {
            gameMinutes++;
            if (gameMinutes >= 60) {
                gameMinutes = 0;
                gameHours++;
                if (gameHours >= 24) {
                    gameHours = 0; // Reinicio a medianoche
                }
            }
            if (updateUICallback != null) {
                updateUICallback.run();
            }
        });
    }
    public String getFormattedTime() {
        String amPm = (gameHours < 12) ? "AM" : "PM";
        int displayHours = (gameHours == 0 || gameHours == 12) ? 12 : gameHours % 12;
        return String.format(" %02d:%02d %s", displayHours, gameMinutes, amPm);
    }

    public void startGameTime(){
        missionTimer.start();
        recoveryTimer.start();
        clockTimer.start();
    }
    public void pauseGameTime(){
        missionTimer.stop();
        recoveryTimer.stop();
        clockTimer.stop();
    }
}
