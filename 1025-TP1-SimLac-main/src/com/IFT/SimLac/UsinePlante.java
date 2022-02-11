package com.IFT.SimLac;

public class UsinePlante extends UsineOrganisme{

    public Plante creerPlante(){
        for (var verif : verifiateurOrganisme){
            if (!verif.initialized)
                throw new IllegalStateException(
                        verif.nomChamps + " must be initialized before the creerCarnivore method can be called");
        }

        return new Plante(nomEspece, energieEnfant, 0, besoinEnergie, efficaciteEnergie, resilience, fertilite,
                ageFertilite, energieEnfant, tailleMaximum);
    }

}