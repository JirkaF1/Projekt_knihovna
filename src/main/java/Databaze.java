import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Databaze {
    private HashMap<String, Knihy> knihovna = new HashMap<>();

    public HashMap<String, Knihy> getKnihovna() {
        return knihovna;
    }

    public Databaze(HashMap<String, Knihy> knihovna) {
        this.knihovna = knihovna;
    }

    public void addKnihy(Knihy kniha) {
        knihovna.put(kniha.getNazev(), kniha);
    }

    public Knihy getKnihy(String nazev) {
        return knihovna.get(nazev);
    }

    public void removeKnihy(String nazev) {
        if (knihovna.containsKey(nazev)) {
            knihovna.remove(nazev);
        }
    }
    
    public List<Knihy> getVsechnyKnihy() {
        ArrayList<Knihy> seznamKnih = new ArrayList<>(knihovna.values());
        Collections.sort(seznamKnih, new Comparator<Knihy>() {
            @Override
            public int compare(Knihy o1, Knihy o2) {
                return o1.getNazev().compareToIgnoreCase(o2.getNazev());
            }
        });
        return seznamKnih;
    }

    public List<Knihy> getKnihyAutora(String autor) {
        List<Knihy> knihyAutora = knihovna.values().stream()
                .filter(kniha -> kniha.getAutor().equalsIgnoreCase(autor))
                .sorted(Comparator.comparingInt(Knihy::getRokVydani))
                .collect(Collectors.toList());
        return knihyAutora;
    }

    public List<Knihy> getKnihyZanru(String zanr) {
        return knihovna.values().stream()
            .filter(kniha -> kniha instanceof Roman && ((Roman) kniha).getZanr().equalsIgnoreCase(zanr))
            .collect(Collectors.toList());
    }

    public Set<String> getDostupneZanry() {
        return knihovna.values().stream()
            .filter(kniha -> kniha instanceof Roman)
            .map(kniha -> ((Roman) kniha).getZanr()) 
            .collect(Collectors.toSet()); 
    }

    public List<Knihy> getVypujceneKnihy() {
        return knihovna.values().stream()
            .filter(kniha -> !kniha.getDostupnost())
            .collect(Collectors.toList());
    }
}
