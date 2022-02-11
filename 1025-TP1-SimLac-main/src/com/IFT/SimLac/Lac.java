package com.IFT.SimLac;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summarizingDouble;

/**
 * Authors : Mathieu Morin & Adrian Sanchez Roy; from template by Simon Génier
 * Date :    March 1st 2021
 *
 * This class represents an ecosystem in which different organisms live, feed, breed, die.
 * A tick represents a unit of time (or a cycle) during which all above events can happen.
 *
 * Each tick, the order in which the different categories of organisms take their turn should follow the flow of energy.
 * This flow pushes upwards the food chain :
 *     Sun (new energy injected) -> Plantes -> Herbivores -> Carnivores
 */

public final class Lac {
    private final int energieSolaire;  // New energy from the sun injected into the ecosystem
    private final List<Plante> plantes;
    private final List<Herbivore> herbivores;
    private final List<Carnivore> carnivores;


    // Constructor
    public Lac(int energieSolaire, List<Plante> plantes, List<Herbivore> herbivores, List<Carnivore> carnivores) {
        this.energieSolaire = energieSolaire;
        this.plantes = plantes;
        this.herbivores = herbivores;
        this.carnivores = carnivores;
    }


    // Simulates one cycle
    public void tick() {
        plantsLifeCycle();
        herbivoresLifeCycle();
        carnivoreLifeCycle();

        enleverMorts();
        vieillissement();
    }

    // For each plant, calculates the energy input from the sun, then goes through its life cycle.
    // i.e. verifies if it survives, have children, grows or shrinks.
    private void plantsLifeCycle() {
        double energieTotaleDesPlantes = calculEnergieTotaleDesPlantes();
        var enfants = new ArrayList<Plante>();

        for (var plante : plantes) {
            plante.bilanEnergetique(energieSolaire, energieTotaleDesPlantes);
            plante.survivalCheck();

            int nombreEnfants = plante.reproductionCheck();
            for (int i = 0; i < nombreEnfants; i++)
                enfants.add(plante.seReproduire());

            plante.croitreDecroitre();
        }
        plantes.addAll(enfants);
    }

    // For each herbivore, calculates the energy input from plants eaten, then goes through its life cycle.
    // i.e. verifies if it survives, have children, grows weaker or stronger.
    private void herbivoresLifeCycle() {
        var enfants = new ArrayList<Herbivore>();

        for (var herbivore : herbivores) {
            var plantesMangees = listerPlantesMangees(herbivore);

            herbivore.bilanEnergetique(plantesMangees);
            herbivore.survivalCheck();

            int nombreEnfants = herbivore.reproductionCheck();
            for (int i = 0; i < nombreEnfants; i++)
                enfants.add(herbivore.seReproduire());

            herbivore.croitreDecroitre();
        }
        herbivores.addAll(enfants);
    }

    // For each carnivore, calculates the energy input from plants herbivores, then goes through its life cycle.
    // i.e. verifies if it survives, have children, grows weaker or stronger.
    private void carnivoreLifeCycle() {
        var enfants = new ArrayList<Carnivore>();

        for (var carnivore : carnivores) {
            var herbivoresMangees = listerHerbivoresMangees(carnivore);

            carnivore.bilanEnergetique(herbivoresMangees);
            carnivore.survivalCheck();

            int nombreEnfants = carnivore.reproductionCheck();
            for (int i = 0; i < nombreEnfants; i++)
                enfants.add(carnivore.seReproduire());

            carnivore.croitreDecroitre();
        }
        carnivores.addAll(enfants);
    }

    // Returns a list of all the plants an herbivore eats during a cycle
    private List<Plante> listerPlantesMangees(Herbivore herbivore) {
        Predicate<Plante> estMangeable = plante -> herbivore.getAliments().contains(plante.getNomEspece());
        var plantesMangeables = plantes.stream()
                .filter(estMangeable)
                .collect(Collectors.toList());

        int nombreRepas = herbivore.nombreRepas();
        if (plantesMangeables.size() <= nombreRepas)
            return plantesMangeables;

        var plantesMangees = new ArrayList<Plante>();
        for (int i = 0; i < nombreRepas; i++) {
            int randomindex = (int) (Math.random() * plantesMangeables.size());
            // Herbivores can't eat more than the plant's energy, nor can they kill it by eating it.
            // So they can eat the same plant twice.
            plantesMangees.add(plantesMangeables.get(randomindex));
        }
        return plantesMangees;
    }

    // Returns a list of all the herbivores a carnivore eats during a cycle
    private List<Herbivore> listerHerbivoresMangees(Carnivore carnivore) {
        Predicate<Herbivore> estMangeable = herbivore ->
                (carnivore.getAliments().contains(herbivore.getNomEspece()) &&
                 carnivore.getEnergie() >= herbivore.getEnergie());

        var herbivoresMangeables = herbivores.stream()
                .filter(estMangeable)
                .collect(Collectors.toList());

        int nombreRepas = carnivore.nombreRepas();
        if (herbivoresMangeables.size() <= nombreRepas)
            return herbivoresMangeables;

        var herbivoresManges = new ArrayList<Herbivore>();
        for (int i = 0; i < nombreRepas; i++) {
            int randomindex = (int) (Math.random() * herbivoresMangeables.size());
            // Carnivores kill their prey and eat them completely; i.e they can't eat the same prey twice.
            herbivoresManges.add(herbivoresMangeables.remove(randomindex));
        }
        return herbivoresManges;
    }

    private void enleverMorts() {
        plantes.removeIf(Plante::isDead);
        herbivores.removeIf(Herbivore::isDead);
        carnivores.removeIf(Carnivore::isDead);
    }

    private void vieillissement() {
        plantes.forEach(Plante::vieillir);
        herbivores.forEach(Herbivore::vieillir);
        carnivores.forEach(Carnivore::vieillir);
    }

    private double calculEnergieTotaleDesPlantes(){
        double energieTotale = 0;
        for (var plante : plantes)
            energieTotale += plante.getEnergie();

        return energieTotale;
    }

    public void imprimeRapport(PrintStream out) {
        var especesPlantes = this.plantes.stream()
                .collect(groupingBy(Plante::getNomEspece, summarizingDouble(Plante::getEnergie)));
        out.println("Il reste " + especesPlantes.size() + " espèces de plantes.");
        for (var entry : especesPlantes.entrySet()) {
            var value = entry.getValue();
            out.printf(
                "%s: %d individus qui contiennent en tout %.2f unités d'énergie.%n",
                entry.getKey(),
                value.getCount(),
                value.getSum());
        }

        var especesHerbivores = this.herbivores.stream().collect(groupingBy(
                Herbivore::getNomEspece,
                summarizingDouble(Herbivore::getEnergie)));
        out.println("Il reste " + especesHerbivores.size() + " espèces d'herbivores.");
        for (var entry : especesHerbivores.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.2f unités d'énergie.%n",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }

        var especesCarnivores = this.carnivores.stream().collect(groupingBy(
                Carnivore::getNomEspece,
                summarizingDouble(Carnivore::getEnergie)));
        out.println("Il reste " + especesCarnivores.size() + " espèces de carnivores.");
        for (var entry : especesCarnivores.entrySet()) {
            var value = entry.getValue();
            out.printf(
                    "%s: %d individus qui contiennent en tout %.2f unités d'énergie.%n",
                    entry.getKey(),
                    value.getCount(),
                    value.getSum());
        }
    }
}
