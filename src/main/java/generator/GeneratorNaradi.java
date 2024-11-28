package generator;

import data.AkuSroubovak;
import data.BruskaVibracni;
import data.MichadloElektrickeRucni;
import data.Naradi;

/**
 * Třída pro generování náhodných objektů nářadí.
 */
public class GeneratorNaradi {

    // Generuje náhodný objekt nářadí
    public Naradi generuj() {

            int typ = (int) (Math.random() * 3) + 1;
            double hmotnost = (Math.random() * 10) + 1;
            int pracovniNapeti = (int) ((Math.random() * 10) + 1);
            double vykon = (Math.random() * 10) + 1;
            double maximalniOtacky = (Math.random() * 10) + 1;

            switch (typ) {
                case 1:
                    AkuSroubovak sroubovak = new AkuSroubovak(hmotnost, pracovniNapeti);
                    return sroubovak;
                case 2:
                    BruskaVibracni bruska = new BruskaVibracni(hmotnost, vykon);
                    return bruska;
                case 3:
                    MichadloElektrickeRucni michadlo = new MichadloElektrickeRucni(maximalniOtacky, hmotnost);
                    return michadlo;
                default:
                    break;
            }
        return null;
    }
}
