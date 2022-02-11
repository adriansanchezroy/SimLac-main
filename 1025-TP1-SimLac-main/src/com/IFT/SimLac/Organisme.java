package com.IFT.SimLac;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy
 * Date :    March 1st 2021
 *
 * This abstract class implements attributes and methods common to all living organisms.
 * Plante, Herbivore and Carnivore classes inherit from this class.
 */

public abstract class Organisme {

    // attributes
    protected final String nomEspece;          // Name of the organism species
    protected final double besoinEnergie;      // Energy needed each cycle to survive.
    protected final double efficaciteEnergie;  // The ratio at which any remaining energy is metabolised.
    protected final double resilience;         // Determines the chance of survival when not enough energy is received.
    protected final double fertilite;          // Determines the chance of having a baby
    protected final int ageFertilite;          // Minimum age required to have babies
    protected final double energieEnfant;      // Starting energy at birth
    protected double energie;                  // Actual energy at the start of a cycle. Updated each cycle.
    private int age;                           // Actual age of an organism. Incremented each cycle.
    protected double tailleMaximum;            // Maximum energy an organism can have. Limits growth.

    protected boolean dead = false;
    protected double budgetEnergetique = 0;    // Energy that can be spent during a cycle. Can be < 0.


    // Constructor
    public Organisme(String nomEspece, double energie, int age, double besoinEnergie, double efficaciteEnergie,
                     double resilience, double fertilite, int ageFertilite, double energieEnfant,
                     double tailleMaximum) {

        this.nomEspece = nomEspece;
        this.energie = energie;
        this.age = age;
        this.besoinEnergie = besoinEnergie;
        this.efficaciteEnergie = efficaciteEnergie;
        this.resilience = resilience;
        this.fertilite = fertilite;
        this.ageFertilite = ageFertilite;
        this.energieEnfant = energieEnfant;
        this.tailleMaximum = tailleMaximum;
    }

    public abstract Organisme seReproduire();


    // Determines if an organism lives or dies.
    // Note : Any fraction of a energy unit missing is considered as a whole energy unit.
    //     Ex.: budgetEnergetique == -1.02 is considered a 2 energy unit deficit.
    public void survivalCheck(){
        if (budgetEnergetique >= 0)
            return;  // Automatic success

        // Makes deficitUniteEnergie a positive integer as per description above.
        int  deficitUniteEnergie = (int) Math.ceil(Math.abs(budgetEnergetique));
        double survivalChance = Math.pow(resilience, deficitUniteEnergie);

        if (Math.random() > survivalChance)
            dead = true;
    }


    // Determines if the organism gives birth and, in which case, how many children are born.
    // Returns the number of children born.
    // Note: Whole energy unit needed for a roll.
    //     Ex.: budgetEnergetique == 1.02 gives only 1 roll.
    public int reproductionCheck(){
        if (budgetEnergetique < 0 || age < ageFertilite)
            return 0;  // Automatic fail

        int nombreEnfantsProduits = 0;
        int reproductionRolls = (int) budgetEnergetique;  // Truncates decimal parts

        // The organism must have enough energy to potentially give birth to roll
        while (reproductionRolls > 0 && energie + budgetEnergetique >= energieEnfant) {
            if (Math.random() <= fertilite) {
                nombreEnfantsProduits++;
                budgetEnergetique -= energieEnfant;
                reproductionRolls -= energieEnfant;
            } else
                reproductionRolls--;
        }
        return nombreEnfantsProduits;
    }

    // Makes an organism grow stronger or weaker.
    // If after shrinking its energy < 0, it dies.
    // Note: a plant may already be dead at this point.
    public void croitreDecroitre(){
        if (budgetEnergetique > 0) {  // grows
            energie =  Math.min(energie + efficaciteEnergie * budgetEnergetique, tailleMaximum);
        } else  // shrinks
            energie += budgetEnergetique;

        if (energie < 0)
            dead = true;
    }


    public void vieillir(){
        age++;
    }

    // Getters
    public String getNomEspece() {
        return nomEspece;
    }

    public double getEnergie() {
        return energie;
    }

    public boolean isDead() {
        return dead;
    }
}
