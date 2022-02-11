package com.IFT.SimLac;

import java.util.*;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy
 * Date :    March 3rd 2021
 *
 * This factory class is used by the ConditionsInitiales class to build Carnivore objects and populate ecosystems.
 * It makes sure all attributes are initialized to a valid value before the builder method can be called.
 */

public class UsineCarnivore extends UsineOrganisme {

    // Attributs
    double debrouillardise;                     // Determines the chance of having one or multiple meals during a cycle.
    Set<String> aliments = new HashSet<>();     // Set of Herbivore objects that can be eaten by this Carnivore.

    // Only used to initialize verifiateurCarnivore.
    private final Verificateur[] arrayVerifCarnivore = {
            new Verificateur("debrouillardise"),
            new Verificateur("aliments")
    };

    // Keeps track of initialized attributes. Used as a condition before the builder method can be called.
    protected final List<Verificateur> verifiateurCarnivore = Arrays.asList(arrayVerifCarnivore);


    // Builder method
    public Carnivore creerCarnivore(){
        var verificateurComplet = new ArrayList<Verificateur>();
        verificateurComplet.addAll(verifiateurOrganisme);
        verificateurComplet.addAll(verifiateurCarnivore);

        for (var verif : verificateurComplet){
            if (!verif.initialized)
                throw new IllegalStateException(
                        verif.nomChamps + " must be initialized before the creerCarnivore method can be called");
        }

        return new Carnivore(nomEspece, energieEnfant, 0, besoinEnergie, efficaciteEnergie, resilience, fertilite,
                ageFertilite, energieEnfant, debrouillardise, aliments, tailleMaximum);
    }


    // Setters
    public void setDebrouillardise(double debrouillardise) {
        if (debrouillardise < 0 || debrouillardise > 1)
            throw new IllegalArgumentException("debrouillardise must be a value between 0 and 1");
        this.debrouillardise = debrouillardise;
        verifiateurCarnivore.stream()
                .filter(f -> f.nomChamps.equals("debrouillardise"))
                .forEach(Verificateur::initialize);
    }

    public void addAliment(String aliment){
        if (aliment.equals(""))
            throw new IllegalArgumentException("aliments can't be empty");
        aliments.add(aliment);
        verifiateurCarnivore.stream()
                .filter(f -> f.nomChamps.equals("aliments"))
                .forEach(Verificateur::initialize);
    }
}
