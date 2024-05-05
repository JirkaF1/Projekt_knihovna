public class Roman extends Knihy {
    private String zanr;

    public Roman(String nazev, String autor, int rokVydani, String zanr, boolean dostupnost) {
        super(nazev, autor, rokVydani, dostupnost);
        this.zanr = zanr;
    }

    public String getZanr() {
        return zanr;
    }

    public void setZanr(String zanr) {
        this.zanr = zanr;
    }

    @Override
    public String toString() {
        return super.toString() + ", Žánr: " + zanr;
    }
}
