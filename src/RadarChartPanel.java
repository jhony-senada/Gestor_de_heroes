import java.awt.*;

import javax.swing.JPanel;

public class RadarChartPanel extends JPanel{
    private Hero heroeActual;
    private final String[] statLabels = {"STR", "AGI", "CHA", "LUCK", "DEF", "INT"}; 
    public RadarChartPanel(){
        setBackground(new Color(240,255,240));
    }
    public void setHero(Hero hero){
        this.heroeActual=hero;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if(heroeActual==null)return;
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width =getWidth();
        int height = getHeight();
        int cx = width / 2; // Centro X
        int cy = height / 2; // Centro Y

        int maxRadius =Math.min(cx, cy)-30;
        int maxValue =6;

        g2.setColor(Color.LIGHT_GRAY);
        Polygon backgroundPoly =new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = (i * Math.PI / 3) - (Math.PI / 2);
            int x = (int) (cx + maxRadius * Math.cos(angle));
            int y = (int) (cy + maxRadius * Math.sin(angle));
            backgroundPoly.addPoint(x, y);
            
            // Dibujar línea desde el centro hasta el borde
            g2.drawLine(cx, cy, x, y);
            
            // Dibujar las etiquetas (STR, AGI, etc.) un poco más afuera del radio máximo
            int labelX = (int) (cx + (maxRadius + 15) * Math.cos(angle)) - 10;
            int labelY = (int) (cy + (maxRadius + 15) * Math.sin(angle)) + 5;
            g2.setColor(Color.RED);
            g2.setFont(new Font("Monospaced", Font.BOLD, 12));
            g2.drawString(statLabels[i], labelX, labelY);
            g2.setColor(Color.LIGHT_GRAY);
        }
        g2.drawPolygon(backgroundPoly);

        int[] heroStats = {
            heroeActual.getStr(), heroeActual.getAgi(), heroeActual.getCha(),
            heroeActual.getLuck(), heroeActual.getDef(), heroeActual.getIntelligence()
        };

        Polygon statsPoly = new Polygon();
        for (int i = 0; i < 6; i++) {
            double angle = (i * Math.PI / 3) - (Math.PI / 2);
            
            // Calculamos qué tan lejos llega el punto basándonos en su valor (1 a 6)
            // Multiplicamos por (maxRadius / maxValue) para escalar el valor al tamaño de la pantalla
            double radius = ((double) heroStats[i] / maxValue) * maxRadius;
            
            int x = (int) (cx + radius * Math.cos(angle));
            int y = (int) (cy + radius * Math.sin(angle));
            statsPoly.addPoint(x, y);
        }
        g2.setColor(new Color(0,150,100));
        g2.fillPolygon(statsPoly);

        g2.setColor(new Color(0,150,150));
        g2.setStroke(new BasicStroke(2));
        g2.drawPolygon(statsPoly);
    }
}
