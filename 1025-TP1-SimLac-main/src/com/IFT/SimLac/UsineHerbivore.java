package com.IFT.SimLac;

import java.util.*;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy
 * Date :    March 2nd 2021
 *
 * This factory class is used by the ConditionsInitiales class to build Herbivore objects and populate ecosystems.
 * It makes sure all attributes are initialized to a valid value before the builder method can be called.
 */

public final class UsineHerbivore extends UsineOrganisme{

    // Attributes
    double debrouillardise;                     // Determines the chance of having one or multiple meals during a cycle.
    double voraciteMin;                         // The minimum ratio of a plant an herbivore eats in one meal.
    double voraciteMax;                         // The maximum ratio of a plant an herbivore eats in one meal.
    Set<String> aliments = new HashSet<>();     // Set of Plante objects that can be eaten by this herbivore.

    // Only used to initialize verifiateurHerbivore.
    private final Verificateur[] arrayVerifHerbivore = {
            new Verificateur("debrouillardise"),
            new Verificateur("voraciteMin"),
            new Verificateur("voraciteMax"),
            new Verificateur("aliments")
    };

    // Keeps track of initialized attributes. Used as a condition before the builder method can be called.
    protected final List<Verificateur> verifiateurHerbivore = Arrays.asList(arrayVerifHerbivore);


    // Builder method
    public Herbivore creerHerbivore() {
        var verificateurComplet = new ArrayList<Verificateur>();
        verificateurComplet.addAll(verifiateurOrganisme);
        verificateurComplet.addAll(verifiateurHerbivore);

        for (var verif : verificateurComplet){
            if (!verif.initialized)
                throw new IllegalStateException(
                        verif.nomChamps + " must be initialized before the creerHerbivore method can be called");
        }

        return new Herbivore(nomEspece, energieEnfant, 0, besoinEnergie, efficaciteEnergie, resilience, fertilite,
                ageFertilite, energieEnfant, debrouillardise, voraciteMin, voraciteMax, aliments, tailleMaximum);
    }


    // Setters
    public void setDebrouillardise(double debrouillardise) {
        if (debrouillardise < 0 || debrouillardise > 1)
            throw new IllegalArgumentException("debrouillardise must be a value between 0 and 1");
        this.debrouillardise = debrouillardise;
        verifiateurHerbivore.stream()
                .filter(f -> f.nomChamps.equals("debrouillardise"))
                .forEach(Verificateur::initialize);
    }

    public void setVoraciteMin(double voraciteMin) {
        if (voraciteMin < 0 || voraciteMin > 1)
            throw new IllegalArgumentException("voraciteMin must be a value between 0 and 1");
        this.voraciteMin = voraciteMin;
        verifiateurHerbivore.stream()
                .filter(f -> f.nomChamps.equals("voraciteMin"))
                .forEach(Verificateur::initialize);
    }

    public void setVoraciteMax(double voraciteMax) {
        if (voraciteMax < 0 || voraciteMax > 1)
            throw new IllegalArgumentException("voraciteMax must be a value between 0 and 1");
        this.voraciteMax = voraciteMax;
        verifiateurHerbivore.stream()
                .filter(f -> f.nomChamps.equals("voraciteMax"))
                .forEach(Verificateur::initialize);
    }

    public void addAliment(String aliment){
        if (aliment.equals(""))
            throw new IllegalArgumentException("aliments can't be empty");
        aliments.add(aliment);
        verifiateurHerbivore.stream()
                .filter(f -> f.nomChamps.equals("aliments"))
                .forEach(Verificateur::initialize);
    }
}
