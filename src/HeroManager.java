public class HeroManager {
    private HeroLinkedList roster;
    HeroManager(){
        roster = new HeroLinkedList();
        loadHeroes();
    }
    private void loadHeroes(){
        roster.add(new Hero("Artorias", 5, 5, 4, 4, 5, 4));
        roster.add(new Hero("Groak", 5, 2, 1, 1, 4, 1));
        roster.add(new Hero("Kobra-kid", 2, 5, 1, 3, 1, 3));
        roster.add(new Hero("Troy", 3, 3, 3, 3, 3, 3));
        roster.add(new Hero("Gober", 1, 1, 1, 1, 1, 6));
        roster.add(new Hero("Gwendan", 1, 1, 6, 4, 1, 1));
    }
    public HeroLinkedList getRoster() {
        return roster;
    }
}
