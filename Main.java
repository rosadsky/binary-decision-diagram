
import java.lang.Math;

public class Main {

    //static int pocetZredukovanychNodov= 0;
    //static int pocetNodov = 0;

    /*

    ROMAN OSADSKÝ
    

     */



    public static void main(String[] args) {

        r1_testovac(2000,14);

    }

    public static void r1_testovac(int pocetTestov, int pocetPremennych){
        String vektor = "";
        int testy = 0;
        int rudukcie_total = 0;
        int pocetNodov_total = 0;
        long start = System.currentTimeMillis();
        int pocetNodovPredRedukovanim = 0;

        System.out.println("************** START TEST **************");

        for (int r = 0; r < pocetTestov; r++) {
          //  pocetNodovPredRedukovanim = 0;
            //pocetZredukovanychNodov = 0;

            String bddUseVektor = "";
            BDD bdd = new BDD();
            BDD rootBDD;
            vektor = generovanieVektora(pocetPremennych);
            bdd = bdd.bddcreate(vektor);

            rootBDD = bdd;

            pocetNodovPredRedukovanim = bdd.pocetNodes;
            pocetNodov_total += pocetNodovPredRedukovanim;
            int redukcie;

            redukcie = bdd.bdd_reduce(rootBDD); //REDUCE

            rudukcie_total += redukcie;

            int dlzkaVektora = rootBDD.root.kluc.length();

            // loop na vykonanie zadaného počtu testoc
            for (int i = 0; i < dlzkaVektora; i++) {
                String useBDD;

                String finalBDD = null;
                String tmpBDD = "0";
                boolean nulaNaZaciatku = false;

                useBDD = Integer.toBinaryString(i); // prevod i na binárne číslo

                // podmianka a následný for pre doplnenie 0 pred binárne číslo
                if (useBDD.length() < bdd.pocetPremennych) {

                    for (int a = 0; a < (bdd.pocetPremennych - useBDD.length() - 1); a++) {
                        if (a == 0) {
                            tmpBDD = "0";
                            nulaNaZaciatku = true;
                        } else
                            tmpBDD += "0";
                    }

                }


                if (nulaNaZaciatku == true)
                    finalBDD = tmpBDD + useBDD;
                else
                    finalBDD = useBDD;


                String vysledok;

                vysledok = bdd.bdd_use(finalBDD, rootBDD.root); //testovanie

                bddUseVektor += vysledok; // pridávanie stringov, ktoré prídu z bdd use

            }


            //overenie funkčnosti nášho riešenia
            if (bddUseVektor.equals(vektor)) {
                testy++;
                // System.out.println("bdd_use: OK");
            } else {
                System.out.println("ZLY VEKTOR: " + bddUseVektor);
                System.out.println("bdd_use: FALSE");
            }

            if (testy == pocetTestov){
                System.out.println("" +
                        "100% \n----------------------------------------" +
                        "\nBDD_USE: OK | " + testy + "/" + pocetTestov + " |");
            }



            //int spocitanie = 0;
            //spocitanieNodes(bdd.root,spocitanie);


            if (r%500==0){
                System.out.print( ((double) r/ (double) pocetTestov)*100 + "%...");
            }

        }


        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;


        System.out.println("POCET PREMENNYCH:" + pocetPremennych );
        System.out.println("----------------------------------------");
        System.out.println("POCET NODOV TOTAL: " + pocetNodov_total );
        System.out.println("POCET REDUKOVANYCH NODOV: " + rudukcie_total);
        System.out.println("TOTAL TIME TIME : " +  timeElapsed + "ms.");
        System.out.println("----------------------------------------");
        System.out.printf("PRIEMER REDUKOVANIA: %.2f %%", ((double)rudukcie_total/(double)pocetNodov_total)*100 ,"% ");

    }

    static void spocitanieNodes(Node node, int spocitanie) {

        if (node == null)
            return;


        spocitanieNodes(node.left,spocitanie);

        if( node.isReduc == false){
            node.isReduc = true;
            //pocetNodov++;
        } else {
            //pocetZredukovanychNodov++;
        }

        spocitanieNodes(node.right,spocitanie);
    }

    public static String generovanieVektora(int pocetPremennych) {

        int velkostVektora = (int) Math.pow(2, pocetPremennych);
        String vektor = "";
        for(int i = 0; i < velkostVektora; i++) {

            int x = (1 + (int)(Math.random() * 100)) % 2;
            vektor = vektor + String.valueOf(x);
        }


        return vektor;
    }

}
