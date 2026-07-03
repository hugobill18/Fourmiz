# Fourmiz

Simulation de fourmis en Java/Swing : des fourmis partent d'une fourmilière et se
déplacent aléatoirement sur un terrain composé de cases vides, d'obstacles et de
nourriture.

Les sources Java sont organisées dans `src/fourmiz` et `src/simengine`.

## Compilation et exécution

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
