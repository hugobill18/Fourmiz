# Fourmiz

Simulation de fourmis en Java/Swing : des fourmis partent d'une fourmilière et se
déplacent aléatoirement sur un terrain composé de cases vides, d'obstacles et de
nourriture.

Le projet existe en deux versions :

- **Java/Swing** : sources dans `src/fourmiz` et `src/simengine` (voir ci-dessous) ;
- **Web** : `fourmiz.html`, une page autonome (HTML/Canvas, sans dépendance) à ouvrir
  directement dans un navigateur — avec phéromones, génération de cartes aléatoires,
  éditeur d'algorithme de déplacement et tableau des scores.

## Compilation et exécution (version Java)

```bash
javac -d out src/fourmiz/*.java src/simengine/*.java
java -cp out fourmiz.JeuWin
```

L'application charge `terrain.dat` (à la racine du projet) au démarrage ; le bouton
**Charger…** permet d'ouvrir un autre fichier de terrain.

## Tests

```bash
gradle test        # JUnit 4
# ou
mvn test
```

## Format d'un fichier terrain (`.dat`)

```
lignes colonnes
nombre_de_fourmis
grille (lignes × colonnes valeurs)
```

Valeurs de la grille : `-1` = fourmilière (unique), `-2` = obstacle,
`n ≥ 0` = case vide contenant `n` unités de nourriture.
