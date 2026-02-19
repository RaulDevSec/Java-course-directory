package ca.uqam.h2024.inf2120.grpe30.tp3;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


public class TestsPartielsRepertoireCoursFinance {

   public final static String FIC = "fichierPourTestsPartiels.csv";

   /**
    * Affiche le titre donne en majuscule et entre deux lignes de tirets de meme longueur.
    * 
    * @param titre le titre a afficher.
    */
   private static void titre(String titre) {
      String s = "\n";
      for (int i = 0; i < titre.length(); i++) {
         s = s + "-";
      }
      s = s + "\n" + titre.toUpperCase() + "\n";
      for (int i = 0; i < titre.length(); i++) {
         s = s + "-";
      }

      System.out.println(s);
   }

   /**
    * Retourne une representation sous forme de chaine de caracteres de la liste donnee en
    * parametre.
    * 
    * @param <T>   le type des elements dans liste.
    * @param liste la liste dont on veut une representation sous forme de chaine de caracteres.
    * @return une representation sous forme de chaine de caracteres de la liste donnee en
    *         parametre
    */
   private static <T> String toString(List<T> liste) {
      String s;

      if (liste == null) {
         s = "Liste null";
      } else if (liste.isEmpty()) {
         s = "[ ]";
      } else {
         s = "[";
         for (T elt : liste) {
            s = s + "\n " + elt;
         }
         s = s + "\n]";
      }
      return s;
   }

   public static void main(String[] args) {

      List<CoursFinance> liste;
      RepertoireCoursFinance repertoireCours = new RepertoireCoursFinance(FIC);
      String[] tab;

      titre("test - obtenir le nombre de cours");

      System.out.println("repertoireCours.obtenirNbCours()");
      System.out.println(repertoireCours.obtenirNbCours());

      // -------------------------------------------------------------

      titre("test - rechercher par titre");

      System.out.println("repertoireCours.rechercherParTitre(\"account\")");
      liste = repertoireCours.rechercherParTitre("account");
      System.out.println(toString(liste));

      // -------------------------------------------------------------

      titre("tests - rechercher par evaluation");

      System.out.println("repertoireCours.rechercherParEvaluation(6.5f, true)");
      liste = repertoireCours.rechercherParEvaluation(6.5f, true);
      System.out.println(toString(liste));

      System.out.println("\nrepertoireCours.rechercherParEvaluation(9.0f, false)");
      liste = repertoireCours.rechercherParEvaluation(9.0f, false);
      System.out.println(toString(liste) + "\n");

      // -------------------------------------------------------------

      titre("tests - rechercher par niveau d'experience");

      System.out.println("\nrepertoireCours.rechercherParNiveauExperience"
            + "(Arrays.asList(\"Junior\", \"Tous\", null))");
      liste = repertoireCours.rechercherParNiveauExperience(Arrays.asList("Junior", "Tous", null));
      System.out.println(toString(liste));

      System.out.println("\nrepertoireCours.rechercherParNiveauExperience"
            + "(Arrays.asList(\"\", \"Intermediaire\", \"Senior\"))");
      liste = repertoireCours
            .rechercherParNiveauExperience(Arrays.asList("", "Intermediaire", "Senior"));
      System.out.println(toString(liste));

      System.out.println("\nrepertoireCours.rechercherParNiveauExperience"
            + "(Arrays.asList(\"\", \"Permanent\", \"Consultant\"))");
      liste = repertoireCours
            .rechercherParNiveauExperience(Arrays.asList("", "Permanent", "Consultant"));
      System.out.println(toString(liste));

      // -------------------------------------------------------------

      titre("tests - rechercher par periode");

      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
      System.out.println(
            "\nrepertoireCours.rechercherParPeriode" + "(30-10-2012 21:05, 23-02-2013 23:47))");
      liste = repertoireCours.rechercherParPeriode(
            LocalDateTime.parse("2012-10-30 21:05", formatter),
            LocalDateTime.parse("2013-02-23 23:47", formatter));
      System.out.println(toString(liste));

      System.out.println("\nrepertoireCours.rechercherParPeriode(05-08-2013 12:02, null)");
      liste = repertoireCours
            .rechercherParPeriode(LocalDateTime.parse("2013-08-05 12:02", formatter), null);
      System.out.println(toString(liste));

      System.out.println("\nrepertoireCours.rechercherParPeriode(null, 12-11-2012 10:01)");
      liste = repertoireCours.rechercherParPeriode(null,
            LocalDateTime.parse("2012-11-12 10:01", formatter));
      System.out.println(toString(liste));

      // -------------------------------------------------------------

      titre("tests - rechercher par nombre d'etudiants");

      System.out.println("\nrepertoireCours.rechercherParNbEtudiants(5)");
      tab = repertoireCours.rechercherParNbEtudiants(5);
      System.out.println(toString(Arrays.asList(tab)));

      System.out.println("\nrepertoireCours.rechercherParNbEtudiants(-7)");
      tab = repertoireCours.rechercherParNbEtudiants(-7);
      System.out.println(toString(Arrays.asList(tab)));

      // -------------------------------------------------------------

      titre("tests - rechercher par pourcentage d'evaluation");

      System.out.println("\nrepertoireCours.rechercherParPourcentageEvaluation(3)");
      tab = repertoireCours.rechercherParPourcentageEvaluation(3);
      System.out.println(toString(Arrays.asList(tab)));

      System.out.println("\nrepertoireCours.rechercherParPourcentageEvaluation(-2)");
      tab = repertoireCours.rechercherParPourcentageEvaluation(-2);
      System.out.println(toString(Arrays.asList(tab)));

      // -------------------------------------------------------------

      titre("tests - rechercher par prix");

      System.out.println("\nrepertoireCours.rechercherParPrix(0, false)");
      liste = repertoireCours.rechercherParPrix(0, false);
      System.out.println(toString(liste));

      System.out.println("\nrepertoireCours.rechercherParPourcentageEvaluation(95, true)");
      liste = repertoireCours.rechercherParPrix(95, true);
      System.out.println(toString(liste));

   }

}
