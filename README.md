# HealthAI Coach API - Documentation

## Vue d'ensemble

L'API REST HealthAI Coach est construite avec **Spring Boot 4.0.3**, **Java 21** et utilise l'authentification **JWT**. Elle fournit des endpoints pour :

- 🔐 Authentification et gestion utilisateurs
- 💪 Catalogue d'exercices et suivi des séances
- 🍽️ Suivi nutritionnel
- 📊 Données biométriques

## Architecture

```
API REST
├── Authentication (JWT)
├── User Management
├── Exercise Catalog
├── Nutrition Tracking
├── Workout Tracking
├── Biometric Tracking
└── PostgreSQL Database (voir MSPR-HealthAI-Coach-BDD)
```

**Note importante** : Les migrations de base de données sont centralisées dans le repo `MSPR-HealthAI-Coach-BDD`. L'API se connecte à une base déjà initialisée.

## Configuration

### Variables d'environnement

```bash
# PostgreSQL
DB_HOST=localhost
DB_PORT=5432
DB_NAME=healthai
DB_USER=healthai_user
DB_PASSWORD=password

# API
API_PORT=8080
SPRING_PROFILE=dev
JWT_SECRET=your_secret_key_min_32_chars
```

### Profiles Spring Boot

- **dev** : Mode développement avec logs détaillés
- **prod** : Mode production avec logs minimales

## Installation et démarrage

### Prérequis

- Java 21
- Maven 3.9+
- PostgreSQL 16 (ou utiliser le repo `MSPR-HealthAI-Coach-BDD`)

### Démarrage de la base de données

**Important** : Avant de démarrer l'API, vous devez avoir une base PostgreSQL opérationnelle avec le schéma HealthAI.

**Option 1 - Avec Docker (recommandé)** :
```bash
# Depuis la racine du projet
cd ../MSPR-HealthAI-Coach-BDD
docker compose up -d
# Attendez que le healthcheck passe
```

**Option 2 - PostgreSQL local** :
```bash
# Exécutez manuellement les migrations depuis MSPR-HealthAI-Coach-BDD/migrations/
psql -U healthai_user -d healthai -f ../MSPR-HealthAI-Coach-BDD/migrations/V1__init_schema.sql
psql -U healthai_user -d healthai -f ../MSPR-HealthAI-Coach-BDD/migrations/V2__diet_recommendations.sql
psql -U healthai_user -d healthai -f ../MSPR-HealthAI-Coach-BDD/migrations/V3__add_unique_constraints.sql
```

### Démarrage de l'API

```bash
cd MSPR-HealthAI-Coach-API

# Compiler le projet
mvn clean install

# Démarrer l'application
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

### Démarrage avec Docker

```bash
# 1. Démarrer d'abord la base de données
cd ../MSPR-HealthAI-Coach-BDD
docker compose up -d

# 2. Puis démarrer l'API
cd ../MSPR-HealthAI-Coach-API
docker compose --profile api up -d
```

## API Documentation

### Swagger UI

Une fois l'API démarrée, accédez à la documentation Swagger :

```
http://localhost:8080/api/swagger-ui.html
```

## Endpoints principaux

### 🔐 Authentification

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/api/auth/register` | Créer un compte utilisateur |
| POST | `/api/auth/login` | Se connecter et obtenir un JWT |

### 👤 Utilisateurs

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/users/me` | Récupérer le profil de l'utilisateur connecté | ✅ |
| GET | `/api/users/{userId}` | Récupérer les infos d'un utilisateur | ✅ |
| PUT | `/api/users/{userId}` | Mettre à jour le profil utilisateur | ✅ |

### 💪 Exercices

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/exercises` | Liste des exercices (paginé) | ✅ |
| GET | `/api/exercises/{id}` | Détails d'un exercice | ✅ |

### 🏋️ Séances d'entraînement

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/workouts/user/{userId}` | Séances d'un utilisateur | ✅ |
| GET | `/api/workouts/{id}` | Détails d'une séance | ✅ |
| POST | `/api/workouts` | Créer une nouvelle séance | ✅ |
| PUT | `/api/workouts/{id}` | Mettre à jour une séance | ✅ |
| DELETE | `/api/workouts/{id}` | Supprimer une séance | ✅ |

### 🍽️ Nutrition

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/nutrition/user/{userId}` | Entrées nutritionnelles d'un utilisateur | ✅ |
| GET | `/api/nutrition/{id}` | Détails d'une entrée | ✅ |
| POST | `/api/nutrition` | Créer une entrée nutritionnelle | ✅ |
| PUT | `/api/nutrition/{id}` | Mettre à jour une entrée | ✅ |
| DELETE | `/api/nutrition/{id}` | Supprimer une entrée | ✅ |

### 📊 Données biométriques

| Méthode | Endpoint | Description | Auth |
|---------|----------|-------------|------|
| GET | `/api/biometrics/user/{userId}` | Données biométriques d'un utilisateur | ✅ |
| GET | `/api/biometrics/{id}` | Détails d'une mesure | ✅ |
| POST | `/api/biometrics` | Créer une mesure biométrique | ✅ |
| PUT | `/api/biometrics/{id}` | Mettre à jour une mesure | ✅ |
| DELETE | `/api/biometrics/{id}` | Supprimer une mesure | ✅ |

### 🏥 Santé

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/health` | Vérifier la santé de l'API |

## Authentification JWT

### Obtenir un token

```bash
# Enregistrement
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@example.com",
    "username": "user",
    "password": "password123",
    "age": 30,
    "gender": "M",
    "weightKg": 75.5,
    "heightCm": 180.0
  }'

# Réponse
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "type": "Bearer",
  "userId": 1,
  "email": "user@example.com",
  "username": "user",
  "role": "USER"
}
```

### Utiliser le token

```bash
curl -X GET http://localhost:8080/api/users/me \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9..."
```

## Structure des réponses

### Succès (200, 201)

```json
{
  "id": 1,
  "email": "user@example.com",
  "username": "user",
  "role": "USER",
  "isPremium": false,
  "age": 30,
  "gender": "M",
  "weightKg": 75.5,
  "heightCm": 180.0,
  "objective": "Lose weight",
  "createdAt": "2024-01-15T10:30:00",
  "lastActivity": "2024-01-15T14:20:00"
}
```

### Erreur

```json
{
  "status": "BAD_REQUEST",
  "message": "Email already exists",
  "error": "Invalid request",
  "timestamp": "2024-01-15T10:30:00",
  "path": "/api/auth/register"
}
```

## Entités principales

### User
- `id` : Identifiant unique
- `email` : Adresse email (unique)
- `username` : Nom d'utilisateur (unique)
- `passwordHash` : Mot de passe hashé (BCrypt)
- `role` : Rôle utilisateur (USER, ADMIN)
- `isPremium` : Abonnement premium
- Données biométriques : age, gender, weightKg, heightCm, objective

### Exercise
- `id` : Identifiant unique
- `externalId` : ID externe (source ExerciseDB)
- `name` : Nom de l'exercice
- `bodyParts` : Parties du corps travaillées
- `targetMuscles` : Muscles ciblés
- `secondaryMuscles` : Muscles secondaires
- `equipments` : Équipement requis
- `instructions` : Instructions d'exécution
- `gifUrl` : Lien vers animation GIF

### NutritionEntry
- `id` : Identifiant unique
- `user` : Utilisateur associé
- `foodName` : Nom de l'aliment
- `category` : Catégorie (fruits, protéines, etc.)
- `mealType` : Type de repas (breakfast, lunch, dinner, snack)
- Macronutriments : calories, protein, carbs, fat, fiber, sugars
- Minéraux : sodium, cholesterol, water

### ExerciseEntry (Workout)
- `id` : Identifiant unique
- `user` : Utilisateur associé
- `workoutType` : Type d'exercice
- `durationMin` : Durée en minutes
- `caloriesBurned` : Calories brûlées
- `steps` : Nombre de pas
- Données cardiaques : heartRateAvg, heartRateMax

### BiometricEntry
- `id` : Identifiant unique
- `user` : Utilisateur associé
- `weightKg`, `heightCm` : Poids et taille
- `bmi` : Indice de masse corporelle
- `fatPercentage` : Pourcentage de graisse
- Données cardiaques : heartRateRest, heartRateAvg, heartRateMax
- `bloodPressure` : Pression artérielle

## Sécurité

- 🔒 **JWT** : Tokens d'authentification avec expiration
- 🔐 **BCrypt** : Hashage des mots de passe
- 🛡️ **CORS** : Configurable pour dev/prod
- ⏰ **Token Expiration** : 24 heures par défaut

## Logs

Les logs sont configurés par profil :

- **Dev** : Niveau DEBUG avec SQL détaillé
- **Prod** : Niveau INFO, logs minimaux

Fichiers de logs : `/app/logs/` (en production Docker)

## Déploiement

### Docker

```bash
# Build image
docker build -t healthai-api:latest API/

# Ou via docker-compose
docker compose --profile api build
docker compose --profile api up -d
```

### Variables d'environnement requises

```bash
DB_HOST=db
DB_PORT=5432
DB_NAME=healthai
DB_USER=healthai_user
DB_PASSWORD=secure_password
JWT_SECRET=your_very_long_secret_key_min_32_chars
API_PORT=8080
SPRING_PROFILE=prod
```

## Tests

```bash
# Exécuter les tests unitaires
mvn test

# Avec couverture de code
mvn test jacoco:report
```

## Troubleshooting

### Erreur de connexion DB
```
Vérifier: DB_HOST, DB_PORT, DB_NAME, DB_USER, DB_PASSWORD
```

### Token expiré
```
Erreur 401 Unauthorized
Solution: Faire une nouvelle connexion pour obtenir un nouveau token
```

### CORS blocked
```
Ajouter les domaines autorisés dans SecurityConfig
```

## Prochains développements

- [ ] Refresh tokens
- [ ] Social login (Google, Apple)
- [ ] Rate limiting
- [ ] Caching (Redis)
- [ ] WebSockets pour real-time data
- [ ] Mobile push notifications
- [ ] Recommandations IA

## Support

Pour toute question, contactez : support@healthai-coach.com

---

**Version** : 1.0.0
**Dernière mise à jour** : 2024-01-15
**Mainteneur** : EPSI Team

