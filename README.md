# SimLac-main

Programme crée par Adrian Sanchez Roy et Mathieu Morin.

#### Objectif du Programme

Ce programme simule l'écosystème d'un lac constitué de plantes, herbivores et carnivores.

---
###### Q1. Suggérez deux façons de garder une trace de quels attributs ont été initialisés. Laquelle avez-vous choisie et pourquoi ?

Afin de garder une trace de quels attributs ont été initialisés, nous avons développé deux approches.
La première consistait à créer un tableau (*Array*) d'entiers initialisé avec autant de 0 que d'attributs dont on
voulait garder la trace. Chaque *setter* changeait en 1 le 0 à l'indice qui lui était propre. Par exemple :

`tableauVerif[4] = 1;`

Ce tableau était vérifié dans la méthode usine juste avant d'appeler le constructeur. Aussitôt qu'un 0 était trouvé, 
un message d'erreur générique était lancé, avertissant qu'au moins un attribut n'avait pas été initialisé.

Cette approche nous permettait bien de proscrire la création d'un objet si ses attributs ne sont pas tous initialisés,
mais ne nous permettait pas de préciser dans le message d'erreur quel était l'attribut non initialisé. De plus, le code 
n'exprimait pas explicitement son intention, puisque chaque attribut était représenté arbitrairement par un indice. En
effet, il n'était pas immédiatement évident de savoir quel attribut correspondait à l'indice 4. 
C'est pourquoi nous avons opté pour la deuxième approche.

Notre seconde approche utilise plutôt une liste d'objets *Verificateur* (classe interne propre à *UsineOrganisme*). Cet 
objet contient deux attributs. Un premier de type *String* contenant textuellement le nom d'un des différents attributs à
initialiser : cet attribut est utilisé comme une clé. Le second est un booléen nommé *isInitialized*, dont la valeur est
*false* par défaut. La liste contient autant de *Verificateurs* qu'il y a d'attributs à initialiser. Chaque *setter* trouve
dans la liste l'objet *Verificateur* qui a comme clé le même nom que l'attribut initialisé, puis change *isInitialized*
à *true*. Avant d'appeler le constructeur, la méthode usine parcours ce tableau pour vérifier que tous les attributs ont
bel et bien été initialisé. Si ce n'est pas le cas, une exception est lancée. Cette dernière précise dans son message 
quel est l'attribut non initialisé lors de la tentative d'appel au constructeur. Cette approche a l'avantage d'être plus
explicite. De plus, elle permet d'identifier exactement lequel des attributs n'a pas été initialisé. 
C'est pourquoi nous l'avons préférée. 

Note: Ici la classe *Verificateur* est utilisée un peu comme une *Map*. Peut-être était-il possible, et plus simple, 
d'utiliser ce type. Cependant, comme il s'agit d'un type avec lequel nous n'étions pas familier, et que ses
particularités (par exemple on ne peut itérer directement sur une Map) ne rendaient pas l'implémentation directement
évidente pour nous, nous avons opté pour l'utilisation d'une classe interne.

---
###### Q2. Expliquer comment les responsabilités entre les classes Lac et Plante ont été divisées et pourquoi. Quels changements avez-vous dû faire ?

Les responsabilités entre les classes Lac et Plante ont essentiellement été divisées selon les principes de la
Programmation Orientée Objet, c'est-à-dire en tentant de définir chaque classe comme son propre objet, avec son propre
comportement. Nous avons établi que le Lac était responsable de gérer le déroulement d'un *cycle*. Cependant, ce sont 
les plantes qui sont responsables de gérer leur propre comportement à chacune des étapes de ce cycle. C'est pourquoi le
lac ne fait que *demander* à la plante de mettre à jour son budget énergétique, *demander* à la plante si elle survit, 
si elle se reproduit, etc.

On ne se souvient pas avoir été forcés d'apporter des changements à la classe *Lac* qui nous a été fournie (excluant 
bien sur l'ajout et l'implementation des *features* demandés). Des modifications à la classe *Plante* ont été apportées
plus tard, lors de l'introduction des nouvelles classes. Notamment, la classe abstraite *Organisme* devenu le parent de
la classe *Plante*, vers laquelle ont migré la quasi-totalité des attributs et méthodes.

---
###### Q3. Expliquer pourquoi les classes Plante et Herbivore héritent d'une classe ou d'une interface Organisme. Expliquer aussi comment vous avez éliminez la répétition de code entre les deux classes-usines.

Les classes *Plante* et *Herbivore* héritent d'une même classe abstraite nommée *Organisme*. Nous avons opté pour une 
classe abstraite puisque nous savions que plusieurs méthodes allaient être communes pour tous les organismes. La classe
abstraite permet donc d'implémenter ces méthodes dans cette class. Les sous classes héritent alors de ces méthodes. 
L'interface ne permet pas l'implémentation de méthode de cette façon. Elle n'est qu'un 'contrat' de ce qui se retrouve
de l'autre côté de l'interface. Il faut donc spécifier ensuite, pour chacune des classes qui implémente l'interface, le
comportement de chacune des méthodes pour chacune des classes. Du code est ainsi inutilement répété.

Cette même logique a été employée afin d'éliminer la répétition de code entre les classes *usineHerbivore* et
*usinePlante*. La classe *usineOrganisme* regroupe tous ce qui est commun aux classes usines.

---
###### Q4. Expliquer comment les responsabilités entre les classes Lac et Herbivore ont été divisées et pourquoi. Avez-vous changé de stratégie par rapport à la première partie?

Nous avons divisé les responsabilités entre les classes *Lac* et *Herbivore* essentiellement de la même manière que
nous les avons divisées entre les classes *Lac* et *Plante*. Nous n'avons donc pas eu à modifier notre stratégie par
rapport à la première partie. 

---
###### Q5. Lors de la modification de la classe ConditionsInitiales, avez-vous réutilisé du code? Si oui, expliquer comment et pourquoi s'est désirable. Si non, expliquez pourquoi c'est indésirable.

Lors de notre modification de la classe ConditionsInitiales pour supporter les carnivores, nous avons réutilisé du code. 
Nous croyons que cela était désirable. Notre principal argument repose sur l'utilisation du cas *default* de la
*switch* qui permet de s'assurer que les attributs qui sont lus sont bien des attributs désirés pour chaque type
d'organisme. Ainsi, il est facile et évident de détecter un fichier XML mal conçu, ou une classe mal conçue.

Il était probablement possible de faire une seule fonction *nextOrganisme* pour éviter de répéter le code, mais il ne
nous apparaissait pas une façon claire et évidente de le faire tout en conservant la détection d'incohérence entre le 
XML et les classes, tel que décrit au paragraphe précédant. La clarté du code et de son intention en aurait probablement
souffert, ce que nous voulions éviter.
