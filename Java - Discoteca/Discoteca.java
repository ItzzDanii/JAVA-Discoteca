import java.util.ArrayList;
import java.util.List;

public class Discoteca extends Thread{

    long start_time;
    List<Persona> listPersone;

    public Discoteca(){
        Singleton.getInstance().tempo_trascorso = 0;
        listPersone = new ArrayList<>();
    }

    @Override
    public void run() {

        start_time = System.currentTimeMillis();
        

        try {
           while (true) {
                long attuale = System.currentTimeMillis() - start_time;
                Singleton.getInstance().tempo_trascorso = attuale;

                if (attuale >= Singleton.getInstance().chiusura_serata) {
                    System.out.println("Serata terminata!");
                    break;
                }

                if(Singleton.getInstance().ingressoAperto){
                    Persona p = new Persona();
                    listPersone.add(p);
                    p.start();
                }

                if(attuale>=30000)
                    Singleton.getInstance().metàserata = true;

                if (attuale >= 45000) {
                    System.out.println("<<< INGRESSO CHIUSO! >>>");
                    Singleton.getInstance().chiudiIngresso();
                }
                if (attuale >= 50000) {
                    System.out.println("<<< BAR CHIUSO! >>>");
                    Singleton.getInstance().chiudiBar();
                }
                if (attuale >= 55000) {
                    System.out.println("<<< MUSICA SPENTA! >>>");
                    Singleton.getInstance().spegniMusica();
                }
                if (attuale >= 60000) {
                    System.out.println("<<< BAGNI CHIUSI! >>>");
                    Singleton.getInstance().chiudiBagni();
                }
                Thread.sleep(1000); 
            }
        } catch (Exception e) {}
    }
}
