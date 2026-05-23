public class HeroLinkedList{
    class HeroNodo {
        public Hero data;
        public HeroNodo next;
        
        HeroNodo(Hero hero){
            this.data=hero;
            this.next=null;
        }
    }
    private HeroNodo head;
    private int size;

    HeroLinkedList(){
        this.head=null;
        this.size=0;
    }

    //Metodo para aggrgar un heroe al final de la lista
    public void add(Hero hero){
        HeroNodo nuevo = new HeroNodo(hero);
        if (head==null){
            head=nuevo;
        }else{
            HeroNodo actual=head;
            while(actual.next!=null){
                actual=actual.next;
            }
            actual.next=nuevo;
        }
        size++;
    }
    //Metodo para obtener un heroe por su posicion
    public Hero get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        HeroNodo current = head;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    // Método para saber el tamaño de la lista
    public int size() {
        return size;
    }
    
}
