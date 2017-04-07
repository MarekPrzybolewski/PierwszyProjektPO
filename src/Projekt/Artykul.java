package Projekt;

import java.sql.Connection;
import java.sql.Statement;

public class Artykul {
    public static final String DRIVER = "org.sqlite.JDBC";
    public static final String DB_URL = "jdbc:sqlite:baza.db";

    private static Connection conn;
    private static Statement stmt;

    private boolean correct;
    private String nazwa;
    private double wartosc;
    private String miara;
    private String rokBadania;
    private String wojewodztwo;
    private String region;

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public double getWartosc() {
        return wartosc;
    }

    public void setWartosc(double wartosc) {
        this.wartosc = wartosc;
    }

    public String getMiara() {
        return miara;
    }

    public void setMiara(String miara) {
        this.miara = miara;
    }

    public String getRokBadania() {
        return rokBadania;
    }

    public void setRokBadania(String rokBadania) {
        this.rokBadania = rokBadania;
    }

    public String getWojewodztwo() {
        return wojewodztwo;
    }

    public void setWojewodztwo(String wojewodztwo) {
        this.wojewodztwo = wojewodztwo;
    }





}
