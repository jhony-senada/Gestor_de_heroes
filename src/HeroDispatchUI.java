import javax.swing.*;
import java.awt.*;

public class HeroDispatchUI extends JFrame {
    private HeroManager heroManager;
    private MissionPQ missionQueue;
    private MissionGenerator missionGenerator;
    private TimeManager timeManager;

    private JTextArea missionsArea;
    private JComboBox<Hero> heroSelector; // ¡Ahora guarda objetos Hero directamente!
    private JProgressBar confidenceBar;

    public HeroDispatchUI() {
        heroManager = new HeroManager();
        missionQueue = new MissionPQ();
        missionGenerator = new MissionGenerator();
        Runnable updateUI = ()->{
            missionsArea.setText(missionQueue.getAllMissionsText());
            
            heroSelector.repaint();
        };
        timeManager = new TimeManager(missionQueue, missionGenerator, heroManager.getRoster(), updateUI);

        setTitle("Hero Dispatch Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. CABECERA (Norte) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel timeLabel = new JLabel(" 10:55 AM", SwingConstants.LEFT);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        confidenceBar = new JProgressBar(0, 100);
        confidenceBar.setValue(80); // 80% de confianza
        confidenceBar.setStringPainted(true);
        confidenceBar.setForeground(Color.RED);
        confidenceBar.setPreferredSize(new Dimension(600, 30));
        
        topPanel.add(timeLabel, BorderLayout.WEST);
        topPanel.add(confidenceBar, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // --- 2. CONTENEDOR CENTRAL (Dividido en 2) ---
        JPanel mainCenterPanel = new JPanel(new GridLayout(1, 2, 15, 0)); // 1 fila, 2 columnas

        // IZQUIERDA: Misiones (Priority Queue View)
        JPanel missionsPanel = new JPanel(new BorderLayout());
        missionsPanel.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2));
        
        missionsArea = new JTextArea(missionQueue.getAllMissionsText());
        missionsArea.setEditable(false);
        missionsArea.setFont(new Font("Monospaced", Font.PLAIN, 14)); // Letra tipo consola
        missionsPanel.add(new JScrollPane(missionsArea), BorderLayout.CENTER);

        // DERECHA: Gestión de Héroes
        JPanel heroesPanel = new JPanel(new BorderLayout(0, 10));

        // Derecha - Arriba: ComboBox
        heroSelector = new JComboBox<>();
        heroSelector.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        // Derecha - Centro: Gráfico de Radar
        HeroLinkedList roster = heroManager.getRoster();
        for (int i = 0; i < roster.size(); i++) {
            heroSelector.addItem(roster.get(i));
        }
        heroesPanel.add(heroSelector, BorderLayout.NORTH);
        RadarChartPanel radarChartPanel = new RadarChartPanel();
        radarChartPanel.setBorder(BorderFactory.createLineBorder(new Color(0,150,150)));
        
        if(roster.size()>0){
            radarChartPanel.setHero(roster.get(0));
        }
        heroesPanel.add(radarChartPanel,BorderLayout.CENTER);
        heroSelector.addActionListener(e -> {
            // Obtenemos el héroe seleccionado y se lo pasamos al radar
            Hero selectedHero = (Hero) heroSelector.getSelectedItem();
            radarChartPanel.setHero(selectedHero);
        });

        // Derecha - Abajo: Botones
        JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        JButton btnMandar = new JButton("MANDAR");
        btnMandar.setForeground(new Color(0, 150, 150));
        btnMandar.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 150), 2));
        
        JButton btnNadota = new JButton("NADOTA");
        btnNadota.setForeground(Color.RED);
        btnNadota.setBorder(BorderFactory.createLineBorder(Color.RED, 2));

        buttonsPanel.add(btnMandar);
        buttonsPanel.add(btnNadota);
        heroesPanel.add(buttonsPanel, BorderLayout.SOUTH);

        // Ensamblar Centro
        mainCenterPanel.add(missionsPanel);
        mainCenterPanel.add(heroesPanel);
        add(mainCenterPanel, BorderLayout.CENTER);
        
        // Márgenes generales
        getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        timeManager.startGameTime();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HeroDispatchUI().setVisible(true);
        });
    }
}