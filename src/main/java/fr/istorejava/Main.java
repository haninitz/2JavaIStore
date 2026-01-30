
package fr.istorejava;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        variables1();
    }

    // Les Variables
    public static void variables1() {
        System.out.println("Execution de la methode Variables1");

        // une variable de type int
        // declaration
        int age = 20;

        // initialisation
        age = 27;

        // affichage de age à la console
        System.out.println("j'ai " + age + " ans");

        // regles de nommage:
        // ne peut pas commencer par un chiffre
        // ne peut en aucun cas contenir de caractère "espace"
        // ne peut pas contenir le tiret du 6 -
        // peut commencer par un $ ou un _
        // préférer la syntaxe camelCase
        // donner un nom significatif

        // variables non reassignables
        final String firstname = "JC";

        // la ligne suivante creera une erreur:
        // firstname = "Marie";

        // Les types primitifs:
        // - byte: Entier signé entre -128 et 127
        // - short: Entier signé entre -32768 et 32767
        // - int: Entier signé 32 bit entre -2147483648 et 2147483647
        // - long: Entier signé 64 bit entre -9223372036854775808 et 9223372036854775807
        // - float: Virgule flottante 32 bit
        // - double: Virgule flottante 64 bit
        // - boolean: true ou false
        // - char: caractere unicode de 16 bit
        // les nombre sont traités comme des double par defaut
        // pour definir un float il faut ajouter 'f' a la fin du chiffre
        float average = 4.8f;

        double average2 = 4.8;

        // les booleans
        boolean isWeekend = false;
        System.out.println(isWeekend);

        boolean isNotWeekend = !isWeekend;
        System.out.println(isNotWeekend);

        // variable de type char
        char percent = '%';
        System.out.println("Signe du pourcentage: '" + percent + "'");

        // utilisation des codes Unicode
        char phi = '\u0278';
        System.out.println("LE caractère phi: " + phi);
    }

    public static void typage() {
        // transtypage implicite
        // effectué par le compilateur quand les types
        // de depar et d'arrivée sont parafaitement compatibles
        int number1 = 8;
        double number2 = number1;
        System.out.println("number2: " + number2);

        // le typage kimplicite ne fonctionne que dans le sens
        // petit nombre d'octets vers grand nombre d'octets.
        double number3 = 10;
        // la ligne suivant ecréera une erreur:
        // int number4 = number3;

        // transtypage implicite possible dans le tesne suivant:
        // byte -> short -> char -> int -> long -> float -> double.

        // transtypage explicite
        // pertmet d'aller en sens invers de la chaine dessus mais avec quelques
        // conditons obligatoires

        double number5 = 3.147;
        int number6 = (int) number5;
        System.out.println("number6: " + number6);

        // exo: creer une variable nommé lettreF contenant le charactere F
        // caster lettreF en 'int'
        // afficher le resultat du cast.
        char lettreF = 'F';
        System.out.println("Transtypage de " + lettreF + " en int: " + (int) lettreF);

        // Classes d'emballage des types natifs:
        // - int -> Integer
        // - long -> Long
        // - short -> Short
        // - byte -> Byte
        // - boolean -> Boolean
        // - char -> Character
        // - float -> Float
        // - double -> Double

        Integer number7 = 12;

        // recuperation du type primitif
        int intValueInsideNumber7 = number7.intValue();
        System.out.println("valeur de type int a l'interieur de la classe d'emballage Integer de number7: " + intValueInsideNumber7);
    }
}