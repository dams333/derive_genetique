package ch.dams333.biologie;

import java.util.*;

public class Biologie implements Runnable {

    private boolean running;
    private final Scanner scanner = new Scanner(System.in);


    private int numberOfGenerations = -1;
    private int individusOfPopulation = -1;
    private int echantillonOfGeneration = -1;
    private Float pProbability = Float.valueOf(-1);
    private boolean isDeleter = false;

    public static void main(String[] args) {
        Biologie biologie = new Biologie();
        try {
            new Thread(biologie, "Biologie").start();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

    }

    public void run() {
        running = true;
        System.out.println("Nombre de générations:");
        while (running){
            if(scanner.hasNextLine()) {
                performInput(scanner.nextLine());
            }
        }
        performCalcul();
        System.exit(0);
    }

    private void performCalcul() {


        int currentGeneration = 1;

        Map<Integer, Float> Astat = new HashMap<Integer, Float>();
        Astat.put(0, this.pProbability);

        int index = 1;

        while (currentGeneration <= numberOfGenerations){

            System.out.println("=========================");

            if(this.pProbability >= 1){
                System.out.println((currentGeneration - 1) + " générations ont fait disparaître l'allèle a");
                break;
            }

            if(this.pProbability <= 0){
                System.out.println((currentGeneration - 1) + " générations ont fait disparaître l'allèle A");
                break;
            }

            Character[] allels = new Character[individusOfPopulation * 2];
            int quantityOfAllelA = Math.round(Float.valueOf(individusOfPopulation) * Float.valueOf(2) * pProbability);
            int quantityOfAllela = (individusOfPopulation * 2) - quantityOfAllelA;
            System.out.println("Dans cette génération, il y a " + quantityOfAllelA + " allèles A. Et " + quantityOfAllela + " allèles a");
            for(int i = 0; i <= quantityOfAllelA; i++){
                allels[i] = "A".charAt(0);
            }
            for(int i = quantityOfAllelA + 1; i < individusOfPopulation * 2; i++){
                allels[i] = "a".charAt(0);
            }
            System.out.println(" ");
            System.out.println("Echantillonage de la génération " + currentGeneration);

            int homoDomi = 0;
            int hetero = 0;
            int homoRece = 0;

            for(int i = 1; i <= echantillonOfGeneration; i++){
                Character firstAllel = allels[random(0, allels.length - 1)];
                Character secondAllel = allels[random(0, allels.length - 1)];
                if(firstAllel.equals("A".charAt(0)) && secondAllel.equals("A".charAt(0))){
                    homoDomi ++;
                }
                if(firstAllel.equals("A".charAt(0)) && secondAllel.equals("a".charAt(0))){
                    hetero ++;
                }
                if(firstAllel.equals("a".charAt(0)) && secondAllel.equals("A".charAt(0))){
                    hetero ++;
                }
                if(firstAllel.equals("a".charAt(0)) && secondAllel.equals("a".charAt(0))){
                    homoRece ++;
                }
            }

            System.out.println("L'échantillon contient:");
            System.out.println("     - " + homoDomi + " homozygotes dominants");
            System.out.println("     - " + hetero + " étérozygotes");
            System.out.println("     - " + homoRece + " homozygotes récessifs");
            System.out.println(" ");
            System.out.println("     - " + ((homoDomi * 2) + hetero) + " allèles A");
            System.out.println("     - " + ((homoRece * 2) + hetero) + " allèles a");

            int allelTotal = 0;

            allelTotal += homoDomi * 2;
            allelTotal += hetero * 2;
            if(!isDeleter){
                allelTotal += homoRece * 2;
            }

            System.out.println(" ");
            System.out.println("     - " + ((homoDomi * 2) + hetero) + " allèles A vont se reproduire => Fréquence: " + ((Float.valueOf(homoDomi) * 2) + Float.valueOf(hetero))/Float.valueOf(allelTotal));
            this.pProbability = ((Float.valueOf(homoDomi) * 2) + Float.valueOf(hetero))/Float.valueOf(allelTotal);
            if(isDeleter) {
                System.out.println("     - " + hetero + " allèles a vont se reproduire => Fréquence: " + (Float.valueOf(hetero)/Float.valueOf(allelTotal)));
            }else{
                System.out.println("     - " + ((homoRece * 2) + hetero) + " allèles a vont se reproduire => Fréquence: " + ((Float.valueOf(homoRece) * 2) + Float.valueOf(hetero))/Float.valueOf(allelTotal));
            }

            Astat.put(index, this.pProbability);


            currentGeneration++;
            index++;
        }

        System.out.println(" ");
        System.out.println("=====================");
        System.out.println("Liste des fréquences de l'allèle A:");
        for(int gen : Astat.keySet()){
            System.out.println(Astat.get(gen));
        }

    }

    private void performInput(String input) {
        if(numberOfGenerations == -1){
            numberOfGenerations = Integer.parseInt(input);
            System.out.println("Nombre d'individus dans la population:");
        }else if(individusOfPopulation == -1){
            individusOfPopulation = Integer.parseInt(input);
            System.out.println("Nombre d'individus à échantilloner à chaque génération:");
        }else if(echantillonOfGeneration == -1){
            echantillonOfGeneration = Integer.parseInt(input);
            System.out.println("Répartition au départ de l'allèle A (0-1):");
        }else if(pProbability == -1){
            pProbability = Float.parseFloat(input);
            System.out.println("Les individus 'aa' meurent-ils ? (y/n):");
        }else{
            if(input.equalsIgnoreCase("y")){
                isDeleter = true;
            }
            running = false;
        }
    }


    private int random(int min, int max){
        return (int)(Math.random() * ((max - min) + 1)) + min;
    }
}
