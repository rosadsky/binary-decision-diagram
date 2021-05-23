import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.ArrayList;

public class BDD {

    Node root;
    int pocetPremennych;
    int pocetNodes = 0;

    public BDD() {
        //  System.out.println("Vytvoreny BDD");
    }


    public BDD bddcreate(String vektor) {
        int velkostBDD = 0;
        int vektorSize = vektor.length();
        BDD bdd = new BDD();

        // do bdd.root zapíšeme prvý node stromu - zavola'sa rekúrzivna funkcia create node
        bdd.root = create_node(vektor, 0, 0, bdd);

        return bdd;
    }

    /*
    Funckia create_node
    - rekúrzívna, ktorá postupne vytvára strom
    - aktualizuje výšku stromu
    - zapisuje koľko nodov sa nacháza v strome

     */

    public Node create_node(String vektor, int vyskaStromu, int pocetNodes, BDD bdd) {
        int velkostBf = vektor.length();
        vyskaStromu++;

        bdd.pocetNodes++;
        Node node = new Node();


        // pre zapametanie výšky stromu
        if (bdd.pocetPremennych < vyskaStromu) {
            bdd.pocetPremennych = vyskaStromu;
            //System.out.println("PREMENNE: " + bdd.pocetPremennych);
        }

        node.level = vyskaStromu;
        node.kluc = vektor;

        /* klasické rekurzívne vytváranie stromu, s tým že ešte popritom voláme funkcie na rozdelenie
           vektora. A taktiež si pamatáme predchádzajúci node, na ktorý je daný node napojený.


         */
        if (velkostBf == 0) {
            return node;
        }
        if (velkostBf == 1) {
            node.right = null;
            node.left = null;
        } else {

            String leftVektor = splitL(vektor);
            node.left = bdd.create_node(leftVektor, vyskaStromu, pocetPremennych, bdd);
            node.left.parent = node;
            String rightVektor = splitR(vektor);

            node.right = bdd.create_node(rightVektor, vyskaStromu, pocetPremennych, bdd);


            node.right.parent = node;

        }

        return node;
    }

    String bdd_use(String searchKey, Node bdd) {

        Node searchBDD = null;
        searchBDD = bdd;


        //iteratívne riešenie rozhodovania, kde sa postupne posúvame v strome podľa zadaného vektora
        int i = 0;
        while (searchBDD.right != null) {
            char character = searchKey.charAt(i);

            if (character == '0') {
                //System.out.println("FALSE");
                searchBDD = searchBDD.left;
            } else {
                //System.out.println("TRUE");
                searchBDD = searchBDD.right;
            }
            i++;
        }

        if (searchBDD.kluc == null) {
            return "-1";
        }

        return searchBDD.kluc;
    }


    int bdd_reduce(BDD bdd) {
        int redukcie = 0;
        int vyska = bdd.pocetPremennych;
        int l = 0;
        for (int j = vyska; j > 0; j--) {

            ArrayList<Node> arrayNodov = new ArrayList<Node>(); // vytvorenie arraylistu pre danú premennú
            zistitVysku(bdd.root, arrayNodov, j);  // tu sa premenná j zapíše do stromu

            /*
            for cykly na porovnávanie kľúčov ktoré sú v strome, keď sa rovnajú cez parenta sa zapíšu
            buď doprava alebo dolava, podla toho ktorá podmienka je spojená

             */

            for (int i = 0; i < arrayNodov.size(); i++) {
                for (int x = i + 1; x < arrayNodov.size(); x++) {
                    if (arrayNodov.get(i) != arrayNodov.get(x)) {
                        if (arrayNodov.get(x).kluc.equals(arrayNodov.get(i).kluc)) {
                            //System.out.println(i + "<-: " + arrayNodov.get(i).kluc + " || " + x + " : " + arrayNodov.get(x).kluc);
                            redukcie++;
                            if (arrayNodov.get(x).parent.right.kluc.equals(arrayNodov.get(i).kluc)) {
                                arrayNodov.get(x).parent.right = arrayNodov.get(i);
                            }
                            if (arrayNodov.get(x).parent.left.kluc.equals(arrayNodov.get(i).kluc)) {
                                arrayNodov.get(x).parent.left = arrayNodov.get(i);
                            }

                            arrayNodov.remove(x);
                        }
                    }
                }
            }
        }

        return redukcie;
    }

    void zistitVysku(Node node, ArrayList<Node> arrayNodov, int vyska) {
        if (node == null)
            return;

        zistitVysku(node.left, arrayNodov, vyska);

        if (node.level == vyska) {
            arrayNodov.add(node);
            //     System.out.print(node.kluc + " | ");
        }

        zistitVysku(node.right, arrayNodov, vyska);
    }


    public String splitR(String vektor) {
        int polka = vektor.length() / 2;
        int koniec = vektor.length();

        vektor = vektor.substring(polka, koniec);

        return vektor;
    }

    public String splitL(String vektor) {
        int polka = vektor.length() / 2;

        vektor = vektor.substring(0, polka);

        return vektor;
    }
}
