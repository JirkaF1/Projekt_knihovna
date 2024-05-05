public class Knihy {
    private String nazev;
    private String autor;
    private int rokVydani;
    private boolean dostupnost;

    public Knihy(String nazev, String autor, int rokVydani, boolean dostupnost) {
        this.nazev = nazev;
        this.autor = autor;
        this.rokVydani = rokVydani;
        this.dostupnost = dostupnost;
    }

    public String getNazev() {
        return nazev;
    }

    public void setNazev(String nazev) {
        this.nazev = nazev;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getRokVydani() {
        return rokVydani;
    }

    public void setRokVydani(int rokVydani) {
        this.rokVydani = rokVydani;
    }

    public boolean getDostupnost() {
        return dostupnost;
    }

    public void setDostupnost(boolean dostupnost) {
        this.dostupnost = dostupnost;
    }

    @Override
    public String toString() {
        return "Název: " + nazev +", Autor: " + autor +", Rok vydání: " + rokVydani +", Dostupnost: " + (dostupnost ? "Dostupná" : "Vypůjčená");
    }
}
