Code Source : 
https://github.com/KEG01/RoverLand

Analyse : 
https://sonarcloud.io/dashboard?id=KEG01_RoverLand

Machine virtuelle : 
https://drive.google.com/open?id=1bPVjk_NGdy9Zo04GraRiRlur4aT6lOlG

Pour se connecter à la machine virtuelle, les identifiants sont test: test. L'utilisateur fait partie des sudoers.

Pour exécuter le projet, récupérer la machine virtuelle (Virtual box) hébergée sur google drive (lien ci-dessus).
Pour importer la VM, il faut en créer une nouvelle avec VirtualBox et choisir d'utiliser un disque existant sur le premier écran de création.
Lancer le serveur RUST (se déplacer dans le répertoire /home/test/Documents/RoverLand/back/api_soft puis executer la commande "cargo run") et l'exécutable .jar correspondant à l'application (test_et_securite_executable.jar) (le fichier est dans /home/test/Documents et la commande à utiliser est "java -jar test_et_securite_executable.jar" ).

Remarque : Le fait d'utiliser l'application sous Debian décale un peu les images. De plus, certains caractères ne s'affichent pas correctement. Se fier aux captures pour les détails de l'affichage.

Si nécessaire, il est possible d'installer l'application sur votre machine, pour cela : 
- Installer RUST et lancer le serveur (cf Intructions dans le rapport)
- Installer Java (JDK 9.0.1)
- Lancer eclipse et / ou récupérer le .jar

Tous les logiciels pour le "front" ainsi que la machine virtuelle sont hébergés sur google drive :
https://drive.google.com/open?id=1bPVjk_NGdy9Zo04GraRiRlur4aT6lOlG

Remarque : Si vous souhaitez effectuer les tests unitaires, il faudra probablement intégrer le .jar de Junit 5 dans le "build path". Celui-ci est également présent dans le dossier "Logiciels".
