package data;

import java.util.Locale;

// Třída BruskaVibracni dědí z třídy Naradi
public class BruskaVibracni extends Naradi {
    private double vykon;

    // Konstruktor pro inicializaci s hmotností a výkonem
    public BruskaVibracni(double hmotnost, double vykon) {
        super(TypyNaradi.BRUSKA_VIBRACNI, hmotnost);
        
        if (vykon <= 0) {
            throw new IllegalArgumentException("Výkon brusky musí být větší než 0.");
        }
        
        this.vykon = vykon;
    }
    
    // Alternativní konstruktor, který přijímá také ID
    public BruskaVibracni(int i, double hmotnost, double vykon) {
        super(i, TypyNaradi.BRUSKA_VIBRACNI, hmotnost);
        
        if (vykon <= 0) {
            throw new IllegalArgumentException("Výkon brusky musí být větší než 0.");
        }
        
        this.vykon = vykon;
    }

    // Getter pro výkon
    public double getVykon() {
        return vykon;
    }

    // Přepsání metody toString pro formátovaný výstup
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "ID=%d, typ=%s, hmotnost=%.2f, výkon=%.2f", getID(),getTyp().nazev(), getHmotnost(), getVykon());
    }
}
