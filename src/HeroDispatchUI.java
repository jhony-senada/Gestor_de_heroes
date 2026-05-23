import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class HeroDispatchUI extends JFrame {
    private HeroManager heroManager;
    private MissionPQ missionQueue;
    private MissionGenerator missionGenerator;
    private TimeManager timeManager;

    private JTextArea missionsArea;
    private JComboBox<Hero> heroSelector; // ¡Ahora guarda objetos Hero directamente!
    private JProgressBar confidenceBar;
    private JLabel timeLabel;
    private JLabel scoreLabel; 
    private int successfulMissions = 0; // Para llevar la cuenta
    public HeroDispatchUI() {
        heroManager = new HeroManager();
        missionQueue = new MissionPQ();
        missionGenerator = new MissionGenerator();
        Runnable updateUI = ()->{
            missionsArea.setText(missionQueue.getAllMissionsText());
            
            heroSelector.repaint();
            if(timeManager !=null &&timeLabel!=null){
                timeLabel.setText(timeManager.getFormattedTime());
            }
        };
        timeManager = new TimeManager(missionQueue, missionGenerator, heroManager.getRoster(), updateUI);

        setTitle("Hero Dispatch Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. CABECERA (Norte) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        timeLabel=new JLabel(" 10:55 AM",SwingConstants.LEFT);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        scoreLabel=new JLabel("Misiones completadas: 0", SwingConstants.CENTER);
        scoreLabel.setFont(new Font("Monospaced", Font.BOLD, 16));
        scoreLabel.setForeground(new Color(0, 100, 0)); // Un verde oscuro elegante
        confidenceBar = new JProgressBar(0, 100);
        confidenceBar.setValue(50); // 50% de confianza
        confidenceBar.setStringPainted(true);
        confidenceBar.setString("Confianza: 50%");
        confidenceBar.setForeground(Color.RED);
        confidenceBar.setPreferredSize(new Dimension(400, 30));
        
        topPanel.add(timeLabel, BorderLayout.WEST);
        topPanel.add(scoreLabel,BorderLayout.CENTER);
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
        heroSelector.setRenderer(new DefaultListCellRenderer(){
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                // Dejamos que Swing configure la base del componente (colores de selección, bordes, etc.)
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                
                if (value instanceof Hero) {
                    Hero hero = (Hero) value;
                    
                    // Si el héroe NO está disponible (está descansando)
                    if (!hero.isAvailable()) {
                        setText(hero.getName() + " 💤 (Descansando)");
                        
                        // Si el elemento está seleccionado por el cursor, usamos un rojo más claro para que resalte
                        if (isSelected) {
                            setForeground(new Color(255, 100, 100)); 
                        } else {
                            setForeground(Color.RED); // Rojo estándar en la lista desplegada
                        }
                    } else {
                        // Si está disponible, se muestra normal
                        setText(hero.getName() + " ⚔️ (Listo)");
                        if (!isSelected) {
                            setForeground(Color.BLACK);
                        }
                    }
                }
                return c;
            }
        });
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
        btnMandar.addActionListener(e->{
if (missionQueue.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay emergencias en este momento.");
                return;
            }

            Hero selectedHero = (Hero) heroSelector.getSelectedItem();
            if (!selectedHero.isAvailable()) {
                JOptionPane.showMessageDialog(this, selectedHero.getName() + " está descansando. ¡Elige a otro héroe!", "Héroe Ocupado", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Extraer la misión y preparar las variables
            Mission mission = missionQueue.dequeue();
            int poolMax = mission.difficulty * 2; // El dado será del 1 al (Dificultad * 2)
            int val1 = getStatValue(selectedHero, mission.stat1);
            int val2 = getStatValue(selectedHero, mission.stat2);
            
            Random dice = new Random();
            boolean success = false;
            String reporte = "";

            if (val1 >= poolMax) {
                success = true;
                reporte = "¡OVERKILL!\nFelicidades, mandaste a dios a bajar a un gato de un árbol.\n" + 
                          selectedHero.getName() + " completó la misión pestañeando porque su " + mission.stat1 + " (" + val1 + ") es una brutalidad para este nivel.";
            }else{

                // 3. INTENTO 1: Estadística Principal
                int roll1 = dice.nextInt(poolMax) + 1; // Tirar dado
                if (roll1 <= val1) {
                    success = true;
                    reporte = "¡ÉXITO!\n" + selectedHero.getName() + " completó la misión magistralmente usando " + mission.stat1 + ".\n(Sacó " + roll1 + " en un dado de " + poolMax + " y necesitaba " + val1 + " o menos)";
                } 
                // 4. INTENTO 2: Estadística Secundaria (Si falló la 1ra)
                else {
                    int roll2 = dice.nextInt(poolMax) + 1;
                    if (roll2 <= val2) {
                        success = true;
                        reporte = "¡POR POCO!\n" + selectedHero.getName() + " falló usando " + mission.stat1 + ", pero logró salvar la situación improvisando con " + mission.stat2 + ".\n(Sacó " + roll2 + " de " + poolMax + " y necesitaba " + val2 + " o menos)";
                    } 
                    // 5. INTENTO 3: Reroll por SUERTE (Si fallaron ambas)
                    else {
                        // Para la suerte, tiramos un dado normal de 6 caras
                        int luckRoll = dice.nextInt(6) + 1; 
                        if (luckRoll <= selectedHero.getLuck()) {
                            success = true;
                            reporte = "¡MILAGRO!\n" + selectedHero.getName() + " falló catastróficamente en sus habilidades... \nPero está usando su SUERTE para que todo salga bien y completó la misión.\n(Tirada de Suerte: " + luckRoll + " vs " + selectedHero.getLuck() + ")";
                        }
                }
            }
        }

            // 6. Consecuencias del Combate
            if (success) {
                // Pequeña recompensa por ganar (Ej: +5 de confianza)
                successfulMissions++;
                scoreLabel.setText("Misiones Completadas: " + successfulMissions);
                int currentConfidence = confidenceBar.getValue();
                int newConf = Math.min(100, currentConfidence + 5);
                confidenceBar.setValue(newConf);
                confidenceBar.setString("Confianza: " + newConf + "%");
                JOptionPane.showMessageDialog(this, reporte, "Reporte de Misión", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Penalización por perder (Misma lógica que el botón NADOTA)
                int penalty = 0;
                switch (mission.difficulty) {
                    case 1: penalty = 1; break; case 2: penalty = 4; break;
                    case 3: penalty = 8; break; case 4: penalty = 12; break;
                    case 5: penalty = 16; break; case 6: penalty = 20; break;
                }
                
                int currentConfidence = confidenceBar.getValue();
                int newConfidence = Math.max(0, currentConfidence - penalty);
                confidenceBar.setValue(newConfidence);
                confidenceBar.setString("Confianza: " + newConfidence + "%"); // Actualizar texto
                
                reporte = "¡FRACASO ROTUNDO!\n" + selectedHero.getName() + " no pudo lidiar con la misión y la ciudad sufrió las consecuencias.\n(-" + penalty + " de Confianza)";
                JOptionPane.showMessageDialog(this, reporte, "Misión Fallida", JOptionPane.ERROR_MESSAGE);

                if (newConfidence == 0) {
                    timeManager.pauseGameTime();
                    JOptionPane.showMessageDialog(this, "¡La confianza de la ciudad ha llegado a cero!\nHas sido despedido de la Agencia de Héroes.", "Game Over", JOptionPane.ERROR_MESSAGE);
                }
            }

            // 7. Cansar al héroe y actualizar la Interfaz
            selectedHero.setAvailable(false);
            missionsArea.setText(missionQueue.getAllMissionsText());
            heroSelector.repaint(); // Refrescar el combobox por si acaso
        });

        JButton btnNadota = new JButton("NADOTA");
        btnNadota.setForeground(Color.RED);
        btnNadota.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
        btnNadota.addActionListener(e->{
            if(!missionQueue.isEmpty()){
                Mission skippedMission = missionQueue.dequeue();
                int penalty=0;
                switch (skippedMission.difficulty) {
                    case 1: penalty =1; break;
                    case 2: penalty =4; break;
                    case 3: penalty =8; break;
                    case 4: penalty =12; break;
                    case 5: penalty =16; break;
                    case 6: penalty =20; break;
                }
                int confianzaActual = confidenceBar.getValue();
                int nuevaConfianza= Math.max(0, confianzaActual-penalty);
                confidenceBar.setValue(nuevaConfianza);
                confidenceBar.setString("Confianza: " + nuevaConfianza + "%"); // Actualizar texto
                missionsArea.setText(missionQueue.getAllMissionsText());
                if (nuevaConfianza <= 0) {
                    timeManager.pauseGameTime(); // Detener los relojes
                    JOptionPane.showMessageDialog(this, 
                        "¡La confianza de la ciudad ha llegado a cero!\nHas sido despedido de la Agencia de Héroes.", 
                        "Game Over", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }else{
                //Mensaje de cuando no hay nada en la lista XD

            }
        });

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
    private int getStatValue(Hero hero, String statName) {
        switch (statName) {
            case "STR": return hero.getStr();
            case "AGI": return hero.getAgi();
            case "CHA": return hero.getCha();
            case "INT": return hero.getIntelligence();
            case "DEF": return hero.getDef();
            default: return 0;
        }
    }
}