import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Spojeni {
    private Connection spojeni;

    public boolean pripojitKDB() {
        if (spojeni == null) {
            try {
                Class.forName("org.sqlite.JDBC");
                spojeni = DriverManager.getConnection("jdbc:sqlite:db/test.db");
            } 
            catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public void vytvoritTabulky() {
        try {
            Statement stmt = spojeni.createStatement();
            String sqlRoman = "CREATE TABLE IF NOT EXISTS Roman (nazev TEXT, autor TEXT, rok INT, zanr TEXT, dostupnost BOOLEAN);";
            String sqlUcebnice = "CREATE TABLE IF NOT EXISTS Ucebnice (nazev TEXT, autor TEXT, rok INT, rocnik INT, dostupnost BOOLEAN);";
            stmt.execute(sqlRoman);
            stmt.execute(sqlUcebnice);
        } 
        catch (SQLException e) {
            System.out.println("Chyba při vytváření tabulek: " + e.getMessage());
        }
    }

    public void ulozitKnihy(Map<String, Knihy> knihy) {
        try {
            String sqlDeleteRoman = "DELETE FROM Roman;";
            String sqlDeleteUcebnice = "DELETE FROM Ucebnice;";
            Statement stmt = spojeni.createStatement();
            stmt.execute(sqlDeleteRoman);
            stmt.execute(sqlDeleteUcebnice);
            PreparedStatement psRoman = spojeni.prepareStatement("INSERT INTO Roman (nazev, autor, rok, zanr, dostupnost) VALUES (?, ?, ?, ?, ?)");
            PreparedStatement psUcebnice = spojeni.prepareStatement("INSERT INTO Ucebnice (nazev, autor, rok, rocnik, dostupnost) VALUES (?, ?, ?, ?, ?)");

            for (Knihy kniha : knihy.values()) {
                if (kniha instanceof Roman) {
                    psRoman.setString(1, kniha.getNazev());
                    psRoman.setString(2, kniha.getAutor());
                    psRoman.setInt(3, kniha.getRokVydani());
                    psRoman.setString(4, ((Roman) kniha).getZanr());
                    psRoman.setBoolean(5, kniha.getDostupnost());
                    psRoman.executeUpdate();
                } 
                else if (kniha instanceof Ucebnice) {
                    psUcebnice.setString(1, kniha.getNazev());
                    psUcebnice.setString(2, kniha.getAutor());
                    psUcebnice.setInt(3, kniha.getRokVydani());
                    psUcebnice.setInt(4, ((Ucebnice) kniha).getRocnik());
                    psUcebnice.setBoolean(5, kniha.getDostupnost());
                    psUcebnice.executeUpdate();
                }
            }
        } 
        catch (SQLException e) {
            System.out.println("Chyba při ukládání knih: " + e.getMessage());
        }
    }

    public Map<String, Knihy> nacistKnihy() {
        Map<String, Knihy> nacteneKnihy = new HashMap<>();
        try {
            Statement stmt = spojeni.createStatement();
            ResultSet rsRoman = stmt.executeQuery("SELECT * FROM Roman");
            while (rsRoman.next()) {
                String nazev = rsRoman.getString("nazev");
                String autor = rsRoman.getString("autor");
                int rok = rsRoman.getInt("rok");
                String zanr = rsRoman.getString("zanr");
                boolean dostupnost = rsRoman.getBoolean("dostupnost");
                nacteneKnihy.put(nazev, new Roman(nazev, autor, rok, zanr, dostupnost));
            }
            ResultSet rsUcebnice = stmt.executeQuery("SELECT * FROM Ucebnice");
            while (rsUcebnice.next()) {
                String nazev = rsUcebnice.getString("nazev");
                String autor = rsUcebnice.getString("autor");
                int rok = rsUcebnice.getInt("rok");
                int rocnik = rsUcebnice.getInt("rocnik");
                boolean dostupnost = rsUcebnice.getBoolean("dostupnost");
                nacteneKnihy.put(nazev, new Ucebnice(nazev, autor, rok, rocnik, dostupnost));
            }
        } 
        catch (SQLException e) {
            System.out.println("Chyba při načítání knih: " + e.getMessage());
        }
        return nacteneKnihy;
    }

    public void zavritSpojeni() {
        if (spojeni != null) {
            try {
                spojeni.close();
            } 
            catch (SQLException e) {
                System.out.println("Chyba při zavírání spojení s databází: " + e.getMessage());
            }
        }
    }
}
