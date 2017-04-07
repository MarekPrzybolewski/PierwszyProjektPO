//Marek Przybolewski 28.10.2016r. Co robi program ?
package Projekt;


import java.io.File;
import java.io.FileNotFoundException;
import java.sql.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:baza.db";

    private static Connection conn;
    private static Statement stmt;


    public static void main(String[] args) throws FileNotFoundException {

        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.err.println("Brak sterownika JDBC");
            e.printStackTrace();
        }

        //nawiazuje polaczenie z baza danych
        try {
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            System.err.println("Problem z otwarciem połączenia");
            e.printStackTrace();
        }

        try{
            String delete = "DROP TABLE IF EXISTS artykul";
            stmt.execute(delete);

            delete = "DROP TABLE IF EXISTS wojewodztwo";
            stmt.execute(delete);
        } catch (SQLException e) {
            System.err.println("Problem z usuwaniem");
            e.printStackTrace();
        }

        String createTable = "CREATE TABLE IF NOT EXISTS wojewodztwo(idWojewodztwo INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nazwa VARCHAR(50), region VARCHAR(100))";

        try {
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.err.println("Blad przy tworzeniu tabeli Artykul");
            e.printStackTrace();
        }

        createTable = "CREATE TABLE IF NOT EXISTS artykul(idArtykul INTEGER PRIMARY KEY AUTOINCREMENT," +
                " nazwa VARCHAR(200), wartosc FLOAT, miara VARCHAR(30), " +
                "rokBadania INTEGER,"+
                "idWojewodztwo INTEGER, FOREIGN KEY (idWojewodztwo) REFERENCES wojewodztwo(id))";
        try {
            stmt.execute(createTable);
        } catch (SQLException e) {
            System.err.println("Blad przy tworzeniu tabeli Artykul");
            e.printStackTrace();
        }

        String sql;

        try{
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Łódzkie','Centralny');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Mazowieckie','Centralny');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Małopolskie','Południowy');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Śląskie','Południowy');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Lubelskie','Wschodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Podkarpackie','Wschodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Podlaskie','Wschodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Świętokrzyskie','Wschodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Lubuskie','Północno-zachodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Wielkopolskie','Północno-zachodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Zachodniopomorskie','Północno-zachodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Dolnośląskie','Południowo-zachodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Opolskie','Południowo-zachodni');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Kujawsko-pomorskie','Północny');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Pomorskie','Północny');";
            stmt.execute(sql);
            sql =  "INSERT INTO wojewodztwo (NAZWA, REGION) VALUES ('Warmińsko-mazurskie','Północny');";
            stmt.execute(sql);

        } catch (SQLException e) {
            System.err.println("Blad przy dodawaniu wojewodztw");
            e.printStackTrace();
        }

        File file = new File("bazaDanych.csv");
        Scanner in = new Scanner(file);
        in.nextLine(); // pomijanie pierwszej lini

        while(in.hasNextLine()){
            Filtr filtered = new Filtr(in.nextLine());

            if(filtered.correct){
                try{
                    sql = "INSERT INTO artykul (NAZWA, WARTOSC, MIARA,ROKBADANIA,IDWOJEWODZTWO) "
                            + "VALUES ('"
                            + filtered.getArtykul() + "','"
                            + filtered.getWartosc() + "','"
                            + filtered.getMiara() + "','"
                            + filtered.getRok()  + "',"
                            + filtered.getIdWojewodztwo() +""
                            + " );";

                    stmt.execute(sql);

                } catch (SQLException e) {
                    System.err.println("Blad przy dodawaniu artykulu");
                    e.printStackTrace();
                }
            }

        }

        // zamienianie wartosci -1 na NULL
        try{
            sql = "UPDATE artykul set wartosc = NULL WHERE wartosc = -1";
            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Blad przy zmienianiu wartosci w tabeli artykul");
            e.printStackTrace();
        }

        menu();

    }


    static void menu(){
        boolean programClose = false;
        int programCheck;
        Scanner odczyt = new Scanner(System.in);
        while(!programClose){

            System.out.println("* * * * * * * * * * * ");
            System.out.println("******** MENU ********");
            System.out.println("* 1. Wyszukaj dane   *");
            System.out.println("* 2. Dodaj dane      *");
            System.out.println("* 3. Zakończ program *");
            System.out.println("* * * * * * * * * * * ");
            System.out.print("Podaj cyfre: ");

            programCheck = odczyt.nextInt();

            switch(programCheck) {

                case 1:
                    wyszukajDane();
                    break;

                case 2:
                    dodajDane();
                    break;

                case 3:
                    System.out.println("Zamykam program");
                    programClose = true;
                    break;

                default:
                    System.out.println("Nie ma takiej cyfry w menu, wybierz inną");
                    break;
            }
        }


    }

    static void dodajDane(){

        boolean notCorrect = true, dodajWpis = false, Odpowiedz = false;
        int sprawdz;
        String wojewodztwo = new String();
        Artykul dodaj = new Artykul();
        Scanner odczyt = new Scanner(System.in);
        while(!dodajWpis) {
            System.out.println("* * * * * * * * * * *  * * * * * * * * * *");
            System.out.println("* * * * * * * * DODAJ DANE * * * * * * * *");
            System.out.printf("* Podaj nazwe artykulu: ");
            dodaj.setNazwa(odczyt.next());
            while(notCorrect) {
                try {
                    System.out.printf("* Podaj wartosc (jeśli nie ma to wpisz \"brak\": ");
                    dodaj.setWartosc(odczyt.nextDouble());
                    notCorrect = false;


                } catch (InputMismatchException e) {
                    System.err.println("Zla postac danej, to musi być liczba");
                    dodajDane();
                }
            }

            notCorrect = true;

            System.out.printf("* Podaj miare: ");
            dodaj.setMiara(odczyt.next());
            System.out.printf("* Rok badania: ");
            dodaj.setRokBadania(odczyt.next());

            while (notCorrect) {
                System.out.print("* Podaj województwo: ");
                wojewodztwo = odczyt.next();
                wojewodztwo = wojewodztwo.toLowerCase();
                wojewodztwo = wojewodztwo.substring(0, 1).toUpperCase() + wojewodztwo.substring(1);

                if (sprawdz(wojewodztwo, "wojewodztwo", "nazwa")) {
                    dodaj.setWojewodztwo(wojewodztwo);
                    notCorrect = false;
                } else {
                    System.out.println("Nie ma takiego województwa, spróbuj jeszcze raz");
                }
            }

            System.out.println("* Czy chcesz dodać takie dane ? ");
            System.out.printf("* Nazwa artykulu: ");
            System.out.println(dodaj.getNazwa());
            System.out.printf("* Wartość: ");
            System.out.println(dodaj.getWartosc());
            System.out.printf("* Miara: ");
            System.out.println(dodaj.getMiara());
            System.out.printf("* Rok badania: ");
            System.out.println(dodaj.getRokBadania());
            System.out.printf("* Wojewodztwo: ");
            System.out.println(dodaj.getWojewodztwo());
            System.out.printf("* Czy chcesz dodać taki wpis ? \n 1. Tak 2. Nie \n");

            while(!Odpowiedz) {
                System.out.printf("Podaj cyfre: ");
                sprawdz = odczyt.nextInt();
                if (sprawdz == 1) {
                    dodajWpis(dodaj);
                    Odpowiedz = true;
                    dodajWpis = false;
                    menu();
                } else {
                    if (sprawdz == 2) {
                        menu();
                    } else {
                        System.out.println("Nie ma takiej opcji wybierz jeszcze raz");
                    }
                }
            }
        }

    }

    static void wyszukajDane(){

        boolean programClose = false;
        int programCheck;
        Scanner odczyt = new Scanner(System.in);
        while(!programClose) {

            System.out.println("* * * * * * * * * * * * * * * * * * * * *");
            System.out.println("************* WYSZUKAJ DANE *************");
            System.out.println("* * * * * * * * * * * * * * * * * * * * *");
            System.out.println("* 1. Wyszukaj dane po województwie      *");
            System.out.println("* 2. Wyszukaj dane dla danego roku      *");
            System.out.println("* 3. Wyszukaj dane dla danego artykułu  *");
            System.out.println("* 4. Wróć                               *");
            System.out.println("* 5. Zakończ progarm                    *");
            System.out.println("* * * * * * * * * * * * * * * * * * * * *");
            System.out.print("Podaj cyfre: ");

            programCheck = odczyt.nextInt();

            switch(programCheck) {
                case 1:
                    wyszukajDanePoWojewodztwie();
                    break;

                case 2:
                    wyszukajDanePoRoku();
                    break;

                case 3:
                    wyszukajDanePoArtykule();
                    break;

                case 4:
                    menu();
                    break;

                case 5:
                    programClose = true;
                    break;

                default:
                    System.out.println("Nie ma takiej cyfry w menu");
                    break;
            }

        }
        System.exit(0);
    }


    private static void wyszukajDanePoArtykule(){
        Scanner odczyt = new Scanner(System.in);
        String artykul;
        while(true){
            System.out.println("* * * * * * * * * * * * * *");
            System.out.println("Podaj nazwe artykułu:      ");
            artykul = odczyt.next();
            artykul = artykul.toLowerCase();
            artykul = artykul.substring(0,1).toUpperCase() + artykul.substring(1);
            if(sprawdz(artykul,"artykul","nazwa")){
                jestArtykul(artykul);
            } else{
                System.out.println("Nie ma takiego produktu");
            }
        }
    }

    private static void wyszukajDanePoWojewodztwie(){
        Scanner odczyt = new Scanner(System.in);
        String wojewodztwo;
        while(true){
            System.out.println("* * * * * * * * * * * * * *");
            System.out.println("Podaj nazwe województwa:   ");
            wojewodztwo = odczyt.next();
            wojewodztwo = wojewodztwo.toLowerCase();
            wojewodztwo = wojewodztwo.substring(0,1).toUpperCase() + wojewodztwo.substring(1);
            if(sprawdz(wojewodztwo,"wojewodztwo","nazwa")){
                jestWojewodztwo(wojewodztwo);
                break;
            } else{
                System.out.println("Nie ma takiego wojewodztwa");
            }
        }

    }

    private static void wyszukajDanePoRoku(){
        Scanner odczyt = new Scanner(System.in);
        String rok;
        while(true){
            System.out.println("* * * * * * * * * * * * * *");
            System.out.println("Podaj rok: ");
            rok = odczyt.next();
            if(sprawdz(rok,"artykul","rokBadania")){
                jestRok(rok);
                break;
            } else{
                System.out.println("Nie ma takiego roku w bazie danych");
            }
        }
    }

    private static void jestArtykul(String artykul){
        boolean programClose = false;
        int programCheck;
        Scanner odczyt = new Scanner(System.in);
        String wojewodztwo;
        String rok;
        while(!programClose){
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("*****************************" + artykul + "*****************************");
            System.out.println("* 1. Pokaż wszystkie dane dla danego artykułu                           *");
            System.out.println("* 2. Pokaż wszystkie dane dla danego artykułu w konkretnym wojewodztwie *");
            System.out.println("* 3. Pokaż dane dla danego roku                                         *");
            System.out.println("* 4. Pokaż dane dla danego roku w danym wojewodztwie                    *");
            System.out.println("* 5. Wróć                                                               *");
            System.out.println("* 6. Zakończ prorgram                                                   *");
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("Podaj cyfre: ");

            programCheck = odczyt.nextInt();

            switch(programCheck) {
                case 1:
                    wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                            " a.miara AS miara, a.rokBadania AS rokBadania, " +
                            "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                            "a.idWojewodztwo = w.idWojewodztwo WHERE a.nazwa LIKE '" + artykul + "'");
                    break;

                case 2:
                    System.out.println("Podaj województwo: ");
                    wojewodztwo = odczyt.next();
                    if(sprawdz(wojewodztwo,"wojewodztwo", "nazwa")){
                        wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                "a.idWojewodztwo = w.idWojewodztwo WHERE a.nazwa LIKE '" + artykul + "' " +
                                "AND w.nazwa LIKE '" +  wojewodztwo + "'");
                    } else{
                        System.out.println("W bazie danych nie ma takuegi wojewodztwa");
                    }

                    break;

                case 3:
                    System.out.println("Podaj rok: ");
                    rok = odczyt.next();
                    if(sprawdz(rok,"artykul","rokBadania")){
                        wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                "a.idWojewodztwo = w.idWojewodztwo WHERE a.nazwa LIKE '" + artykul + "' " +
                                "AND a.rokBadania LIKE '" +  rok + "'");
                    } else{
                        System.out.println("W bazie danych nie ma takiego roku");
                    }
                    break;

                case 4:
                    System.out.println("Podaj rok: ");
                    rok = odczyt.next();
                    if(sprawdz(rok,"artykul","rokBadania")){
                        System.out.println("Podaj wojewodztwo: ");
                        wojewodztwo = odczyt.next();
                        if(sprawdz(wojewodztwo,"wojewodztwo","nazwa")){
                            wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                    " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                    "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                    "a.idWojewodztwo = w.idWojewodztwo WHERE w.nazwa LIKE '" + wojewodztwo + "' " +
                                    "AND a.nazwa LIKE '" + artykul + "' AND a.rokBadania LIKE '" + rok + "'");
                        }
                        else{
                            System.out.println("Nie ma takiego wojewodztwa w bazie danych");
                        }
                    }else{
                        System.out.println("Nie ma takiego roku w bazie danych");
                    }
                    break;

                case 5:
                    menu();
                    break;

                case 6:
                    programClose = true;
                    break;

                default:
                    System.out.println("Nie ma takiej opcji");
                    break;
            }

        }
        System.exit(0);
    }


    private static void jestRok(String rok) {
        boolean programClose = false;
        int programCheck;
        Scanner odczyt = new Scanner(System.in);
        String wojewodztwo;
        String artykul;
        while(!programClose){
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("******************************" + rok + " *****************************");
            System.out.println("* 1. Pokaż wszystkie dane dla danego roku                             *");
            System.out.println("* 2. Pokaż wszystkie dane dla danego roku w konkretnym wojewodztwie   *");
            System.out.println("* 3. Pokaż dane dla danego artykułu                                   *");
            System.out.println("* 4. Pokaż dane dla danego artykułu w danym wojewodztwie              *");
            System.out.println("* 5. Wróć                                                             *");
            System.out.println("* 6. Zakończ prorgram                                                 *");
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("Podaj cyfre: ");

            programCheck = odczyt.nextInt();

            switch(programCheck) {
                case 1:
                    wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                            " a.miara AS miara, a.rokBadania AS rokBadania, " +
                            "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                            "a.idWojewodztwo = w.idWojewodztwo WHERE a.rokBadania LIKE '" + rok + "'");
                    break;

                case 2:
                    System.out.println("Podaj województwo: ");
                    wojewodztwo = odczyt.next();
                    if(sprawdz(wojewodztwo,"wojewodztwo", "nazwa")){
                        wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                "a.idWojewodztwo = w.idWojewodztwo WHERE a.rokBadania LIKE '" + rok + "' " +
                                "AND w.nazwa LIKE '" +  wojewodztwo + "'");
                    } else{
                        System.out.println("W bazie danych nie ma takuegi wojewodztwa");
                    }

                    break;

                case 3:
                    System.out.println("Podaj nazwe artykulu: ");
                    artykul = odczyt.next();
                    if(sprawdz(artykul,"artykul","nazwa")){
                        wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                "a.idWojewodztwo = w.idWojewodztwo WHERE a.rokBadania LIKE '" + rok + "' " +
                                "AND a.nazwa LIKE '" +  artykul + "'");
                    } else{
                        System.out.println("W bazie danych nie ma takiego artykulu");
                    }
                    break;

                case 4:
                    System.out.println("Podaj nazwe artykulu: ");
                    artykul = odczyt.next();
                    if(sprawdz(artykul,"artykul","nazwa")){
                        System.out.println("Podaj wojewodztwo: ");
                        wojewodztwo = odczyt.next();
                        if(sprawdz(rok,"artykul","rokBadania")){
                            wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                    " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                    "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                    "a.idWojewodztwo = w.idWojewodztwo WHERE w.nazwa LIKE '" + wojewodztwo + "' " +
                                    "AND a.nazwa LIKE '" + artykul + "' AND a.rokBadania LIKE '" + rok + "'");
                        }
                        else{
                            System.out.println("Nie ma takiego wojewodztwa w bazie danych");
                        }
                    }else{
                        System.out.println("Nie ma takiego produktu");
                    }
                    break;

                case 5:
                    menu();
                    break;

                case 6:
                    programClose = true;
                    break;

                default:
                    System.out.println("Nie ma takiej opcji");
                    break;
            }

        }
        System.exit(0);
    }

    private static void jestWojewodztwo(String wojewodztwo){
        boolean programClose = false;
        int programCheck;
        Scanner odczyt = new Scanner(System.in);
        String artykul;
        String rok;
        while(!programClose) {
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("******************* " + wojewodztwo.toUpperCase() + " *******************");
            System.out.println("* 1. Pokaż wszystkie dane dla danego województwa                      *");
            System.out.println("* 2. Pokaż wszystkie dane dla danego województwa w konkretnym roku    *");
            System.out.println("* 3. Pokaż dane dla danego artykułu                                   *");
            System.out.println("* 4. Pokaż dane dla danego artykułu w danym roku                      *");
            System.out.println("* 5. Wróć                                                             *");
            System.out.println("* 6. Zakończ prorgram                                                 *");
            System.out.println("* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *");
            System.out.println("Podaj cyfre: ");

            programCheck = odczyt.nextInt();

            switch (programCheck) {
                case 1:
                    wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                            " a.miara AS miara, a.rokBadania AS rokBadania, " +
                            "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                            "a.idWojewodztwo = w.idWojewodztwo WHERE w.nazwa LIKE '" + wojewodztwo + "'");
                    break;

                case 2:
                    System.out.println("Podaj rok: ");
                    rok = odczyt.next();
                    if(sprawdz(rok,"artykul","rokBadania")){

                        wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                "a.idWojewodztwo = w.idWojewodztwo WHERE w.nazwa LIKE '" + wojewodztwo + "' " +
                                "AND a.rokBadania LIKE '" + rok + "'");
                    }
                    else{
                        System.out.println("Nie ma w bazie danych takiego roku");
                    }
                    break;

                case 3:
                    System.out.println("Podaj nazwe produktu: ");
                    artykul = odczyt.next();
                    if(sprawdz(artykul,"artykul","nazwa")){

                        wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                "a.idWojewodztwo = w.idWojewodztwo WHERE w.nazwa LIKE '" + wojewodztwo + "' " +
                                "AND a.nazwa LIKE '" + artykul + "'");
                    }else{
                        System.out.println("Nie ma takiego produktu");
                    }
                    break;

                case 4:
                    System.out.println("Podaj nazwe produktu: ");
                    artykul = odczyt.next();
                    if(sprawdz(artykul,"artykul","nazwa")){
                        System.out.println("Podaj rok: ");
                        rok = odczyt.next();
                        if(sprawdz(rok,"artykul","rokBadania")){

                            wojewodztwoZapytanie("SELECT a.idArtykul AS idArtykul, a.nazwa AS nazwa, a.wartosc AS wartosc," +
                                    " a.miara AS miara, a.rokBadania AS rokBadania, " +
                                    "w.nazwa AS wojewodztwo, w.region AS region FROM artykul a INNER JOIN wojewodztwo w ON " +
                                    "a.idWojewodztwo = w.idWojewodztwo WHERE w.nazwa LIKE '" + wojewodztwo + "' " +
                                    "AND a.nazwa LIKE '" + artykul + "' AND a.rokBadania LIKE '" + rok + "'");
                        }
                        else{
                            System.out.println("Nie ma takiego roku w bazie danych");
                        }
                    }else{
                        System.out.println("Nie ma takiego produktu");
                    }
                    break;

                case 5:
                    menu();
                    break;

                case 6:
                    programClose = true;
                    break;

                default:
                    System.out.println("Nie ma takiej opcji");
                    break;
            }
        }
        System.exit(0);
    }


   private static void wojewodztwoZapytanie(String sql){
        try {
            ResultSet result = stmt.executeQuery(sql);
            int idArtykul,rokBadania;
            String nazwa,miara,region,wojewodztwo;
            double wartosc;

            while(result.next()) {
                idArtykul = result.getInt("idArtykul");
                nazwa = result.getString("nazwa");
                wartosc = result.getDouble("wartosc");
                miara = result.getString("miara");
                rokBadania = result.getInt("rokBadania");
                wojewodztwo = result.getString("wojewodztwo");
                region = result.getString("region");
                System.out.println("id=" + idArtykul + ", nazwa=" + nazwa + ", wartosc=" + wartosc +
                        ", miara=" + miara + ",rok badania=" + rokBadania + ", Wojewodztwo=" + wojewodztwo +
                        ", Region=" + region);
            }
        } catch (SQLException e) {
            System.err.println("Blad przy wkonywaniu SELECT");
            e.printStackTrace();
        }
    }

    public static boolean sprawdz(String dana, String tabela, String kolumna) {
        int howManyRows = 0;
        try {
            ResultSet result = stmt.executeQuery("SELECT * FROM " + tabela + " WHERE " + kolumna + " LIKE '"
                    + dana + "'");

            while (result.next()) {
                howManyRows++;
            }


        } catch (SQLException e) {
            System.err.println("Blad przy zapytaniu o rok");
            e.printStackTrace();
        }

        if (howManyRows == 0) {
            return false;
        } else {
            return true;
        }
    }

    static public double stringToDouble(String word){
        String ourDouble = word.replace(",",".");


        return Double.parseDouble(ourDouble);

    }



    public static int sprawdzId(String wojewodztwo) {
        try {
            ResultSet result = stmt.executeQuery("SELECT idWojewodztwo FROM wojewodztwo WHERE nazwa LIKE '" + wojewodztwo +"'");
            int idWojewodztwo;

            while(result.next()) {
                idWojewodztwo = Integer.parseInt(result.getString("idWojewodztwo"));
                return idWojewodztwo;
            }
        } catch (SQLException e) {
            System.err.println("Blad przy wkonywaniu SELECT");
            e.printStackTrace();
        }
        return 0;
    }

    public static void dodajWpis(Artykul dodaj){
        System.out.println("Dodaje wpis");
        String sql;

        try{
            sql =  "INSERT INTO artykul (NAZWA, WARTOSC, MIARA, ROKBADANIA, IDWOJEWODZTWO) " +
                    "VALUES ('" + dodaj.getNazwa() + "','"
                    + dodaj.getWartosc() +"','"
                    + dodaj.getMiara() + "','"
                    + dodaj.getRokBadania() + "',"
                    + sprawdzId(dodaj.getWojewodztwo()) + ");";
            stmt.execute(sql);


        } catch (SQLException e) {
            System.err.println("Blad przy dodawaniu wpisu");
            e.printStackTrace();
        }
    }



}

