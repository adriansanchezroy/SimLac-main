package com.IFT.SimLac;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy
 * Date :    March 1st 2021
 *
 * This class is used to instantiate all plant organisms.
 * All plants from initial conditions are built by UsinePlante.
 * All newborns are then instantiated using the seReproduire method.
 */

public final class Plante extends Organisme {

    // All attributes inherited from Organisme

    // Constructor
    public Plante(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie,
                  double resilience, double fertilite, int ageFertilite, double energieEnfant, double tailleMaximum) {

        super(nomEspece, energie, age, besoinEnergie, efficaciteEnergie, resilience, fertilite, ageFertilite,
                energieEnfant, tailleMaximum);
    }

    @Override
    public Plante seReproduire(){
        return new Plante(nomEspece, energieEnfant, 0, besoinEnergie, efficaciteEnergie, resilience,
                fertilite, ageFertilite, energieEnfant, tailleMaximum);
    }


    // Updates the energetic budget of this plant.
    public void bilanEnergetique(int energieSolaire, double energieTotaleDesPlantes) {
        double energieRecue = energieSolaire * (energie / energieTotaleDesPlantes);
        budgetEnergetique = energieRecue - besoinEnergie;
    }


    // Plants loose the amount of energy the herbivore eats.
    public void estMange(double energieMangee) {
        energie -= energieMangee;
        if (energie < 0)
            dead = true;
    }
}
