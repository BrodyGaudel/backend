
---

# Backend Product Management API

## Description

Cette application est un backend de gestion de produits développée en Java avec Spring Boot. Elle fournit une API REST permettant de gérer les produits, incluant des fonctionnalités telles que la création, la mise à jour, la suppression, la recherche et la récupération de produits. Les produits sont stockés dans une base de données MySQL.

## Fonctionnalités

- Ajouter un nouveau produit avec des informations détaillées (code-barres, nom, description, prix, quantité).
- Mettre à jour les informations d’un produit existant.
- Supprimer un produit par son ID.
- Rechercher des produits par mot-clé dans le nom ou la description.
- Lister tous les produits avec ou sans pagination.
- Récupérer un produit spécifique par son ID ou son code-barres.

## Prérequis

Avant de lancer l'application, assurez-vous d'avoir les éléments suivants installés :

- [Docker](https://docs.docker.com/get-docker/)
- [JDK 21](https://jdk.java.net/21/)
- [Maven](https://maven.apache.org/download.cgi) (optionnel si vous construisez directement avec Docker)

## Technologies utilisées

- **Java 21**
- **Spring Boot 3.3.4**
- **MySQL**
- **Docker**
- **Maven**
- **Hibernate/JPA**
- **Lombok**

## Installation et configuration

### 1. Cloner le projet

```bash
git clone https://github.com/BrodyGaudel/backend.git
cd backend
mvn clean install
mvn test
mvn spring-boot:run
```

### 2. Configurer la base de données

L'application est configurée pour utiliser une base de données MySQL. Par défaut, les variables d'environnement de la base de données sont :

- `MYSQL_USER`: `root`
- `MYSQL_PWD`: `root`
- `MYSQL_HOST`: `172.20.0.2`
- `MYSQL_PORT`: `3307`
- `MYSQL_DATABASE`: `products_db`

Vous pouvez modifier ces valeurs en fonction de votre configuration MySQL dans un fichier `.env` ou les passer directement lors de l’exécution du conteneur Docker.

### 3. Construire l'application avec Docker

L'application est prête à être construite avec Docker. Suivez ces étapes pour construire et exécuter le conteneur Docker :

#### a. Construire l'image Docker

```bash
docker build -t product-backend .
```

#### b. Exécuter le conteneur Docker

```bash
docker run -d --name product-backend -p 8888:8888 -e MYSQL_USER=root -e MYSQL_PWD=root -e MYSQL_HOST=172.20.0.2 -e MYSQL_PORT=3307 -e MYSQL_DATABASE=products_db product-backend
```
ou encore avec un fichier d'environnement

```bash
docker run --env-file .env -d --name product-backend -p 8888:8888 product-backend
```

L'application sera disponible à l’adresse `http://localhost:8888`.

### 4. Utiliser l'API

Voici quelques exemples de points de terminaison de l'API :

- **GET /products/all** : Récupère tous les produits
- **GET /products/get/{id}** : Récupère un produit par son ID
- **POST /products/create/** : Crée un nouveau produit (requiert un `ProductRequestDTO`)
- **PUT /products/update/{id}** : Met à jour un produit existant
- **DELETE /products/delete/{id}** : Supprime un produit par son ID
- **GET /products/search?keyword=...** : Recherche des produits par mot-clé

### 5. Documentation API

L'application expose une documentation Swagger pour faciliter l'exploration et le test des endpoints. Vous pouvez accéder à la documentation en ouvrant :

```
http://localhost:8888/api/swagger-ui.html
```

### 6. Exécuter les tests

Les tests unitaires sont écrits avec JUnit et Mockito. Vous pouvez exécuter les tests avec Maven :

```bash
mvn test
```

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une pull request ou à signaler des problèmes via les [issues](https://github.com/votre-utilisateur/votre-repo/issues).

## Auteurs

- **Brody Gaudel MOUNANGA BOUKA** - Auteur et mainteneur principal.

## Licence

Ce projet est sous licence MIT. Consultez le fichier [LICENSE](LICENSE) pour plus de détails.

---

### Notes supplémentaires

- Si vous utilisez un autre port pour MySQL, pensez à adapter la variable d'environnement `MYSQL_PORT`.
- Si vous déployez cette application sur un environnement de production, assurez-vous d'utiliser des mots de passe et des configurations de sécurité appropriés.

---