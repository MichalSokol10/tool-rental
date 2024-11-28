package data;

/**
 * Výčtový typ (enum) pro reprezentaci různých typů nářadí.
 */
public enum TypyNaradi {
    AKU_SROUBOVAK("aku šroubovák"),
    MICHADLO_ELEKTRICKE_RUCNI("míchadlo elektrické ruční"),
    BRUSKA_VIBRACNI("bruska vibrační");
    
    private final String nazev;

    private TypyNaradi(String nazev) {
        this.nazev = nazev;
    }

    // Getter pro název typu nářadí
    public String nazev() {
        return nazev;
    }

    // Přetížená metoda toString pro vrácení názvu typu nářadí.
    @Override
    public String toString() {
        return nazev;
    }
}
