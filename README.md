# Gestion des commandes

Application web Java de gestion des clients, des produits, des commandes et des lignes de commande. Ce projet a été réalisé dans le cadre d’un atelier consacré aux Servlets, aux JSP, aux JavaBeans, à JDBC et à Maven.

## Fonctionnalités

- **Clients** : créer, afficher, modifier et supprimer des clients.
- **Produits** : créer, afficher, modifier et supprimer des produits.
- **Commandes** : créer et gérer des commandes associées à un client.
- **Lignes de commande** : ajouter un produit et une quantité à une commande, puis afficher, modifier ou supprimer ses lignes.

Les opérations CRUD des quatre espaces ont été testées dans l’environnement local.

## Technologies

- Java 17 (compilation Maven configurée au niveau Java 11)
- IntelliJ IDEA
- Maven
- Java Servlet API 4.0.1 (`javax.servlet`)
- JSP et JSTL 1.2
- JDBC et MySQL Connector/J 9.7.0
- MySQL
- Apache Tomcat 9

## Architecture

```text
Navigateur
   │ requête HTTP
   ▼
Servlets (contrôleurs)
   │ appels métier et données
   ▼
DAO ── JDBC ── MySQL
   │ résultats sous forme d'entités
   ▼
Servlets ── attributs de requête ── JSP/JSTL (vues)
   │
   ▼
Navigateur (HTML)
```

Le modèle MVC du projet sépare les entités et l’accès aux données, le traitement des requêtes par les Servlets, et l’affichage par les JSP.

### Organisation du code

```text
src/main/java/ma/fstt/
├── entities/       # JavaBeans : Client, Produit, Commande, LigneDeCommande
├── service/        # Interface générique CrudService<T>
├── dao/            # DAO JDBC et DBConnection
└── servlet/        # Servlets contrôleurs

src/main/webapp/
├── css/            # Feuille de style
└── WEB-INF/
    ├── web.xml
    └── views/
        ├── clients/
        ├── produits/
        ├── commandes/
        └── lignes/
```

## Modèle de données

La base MySQL utilisée est `gestion_commandes`.

| Table | Champs principaux | Description |
|---|---|---|
| `client` | `id`, `nom`, `email`, `adresse` | Informations des clients |
| `produit` | `id`, `nom`, `description`, `prix`, `stock` | Catalogue des produits |
| `commande` | `id`, `date_commande`, `status`, `client_id` | Commandes rattachées à un client |
| `ligne_commande` | `id`, `commande_id`, `produit_id`, `quantite`, `prix_unitaire` | Détail des produits d’une commande |

`client_id`, `commande_id` et `produit_id` représentent les relations entre les tables. La base utilise le nom de colonne `status`, tandis que la propriété Java de l’entité `Commande` s’appelle `statut`; le DAO fait la correspondance entre ces deux noms.

## Prérequis

Avant de lancer l’application, il faut disposer de :

1. Java JDK 17 ou une version compatible avec la configuration Maven du projet ;
2. Maven (ou l’intégration Maven d’IntelliJ IDEA) ;
3. MySQL avec la base `gestion_commandes` et ses quatre tables ;
4. Apache Tomcat 9.

## Configuration de MySQL

La connexion JDBC est définie dans `ma.fstt.dao.DBConnection`. Vérifier que l’URL, le nom d’utilisateur et le mot de passe correspondent à l’installation MySQL locale.

Ne pas publier de vrai mot de passe, jeton ou donnée personnelle dans le dépôt. Pour un projet réel, les identifiants doivent être fournis séparément du code source.

## Construire le projet

Dans IntelliJ IDEA :

1. Ouvrir la fenêtre **Maven**.
2. Déplier **Lifecycle**.
3. Exécuter `clean`, puis `package`.
4. Vérifier que la construction se termine par `BUILD SUCCESS`.

Le fichier WAR est généré ici :

```text
target/GestionCommandes-1.0-SNAPSHOT.war
```

## Déployer sur Tomcat

1. Copier le fichier WAR dans le dossier `webapps` de Tomcat.
2. Démarrer Tomcat et vérifier que son port HTTP est libre.
3. Ouvrir l’espace voulu dans le navigateur.

Exemple avec le port Tomcat par défaut :

```text
http://localhost:8080/GestionCommandes-1.0-SNAPSHOT/clients
```

Si le port HTTP de Tomcat a été modifié dans `conf/server.xml`, remplacer `8080` par le port configuré. Par exemple, pour le port `8082` :

```text
http://localhost:8082/GestionCommandes-1.0-SNAPSHOT/clients
```

Les autres espaces sont accessibles avec les chemins `/produits`, `/commandes` et `/lignes`. L’espace des lignes de commande s’ouvre depuis une commande existante.

## Tests fonctionnels

| Espace | Créer | Afficher | Modifier | Supprimer |
|---|---:|---:|---:|---:|
| Clients | Oui | Oui | Oui | Oui |
| Produits | Oui | Oui | Oui | Oui |
| Commandes | Oui | Oui | Oui | Oui |
| Lignes de commande | Oui | Oui | Oui | Oui |

## Diagramme de classes

Le modèle métier comprend les classes `Client`, `Commande`, `Produit` et `LigneDeCommande` : un client peut passer plusieurs commandes ; une commande peut contenir plusieurs lignes ; chaque ligne référence un produit et conserve la quantité ainsi que le prix unitaire appliqué.
