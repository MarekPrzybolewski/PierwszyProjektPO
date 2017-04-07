package Projekt;


public class Wojewodztwo {
    private int idWojewodztwo;
    private String nazwa;
    private String reigon;

    public Wojewodztwo(int idWojewodztwo, String nazwa, String reigon) {
        this.idWojewodztwo = idWojewodztwo;
        this.nazwa = nazwa;
        this.reigon = reigon;
    }

    public int getIdWojewodztwo() {
        return idWojewodztwo;
    }

    public void setIdWojewodztwo(int idWojewodztwo) {
        this.idWojewodztwo = idWojewodztwo;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getReigon() {
        return reigon;
    }

    public void setReigon(String reigon) {
        this.reigon = reigon;
    }

    @Override
    public String toString() {
        return "Wojewodztwo{" + "idWojewodztwo=" + idWojewodztwo +  ", nazwa='" + nazwa + '\'' +  ", reigon='" + reigon + '\'' +  '}';
    }

}
