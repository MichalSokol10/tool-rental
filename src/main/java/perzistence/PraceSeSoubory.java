package perzistence;

import data.AkuSroubovak;
import data.BruskaVibracni;
import data.MichadloElektrickeRucni;
import data.Naradi;
import data.TypyNaradi;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import kolekce.SpojovySeznam;

/**
 * Třída pro práci se soubory, která obsahuje metody pro ukládání a načítání kolekce nářadí.
 * Tato třída podporuje ukládání nářadí do textových a binárních souborů, načítání nářadí z textových a binárních souborů.
 */
public class PraceSeSoubory {

    /**
    * Uloží seznam nářadí do textového souboru ve formátu CSV.
    * 
    * @param seznam Seznam nářadí, který bude uložen do souboru.
    * @param nazevSouboru Název souboru, do kterého bude seznam nářadí uložen.
    * @throws IOException Pokud dojde k chybě při práci se souborem.
    */
    public static void ulozTxt(SpojovySeznam<Naradi> seznam, String nazevSouboru) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(nazevSouboru, false));
        String str = "";
        Iterator iterator = seznam.iterator();

        while (iterator.hasNext()) {
            Object obj = iterator.next();
            str += ((Naradi) obj).getID();
            str += ", ";
            str += ((Naradi) obj).getTyp();
            str += ", ";
            str += ((Naradi) obj).getHmotnost();
            str += ", ";
            if (obj instanceof AkuSroubovak) {
                str += ((AkuSroubovak) obj).getPracovniNapeti();
                str += "\n";
            } else if (obj instanceof BruskaVibracni) {
                str += ((BruskaVibracni) obj).getVykon();
                str += "\n";
            } else {
                str += ((MichadloElektrickeRucni) obj).getMaximalniOtacky();
                str += "\n";
            }
            writer.write(str);
            writer.flush();
            str = "";
        }
        writer.close();
    }

    /**
    * Načte seznam nářadí z textového souboru ve formátu CSV.
    * 
    * @param nazevSouboru Název souboru, ze kterého bude seznam nářadí načten.
    * @return Načtený seznam nářadí.
    * @throws IOException Pokud dojde k chybě při práci se souborem.
    */
    public static SpojovySeznam<Naradi> nactiTxt(String nazevSouboru) throws IOException {
        SpojovySeznam<Naradi> seznam = new SpojovySeznam<>();
        BufferedReader reader = new BufferedReader(new FileReader(nazevSouboru));
        String radek;
        while ((radek = reader.readLine()) != null) {
            String[] data = radek.split(", ");
            int id = Integer.parseInt(data[0]);
            String typ = data[1];
            double hmotnost = Double.parseDouble(data[2]);
            switch (typ) {
                case "aku šroubovák":
                    int pracovniNapeti = Integer.parseInt(data[3]);
                    seznam.vlozPosledni(new AkuSroubovak(id, hmotnost, pracovniNapeti));
                    break;
                case "bruska vibrační":
                    double vykon = Double.parseDouble(data[3]);
                    seznam.vlozPosledni(new BruskaVibracni(id, hmotnost, vykon));
                    break;
                default:
                    double maximalniOtacky = Double.parseDouble(data[3]);
                    seznam.vlozPosledni(new MichadloElektrickeRucni(id, maximalniOtacky, hmotnost));
                    break;
            }
        }
        reader.close();
        return seznam;
    }

    /**
    * Uloží seznam nářadí do binárního souboru.
    * 
    * @param seznam Seznam nářadí, který bude uložen do souboru.
    * @param nazevSouboru Název souboru, do kterého bude seznam nářadí uložen.
    * @throws IOException Pokud dojde k chybě při práci se souborem.
    */
    public static void ulozBinarni(SpojovySeznam<Naradi> seznam, String nazevSouboru) throws IOException {
        DataOutputStream stream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(nazevSouboru, false)));
        try {
            for (Naradi naradi : seznam) {
                stream.writeInt(naradi.getID());
                stream.writeUTF(naradi.getTyp().toString());
                stream.writeDouble(naradi.getHmotnost());
                if (naradi instanceof AkuSroubovak) {
                    AkuSroubovak aku = (AkuSroubovak) naradi;
                    stream.writeInt(aku.getPracovniNapeti());
                } else if (naradi instanceof BruskaVibracni) {
                    BruskaVibracni bruska = (BruskaVibracni) naradi;
                    stream.writeDouble(bruska.getVykon());
                } else if (naradi instanceof MichadloElektrickeRucni) {
                    MichadloElektrickeRucni michadlo = (MichadloElektrickeRucni) naradi;
                    stream.writeDouble(michadlo.getMaximalniOtacky());
                }
            }
        } finally {
            stream.close();
        }
    }

    /**
    * Načte seznam nářadí z binárního souboru.
    * 
    * @param nazevSouboru Název souboru, ze kterého bude seznam nářadí načten.
    * @return Načtený seznam nářadí.
    * @throws IOException Pokud dojde k chybě při práci se souborem.
    */
    public static SpojovySeznam<Naradi> nactiBinarni(String nazevSouboru) throws IOException {
        DataInputStream stream = new DataInputStream(new BufferedInputStream(new FileInputStream(nazevSouboru)));
        SpojovySeznam<Naradi> seznam = new SpojovySeznam<>();

        try {
            while (stream.available() > 0) {
                int id = stream.readInt();
                String typ = stream.readUTF();
                double hmotnost = stream.readDouble();

                if (typ.equals(TypyNaradi.AKU_SROUBOVAK.toString())) {
                    int pracovniNapeti = stream.readInt();
                    seznam.vlozPosledni(new AkuSroubovak(id, hmotnost, pracovniNapeti));
                } else if (typ.equals(TypyNaradi.BRUSKA_VIBRACNI.toString())) {
                    double vykon = stream.readDouble();
                    seznam.vlozPosledni(new BruskaVibracni(id, hmotnost, vykon));
                } else if (typ.equals(TypyNaradi.MICHADLO_ELEKTRICKE_RUCNI.toString())) {
                    double maximalniOtacky = stream.readDouble();
                    seznam.vlozPosledni(new MichadloElektrickeRucni(id, hmotnost, maximalniOtacky));
                } else {
                    throw new IOException("Chybný typ nástroje v souboru.");
                }
            }
        } finally {
            stream.close();
        }

        return seznam;
    }
}
