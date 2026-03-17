public class Discoteca extends Thread{
    @Override
    public void run() {
        try {
            Singleton.getInstance().tempo_trascorso = System.currentTimeMillis();

        while (Singleton.getInstance().tempo_trascorso <= Singleton.getInstance().chiusura_serata) {

            Persona p = new Persona();
            p.start();

            
            Singleton.getInstance().tempo_trascorso = System.currentTimeMillis(); // aggiorna tempo

            if(Singleton.getInstance().tempo_trascorso >= 45000)
                Singleton.getInstance().chiudiIngresso();

            if(Singleton.getInstance().tempo_trascorso >= 50000)
                Singleton.getInstance().chiudiBar();
            
            if(Singleton.getInstance().tempo_trascorso >= 55000)
                Singleton.getInstance().spegniMusica();

            if(Singleton.getInstance().tempo_trascorso >= 60000)
                Singleton.getInstance().chiudiBagni();
            
        }
        } catch (Exception e) {}
    }
}
