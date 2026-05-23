import java.util.Random;
public class Mission {
    public String title;
    public String stat1;
    public String stat2;
    public int difficulty;

    public Mission(String title, String stat1, String stat2, int difficulty) {
        this.title = title;
        this.stat1 = stat1;
        this.stat2 = stat2;
        this.difficulty = difficulty;
    }

    // Este método nos servirá para que al imprimir la misión en la interfaz
    // se vea con el formato "[4★] Defender la aldea (DEF, CHA)"
    @Override
    public String toString() {
        return String.format("[%d★] %s (%s, %s)", difficulty, title, stat1, stat2);
    }
}
    class MissionNodo{
        public Mission data;
        public MissionNodo next;

    public MissionNodo(Mission mission) {
        this.data = mission;
        this.next = null;
    }
}
    class MissionPQ{
        private MissionNodo head;
        private int size;

        public MissionPQ(){
            this.head=null;
            this.size=0;
        }
        public void enqueue(Mission nuevaM){
            MissionNodo nuevo = new MissionNodo(nuevaM);
            if(head==null||nuevo.data.difficulty>head.data.difficulty){
                nuevo.next = head;
                head = nuevo;
            }else{
                MissionNodo actual=head;
                while (actual.next!=null && actual.next.data.difficulty>=nuevo.data.difficulty){
                    actual= actual.next;
                }
                nuevo.next=actual.next;
                actual.next=nuevo;
            }
            size++;
        }
        public Mission dequeue(){
            if(head==null){
                return null;
            }
            Mission topPrio = head.data;
            head=head.next;
            size--;
            return topPrio;
        }
        public boolean isEmpty() {
        return head == null;
    }

    public int getSize() {
        return size;
    }
    
    // Método para obtener todas las misiones en texto (Para actualizar tu JTextArea en la interfaz)
    public String getAllMissionsText() {
        if (isEmpty()) {
            return "No hay misiones pendientes.";
        }
        
        StringBuilder sb = new StringBuilder();
        MissionNodo actual = head;
        int index = 1;
        while (actual != null) {
            sb.append(index).append(". ").append(actual.data.toString()).append("\n");
            actual = actual.next;
            index++;
        }
        return sb.toString();
    }
    }
    class MissionGenerator{
        private Random rand;
        //! Final es pa´ que no se pueda modificar en la ejecucion
        private final String[][] actions = {
        {"Defender", "DEF"}, 
        {"Investigar", "INT"}, 
        {"Detener criminales en", "STR"}, 
        {"Infiltrarse en", "AGI"}, 
        {"Negociar la paz en", "CHA"}
    };
    private final String[][] targets = {
        {"la plaza principal", "CHA"}, 
        {"la escena del crimen", "INT"}, 
        {"el banco de la ciudad", "STR"}, 
        {"el barrio peligroso", "AGI"},
        {"palacio nacional", "DEF"}
    };
    public MissionGenerator() {
        rand = new Random();
    }
    public Mission generateMission(){
        String[] action = actions[rand.nextInt(actions.length)];
        String[] target = targets[rand.nextInt(targets.length)];
        
        String title = action[0] + " " + target[0];
        String stat1 = action[1];
        String stat2 = target[1];
        
        int difficulty = rand.nextInt(6) + 1; // Dificultad del 1 al 6
        
        return new Mission(title, stat1, stat2, difficulty);
    }
    }

