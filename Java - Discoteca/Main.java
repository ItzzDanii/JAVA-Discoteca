public class Main {
    public static void main(String[] args) {
        Discoteca discoteca = new Discoteca();

        discoteca.start();

        try {
            discoteca.join();
        } catch (Exception e) {}

        String riepilogo = 
        "Drink acquistati: "+Singleton.getInstance().num_drink_acquistati +
        "\nPersone con biglietto online: "+Singleton.getInstance().num_persone_online+
        "\nPersone con biglietto loco: "+Singleton.getInstance().num_persone_loco+
        "\nTempo medio in discoteca: "+Singleton.getInstance().num_persone_online+
        "\nNumero persone metà serata: "+Singleton.getInstance().num_persone_metà_serata+
        "\nGuadagno totale: "+Singleton.getInstance().guadagno_totale+"$";

        System.out.println();
        System.out.println("=== RIEPILOGO SERATA ===");
        System.out.println(riepilogo);
    }
}
