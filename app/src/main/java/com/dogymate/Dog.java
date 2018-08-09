package com.dogymate;

import java.util.ArrayList;

class Dog {
    String kuceceime;
    String kucecegodine;
    String kucecipol;
    String kucecarasa;
    ArrayList<String> kucecikarakter=new ArrayList<>();
    String kuceciopis;
    String kucecisprayed;

    public String getKuceceime() {
        return kuceceime;
    }

    public String getKucecegodine() {
        return kucecegodine;
    }

    public String getKucecipol() {
        return kucecipol;
    }

    public String getKucecarasa() {
        return kucecarasa;
    }

    public ArrayList<String> getKucecikarakter() {
        return kucecikarakter;
    }

    public String getKuceciopis() {
        return kuceciopis;
    }

    public String getKucecisprayed() {
        return kucecisprayed;
    }

    public Dog(String kuceceime, String kucecegodine, String kucecipol, String kucecarasa, ArrayList<String> kucecikarakter, String kuceciopis, String kucecisprayed) {
        this.kuceceime = kuceceime;
        this.kucecegodine = kucecegodine;
        this.kucecipol = kucecipol;
        this.kucecarasa = kucecarasa;
        this.kucecikarakter = kucecikarakter;
        this.kuceciopis = kuceciopis;
        this.kucecisprayed = kucecisprayed;

    }
}


