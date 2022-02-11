package com.IFT.SimLac;

import java.util.Arrays;
import java.util.List;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy
 * Date :    March 1st 2021
 *
 * This abstract class implements attributes and methods common to all Usine classes.
 * Factory classes are used by the ConditionsInitiales class to build and populate ecosystems, such as a Lac object.
 * It makes sure all attributes are initialized to a valid value before the builder method can be called.
 */

public abstract class UsineOrganisme {

    // Verificateur inner class is used as a condition to make sure all attributes are initialized before the builder
    // methods are called. It also makes possible to get the name of the uninitialized field so the builder method can
    // throw the appropriate error message.
    protected static class Verificateur {
        protected String nomChamps;
        protected boolean initialized;

        protected Verificateur(String nomChamps) {
            this.nomChamps = nomChamps;
            this.initialized = false;
        }

        protected void initialize(){
            this.initialized = true;
        }
    }


    // attributes
    protected String nomEspece;               // Name of the organism species
    protected double besoinEnergie;           // Energy needed each cycle to survive.
    protected double efficaciteEnergie;       // The ratio at which any remaining energy is metabolised.
    protected double resilience;              // Determines the chance of survival when not enough energy is received.
    protected double fertilite;               // Determines the chance of having a baby
    protected int ageFertilite;               // Minimum age required to have babies
    protected double energieEnfant;           // Starting energy at birth
    protected double tailleMaximum = -1;      // Limits growth. -1 means 'not yet initialized'.
                                                  // Optional field in the XML file. Default value: 20 * energieEnfant


    // Only used to initialize verifiateurOrganisme.
    private final Verificateur[] arrayVerifOrganisme = {
            new Verificateur("nomEspece"),
            new Verificateur("besoinEnergie"),
            new Verificateur("efficaciteEnergie"),
            new Verificateur("resilience"),
            new Verificateur("fertilite"),
            new Verificateur("ageFertilite"),
            new Verificateur("energieEnfant"),
    };

    // Keeps track of initialized attributes. Used as a condition before the builder method can be called.
    protected final List<Verificateur> verifiateurOrganisme = Arrays.asList(arrayVerifOrganisme);


    // Setters
    public void setNomEspece(String nomEspece) {
        if (nomEspece.equals(""))
            throw new IllegalArgumentException("nomEspece can't be empty");
        this.nomEspece = nomEspece;
        verifiateurOrganisme.stream()
                .filter(f -> f.nomChamps.equals("nomEspece"))
                .forEach(Verificateur::initialize);
    }

    public void setBesoinEnergie(double besoinEnergie) {
        if (besoinEnergie <= 0)
            throw new IllegalArgumentException("besoinEnergie must be greater than 0");
        this.besoinEnergie = besoinEnergie;
        verifiateurOrganisme.stream()
                .filter(f -> f.nomChamps.equals("besoinEnergie"))
                .forEach(Verificateur::initialize);
    }

    public void setEfficaciteEnergie(double efficaciteEnergie) {
        if (efficaciteEnergie < 0 || efficaciteEnergie > 1)
            throw new IllegalArgumentException("efficaciteEnergie must be a value between 0 and 1");
        this.efficaciteEnergie = efficaciteEnergie;
        verifiateurOrganisme.stream()
                .filter(f -> f.nomChamps.equals("efficaciteEnergie"))
                .forEach(Verificateur::initialize);
    }

    public void setResilience(double resilience) {
        if (resilience < 0 || resilience > 1)
            throw new IllegalArgumentException("resilience must be a value between 0 and 1");
        this.resilience = resilience;
        verifiateurOrganisme.stream()
                .filter(f -> f.nomChamps.equals("resilience"))
                .forEach(Verificateur::initialize);
    }

    public void setFertilite(double fertilite) {
        if (fertilite < 0 || fertilite > 1)
            throw new IllegalArgumentException("fertilite must be a value between 0 and 1");
        this.fertilite = fertilite;
        verifiateurOrganisme.stream()
                .filter(f -> f.nomChamps.equals("fertilite"))
                .forEach(Verificateur::initialize);
    }

    public void setAgeFertilite(int ageFertilite) {
        if (ageFertilite < 0 )
            throw new IllegalArgumentException("ageFertilite must be greater or equal to 0");
        this.ageFertilite = ageFertilite;
        verifiateurOrganisme.stream()
                .filter(f -> f.nomChamps.equals("ageFertilite"))
                .forEach(Verificateur::initialize);
    }

    public void setEnergieEnfant(double energieEnfant) {
        if (energieEnfant <= 0 )
            throw new IllegalArgumentException("energieEnfant must be greater than 0");
        this.energieEnfant = energieEnfant;

        if (tailleMaximum == -1)
            this.tailleMaximum = 20 * energieEnfant;

        verifiateurOrganisme.stream()
                .filter(f -> f.nomChamps.equals("energieEnfant"))
                .forEach(Verificateur::initialize);
    }


    public void setTailleMaximum(double tailleMaximum) {
        if (tailleMaximum <= 0)
            throw new IllegalArgumentException("tailleMaximum must be greater than 0");
        this.tailleMaximum = tailleMaximum;
    }
}


