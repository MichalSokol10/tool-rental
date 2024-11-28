package data;

import java.util.Locale;

// Třída AkuSroubovak dědí z třídy Naradi
public class AkuSroubovak extends Naradi {
    private int pracovniNapeti;

    // Konstruktor pro inicializaci s hmotností a pracovním napětím
    public AkuSroubovak(double hmotnost, int pracovniNapeti) {
        super(TypyNaradi.AKU_SROUBOVAK, hmotnost); 
   
        if (pracovniNapeti < 1) {
            throw new IllegalArgumentException("Pracovní napětí musí být větší než 0.");
        }
        
        this.pracovniNapeti = pracovniNapeti;
    }
    
    // Alternativní konstruktor, který přijímá také ID
    public AkuSroubovak(int i, double hmotnost, int pracovniNapeti) {
        super(i, TypyNaradi.AKU_SROUBOVAK, hmotnost); 
   
        if (pracovniNapeti < 1) {
            throw new IllegalArgumentException("Pracovní napětí musí být větší než 0.");
        }
        
        this.pracovniNapeti = pracovniNapeti;
    }

    // Getter pro pracovní napětí
    public int getPracovniNapeti() {
        return pracovniNapeti;
    }

    // Přepsání metody toString pro formátovaný výstup
    @Override
    public String toString() {
        return String.format(Locale.ENGLISH, "ID=%d, typ=%s, hmotnost=%.2f, pracovníNapětí=%d", getID(),getTyp().nazev(), getHmotnost(), getPracovniNapeti());
    }
}
