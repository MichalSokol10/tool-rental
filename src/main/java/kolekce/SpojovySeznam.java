package kolekce;

import data.Naradi;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class SpojovySeznam<E> implements Seznam<E> {

    private Prvek<E> prvni;
    private Prvek<E> posledni;
    private Prvek<E> aktualni;
    private int pocetPrvku;

    public SpojovySeznam() {
        this.prvni = null;
        this.posledni = null;
        this.aktualni = null;
        this.pocetPrvku = 0;
    }

    @Override
    public void nastavPrvni() throws KolekceException {
        kontrolaPrazdnosti();

        this.aktualni = prvni;
    }

    private void kontrolaPrazdnosti() throws KolekceException {
        if (pocetPrvku == 0) {
            throw new KolekceException();
        }
    }

    @Override
    public void nastavPosledni() throws KolekceException {
        kontrolaPrazdnosti();

        this.aktualni = posledni;
    }

    @Override
    public void dalsi() throws KolekceException {
        if (aktualni == null || aktualni == posledni) {
            throw new KolekceException();
        }

        this.aktualni = this.aktualni.dalsi;
    }
    
    public void predchozi() throws KolekceException {
        if (aktualni == null || aktualni == prvni) {
            throw new KolekceException();
        }
        
        Prvek<E> prvek = prvni;
        while (prvek.dalsi != aktualni) {
            prvek = prvek.dalsi;
        }
        
        this.aktualni = prvek;
    }

    @Override
    public boolean jeDalsi() {
        return pocetPrvku > 1 && aktualni != posledni;
    }

    @Override
    public void vlozPrvni(E data) {
        kontrolaNullParametru(data);

        if (pocetPrvku == 0) {
            Prvek<E> novyPrvek = new Prvek<>(data);
            this.prvni = novyPrvek;
            this.posledni = novyPrvek;
        } else {
            Prvek<E> novyPrvek = new Prvek<>(data, prvni);
            this.prvni = novyPrvek;
        }

        pocetPrvku++;
    }

    private void kontrolaNullParametru(E data) throws NullPointerException {
        if (data == null) {
            throw new NullPointerException();
        }
    }

    @Override
    public void vlozPosledni(E data) {
        kontrolaNullParametru(data);

        Prvek<E> novyPrvek = new Prvek<>(data);

        if (pocetPrvku == 0) {
            this.prvni = novyPrvek;
            this.posledni = novyPrvek;
        } else {
            this.posledni.dalsi = novyPrvek;
            this.posledni = novyPrvek;
        }

        pocetPrvku++;
    }

    @Override
    public void vlozZaAktualni(E data) throws KolekceException {
        if (aktualni == null) {
            throw new KolekceException();
        }

        kontrolaNullParametru(data);

        Prvek<E> novyPrvek = new Prvek<>(data, aktualni.dalsi);
        this.aktualni.dalsi = novyPrvek;

        if (aktualni == posledni) {
            this.posledni = novyPrvek;
        }

        pocetPrvku++;
    }

    @Override
    public boolean jePrazdny() {
        return pocetPrvku == 0;
    }

    @Override
    public E dejPrvni() throws KolekceException {
        kontrolaPrazdnosti();

        return this.prvni.data;
    }

    @Override
    public E dejPosledni() throws KolekceException {
        kontrolaPrazdnosti();

        return this.posledni.data;
    }

    @Override
    public E dejAktualni() throws KolekceException {
        kontrolaPrazdnostiAktualni();

        return this.aktualni.data;
    }

    private void kontrolaPrazdnostiAktualni() throws KolekceException {
        if (pocetPrvku == 0 || aktualni == null) {
            throw new KolekceException();
        }
    }

    @Override
    public E dejZaAktualnim() throws KolekceException {
        kontrolaPrazdnostiAktualni();

        if (aktualni == posledni) {
            throw new NullPointerException();
        }

        return aktualni.dalsi.data;
    }

    @Override
    public E odeberPrvni() throws KolekceException {
        kontrolaPrazdnosti();

        E odebranyPrvek = this.prvni.data;

        if (prvni == aktualni) {
            aktualni = null;
        }

        if (prvni == posledni) {
            prvni = null;
            posledni = null;
        } else {
            prvni = prvni.dalsi;
        }

        pocetPrvku--;
        return odebranyPrvek;
    }

    @Override
    public E odeberPosledni() throws KolekceException {
        kontrolaPrazdnosti();

        E odebranyPrvek = this.posledni.data;

        if (posledni == aktualni) {
            aktualni = null;
        }

        if (posledni == prvni) {
            prvni = null;
            posledni = null;
        } else {
            Prvek<E> prvek = prvni;
            while (prvek.dalsi != posledni) {
                prvek = prvek.dalsi;
            }
            posledni = prvek;
            posledni.dalsi = null;
        }

        pocetPrvku--;
        return odebranyPrvek;
    }

    @Override
    public E odeberAktualni() throws KolekceException {
        kontrolaPrazdnostiAktualni();

        E odebranyPrvek = this.aktualni.data;

        if (aktualni == prvni) {
            odeberPrvni();
        } else if (aktualni == posledni) {
            odeberPosledni();
        } else {
            Prvek<E> prvek = prvni;
            while (prvek.dalsi != aktualni) {
                prvek = prvek.dalsi;
            }
            prvek.dalsi = aktualni.dalsi;
            pocetPrvku--;
        }

        aktualni = null;

        return odebranyPrvek;
    }

    @Override
    public E odeberZaAktualnim() throws KolekceException {
        kontrolaPrazdnostiAktualni();

        if (aktualni.dalsi == null) {
            throw new NullPointerException();
        }

        E odebranyPrvek = this.aktualni.dalsi.data;

        if (aktualni.dalsi != posledni) {
            this.aktualni.dalsi = aktualni.dalsi.dalsi;
            pocetPrvku--;
        } else {
            odeberPosledni();
        }

        return odebranyPrvek;
    }

    @Override
    public int size() {
        return this.pocetPrvku;
    }

    @Override
    public void zrus() {
        pocetPrvku = 0;
        prvni = null;
        posledni = null;
        aktualni = null;
    }

    public List<Naradi> toList() {
        List<Naradi> list = new ArrayList<>();
        Prvek<E> aktualni = prvni;
        while (aktualni != null) {
            list.add((Naradi) aktualni.data);
            aktualni = aktualni.dalsi;
        }
        return list;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {

            Prvek<E> prvek = prvni;

            @Override
            public boolean hasNext() {
                return prvek != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                } else {
                    E data = prvek.data;
                    prvek = prvek.dalsi;
                    return data;
                }
            }
        };
    }

    private class Prvek<E> {

        private E data;
        private Prvek<E> dalsi;

        public Prvek(E data, Prvek dalsi) {
            this.data = data;
            this.dalsi = dalsi;
        }

        public Prvek(E data) {
            this.data = data;
            this.dalsi = null;
        }

    }

}
