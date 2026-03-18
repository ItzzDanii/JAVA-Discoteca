import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Singleton {

    Semaphore S_fila_online = new Semaphore(20);
    Semaphore S_fila_loco = new Semaphore(50);
    Semaphore S_guardaroba = new Semaphore(10);
    Semaphore S_discoteca = new Semaphore(400);
    Semaphore S_bar = new Semaphore(10);
    Semaphore S_bagno = new Semaphore(5);

    final int COSTO_TICKET_ONLINE = 10;
    final int COSTO_TICKET_LOCO_M = 20;
    final int COSTO_TICKET_LOCO_F = 15;
    final int COSTO_GUARDAROBA = 5;
    final int COSTO_DRINK = 10;

    long chiusura_serata = 60000;
    long tempo_trascorso = 00000;

    boolean ingressoAperto = true;
    boolean barAperto = true;
    boolean musicaAccesa = true;
    boolean bagnoAperto = true;
    boolean metàserata = false;

    int num_drink_acquistati = 0;
    int num_persone_online = 0;
    int num_persone_loco = 0;
    long tempo_medio = 0;
    int num_persone_metà_serata = 0;
    float guadagno_totale = 0;

    List<Persona> listFilaOnline = new ArrayList<>(20);    
    List<Persona> listFilaLoco = new ArrayList<>(50);
    List<Persona> listGuardaroba = new ArrayList<>(10);
    List<Persona> listDiscoteca = new ArrayList<>(400);
    List<Persona> listBar = new ArrayList<>(10);
    List<Persona> listBagniOccupati = new ArrayList<>(5);
    List<Persona> listBagniAttesa = new ArrayList<>();

    private Singleton(){

    }

    public void vaiFilaOnline(Persona p){
        num_persone_online++;

        if(!ingressoAperto)
            return;

        boolean acquireSuccess = false;

            try {
                S_fila_online.acquire();
                acquireSuccess=true;

                guadagno_totale+=COSTO_TICKET_ONLINE;

                System.out.println(p.nome + " (online)");

                p.zona = 1;
                listFilaOnline.add(p);

                Thread.sleep(10 + p.rand.nextInt(5));
                
                listFilaOnline.remove(p);
                vaiInGuardaroba(p);
            } catch (Exception e) {}

            if(acquireSuccess)
                S_fila_online.release();
    }

   public void vaiFilaLoco(Persona p){
        if(!ingressoAperto)
            return;

        boolean acquireSuccess = false;

        try {
            S_fila_loco.acquire();
            acquireSuccess = true;

            System.out.println(p.nome + " (loco)");

            p.zona = 2;
            listFilaLoco.add(p);

            Thread.sleep(20 + p.rand.nextInt(10));

            if(p.conto - COSTO_TICKET_LOCO_M <= 0 ||
            p.conto - COSTO_TICKET_LOCO_F <= 0){
                System.out.println(p.nome + " (no soldi x loco)");
                listFilaLoco.remove(p);
                S_fila_loco.release();
                return;
            }

            if(p.sesso == 0){
                p.conto -= COSTO_TICKET_LOCO_M;
                guadagno_totale+=COSTO_TICKET_LOCO_M;
                num_persone_loco++;
            }else{
                p.conto -= COSTO_TICKET_LOCO_F;
                guadagno_totale+=COSTO_TICKET_LOCO_F;
                num_persone_loco++;
            }

            listFilaLoco.remove(p);
            vaiInGuardaroba(p);

        } catch (Exception e) {}

        if(acquireSuccess)
            S_fila_loco.release();
    }

    public void vaiInGuardaroba(Persona p){
        if(!metàserata)
            num_persone_metà_serata++;

        boolean acquireSuccess = false;

        try {
            S_guardaroba.acquire();
            acquireSuccess = true;

            System.out.println(p.nome+" (guardaroba)");

            p.zona = 3;
            listGuardaroba.add(p);

            if(p.lasciaGiubbetto && p.conto - COSTO_GUARDAROBA > 0){
                p.conto -= COSTO_GUARDAROBA;
                guadagno_totale+=COSTO_GUARDAROBA;
                p.id_locker = p.rand.nextInt(400);
                Thread.sleep(25 + p.rand.nextInt(10));
            } else {
                Thread.sleep(0);
            }

            listGuardaroba.remove(p);
            vaiABallare(p);

        } catch (Exception e) {}

        if(acquireSuccess)
            S_guardaroba.release();
    }

    public void vaiABallare(Persona p){
        if(!musicaAccesa){
            p.zona = 0;
            listDiscoteca.remove(p);
            S_discoteca.release();
            return;
        }

        boolean acquireSuccess = false;
        try {
            S_discoteca.acquire();
            acquireSuccess = true;

            p.zona = 4;
            System.out.println(p.nome + " (balla)");
            listDiscoteca.add(p);
            while(true){
                float scelta = p.rand.nextFloat();

                if(scelta < 0.05){
                    System.out.println(p.nome + " (uscito)");
                    listDiscoteca.remove(p);
                    p.zona = 0;
                    S_discoteca.release();
                    return;
                }
                else if(scelta < 0.20){
                    listDiscoteca.remove(p);
                    vaiAlBar(p);
                    return;
                }
                else{
                    Thread.sleep(50 + p.rand.nextInt(100));
                }

            }
            
        } catch (Exception e) {}

        if(acquireSuccess)
            S_discoteca.release();
    }

    public void vaiAlBar(Persona p){
        if(!barAperto){
            vaiABallare(p);
            return;
        }

        if(S_bar.tryAcquire()) {
            try {
                System.out.println(p.nome+" (bar)");
                if(p.conto - COSTO_DRINK > 0){
                    p.conto -= COSTO_DRINK;
                    guadagno_totale+=COSTO_DRINK;
                    num_drink_acquistati++;
                    listBar.add(p);
                    p.zona = 5;

                    Thread.sleep(20 + p.rand.nextInt(60));
                    Thread.sleep(100 + p.rand.nextInt(100));

                    listBar.remove(p);
                    vaiInBagno(p);
                } else {
                    vaiABallare(p);
                }

            } catch (Exception e) {}
        } else vaiABallare(p);
    }

    public void vaiInBagno(Persona p){
        if(!bagnoAperto){
            vaiABallare(p);
            S_bagno.release();
            return;
        }

        boolean acquireSuccess = false;
        try {
            S_bagno.acquire();
            acquireSuccess = true;

            System.out.println(p.nome+" (bagno)");

            p.zona = 6;
            listBagniOccupati.add(p);

            Thread.sleep(40 + p.rand.nextInt(10));

            listBagniOccupati.remove(p);
            vaiABallare(p);

        } catch (Exception e) {}

        if(acquireSuccess)
            S_bagno.release();
    }

    public void chiudiIngresso(){
        ingressoAperto = false;
    }

    public void chiudiBar(){
        barAperto = false;
    }

    public void spegniMusica(){
        musicaAccesa = false;
    }

    public void chiudiBagni(){
        bagnoAperto = false;
    }

    private static Singleton instance;

    public synchronized static Singleton getInstance(){
        if(instance == null)
            instance = new Singleton();
        return instance;
    }
}
