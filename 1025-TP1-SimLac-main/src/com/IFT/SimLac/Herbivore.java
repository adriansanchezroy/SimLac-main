package com.IFT.SimLac;

import java.util.List;
import java.util.Set;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy
 * Date :    March 2nd 2021
 *
 * This class is used to instantiate all herbivore organisms.
 * All plants from initial conditions are built by UsineHerbivore.
 * All newborns are then instantiated using the seReproduire method.
 */

public final class Herbivore extends Organisme{

    // Attributes
    private final double debrouillardise;  // Determines the chance of having one or multiple meals during a cycle.
    private final double voraciteMin;      // The minimum ratio of a plant an herbivore eats in one meal.
    private final double voraciteMax;      // The maximum ratio of a plant an herbivore eats in one meal.
    private final Set<String> aliments;    // Set of Plante objects that can be eaten by this herbivore.


    // Constructor
    public Herbivore(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie,
                     double resilience, double fertilite, int ageFertilite, double energieEnfant,
                     double debrouillardise, double voraciteMin, double voraciteMax, Set<String> aliments,
                     double tailleMaximum) {

        super(nomEspece, energie, age, besoinEnergie, efficaciteEnergie, resilience, fertilite, ageFertilite,
                energieEnfant, tailleMaximum);

        this.debrouillardise = debrouillardise;
        this.voraciteMin = voraciteMin;
        this.voraciteMax = voraciteMax;
        this.aliments = aliments;
        this.tailleMaximum = tailleMaximum;
    }

    @Override
    public Herbivore seReproduire(){
        return new Herbivore(nomEspece, energieEnfant, 0, besoinEnergie, efficaciteEnergie, resilience,
                fertilite, ageFertilite, energieEnfant, debrouillardise, voraciteMin, voraciteMax, aliments,
                tailleMaximum);
    }


    // Returns the number of times this herbivore feeds.
    // If the project gets any larger, consider putting this method into an Animal abstract class.
    public int nombreRepas(){
        int nombrePlantes = 0;

        double roll = Math.random();
        while (roll <= debrouillardise) {
            nombrePlantes++;
            roll = Math.random();
        }
        return nombrePlantes;
    }


    // Updates the energetic budget of this herbivore.
    public void bilanEnergetique(List<Plante> plantesMangees) {
        double energieRecue = 0;

        for (var planteMangee : plantesMangees) {
            double range = voraciteMax - voraciteMin;
            double fractionEnergieMangee = voraciteMin + Math.random() * range;

            double energieMangee = planteMangee.getEnergie() * fractionEnergieMangee;

            energieRecue += energieMangee;
            planteMangee.estMange(energieMangee);
        }
        budgetEnergetique = energieRecue - besoinEnergie;
    }


    // When eaten, an herbivore dies.
    public void estMange() {
            dead = true;
    }

    // Getters
    public Set<String> getAliments() {
        return Set.copyOf(aliments);
    }
}
