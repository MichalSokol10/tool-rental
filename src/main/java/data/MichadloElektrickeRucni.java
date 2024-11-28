package data;

import java.util.Locale;

// Třída MichadloElektrickeRucni dědí z třídy Naradi
public class MichadloElektrickeRucni extends Naradi {

    private double maximalniOtacky;

    // Konstruktor pro inicializaci s maximálními otáčkami a hmotností
    public MichadloElektrickeRucni(double maximalniOtacky, double hmotnost) {
        super(TypyNaradi.MICHADLO_ELEKTRICKE_RUCNI, hmotnost);
        
        this.maximalniOtacky = maximalniOtacky;
    }
    
    // Alternativní konstruktor, který přijímá také ID
    public MichadloElektrickeRucni(int i, double maximalniOtacky, double hmotnost) {
        super(i, TypyNaradi.MICHADLO_ELEKTRICKE_RUCNI, hmotnost);
        
        this.maximalniOtacky = maximalniOtacky;
    }

    // Getter pro maximální otáčky
    public double getMaximalniOtacky() {
        return maximalniOtacky;
    }

    // Přepsání metody toString pro formátovaný výstup
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "ID=%d, typ=%s, hmotnost=%.2f, maximálníOtáčky=%.2f", getID(), getTyp().nazev(), getHmotnost(), getMaximalniOtacky());
    }
}
