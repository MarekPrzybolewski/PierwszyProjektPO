package Projekt;


import java.util.StringTokenizer;

import static Projekt.Main.stringToDouble;

public class Filtr {
    int idWojewodztwo;
    String wojewodztwo;
    String artykul;
    int rok;
    double wartosc;
    String miara;
    boolean correct;

    public Filtr(String line) {
        StringTokenizer stringTokenizer = new StringTokenizer(line,";");
        stringTokenizer.nextToken(); // filtrowanie z pierwszego elementu z lini
        wojewodztwo = stringTokenizer.nextToken(); // nastepny fragment odpowiada za wojewodztwo wiec wpisuje je do zmiennej wojewodztwo
        wojewodztwo = wojewodztwo.toLowerCase();
        wojewodztwo = wojewodztwo.substring(0,1).toUpperCase() + wojewodztwo.substring(1);
        artykul = stringTokenizer.nextToken();
        String changeRok;
        changeRok = stringTokenizer.nextToken();
        //zamiana String na int dla pola Rok
        rok = Integer.parseInt(changeRok);
        String checkWartosc = stringTokenizer.nextToken();
        if(checkWartosc.equals("-")) {
            wartosc = -1.0;
        }
        else{
            wartosc = stringToDouble(checkWartosc);
        }

        miara = stringTokenizer.nextToken();
        // filtrowanie ktore eliminuje rekordy ktore nie maja wojewodztwa
        switch(wojewodztwo) {

            case "Łódzkie":
                idWojewodztwo = 1;
                correct = true;
                break;

            case "Mazowieckie":
                idWojewodztwo = 2;
                correct = true;
                break;

            case "Małopolskie":
                idWojewodztwo = 3;
                correct = true;
                break;

            case "Śląskie":
                idWojewodztwo = 4;
                correct = true;
                break;

            case "Lubelskie":
                idWojewodztwo = 5;
                correct = true;
                break;

            case "Podkarpackie":
                idWojewodztwo = 6;
                correct = true;
                break;

            case "Podlaskie":
                idWojewodztwo = 7;
                correct = true;
                break;

            case "Świętokrzyskie":
                idWojewodztwo = 8;
                correct = true;
                break;

            case "Lubuskie":
                idWojewodztwo = 9;
                correct = true;
                break;

            case "Wielkopolskie":
                idWojewodztwo = 10;
                correct = true;
                break;

            case "Zachodniopomorskie":
                idWojewodztwo = 11;
                correct = true;
                break;

            case "Dolnośląskie":
                idWojewodztwo = 12;
                correct = true;
                break;

            case "Opolskie":
                idWojewodztwo = 13;
                correct = true;
                break;

            case "Kujawsko-pomorskie":
                idWojewodztwo = 14;
                correct = true;
                break;

            case "Pomorskie":
                idWojewodztwo = 15;
                correct = true;
                break;

            case "Warmińsko-mazurskie":
                idWojewodztwo = 16;
                correct = true;
                break;

            default:
                correct = false;
                break;
        }

    }

    @Override
    public String toString() {
        return "Filtr{" +
                "wojewodztwo='" + wojewodztwo + '\'' +
                ", artykul='" + artykul + '\'' +
                ", rok='" + rok + '\'' +
                ", wartosc=" + wartosc +
                ", miara='" + miara + '\'' +
                ", correct=" + correct +
                '}';
    }

    public String getWojewodztwo() {
        return wojewodztwo;
    }

    public int getIdWojewodztwo() { return idWojewodztwo;}

    public String getArtykul() {
        return artykul;
    }

    public int getRok() {
        return rok;
    }

    public double getWartosc() {
        return wartosc;
    }

    public String getMiara() {
        return miara;
    }

    public boolean isCorrect() {
        return correct;
    }
}


