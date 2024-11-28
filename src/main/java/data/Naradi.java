package data;

import java.util.Locale;
import java.util.Objects;

/**
 * Třída reprezentuje obecné nářadí. 
 */
public abstract class Naradi implements Cloneable {
    private TypyNaradi typ;
    private double hmotnost;
    public static int dalsiId = 1;
    private int id;

    // Konstruktor pro inicializaci typu a hmotnosti nářadí
    public Naradi(TypyNaradi typ, double hmotnost) {
        if (hmotnost <= 0) {
            throw new IllegalArgumentException("Hmotnost musí být větší než 0.");
        }
        
        this.id = dalsiId;
        this.typ = typ;
        this.hmotnost = hmotnost;
        
        dalsiId++;
    }
    
    // Alternativní konstruktor, který přijímá ID
    public Naradi(int id, TypyNaradi typ, double hmotnost) {
        if (hmotnost <= 0) {
            throw new IllegalArgumentException("Hmotnost musí být větší než 0.");
        }
        
        this.id = id;
        this.typ = typ;
        this.hmotnost = hmotnost;
        
        dalsiId = ++id;
    }

    // Getter pro typ nářadí
    public TypyNaradi getTyp() {
        return typ;
    }

    // Getter pro hmotnost nářadí
    public double getHmotnost() {
        return hmotnost;
    }
    
    // Getter pro ID nářadí
    public int getID() {
        return id;
    }

    // Metoda hashCode pro generování unikátního hash kódu
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.typ);
        hash = 59 * hash + (int) (Double.doubleToLongBits(this.hmotnost) ^ (Double.doubleToLongBits(this.hmotnost) >>> 32));
        return hash;
    }

    // Metoda equals pro porovnání dvou objektů typu Naradi
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Naradi other = (Naradi) obj;
        if (Double.doubleToLongBits(this.hmotnost) != Double.doubleToLongBits(other.hmotnost)) {
            return false;
        }
        return this.typ == other.typ;
    }

    // Přepsání metody toString pro zobrazení informací o nářadí
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "ID= %d, typ=%s, hmotnost=%.2f", getID(), getTyp().nazev(), getHmotnost());
    }
}

