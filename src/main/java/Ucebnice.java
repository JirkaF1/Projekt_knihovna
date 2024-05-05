public class Ucebnice extends Knihy {
    private int rocnik;

    public Ucebnice(String nazev, String autor, int rokVydani, int rocnik, boolean dostupnost) {
        super(nazev, autor, rokVydani, dostupnost);
        this.rocnik = rocnik;
    }

    public int getRocnik() {
        return rocnik;
    }

    public void setRocnik(int rocnik) {
        this.rocnik = rocnik;
    }

    @Override
    public String toString() {
        return super.toString() + ", Ročník: " + rocnik;
    }
}
