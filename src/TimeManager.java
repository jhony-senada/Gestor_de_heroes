import javax.swing.Timer;
public class TimeManager {
    private Timer missionTimer;
    private Timer recoveryTimer;
    private MissionPQ missionPQ;
    private MissionGenerator missionGen;
    private HeroLinkedList heroRoster;

    private Runnable updateUICallback;

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
    }
    public void startGameTime(){
        missionTimer.start();
        recoveryTimer.start();
    }
    public void pauseGameTime(){
        missionTimer.stop();
        recoveryTimer.stop();
    }
}
