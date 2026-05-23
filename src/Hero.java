public class Hero {
    private String name;
    private int str;
    private int agi;
    private int cha;
    private int intelligence; // Usamos 'intelligence' porque 'int' es una palabra reservada
    private int def;
    private int luck;
    private boolean isAvailable;

    // Constructor
    public Hero(String name, int str, int agi, int cha, int intelligence, int def, int luck) {
        this.name = name;
        // La función clamp limita el valor al rango de 1 a 6
        this.str = clamp(str);
        this.agi = clamp(agi);
        this.cha = clamp(cha);
        this.intelligence = clamp(intelligence);
        this.def = clamp(def);
        this.luck = clamp(luck);
    }

    // Método de seguridad para limitar estadísticas (mínimo 1, máximo 6)
    private int clamp(int value) {
        return Math.max(1, Math.min(6, value));
    }

    // --- Getters ---
    public String getName() { return name; }
    public int getStr() { return str; }
    public int getAgi() { return agi; }
    public int getCha() { return cha; }
    public int getIntelligence() { return intelligence; }
    public int getDef() { return def; }
    public int getLuck() { return luck; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) { this.isAvailable = available; }
    // Para que al imprimir el objeto nos devuelva el nombre directamente (útil para el JComboBox)
    @Override
    public String toString() {
        return name;
    }
}
