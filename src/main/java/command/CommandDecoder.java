package command;

import data.AkuSroubovak;
import data.BruskaVibracni;
import data.MichadloElektrickeRucni;
import data.Naradi;
import generator.GeneratorNaradi;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;
import kolekce.KolekceException;
import kolekce.SpojovySeznam;
import perzistence.PraceSeSoubory;

/**
 * Třída implementuje textové uživatelské rozhraní (CLI), které 
 * umožňuje uživateli ovládat spojový seznam pomocí zadaných příkazů. 
 * 
 * Uživatel může zadávat příkazy prostřednictvím konzole a provádět různé operace nad 
 * daty uloženými v seznamu, jako je přidávání, mazání, úpravy, vyhledávání nebo 
 * ukládání dat do souborů..
 */
public class CommandDecoder {

    public static void main(String[] args) throws KolekceException, IOException, ClassNotFoundException {
        boolean pruchod = true;
        SpojovySeznam seznam = new SpojovySeznam();
        PraceSeSoubory prace = new PraceSeSoubory();

        System.out.println("-------------------------------------------------------------------");
        System.out.println("Seznam příkazů:");
        System.out.println(" help, h     - výpis příkazů");
        System.out.println(" novy,no     - vytvoř novou instanci a vlož data za aktuální prvek");
        System.out.println(" najdi,na,n  - najdi v seznamu data podle hodnoty nějakého atributu");
        System.out.println(" odeber,od   - odeber data ze seznamu podle nějaké hodnoty atributu");
        System.out.println(" dej         - zobraz aktuální data v seznamu");
        System.out.println(" edituj,edit - edituj aktuální data v seznamu");
        System.out.println(" vyjmi       - vyjmi aktuální data ze seznamu");
        System.out.println(" prvni,pr    - nastav jako aktuální první data v seznamu");
        System.out.println(" dalsi,da    - přejdi na další data");
        System.out.println(" posledni,po - přejdi na poslední data");
        System.out.println(" pocet       - zobraz počet položek v seznamu");
        System.out.println(" obnov       - obnov seznam dat z binárního souboru");
        System.out.println(" zalohuj     - zálohuj seznam dat do binárního souboru");
        System.out.println(" vypis       - zobraz seznam dat");
        System.out.println(" nactitext,nt- načti seznam dat z textového souboru");
        System.out.println(" uloztext,ut - ulož seznam dat do textového souboru");
        System.out.println(" generuj,g   - generuj náhodně data pro testovaní");
        System.out.println(" zrus        - zruš všechna data v seznamu");
        System.out.println(" exit        - ukončení programu");
        System.out.println("-------------------------------------------------------------------");

        while (pruchod) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("Zadejte příkaz: ");
            String prikaz = scanner.nextLine();

            switch (prikaz) {
                case "help":
                case "h":
                    System.out.println("-------------------------------------------------------------------");
                    System.out.println("Seznam příkazů:");
                    System.out.println(" help, h     - výpis příkazů");
                    System.out.println(" novy,no     - vytvoř novou instanci a vlož data za aktuální prvek");
                    System.out.println(" najdi,na,n  - najdi v seznamu data podle hodnoty nějakého atributu");
                    System.out.println(" odeber,od   - odeber data ze seznamu podle nějaké hodnoty atributu");
                    System.out.println(" dej         - zobraz aktuální data v seznamu");
                    System.out.println(" edituj,edit - edituj aktuální data v seznamu");
                    System.out.println(" vyjmi       - vyjmi aktuální data ze seznamu");
                    System.out.println(" prvni,pr    - nastav jako aktuální první data v seznamu");
                    System.out.println(" dalsi,da    - přejdi na další data");
                    System.out.println(" posledni,po - přejdi na poslední data");
                    System.out.println(" pocet       - zobraz počet položek v seznamu");
                    System.out.println(" obnov       - obnov seznam dat z binárního souboru");
                    System.out.println(" zalohuj     - zálohuj seznam dat do binárního souboru");
                    System.out.println(" vypis       - zobraz seznam dat");
                    System.out.println(" nactitext,nt- načti seznam dat z textového souboru");
                    System.out.println(" uloztext,ut - ulož seznam dat do textového souboru");
                    System.out.println(" generuj,g   - generuj náhodně data pro testovaní");
                    System.out.println(" zrus        - zruš všechna data v seznamu");
                    System.out.println(" exit        - ukončení programu");
                    System.out.println("-------------------------------------------------------------------");
                    break;
                case "novy":
                case "no":
                    int typNaradi;

                    do {
                        try {
                            System.out.println("-------------------------------------------------------------------");
                            System.out.println("1 - AkuSroubovak");
                            System.out.println("2 - BruskaVibracni");
                            System.out.println("3 - MichadloElektrickeRucni");
                            System.out.println("-------------------------------------------------------------------");
                            System.out.println("Jaké nářadí chcete? Zadejte 1-3: ");

                            typNaradi = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Chyba: Musíte zadat číslo.");
                            scanner.nextLine();
                            typNaradi = 0;
                        }
                    } while (typNaradi < 1 || typNaradi > 3);

                    double hmotnost;

                    do {
                        try {
                            System.out.print("Zadejte hmotnost nářadí: ");
                            hmotnost = scanner.nextDouble();
                            if (hmotnost <= 0) {
                                throw new Exception("Hmotnost nářadí musí být větší než 0.");
                            }
                        } catch (Exception e) {
                            System.out.println("Chyba: " + e.getMessage());
                            scanner.nextLine();
                            hmotnost = 0;
                        }
                    } while (hmotnost <= 0);

                    switch (typNaradi) {
                        case 1:
                            int pracovniNapeti;
                            do {
                                try {
                                    System.out.print("Zadejte pracovní napětí: ");
                                    pracovniNapeti = scanner.nextInt();
                                    if (pracovniNapeti <= 0) {
                                        throw new Exception("Pracovní napětí musí být větší než 0.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Chyba: " + e.getMessage());
                                    scanner.nextLine();
                                    pracovniNapeti = 0;
                                }
                            } while (pracovniNapeti <= 0);
                            AkuSroubovak sroubovak = new AkuSroubovak(hmotnost, pracovniNapeti);
                            seznam.vlozPosledni(sroubovak);
                            break;
                        case 2:
                            double vykon;
                            do {
                                try {
                                    System.out.print("Zadejte výkon: ");
                                    vykon = scanner.nextDouble();
                                    if (vykon <= 0) {
                                        throw new Exception("Výkon musí být větší než 0.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Chyba: " + e.getMessage());
                                    scanner.nextLine();
                                    vykon = 0;
                                }
                            } while (vykon <= 0);
                            BruskaVibracni bruska = new BruskaVibracni(hmotnost, vykon);
                            seznam.vlozPosledni(bruska);
                            break;
                        case 3:
                            double maximalniOtacky;
                            do {
                                try {
                                    System.out.print("Zadejte maximální otáčky: ");
                                    maximalniOtacky = scanner.nextDouble();
                                    if (maximalniOtacky <= 0) {
                                        throw new Exception("Maximální otáčky musí být větší než 0.");
                                    }
                                } catch (Exception e) {
                                    System.out.println("Chyba: " + e.getMessage());
                                    scanner.nextLine();
                                    maximalniOtacky = 0;
                                }
                            } while (maximalniOtacky <= 0);
                            MichadloElektrickeRucni michadlo = new MichadloElektrickeRucni(maximalniOtacky, hmotnost);
                            seznam.vlozPosledni(michadlo);
                            break;
                        default:
                            break;
                    }

                    break;
                case "najdi":
                case "na":
                case "n":
                    int atributVyhledavani;
                    do {
                        try {
                            System.out.println("1 - Druh nářadí");
                            System.out.println("2 - Hmotnost");
                            System.out.println("3 - Pracovní napětí");
                            System.out.println("4 - Výkon");
                            System.out.println("5 - Maximální otáčky");
                            System.out.println("Podle kterého atributu chcete vyhledávat?");
                            atributVyhledavani = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Chyba: Musíte zadat číslo.");
                            atributVyhledavani = 0;
                            scanner.nextLine();
                        }
                    } while (atributVyhledavani < 1 || atributVyhledavani > 5);

                    int druhNaradi;
                    int pracovniNapeti;
                    double vykon;
                    double maximalniOtacky;

                    if (atributVyhledavani == 1) {

                        do {
                            try {
                                System.out.println("1 - Aku šroubovák");
                                System.out.println("2 - Bruska vibrační");
                                System.out.println("3 - Míchadlo elektrické ruční");
                                System.out.println("Podle kterého druhu nářadí chcete vyhledávat?");
                                druhNaradi = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                druhNaradi = 0;
                                scanner.nextLine();
                            }
                        } while (druhNaradi < 1 || druhNaradi > 3);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();
                            if (druhNaradi == 1) {
                                if (obj instanceof AkuSroubovak) {
                                    System.out.println(obj.toString());
                                }
                            } else if (druhNaradi == 2) {
                                if (obj instanceof BruskaVibracni) {
                                    System.out.println(obj.toString());
                                }
                            } else {
                                if (obj instanceof MichadloElektrickeRucni) {
                                    System.out.println(obj.toString());
                                }
                            }
                        }

                    } else if (atributVyhledavani == 2) {

                        do {
                            try {
                                System.out.println("Kterou hodnotu hmotnosti chcete vyhledat?");
                                hmotnost = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                hmotnost = 0;
                                scanner.nextLine();
                            }
                        } while (hmotnost <= 0);

                        System.out.println("Hledám prvky s hmotností: " + hmotnost);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();
                            Naradi naradi = (Naradi) obj;
                            if (naradi.getHmotnost() == hmotnost) {
                                System.out.println(obj.toString());
                            }
                        }

                    } else if (atributVyhledavani == 3) {
                        do {
                            try {
                                System.out.println("Kterou hodnotu pracovního napětí chcete vyhledat?");
                                pracovniNapeti = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                pracovniNapeti = 0;
                                scanner.nextLine();
                            }
                        } while (pracovniNapeti <= 0);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();
                            if (obj instanceof AkuSroubovak) {
                                if (((AkuSroubovak) obj).getPracovniNapeti() == pracovniNapeti) {
                                    System.out.println(obj.toString());
                                }
                            }
                        }

                    } else if (atributVyhledavani == 4) {

                        do {
                            try {
                                System.out.println("Kterou hodnotu výkonu chcete vyhledat?");
                                vykon = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                vykon = 0;
                                scanner.nextLine();
                            }
                        } while (vykon <= 0);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();
                            if (obj instanceof BruskaVibracni) {
                                if (((BruskaVibracni) obj).getVykon() == vykon) {
                                    System.out.println(obj.toString());
                                }
                            }
                        }

                    } else {

                        do {
                            try {
                                System.out.println("Kterou hodnotu maximálních otáček chcete vyhledat?");
                                maximalniOtacky = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                maximalniOtacky = 0;
                                scanner.nextLine();
                            }
                        } while (maximalniOtacky <= 0);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();
                            if (obj instanceof MichadloElektrickeRucni) {
                                if (((MichadloElektrickeRucni) obj).getMaximalniOtacky() == maximalniOtacky) {
                                    System.out.println(obj.toString());
                                }
                            }
                        }
                    }
                    break;

                case "odeber":
                case "od":

                    do {
                        try {
                            System.out.println("1 - Druh nářadí");
                            System.out.println("2 - Hmotnost");
                            System.out.println("3 - Pracovní napětí");
                            System.out.println("4 - Výkon");
                            System.out.println("5 - Maximální otáčky");
                            System.out.println("Podle jakého atributu chcete odebrat prvky?");
                            atributVyhledavani = scanner.nextInt();
                        } catch (InputMismatchException e) {
                            System.out.println("Chyba: Musíte zadat číslo.");
                            atributVyhledavani = 0;
                            scanner.nextLine();
                        }
                    } while (atributVyhledavani < 1 || atributVyhledavani > 5);

                    if (atributVyhledavani == 1) {

                        do {
                            try {
                                System.out.println("1 - Aku šroubovák");
                                System.out.println("2 - Bruska vibrační");
                                System.out.println("3 - Míchadlo elektrické ruční");
                                System.out.println("Podle kterého druhu nářadí chcete odebrat prvky?");
                                druhNaradi = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                druhNaradi = 0;
                                scanner.nextLine();
                            }
                        } while (druhNaradi < 1 || druhNaradi > 3);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();

                            seznam.nastavPrvni();
                            while (obj != seznam.dejAktualni()) {
                                seznam.dalsi();
                            }

                            if (druhNaradi == 1) {
                                if (obj instanceof AkuSroubovak) {
                                    seznam.odeberAktualni();
                                }
                            } else if (druhNaradi == 2) {
                                if (obj instanceof BruskaVibracni) {
                                    seznam.odeberAktualni();
                                }
                            } else {
                                if (obj instanceof MichadloElektrickeRucni) {
                                    seznam.odeberAktualni();
                                }
                            }
                        }

                    } else if (atributVyhledavani == 2) {

                        do {
                            try {
                                System.out.println("Kterou hodnotu hmotnosti chcete odebrat?");
                                hmotnost = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                hmotnost = 0;
                                scanner.nextLine();
                            }
                        } while (hmotnost <= 0);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();

                            seznam.nastavPrvni();

                            while (obj != seznam.dejAktualni()) {
                                seznam.dalsi();
                            }

                            if (((Naradi) obj).getHmotnost() == hmotnost) {
                                seznam.odeberAktualni();
                            }
                        }

                    } else if (atributVyhledavani == 3) {
                        do {
                            try {
                                System.out.println("Kterou hodnotu pracovního napětí chcete odebrat?");
                                pracovniNapeti = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                pracovniNapeti = 0;
                                scanner.nextLine();
                            }
                        } while (pracovniNapeti <= 0);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();

                            seznam.nastavPrvni();

                            while (obj != seznam.dejAktualni()) {
                                seznam.dalsi();
                            }

                            if (obj instanceof AkuSroubovak) {
                                if (((AkuSroubovak) obj).getPracovniNapeti() == pracovniNapeti) {
                                    seznam.odeberAktualni();
                                }
                            }
                        }

                    } else if (atributVyhledavani == 4) {

                        do {
                            try {
                                System.out.println("Kterou hodnotu výkonu chcete odebrat?");
                                vykon = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                vykon = 0;
                                scanner.nextLine();
                            }
                        } while (vykon <= 0);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();

                            seznam.nastavPrvni();

                            while (obj != seznam.dejAktualni()) {
                                seznam.dalsi();
                            }

                            if (obj instanceof BruskaVibracni) {
                                if (((BruskaVibracni) obj).getVykon() == vykon) {
                                    seznam.odeberAktualni();
                                }
                            }
                        }

                    } else {

                        do {
                            try {
                                System.out.println("Kterou hodnotu maximálních otáček chcete vyhledat?");
                                maximalniOtacky = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                maximalniOtacky = 0;
                                scanner.nextLine();
                            }
                        } while (maximalniOtacky <= 0);

                        Iterator iterator = seznam.iterator();
                        while (iterator.hasNext()) {
                            Object obj = iterator.next();

                            seznam.nastavPrvni();

                            while (obj != seznam.dejAktualni()) {
                                seznam.dalsi();
                            }

                            if (obj instanceof MichadloElektrickeRucni) {
                                if (((MichadloElektrickeRucni) obj).getMaximalniOtacky() == maximalniOtacky) {
                                    seznam.odeberAktualni();
                                }
                            }
                        }
                    }

                    break;

                case "dej":
                    try {
                    System.out.println(seznam.dejAktualni().toString());
                } catch (Exception e) {
                    System.out.println("Nebyl nastaven aktuální prvek.");
                }
                break;
                case "edituj":
                case "edit":
                    try {
                    System.out.println(seznam.dejAktualni().toString());
                } catch (Exception e) {
                    System.out.println("Nebyl zvolen aktuální prvek.");
                    break;
                }
                    
                System.out.println("Jak chcete změnit následující prvek?");

                do {
                    try {
                        System.out.println("-------------------------------------------------------------------");
                        System.out.println("1 - AkuSroubovak");
                        System.out.println("2 - BruskaVibracni");
                        System.out.println("3 - MichadloElektrickeRucni");
                        System.out.println("-------------------------------------------------------------------");
                        System.out.println("Jaké nářadí chcete? Zadejte 1-3: ");

                        typNaradi = scanner.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("Chyba: Musíte zadat číslo.");
                        typNaradi = 0;
                        scanner.nextLine();
                    }
                } while (typNaradi < 1 || typNaradi > 3);

                do {
                    try {
                        System.out.print("Zadejte hmotnost nářadí: ");
                        hmotnost = scanner.nextDouble();
                    } catch (InputMismatchException e) {
                        System.out.println("Chyba: Musíte zadat číslo.");
                        hmotnost = 0;
                        scanner.nextLine();
                    }
                } while (hmotnost <= 0);

                switch (typNaradi) {
                    case 1:
                        do {
                            try {
                                System.out.print("Zadejte pracovní napětí: ");
                                pracovniNapeti = scanner.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                pracovniNapeti = 0;
                                scanner.nextLine();
                            }
                        } while (pracovniNapeti <= 0);
                        AkuSroubovak sroubovak = new AkuSroubovak(hmotnost, pracovniNapeti);
                        seznam.vlozZaAktualni(sroubovak);
                        seznam.odeberAktualni();
                        break;
                    case 2:
                        do {
                            try {
                                System.out.print("Zadejte výkon: ");
                                vykon = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                vykon = 0;
                                scanner.nextLine();
                            }
                        } while (vykon <= 0);
                        BruskaVibracni bruska = new BruskaVibracni(hmotnost, vykon);
                        seznam.vlozZaAktualni(bruska);
                        seznam.odeberAktualni();
                        break;
                    case 3:
                        do {
                            try {
                                System.out.print("Zadejte maximální otáčky: ");
                                maximalniOtacky = scanner.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("Chyba: Musíte zadat číslo.");
                                maximalniOtacky = 0;
                                scanner.nextLine();
                            }
                        } while (maximalniOtacky <= 0);
                        MichadloElektrickeRucni michadlo = new MichadloElektrickeRucni(maximalniOtacky, hmotnost);
                        seznam.vlozZaAktualni(michadlo);
                        seznam.odeberAktualni();
                        break;
                    default:
                        break;
                }
                break;

                case "vyjmi":
                    try {
                    seznam.odeberAktualni();
                } catch (Exception e) {
                    System.out.println("Nebyl zvolen aktuální prvek.");
                }
                break;
                case "prvni":
                case "pr":
                    try {
                    seznam.nastavPrvni();
                } catch (Exception e) {
                    System.out.println("Seznam je prázdný, nelze provést tuto operaci.");
                }
                break;
                case "dalsi":
                case "da":
                    try {
                    seznam.dalsi();
                } catch (Exception e) {
                    System.out.println("Žádný další prvek v seznamu není.");
                }
                break;
                case "posledni":
                case "po":
                    
                    try {
                    seznam.nastavPosledni();
                } catch (Exception e) {
                    System.out.println("Seznam je prázdný, nelze provést tuto operaci.");
                }

                break;
                case "pocet":
                    if (seznam.size() == 0) {
                        System.out.println("Seznam je prázdný.");
                        break;
                    }

                    System.out.println("Počet prvků v seznamu je: " + seznam.size());
                    break;
                case "obnov":
                    try {
                    seznam = prace.nactiBinarni("zaloha.bin");
                } catch (Exception e) {
                    System.out.println("Obnova seznamu se nezdařila.");
                }
                break;
                case "zalohuj":
                    try {
                    prace.ulozBinarni(seznam, "zaloha.bin");
                } catch (Exception e) {
                    System.out.println("Záloha seznamu se nezdařila.");
                }
                break;
                case "vypis":
                    if (seznam.size() == 0) {
                        System.out.println("Seznam je prázdný.");
                    }

                    Iterator iterator = seznam.iterator();

                    while (iterator.hasNext()) {
                        System.out.println(iterator.next().toString());
                    }
                    break;
                case "nactitext":
                case "nt":
                    try {
                    seznam = prace.nactiTxt("zaloha.txt");
                } catch (Exception e) {
                    System.out.println("Nepodařilo se načíst textovou zálohu.");
                }
                break;
                case "uloztext":
                case "ut":
                    try {
                    prace.ulozTxt(seznam, "zaloha.txt");
                } catch (Exception e) {
                    System.out.println("Nepodařilo se uložit textovou zálohu.");
                }
                break;
                case "generuj":
                case "g":
                    int pocetPrvku = 0;
                    boolean vstup = false;

                    while (!vstup || pocetPrvku < 1) {
                        System.out.println("Kolik prvků chcete vygenerovat?");
                        try {
                            pocetPrvku = scanner.nextInt();
                            vstup = true;
                        } catch (InputMismatchException e) {
                            System.out.println("Neplatný vstup, zadejte celé číslo.");
                            scanner.nextLine();
                        }
                    }
                    
                    seznam.zrus();

                    GeneratorNaradi generovani = new GeneratorNaradi();
                    for (int i = 0; i < pocetPrvku; i++) {
                        seznam.vlozPosledni(generovani.generuj());
                    }

                    System.out.println("Bylo vygenerováno: " + pocetPrvku);
                    break;
                case "zrus":
                    seznam.zrus();
                    break;
                case "exit":
                    System.out.println("Ukončil jste program.");
                    pruchod = false;
                    break;
                default:
                    System.out.println("Zadal jste neplatný příkaz!");
                    break;
            }
        }
    }
}
