package com.IFT.SimLac;

import java.util.List;
import java.util.Set;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy
 * Date :    March 3rd 2021
 *
 * This class is used to instantiate all carnivore organisms.
 * All plants from initial conditions are built by UsineCarnivore.
 * All newborns are then instantiated using the seReproduire method.
 */

public final class Carnivore extends Organisme {

    // Attributes
    private final double debrouillardise;  // Determines the chance of having one or multiple meals during a cycle.
    private final Set<String> aliments;    // Set of Herbivore objects that can be eaten by this Carnivore.


    // Constructor
    public Carnivore(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie,
                     double resilience, double fertilite, int ageFertilite, double energieEnfant,
                     double debrouillardise, Set<String> aliments, double tailleMaximum) {

        super(nomEspece, energie, age, besoinEnergie, efficaciteEnergie, resilience, fertilite, ageFertilite,
                energieEnfant, tailleMaximum);

        this.debrouillardise = debrouillardise;
        this.aliments = aliments;
        this.tailleMaximum = tailleMaximum;
    }

    @Override
    public Carnivore seReproduire() {
        return new Carnivore(nomEspece, energieEnfant, 0, besoinEnergie, efficaciteEnergie, resilience,
                fertilite, ageFertilite, energieEnfant, debrouillardise, aliments, tailleMaximum);
    }


    // Returns the number of times this carnivore feeds.
    // If the project gets any larger, consider putting this method into an Animal abstract class.
    public int nombreRepas() {
        int nombreHerbivoresManges = 0;

        double roll = Math.random();
        while (roll <= debrouillardise) {
            nombreHerbivoresManges++;
            roll = Math.random();
        }

        return nombreHerbivoresManges;
    }


    // Updates the energetic budget of this carnivore.
    public void bilanEnergetique(List<Herbivore> herbivoresManges) {
        double energieRecue = 0;

        for (var herbivoreMange : herbivoresManges) {
            energieRecue += herbivoreMange.getEnergie();
            herbivoreMange.estMange();
        }
        budgetEnergetique = energieRecue - besoinEnergie;
    }


    // Getters
    public Set<String> getAliments() { return Set.copyOf(aliments); }
}


