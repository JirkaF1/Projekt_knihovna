import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Test {
    private static Scanner scanner = new Scanner(System.in);
    private static Databaze databaze = null;

    public static void main(String[] args) {
        Spojeni spojeni = new Spojeni();
        
        if (spojeni.pripojitKDB()) {
            spojeni.vytvoritTabulky();
            databaze = new Databaze(new HashMap<>(spojeni.nacistKnihy()));
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                spojeni.ulozitKnihy(databaze.getKnihovna());
                spojeni.zavritSpojeni();
            }));
        } 
        else {
            System.out.println("Nepodařilo se připojit k databázi.");
        }
        boolean loop = true;
        while (loop) {
            System.out.println("\nVyberte požadovanou činnost:");
            System.out.println("1 - Přidat knihu");
            System.out.println("2 - Upravit knihu");
            System.out.println("3 - Smazat knihu");
            System.out.println("4 - Označit stav knihy");
            System.out.println("5 - Vypsat všechny knihy abecedně");
            System.out.println("6 - Zobrazit konkrétní knihu");
            System.out.println("7 - Vypsat knihy daného autora");
            System.out.println("8 - Vypsat knihy daného žánru");
            System.out.println("9 - Vypsat vypůjčené knihy");
            System.out.println("10 - Uložit knihu do souboru");
            System.out.println("11 - Načíst knihu ze souboru");
            System.out.println("0 - Konec");    
            System.out.print("Zadejte volbu: ");
            int volba = kontrolaCisla();

            switch (volba) {
                case 1:
                    pridatKnihu();
                    break;
                case 2:
                    upravitKnihu();
                    break;
                case 3:
                    smazatKnihu();
                    break;
                case 4:
                    oznacitStavKnihy();
                    break;
                case 5:
                    vypsatVsechnyKnihy();
                    break;
                case 6:
                    zobrazitKnihu();
                    break;
                case 7:
                    vypsatKnihyAutora();
                    break;
                case 8:
                    vypsatKnihyZanru();
                    break;
                case 9:
                    vypsatVypujceneKnihy();
                    break;
                case 10:
                    ulozitKnihuDoSouboru();
                    break;
                case 11:
                    nacistKnihuZeSouboru();
                    break;
                case 0:
                    loop = false;
                    break;
                default:
                    System.out.println("Neplatná volba, zkuste to znovu.");
                    break;
            }
        }
        scanner.close();
    }
    private static int kontrolaCisla() {
        while (!scanner.hasNextInt()) {
            System.out.println("Neplatný vstup. Zadejte prosím celé číslo: ");
            scanner.next();
        }
        int number = scanner.nextInt();
        scanner.nextLine();
        return number;
    }    


    private static void pridatKnihu() {
        System.out.print("Zadejte název knihy: ");
        String nazev = scanner.nextLine();
        System.out.print("Zadejte autora knihy: ");
        String autor = scanner.nextLine();
        System.out.print("Zadejte rok vydání: ");
        int rokVydani = kontrolaCisla();
        boolean dostupnost = true;
        System.out.println("Vyberte typ knihy:");
        System.out.println("1. Román");
        System.out.println("2. Učebnice");
        int typKnihy = kontrolaCisla();

        switch (typKnihy) {
            case 1:
                System.out.print("Zadejte žánr románu: ");
                String zanr = scanner.nextLine();
                Roman roman = new Roman(nazev, autor, rokVydani, zanr, dostupnost);
                databaze.addKnihy(roman);
                System.out.println("Román byl úspěšně přidán do knihovny.");
                break;
            case 2:
                System.out.print("Zadejte vhodný ročník pro učebnici: ");
                int rocnik = kontrolaCisla();
                Ucebnice ucebnice = new Ucebnice(nazev, autor, rokVydani, rocnik, dostupnost);
                databaze.addKnihy(ucebnice);
                System.out.println("Učebnice byla úspěšně přidána do knihovny.");
                break;
            default:
                System.out.println("Neplatná volba. Prosím, zkuste to znovu.");
                break;
        }
    }

    private static void upravitKnihu() {
        System.out.print("Zadejte název knihy, kterou chcete upravit: ");
        String nazev = scanner.nextLine();
        Knihy kniha = databaze.getKnihy(nazev);
    
        if (kniha != null) {
            System.out.println("Vyberte, co chcete upravit:");
            System.out.println("1. Autora knihy");
            System.out.println("2. Rok vydání");
            int volba = kontrolaCisla();
    
            switch (volba) {
                case 1:
                    System.out.print("Zadejte nového autora knihy: ");
                    String novyAutor = scanner.nextLine();
                    kniha.setAutor(novyAutor);
                    System.out.println("Autor byl změněn.");
                    break;
                case 2:
                    System.out.print("Zadejte nový rok vydání: ");
                    int novyRokVydani = kontrolaCisla();
                    kniha.setRokVydani(novyRokVydani);
                    System.out.println("Rok vydání byl změněn.");
                    break;
                default:
                    System.out.println("Neplatná volba.");
                    break;
            }
        } 
        else {
            System.out.println("Kniha " + nazev + " nebyla nalezena.");
        }
    }

    private static void smazatKnihu() {
        System.out.print("Zadejte název knihy, kterou chcete smazat: ");
        String nazev = scanner.nextLine();
        Knihy kniha = databaze.getKnihy(nazev);
        
        if (kniha != null) {
            databaze.removeKnihy(nazev);
            System.out.println("Kniha byla úspěšně smazána.");
        } 
        else {
            System.out.println("Kniha " + nazev + " nebyla nalezena.");
        }
    }

    private static void oznacitStavKnihy() {
        System.out.print("Zadejte název knihy, jejíž stav chcete změnit: ");
        String nazev = scanner.nextLine();
        Knihy kniha = databaze.getKnihy(nazev);
    
        if (kniha != null) {
            System.out.println("Aktuálně je kniha " + (kniha.getDostupnost() ? "Dostupná" : "Vypůjčená"));
            System.out.println("1. Označit jako vypůjčenou");
            System.out.println("2. Označit jako vrácenou");
            int volba = kontrolaCisla();
    
            switch (volba) {
                case 1:
                    kniha.setDostupnost(false);
                    System.out.println("Kniha byla označena jako vypůjčená.");
                    break;
                case 2:
                    kniha.setDostupnost(true);
                    System.out.println("Kniha byla označena jako vrácená.");
                    break;
                default:
                    System.out.println("Neplatná volba.");
                    break;
            }
        } 
        else {
            System.out.println("Kniha " + nazev + " nebyla nalezena.");
        }
    }
    
    
    private static void vypsatVsechnyKnihy() {
        List<Knihy> seznamKnih = databaze.getVsechnyKnihy();
        if (seznamKnih.isEmpty()) {
            System.out.println("V knihovně nejsou žádné knihy.");
        } 
        else {
            System.out.println("Všechny knihy v abecedním pořadí:");
            for (Knihy kniha : seznamKnih) {
                System.out.println(kniha);
            }
        }
    }

    private static void zobrazitKnihu() {
        System.out.print("Zadejte název knihy k zobrazení: ");
        String nazev = scanner.nextLine();
        Knihy kniha = databaze.getKnihy(nazev);

        if (kniha != null) {
            System.out.println("Informace o knize: " + kniha);
        } 
        else {
            System.out.println("Kniha " + nazev + " nebyla nalezena.");
        }
    }


    private static void vypsatKnihyAutora() {
        System.out.print("Zadejte jméno autora, jehož knihy chcete zobrazit: ");
        String autor = scanner.nextLine();
        List<Knihy> knihyAutora = databaze.getKnihyAutora(autor);
        
        if (knihyAutora.isEmpty()) {
            System.out.println("Žádné knihy od autora " + autor + " nebyly nalezeny.");
        } 
        else {
            System.out.println("Knihy od autora " + autor + " v chronologickém pořadí:");
            for (Knihy kniha : knihyAutora) {
                System.out.println(kniha);
            }
        }
    }


    private static void vypsatKnihyZanru() {
        System.out.println("Dostupné žánry:");
        Set<String> zanry = databaze.getDostupneZanry();
        zanry.forEach(zanr -> System.out.println(zanr));
        System.out.print("Zadejte žánr knih, které chcete zobrazit: ");
        String zanr = scanner.nextLine();
        List<Knihy> knihyZanru = databaze.getKnihyZanru(zanr);
    
        if (knihyZanru.isEmpty()) {
            System.out.println("Žádné knihy žánru " + zanr + " nebyly nalezeny.");
        } 
        else {
            System.out.println("Knihy žánru " + zanr + ":");
            for (Knihy kniha : knihyZanru) {
                System.out.println(kniha);
            }
        }
    }

    private static void vypsatVypujceneKnihy() {
        List<Knihy> vypujceneKnihy = databaze.getVypujceneKnihy();
    
        if (vypujceneKnihy.isEmpty()) {
            System.out.println("Nejsou žádné vypůjčené knihy.");
        } 
        else {
            System.out.println("Všechny vypůjčené knihy:");
            for (Knihy kniha : vypujceneKnihy) {
                String typKnihy = (kniha instanceof Roman) ? "Román" :  "Učebnice";
                System.out.println(kniha + " - Typ: " + typKnihy);
            }
        }
    }


    private static void ulozitKnihuDoSouboru() {
        System.out.print("Zadejte název knihy, kterou chcete uložit do souboru: ");
        String nazev = scanner.nextLine();
        Knihy kniha = databaze.getKnihy(nazev);

        if (kniha != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nazev + ".txt"))) {
                writer.write(kniha.toString());
                System.out.println("Informace o knize byly uloženy do souboru " + nazev);
            } 
            catch (IOException e) {
                System.out.println("Nepodařilo se uložit informace o knize: " + e.getMessage());
            }
        } 
        else {
        System.out.println("Kniha " + nazev + " nebyla nalezena.");
        }
    }


    private static void nacistKnihuZeSouboru() {
        System.out.print("Zadejte název knihy, kterou chcete načíst ze souboru: ");
        String nazev = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader(nazev + ".txt"))) {
            String line = reader.readLine();
            System.out.println("Načtené informace o knize z souboru:");
            System.out.println(line);
        } 
        catch (IOException e) {
            System.out.println("Nepodařilo se načíst informace o knize: " + e.getMessage());
        }
    }
}



