## Savasana project (english version)

This project consists of implementing the complete testing strategy for the application, from writing test cases to their execution, to ensure the robustness of the code before launch.

# How to Set Up This Project

First, you need to fork and clone this repository: 

[Savasana](https://github.com/AlexisTrouwaert/P5-Test-application.git)

Once done, follow these steps:

1 - Open the resources directory, and go to the sql folder to find "script.sql".

2 - Open your SQL command-line client and log in.

3 - Create your database by running: 
    `CREATE DATABASE name_of_your_database;`
    (You can choose any name you want for your database.)

4 - Verify that your database has been created by running:
    `SHOW DATABASES;`

5 - Select your database:
    `USE name_of_your_database;`

6 - Run your SQL script with the following command (on Windows CMD):
    `mysql --user="your_username" --database="name_of_your_database" --password="your_password" < path/to/script.sql`

7 - To ensure everything is set up correctly, run:
    `SHOW TABLES`
    You should see three new tables: TEACHERS, SESSIONS, USERS and PARTICIPATE.

# Finalizing the Setup

Now that your database is set up, follow these steps:

1 - Open the terminal in your IDE, navigate to the root of the Angular project, and run:
    `npm install`

2 - In another terminal, navigate to the root of the Spring Boot project and run:
    `mvn install`

Configuring DB connection :

1 - Go to the root of the project then open back/src/main/ressources/application.properties

2 - Then add your credentials to connect to the DB.

The url of your DB should looks like this : jdbc:mysql://localhost:3306/name_of_your_database

# Run the project

1 - Open the terminal in your IDE, navigate to the root of the Angular project, and run:
    `ng serve`

2 - In another terminal, navigate to the root of the Spring Boot project and run:
    `mvn spring-boot:run`

3 - In your browser go to http://localhost:4200/

# Rune tests

1 - Front end integrations tests : `npm run test:integration`

2 - Front end all tests : `npm run test`

3 - Back end run integrations test : `mvn failsafe:integration-test -Dit.test="**/*IT`

4 - Back end run all tests : `mvn verify`

## Projet Savasana (version française)

Ce projet consiste à mettre en place la stratégie de tests complète de l'application, de la rédaction des cas de test (test cases) à leur exécution, pour assurer la robustesse du code avant le lancement.

# Comment configurer ce projet

Tout d'abord, vous devez forker et cloner ce dépôt :

[Savasana](https://github.com/AlexisTrouwaert/P5-Test-application.git)

Une fois cela fait, suivez ces étapes :

1 - Accédez au projet, ouvrez le dossier resources, puis rendez-vous dans le dossier sql pour trouver "script.sql".

2 - Ouvrez votre client SQL en ligne de commande et connectez-vous.

3 - Créez votre base de données en exécutant la commande suivante :
    `CREATE DATABASE name_of_your_database;`
    (Vous pouvez choisir n'importe quel nom pour votre base de données.)

4 - Vérifiez que votre base de données a bien été créée en exécutant :
    `SHOW DATABASES;`

5 - Sélectionnez votre base de données :
    `USE name_of_your_database;`

6 - Exécutez votre script SQL avec la commande suivante (sur Windows CMD) :
    `mysql --user="your_username" --database="name_of_your_database" --password="your_password" < chemin/vers/script.sql`

7 - Pour vérifier que tout est bien configuré, exécutez :
    `SHOW TABLES`
    Vous devriez voir trois nouvelles tables : TEACHERS, SESSIONS, USERS and PARTICIPATE.

# Finalisation de l'installation

Maintenant que votre base de données est configurée, suivez ces étapes :

1 - Ouvrez le terminal dans votre IDE, naviguez à la racine du projet Angular, puis exécutez :
    `npm install`

2 - Dans un autre terminal, naviguez à la racine du projet Spring Boot, puis exécutez :
    `mvn install`

Configuration de la connection à la BDD

3 - Aller dans le dossier root du projet et ouvrez dans bacl/src/main/ressources/applications.properties

4 - Pusi ajoutez vos identifiants de connection mySql

L'URL de la base de donnée ressemble à cela : jdbc:mysql://localhost:3306/name_of_your_database

# Lancer tests

1 - Front end tests d'integrations : `npm run test:integration`

2 - Front end tout les tests : `npm run test`

3 - Front end tests end 2 end : `npm run cypress:run`

3 - Back end run tests d'integrations : `mvn failsafe:integration-test -Dit.test="**/*IT`

4 - Back end run tout les tests : `mvn verify`