import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Persona extends Thread{
    float conto;
    boolean lasciaGiubbetto;
    String nome;
    int zona; // consulta legenda in fondo al file
    long temp_disco;
    float spesa_totale;
    int tipo_ticket; // 0 = online - 1 = loco
    int id_locker;
    int sesso; // 0 = maschio - 1 femmina

    Random rand;

    List<String> listNomi;

    public Persona(){
        // valori predefiniti
        listNomi = new ArrayList<>();
        riempiListNomi();
        zona = 0; //fuori discoteca
        temp_disco = 0;
        
        // valori random
        rand = new Random();
        conto = 0 + rand.nextFloat() * (500);
        conto = Math.round(conto);
        sesso = rand.nextInt(2);

        if (sesso == 0) {
            // nomi maschio 0-12
            nome = listNomi.get(rand.nextInt(13));
        } else {
            // nomi femmina 13-20
            nome = listNomi.get(13 + rand.nextInt(8));
        }
        
        tipo_ticket = rand.nextInt(2);
        
        if(tipo_ticket==1){
            if(conto-Singleton.getInstance().COSTO_TICKET_LOCO_M <= 0 || conto-Singleton.getInstance().COSTO_TICKET_LOCO_F<=0) // se non ha abbastanza soldi
                return;

            if(sesso==0)
                spesa_totale += Singleton.getInstance().COSTO_TICKET_LOCO_M;
            else spesa_totale+=Singleton.getInstance().COSTO_TICKET_LOCO_F;
        }

        if(rand.nextFloat() < 0.8) //80% che usi guardaroba
            lasciaGiubbetto = true;
        else lasciaGiubbetto = false;

        if(lasciaGiubbetto && conto-5>0){
            spesa_totale+=Singleton.getInstance().COSTO_GUARDAROBA;
            id_locker = rand.nextInt(400);
        }
    }

    @Override
    public void run() {
        if(tipo_ticket == 0)
            Singleton.getInstance().vaiFilaOnline(this);
        else
            Singleton.getInstance().vaiFilaLoco(this);
    }

    @Override
    public String toString(){
        String ris = "";
        ris+="Nome: "+nome;
        ris+="\nTempo in disco: "+temp_disco+" sec";
        ris+="\nConto iniziale: "+conto+"$";
        ris+="\nConto attuale: "+(conto-spesa_totale)+"$ (-"+spesa_totale+"$)";
        
        if(sesso==0)
            ris+="\nMaschio";
        else ris+="\nFemmina";

        return ris;
    }

    public void riempiListNomi(){
        // 20 nomi
        listNomi.add("Mario");
        listNomi.add("Luca");
        listNomi.add("Giacomo");
        listNomi.add("Francesco");
        listNomi.add("Davide");
        listNomi.add("Matteo");
        listNomi.add("Marco");
        listNomi.add("Riccardo");
        listNomi.add("Diego");
        listNomi.add("Pietro");
        listNomi.add("Paolo");
        listNomi.add("Andrea");
        listNomi.add("Jamal");

        listNomi.add("Laura");
        listNomi.add("Sofia");
        listNomi.add("Elisa");
        listNomi.add("Elena");
        listNomi.add("Carola");
        listNomi.add("Giorgia");
        listNomi.add("Veronica");
        listNomi.add("Alice");
    }
}


// ZONA>>>
    // 0 = fuori dalla discoteca
    // 1 = fila online
    // 2 = fila loco
    // 3 = guardaroba
    // 4 = discoteca (ballare)
    // 5 = bar
    // 6 = bagni