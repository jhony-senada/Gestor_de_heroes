import javax.swing.*;
import java.awt.*;

public class HeroDispatchUI extends JFrame {

    public HeroDispatchUI() {
        setTitle("Hero Dispatch Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 1. CABECERA (Norte) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel timeLabel = new JLabel(" 10:55 AM", SwingConstants.LEFT);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 18));
        
        JProgressBar confidenceBar = new JProgressBar(0, 100);
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
        JTextArea missionsArea = new JTextArea("1. Mision Rapida...\n2. Defender la aldea...");
        missionsArea.setEditable(false);
        missionsPanel.add(new JScrollPane(missionsArea), BorderLayout.CENTER);

        // DERECHA: Gestión de Héroes
        JPanel heroesPanel = new JPanel(new BorderLayout(0, 10));

        // Derecha - Arriba: ComboBox
        String[] heroNames = {"Heroe 1", "Heroe 2", "Heroe 3"};
        JComboBox<String> heroSelector = new JComboBox<>(heroNames);
        heroSelector.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));
        heroesPanel.add(heroSelector, BorderLayout.NORTH);

        // Derecha - Centro: Gráfico de Radar (Placeholder)
        JPanel radarChartPanel = new JPanel();
        radarChartPanel.setBorder(BorderFactory.createLineBorder(new Color(0, 150, 150), 2));
        radarChartPanel.setBackground(new Color(240, 255, 240));
        radarChartPanel.add(new JLabel("Aquí se dibujará el gráfico de Radar con Graphics2D"));
        heroesPanel.add(radarChartPanel, BorderLayout.CENTER);

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
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new HeroDispatchUI().setVisible(true);
        });
    }
}