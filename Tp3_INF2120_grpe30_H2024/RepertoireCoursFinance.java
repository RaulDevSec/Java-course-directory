package ca.uqam.h2024.inf2120.grpe30.tp3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RepertoireCoursFinance {

    String cheminFichier = "/repertoireCoursFinances.csv";
    private final List<CoursFinance> listeDesCours = new ArrayList<>();
    public RepertoireCoursFinance(String cheminFichier) {

        try (Stream<String> lignes = Files.lines(Paths.get(cheminFichier))) {

            lignes.skip(1)
                    .map(ligne -> {
                        String[] colonnes = ligne.split(";");
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
                        try {
                            return new CoursFinance(
                                    Integer.parseInt(colonnes[0]), colonnes[1], Float.parseFloat(colonnes[2]),
                                    Integer.parseInt(colonnes[3]), Integer.parseInt(colonnes[4]),
                                    Float.parseFloat(colonnes[5]), colonnes[6], Integer.parseInt(colonnes[7]),
                                    LocalDateTime.parse(colonnes[8], formatter)

                            );
                        } catch (CoursFinanceInvalideException e) {
                            throw new RuntimeException(e);

                        }
                    })
                    .forEach(listeDesCours::add);
        }catch (IOException e) {

            System.out.println("Une erreur s'est produite!");
            e.printStackTrace();
        }
    }

    public int obtenirNbCours(){

        return this.listeDesCours.stream().mapToInt(cours -> 1).sum();
    }

    public List<CoursFinance> rechercherParTitre(String chaineCaracteres){

        String chaineLowerCase = chaineCaracteres.toLowerCase();

        return this.listeDesCours.stream()
                .filter(cours -> cours.getTitre().toLowerCase().contains(chaineLowerCase))
                .collect(Collectors.toList());
    }

    public List<CoursFinance> rechercherParEvaluation (float eval, boolean plusPetite) {

        return this.listeDesCours.stream()
                .filter(cours -> (plusPetite ? cours.getEvaluation() < eval : cours.getEvaluation() >= eval))
                .sorted(Comparator.comparingDouble(CoursFinance::getEvaluation)
                        .thenComparing(CoursFinance::getNiveauExperience)
                        .thenComparingInt(CoursFinance::getId))
                .collect(Collectors.toList());
    }

    public List<CoursFinance> rechercherParNiveauExperience (List<String> niveauxExperience) {
        if (niveauxExperience == null || niveauxExperience.isEmpty()) {

            return new ArrayList<>();

        }

        return listeDesCours.stream()
                .filter(cours -> niveauxExperience.stream()
                        .anyMatch(niveau -> cours.getNiveauExperience().equalsIgnoreCase(niveau)))
                .sorted((c1, c2) -> {

                    int comparaisonParTitre = c1.getTitre().compareToIgnoreCase(c2.getTitre());
                    return comparaisonParTitre != 0 ? comparaisonParTitre : Integer.compare(c1.getId(), c2.getId());

                })
                .collect(Collectors.toList());
    }

    public List<CoursFinance> rechercherParPeriode (LocalDateTime dateHeureDebut, LocalDateTime dateHeureFin) {

        Comparator<CoursFinance> trierParDateHeurePub = Comparator.comparing(CoursFinance::getDateHeurePublication);
        Comparator<CoursFinance> trierParId = Comparator.comparingInt(CoursFinance::getId);
        Stream<CoursFinance> coursFinanceStream;

        if ((dateHeureDebut == null && dateHeureFin == null)) {
            
            throw new IllegalArgumentException("Les dates de début et de fin sont invalides.");
            
        }
        
        coursFinanceStream = listeDesCours.stream().filter(coursFinance -> {

            if(dateHeureDebut != null && dateHeureFin != null) {
                return !coursFinance.getDateHeurePublication().isBefore(dateHeureDebut) &&
                        !coursFinance.getDateHeurePublication().isAfter(dateHeureFin);

            } else if (dateHeureDebut == null && dateHeureFin != null) {
                return coursFinance.getDateHeurePublication().equals(dateHeureFin) &&
                        !coursFinance.getDateHeurePublication().isAfter(dateHeureFin);

            } else if (dateHeureDebut != null && dateHeureFin == null) {

                return coursFinance.getDateHeurePublication().isAfter(dateHeureDebut) &&
                        coursFinance.getDateHeurePublication().equals(dateHeureDebut);

            } else if (dateHeureDebut.equals(dateHeureFin)) {
                return coursFinance.getDateHeurePublication().equals(dateHeureDebut) || coursFinance.getDateHeurePublication().equals(dateHeureFin);

            }
            return true;
        });

        return coursFinanceStream
                .sorted(trierParDateHeurePub.thenComparing(trierParId))
                .collect(Collectors.toList());
    }

    public String[] rechercherParNbEtudiants (int n) {

        List<String> resultats = listeDesCours.stream()
                .sorted((c1, c2) -> {

                    int etudiants = Integer.compare(c1.getNbEtudiantsInscrits(), c2.getNbEtudiantsInscrits());
                    if (etudiants == 0) {

                        return Integer.compare(c1.getId(), c2.getId());

                    }
                    return etudiants;

                })
                .limit(Math.abs(n))
                .map(CoursFinance::getTitre)
                .toList();

        return resultats.toArray(new String[0]);
    }

    public String[] rechercherParPourcentageEvaluation (int n){

        listeDesCours.stream()
                .sorted((c1, c2) -> {
                    float pourcentage1 = ((float) c1.getNbEvaluations() / c1.getNbEtudiantsInscrits()) * 100;
                    float pourcentage2 = ((float) c2.getNbEvaluations() / c2.getNbEtudiantsInscrits()) * 100;

                    int resultat = (n > 0) ? Float.compare(pourcentage2, pourcentage1) : Float.compare(pourcentage1, pourcentage2);

                    if (resultat == 0) {

                        resultat = Integer.compare(c1.getId(), c2.getId());
                    }
                    return resultat;
                });

        return listeDesCours.stream().map(CoursFinance::getTitre).toArray(String[]::new);
    }

    public List<CoursFinance> rechercherParPrix (float prix, boolean estPlusGrand){
            Comparator<CoursFinance> trierParPrix = Comparator.comparingDouble(CoursFinance::getPrix);
            Comparator<CoursFinance> trierParId = Comparator.comparingInt(CoursFinance::getId);
            return this.listeDesCours.stream()
                .filter(coursFinance -> estPlusGrand ? coursFinance.getPrix() > prix : coursFinance.getPrix() <= prix)
                .sorted(trierParPrix.thenComparing(trierParId))
                .collect(Collectors.toList());

    }
}
