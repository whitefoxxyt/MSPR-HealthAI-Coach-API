# HealthAI Coach API - Documentation Complète

## 📋 Table des matières

1. [Vue d'ensemble](#vue-densemble)
2. [Architecture](#architecture)
3. [Configuration & Démarrage](#configuration--démarrage)
4. [Authentification JWT](#authentification-jwt)
5. [Endpoints API](#endpoints-api)
6. [DTOs (Data Transfer Objects)](#dtos-data-transfer-objects)
7. [Exceptions & Gestion des erreurs](#exceptions--gestion-des-erreurs)
8. [Base de données](#base-de-données)
9. [Sécurité](#sécurité)
10. [Exemples d'utilisation](#exemples-dutilisation)

---

## 🎯 Vue d'ensemble

L'API REST HealthAI Coach est construite avec **Spring Boot 4.0.3** et **Java 21**. Elle fournit une solution complète pour le suivi de la santé et du fitness incluant :

- 🔐 **Authentification JWT** sécurisée
- 👤 **Gestion des utilisateurs** avec profils personnalisés
- 💪 **Catalogue d'exercices** avec données détaillées
- 🏋️ **Suivi des séances d'entraînement** (workouts)
- 🍽️ **Suivi nutritionnel** avec macronutriments
- 📊 **Données biométriques** (poids, IMC, fréquence cardiaque, etc.)

### Technologies utilisées

- **Backend** : Spring Boot 4.0.3, Java 21
- **Base de données** : PostgreSQL 16
- **ORM** : JPA/Hibernate
- **Migration** : Flyway
- **Sécurité** : Spring Security + JWT (jjwt 0.12.3)
- **Documentation** : Swagger/OpenAPI 3
- **Build** : Maven

---

## 🏗️ Architecture

### Structure du projet

```
API/
├── src/main/java/fr/epsi/healthaicoachapi/
│   ├── config/              # Configuration (Security, OpenAPI)
│   ├── controller/          # Contrôleurs REST
│   ├── dto/                 # Data Transfer Objects
│   ├── entity/              # Entités JPA
│   ├── exception/           # Exceptions personnalisées
│   ├── repository/          # Repositories JPA
│   ├── security/            # JWT Filter & Token Provider
│   └── service/             # Logique métier
├── src/main/resources/
│   ├── db/migration/        # Scripts SQL Flyway
│   ├── application.properties
│   ├── application-dev.properties
│   └── application-prod.properties
└── pom.xml
```

### Architecture en couches

```
┌─────────────────────────────────┐
│     Controllers (REST API)      │ ← Exposition des endpoints
├─────────────────────────────────┤
│     Services (Business Logic)   │ ← Logique métier + Sécurité
├─────────────────────────────────┤
│     Repositories (Data Access)  │ ← Accès données
├─────────────────────────────────┤
│     PostgreSQL Database          │ ← Persistance
└─────────────────────────────────┘
```

---

## ⚙️ Configuration & Démarrage

### Variables d'environnement requises

```bash
# Base de données
DB_HOST=localhost
DB_PORT=5432
DB_NAME=healthai
DB_USER=healthai_user
DB_PASSWORD=your_secure_password

# API
API_PORT=8080
SPRING_PROFILE=dev  # ou 'prod'

# JWT
JWT_SECRET=your_very_long_secret_key_minimum_32_characters
```

### Démarrage local

```bash
# Compiler le projet
mvn clean install

# Démarrer l'application
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"

# Ou avec Docker
docker compose up db -d
docker compose --profile api up -d
```

### Accès à la documentation Swagger

Une fois l'API démarrée :
```
http://localhost:8080/api/swagger-ui.html
```

---

## 🔐 Authentification JWT

### Workflow d'authentification

1. **Inscription** : `POST /api/auth/register`
2. **Connexion** : `POST /api/auth/login` → Récupération du JWT
3. **Utilisation** : Header `Authorization: Bearer <token>` sur toutes les requêtes protégées

### Endpoints d'authentification

#### 📌 POST `/api/auth/register`

Créer un nouveau compte utilisateur.

**Request Body :**
```json
{
  "email": "user@example.com",
  "username": "johndoe",
  "password": "SecurePass123!",
  "age": 30,
  "gender": "M",
  "weightKg": 75.5,
  "heightCm": 180.0,
  "objective": "Perte de poids"
}
```

**Response (201 Created) :**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ1c2VyQGV4YW1wbGUuY29tIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3MDk4MTIzNDUsImV4cCI6MTcwOTg5ODc0NX0...",
  "userId": 1,
  "email": "user@example.com",
  "username": "johndoe",
  "role": "USER"
}
```

**Erreurs possibles :**
- `409 Conflict` : Email ou username déjà utilisé
- `422 Unprocessable Entity` : Validation des champs échouée

---

#### 📌 POST `/api/auth/login`

Connexion et obtention d'un token JWT.

**Request Body :**
```json
{
  "email": "user@example.com",
  "password": "SecurePass123!"
}
```

**Response (200 OK) :**
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "userId": 1,
  "email": "user@example.com",
  "username": "johndoe",
  "role": "USER"
}
```

**Erreurs possibles :**
- `401 Unauthorized` : Email ou mot de passe incorrect

---

## 📡 Endpoints API

Tous les endpoints (sauf `/auth/**`) nécessitent un token JWT valide dans le header :
```
Authorization: Bearer <votre_token>
```

### 👤 Utilisateurs

#### 📌 GET `/api/users/me`

Récupère le profil de l'utilisateur connecté.

**Response (200 OK) :**
```json
{
  "id": 1,
  "email": "user@example.com",
  "username": "johndoe",
  "role": "USER",
  "isPremium": false,
  "age": 30,
  "gender": "M",
  "weightKg": 75.5,
  "heightCm": 180.0,
  "objective": "Perte de poids",
  "createdAt": "2024-01-15T10:30:00",
  "lastActivity": "2024-01-20T14:20:00"
}
```

---

#### 📌 GET `/api/users/{userId}`

Récupère les informations d'un utilisateur par ID.

**Response (200 OK) :** Identique à `/users/me`

**Erreurs possibles :**
- `404 Not Found` : Utilisateur inexistant

---

#### 📌 PUT `/api/users/{userId}`

Met à jour le profil d'un utilisateur.

**Request Body :**
```json
{
  "age": 31,
  "weightKg": 73.2,
  "objective": "Maintien"
}
```

**Response (200 OK) :** Profil mis à jour

---

#### 📌 PUT `/api/users/me/activity`

Met à jour la dernière activité de l'utilisateur connecté.

**Response (200 OK) :** Profil avec `lastActivity` actualisé

---

### 💪 Exercices (Catalogue)

#### 📌 GET `/api/exercises`

Liste tous les exercices disponibles (paginé).

**Query Parameters :**
- `page` : Numéro de page (défaut: 0)
- `size` : Nombre d'éléments (défaut: 20)
- `sort` : Tri (ex: `name,asc`)

**Response (200 OK) :**
```json
{
  "content": [
    {
      "id": 1,
      "externalId": "ex001",
      "name": "Push-up",
      "bodyParts": ["chest", "arms"],
      "targetMuscles": ["pectorals", "triceps"],
      "secondaryMuscles": ["deltoids"],
      "equipments": ["body weight"],
      "instructions": "Instructions détaillées...",
      "gifUrl": "https://example.com/pushup.gif",
      "source": "EXERCISEDB",
      "createdAt": "2024-01-10T08:00:00"
    }
  ],
  "pageable": { ... },
  "totalPages": 50,
  "totalElements": 1000
}
```

---

#### 📌 GET `/api/exercises/{id}`

Détails d'un exercice par ID.

**Response (200 OK) :** Un objet `ExerciseDTO`

**Erreurs possibles :**
- `404 Not Found` : Exercice inexistant

---

### 🏋️ Séances d'entraînement (Workouts)

#### 📌 GET `/api/workouts/user/{userId}`

Liste toutes les séances d'entraînement d'un utilisateur.

**Sécurité :** L'utilisateur ne peut accéder qu'à ses propres données (sauf ADMIN).

**Response (200 OK) :**
```json
[
  {
    "id": 1,
    "userId": 1,
    "workoutType": "Running",
    "durationMin": 45,
    "caloriesBurned": 420,
    "steps": 6500,
    "heartRateAvg": 145,
    "heartRateMax": 172,
    "source": "APPLE_WATCH",
    "status": "BRUT",
    "createdAt": "2024-01-20T07:30:00"
  }
]
```

**Erreurs possibles :**
- `403 Forbidden` : Accès non autorisé aux données d'un autre utilisateur

---

#### 📌 GET `/api/workouts/{id}`

Détails d'une séance d'entraînement par ID.

**Response (200 OK) :** Un objet `ExerciseEntryDTO`

**Erreurs possibles :**
- `404 Not Found` : Séance inexistante
- `403 Forbidden` : Séance appartenant à un autre utilisateur

---

#### 📌 POST `/api/workouts`

Créer une nouvelle séance d'entraînement.

**Request Body :**
```json
{
  "userId": 1,
  "workoutType": "Cycling",
  "durationMin": 60,
  "caloriesBurned": 520,
  "steps": 0,
  "heartRateAvg": 135,
  "heartRateMax": 165,
  "source": "MANUAL",
  "status": "BRUT"
}
```

**Response (201 Created) :** Séance créée

**Erreurs possibles :**
- `403 Forbidden` : Tentative de création pour un autre utilisateur
- `422 Unprocessable Entity` : Validation échouée

---

#### 📌 PUT `/api/workouts/{id}`

Met à jour une séance d'entraînement.

**Request Body :** (champs partiels acceptés)
```json
{
  "caloriesBurned": 550,
  "status": "VALIDATED"
}
```

**Response (200 OK) :** Séance mise à jour

---

#### 📌 DELETE `/api/workouts/{id}`

Supprime une séance d'entraînement.

**Response (204 No Content)**

---

### 🍽️ Nutrition

#### 📌 GET `/api/nutrition/user/{userId}`

Liste toutes les entrées nutritionnelles d'un utilisateur.

**Response (200 OK) :**
```json
[
  {
    "id": 1,
    "userId": 1,
    "foodName": "Poulet grillé",
    "category": "Protéines",
    "mealType": "lunch",
    "calories": 250,
    "cholesterolMg": 85,
    "proteinG": 30,
    "carbsG": 0,
    "fatG": 12,
    "fiberG": 0,
    "sugarsG": 0,
    "sodiumMg": 420,
    "waterMl": 100,
    "source": "MANUAL",
    "status": "BRUT",
    "createdAt": "2024-01-20T12:30:00"
  }
]
```

---

#### 📌 GET `/api/nutrition/{id}`

Détails d'une entrée nutritionnelle par ID.

---

#### 📌 POST `/api/nutrition`

Créer une nouvelle entrée nutritionnelle.

**Request Body :**
```json
{
  "userId": 1,
  "foodName": "Salade César",
  "category": "Repas complet",
  "mealType": "dinner",
  "calories": 350,
  "proteinG": 15,
  "carbsG": 25,
  "fatG": 20,
  "fiberG": 5,
  "source": "MANUAL"
}
```

**Response (201 Created)**

---

#### 📌 PUT `/api/nutrition/{id}`

Met à jour une entrée nutritionnelle.

---

#### 📌 DELETE `/api/nutrition/{id}`

Supprime une entrée nutritionnelle.

**Response (204 No Content)**

---

### 📊 Biométrie

#### 📌 GET `/api/biometrics/user/{userId}`

Liste toutes les mesures biométriques d'un utilisateur.

**Response (200 OK) :**
```json
[
  {
    "id": 1,
    "userId": 1,
    "weightKg": 75.5,
    "heightCm": 180,
    "bmi": 23.3,
    "fatPercentage": 18.5,
    "heartRateRest": 62,
    "heartRateAvg": 75,
    "heartRateMax": 185,
    "bloodPressure": "120/80",
    "source": "WITHINGS_SCALE",
    "status": "BRUT",
    "createdAt": "2024-01-20T08:00:00"
  }
]
```

---

#### 📌 GET `/api/biometrics/{id}`

Détails d'une mesure biométrique par ID.

---

#### 📌 POST `/api/biometrics`

Créer une nouvelle mesure biométrique.

**Request Body :**
```json
{
  "userId": 1,
  "weightKg": 74.2,
  "heightCm": 180,
  "bmi": 22.9,
  "fatPercentage": 17.8,
  "heartRateRest": 60,
  "bloodPressure": "118/78",
  "source": "MANUAL"
}
```

**Response (201 Created)**

---

#### 📌 PUT `/api/biometrics/{id}`

Met à jour une mesure biométrique.

---

#### 📌 DELETE `/api/biometrics/{id}`

Supprime une mesure biométrique.

**Response (204 No Content)**

---

## 📦 DTOs (Data Transfer Objects)

### AuthResponse
```java
{
  "token": String,        // JWT token
  "userId": Long,         // ID utilisateur
  "email": String,        // Email
  "username": String,     // Nom d'utilisateur
  "role": String          // Rôle (USER, ADMIN)
}
```

### UserDTO
```java
{
  "id": Long,
  "email": String,
  "username": String,
  "role": String,
  "isPremium": Boolean,
  "age": Integer,
  "gender": String,
  "weightKg": Double,
  "heightCm": Double,
  "objective": String,
  "createdAt": LocalDateTime,
  "lastActivity": LocalDateTime
}
```

### ExerciseDTO
```java
{
  "id": Long,
  "externalId": String,
  "name": String,
  "bodyParts": List<String>,
  "targetMuscles": List<String>,
  "secondaryMuscles": List<String>,
  "equipments": List<String>,
  "instructions": String,
  "gifUrl": String,
  "source": String,
  "createdAt": LocalDateTime
}
```

### ExerciseEntryDTO (Workout)
```java
{
  "id": Long,
  "userId": Long,                    // Requis
  "workoutType": String,
  "durationMin": BigDecimal,         // Validation: Positive
  "caloriesBurned": BigDecimal,      // Validation: Positive
  "steps": Integer,
  "heartRateAvg": Integer,
  "heartRateMax": Integer,
  "source": String,                  // Requis (ex: "APPLE_WATCH", "MANUAL")
  "status": String,                  // Défaut: "BRUT"
  "createdAt": LocalDateTime
}
```

### NutritionEntryDTO
```java
{
  "id": Long,
  "userId": Long,                    // Requis
  "foodName": String,                // Requis
  "category": String,
  "mealType": String,                // ex: "breakfast", "lunch", "dinner", "snack"
  "calories": BigDecimal,            // Validation: Positive
  "cholesterolMg": BigDecimal,
  "proteinG": BigDecimal,            // Validation: Positive
  "carbsG": BigDecimal,              // Validation: Positive
  "fatG": BigDecimal,                // Validation: Positive
  "fiberG": BigDecimal,
  "sugarsG": BigDecimal,
  "sodiumMg": BigDecimal,
  "waterMl": BigDecimal,
  "source": String,                  // Requis
  "status": String,                  // Défaut: "BRUT"
  "createdAt": LocalDateTime
}
```

### BiometricEntryDTO
```java
{
  "id": Long,
  "userId": Long,                    // Requis
  "weightKg": BigDecimal,            // Validation: Positive
  "heightCm": BigDecimal,            // Validation: Positive
  "bmi": BigDecimal,
  "fatPercentage": BigDecimal,
  "heartRateRest": Integer,
  "heartRateAvg": Integer,
  "heartRateMax": Integer,
  "bloodPressure": String,
  "source": String,                  // Requis
  "status": String,                  // Défaut: "BRUT"
  "createdAt": LocalDateTime
}
```

---

## ⚠️ Exceptions & Gestion des erreurs

### Exceptions personnalisées

| Exception | Code HTTP | Description |
|-----------|-----------|-------------|
| `ResourceNotFoundException` | 404 | Ressource non trouvée (User, Exercise, Entry...) |
| `UnauthorizedAccessException` | 403 | Accès refusé (tentative d'accès aux données d'un autre user) |
| `EmailAlreadyExistsException` | 409 | Email déjà enregistré |
| `UsernameAlreadyExistsException` | 409 | Username déjà pris |
| `InvalidCredentialsException` | 401 | Email ou mot de passe incorrect |

### Format de réponse d'erreur

```json
{
  "status": "NOT_FOUND",
  "message": "Workout entry with id 999 not found",
  "error": "Resource not found",
  "timestamp": "2024-01-20T15:30:00",
  "path": "/api/workouts/999"
}
```

### Codes HTTP utilisés

- `200 OK` : Requête réussie
- `201 Created` : Ressource créée
- `204 No Content` : Suppression réussie
- `400 Bad Request` : Requête invalide
- `401 Unauthorized` : Authentification requise/échec
- `403 Forbidden` : Accès refusé
- `404 Not Found` : Ressource inexistante
- `409 Conflict` : Conflit (email/username déjà utilisé)
- `422 Unprocessable Entity` : Validation échouée
- `500 Internal Server Error` : Erreur serveur

---

## 🗄️ Base de données

### Architecture centralisée

**Important** : Les migrations de base de données sont centralisées dans le dépôt `MSPR-HealthAI-Coach-BDD`.

L'API HealthAI Coach :
- Se connecte à une base PostgreSQL déjà initialisée
- Utilise `spring.jpa.hibernate.ddl-auto=validate` (validation du schéma)
- **Ne gère PAS** les migrations (Flyway désactivé)

### Démarrage de la base de données

**Option 1 - Avec Docker (recommandé)** :
```bash
cd MSPR-HealthAI-Coach-BDD
docker compose up -d
```

**Option 2 - PostgreSQL local** :
Exécutez manuellement les migrations depuis `MSPR-HealthAI-Coach-BDD/migrations/` :
```bash
psql -U healthai_user -d healthai -f V1__init_schema.sql
psql -U healthai_user -d healthai -f V2__diet_recommendations.sql
psql -U healthai_user -d healthai -f V3__add_unique_constraints.sql
```

### Schéma PostgreSQL

#### Table `users`
```sql
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER',
    is_premium BOOLEAN NOT NULL DEFAULT FALSE,
    age INTEGER,
    gender VARCHAR(10),
    weight_kg NUMERIC(5, 2),
    height_cm NUMERIC(5, 2),
    objective VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    last_activity TIMESTAMP
);
```

#### Table `exercises`
```sql
CREATE TABLE exercises (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255) NOT NULL,
    body_parts TEXT[],
    target_muscles TEXT[],
    secondary_muscles TEXT[],
    equipments TEXT[],
    instructions TEXT,
    gif_url VARCHAR(500),
    source VARCHAR(50) NOT NULL DEFAULT 'EXERCISEDB',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

#### Table `exercise_entries`
```sql
CREATE TABLE exercise_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    workout_type VARCHAR(100),
    duration_min NUMERIC(10, 2),
    calories_burned NUMERIC(10, 2),
    steps INTEGER,
    heart_rate_avg INTEGER,
    heart_rate_max INTEGER,
    source VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'BRUT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_exercise_entries_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

#### Table `nutrition_entries`
```sql
CREATE TABLE nutrition_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    food_name VARCHAR(255) NOT NULL,
    category VARCHAR(100),
    meal_type VARCHAR(50),
    calories NUMERIC(10, 2),
    cholesterol_mg NUMERIC(10, 2),
    protein_g NUMERIC(10, 2),
    carbs_g NUMERIC(10, 2),
    fat_g NUMERIC(10, 2),
    fiber_g NUMERIC(10, 2),
    sugars_g NUMERIC(10, 2),
    sodium_mg NUMERIC(10, 2),
    water_ml NUMERIC(10, 2),
    source VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'BRUT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_nutrition_entries_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

#### Table `biometric_entries`
```sql
CREATE TABLE biometric_entries (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    weight_kg NUMERIC(5, 2),
    height_cm NUMERIC(5, 2),
    bmi NUMERIC(5, 2),
    fat_percentage NUMERIC(5, 2),
    heart_rate_rest INTEGER,
    heart_rate_avg INTEGER,
    heart_rate_max INTEGER,
    blood_pressure VARCHAR(20),
    source VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL DEFAULT 'BRUT',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_biometric_entries_user FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
```

### Migrations

**Les migrations SQL sont gérées par le repo `MSPR-HealthAI-Coach-BDD`.**

Localisation : `MSPR-HealthAI-Coach-BDD/migrations/`

| Fichier | Contenu |
|---------|---------|
| `V1__init_schema.sql` | Schema principal : users, exercises, nutrition_entries, exercise_entries, biometric_entries, etl_logs |
| `V2__diet_recommendations.sql` | Table diet_recommendations |
| `V3__add_unique_constraints.sql` | Contraintes d'unicité pour les ON CONFLICT |

Les migrations sont exécutées automatiquement au premier démarrage de la base via `docker-entrypoint-initdb.d`.

**Note** : L'API n'utilise **pas** Flyway. La configuration `spring.flyway.enabled=false` est définie.

---

## 🔒 Sécurité

### Contrôles d'accès

1. **Authentification JWT** : Tous les endpoints (sauf `/auth/**`) nécessitent un token valide
2. **Isolation des données** : Chaque utilisateur ne peut accéder qu'à ses propres données
3. **Rôle ADMIN** : Peut accéder à toutes les données (implémenté dans les services)

### Validation

- **DTO Validation** : Utilisation de `@Valid` et annotations Jakarta Validation
- **Business Rules** : Validation dans les services (ex: userId cohérent avec l'utilisateur connecté)

### Mot de passe

- **Hash** : BCrypt avec salt automatique
- **Jamais retourné** : Le mot de passe hashé n'est jamais exposé dans les DTOs

---

## 💡 Exemples d'utilisation

### Workflow complet

```bash
# 1. Inscription
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "john@example.com",
    "username": "johndoe",
    "password": "SecurePass123!",
    "age": 30,
    "gender": "M",
    "weightKg": 75.5,
    "heightCm": 180.0
  }'

# Réponse : { "token": "eyJhbG...", "userId": 1, ... }

# 2. Sauvegarder le token
export TOKEN="eyJhbGciOiJIUzUxMiJ9..."

# 3. Créer une séance d'entraînement
curl -X POST http://localhost:8080/api/workouts \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "workoutType": "Running",
    "durationMin": 45,
    "caloriesBurned": 420,
    "steps": 6500,
    "heartRateAvg": 145,
    "heartRateMax": 172,
    "source": "APPLE_WATCH"
  }'

# 4. Récupérer toutes ses séances
curl http://localhost:8080/api/workouts/user/1 \
  -H "Authorization: Bearer $TOKEN"

# 5. Ajouter une entrée nutritionnelle
curl -X POST http://localhost:8080/api/nutrition \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "foodName": "Poulet grillé",
    "category": "Protéines",
    "mealType": "lunch",
    "calories": 250,
    "proteinG": 30,
    "carbsG": 0,
    "fatG": 12,
    "source": "MANUAL"
  }'

# 6. Enregistrer des données biométriques
curl -X POST http://localhost:8080/api/biometrics \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "userId": 1,
    "weightKg": 74.5,
    "heightCm": 180,
    "bmi": 23.0,
    "fatPercentage": 18.2,
    "heartRateRest": 62,
    "bloodPressure": "120/80",
    "source": "MANUAL"
  }'
```

---

## 📚 Points importants

### Statut des données

Tous les entries (workout, nutrition, biometric) ont un champ `status` :
- **BRUT** : Données brutes importées (valeur par défaut)
- **VALIDATED** : Données validées par l'utilisateur
- **PROCESSED** : Données traitées par l'ETL/IA

### Sources de données

Le champ `source` indique la provenance :
- **MANUAL** : Saisie manuelle
- **APPLE_WATCH** : Importé depuis Apple Watch
- **GOOGLE_FIT** : Importé depuis Google Fit
- **WITHINGS_SCALE** : Balance connectée Withings
- **EXERCISEDB** : Base de données d'exercices externe

### Bonnes pratiques

1. **Toujours valider** les DTOs avec `@Valid`
2. **Vérifier userId** : Les services vérifient automatiquement que l'utilisateur connecté accède à ses propres données
3. **Gérer les erreurs** : Toutes les exceptions custom sont automatiquement interceptées et formatées
4. **Pagination** : Utiliser `Pageable` pour les grandes listes (exercices)

---

## 🚀 Prochaines évolutions

- [ ] Refresh tokens JWT
- [ ] Authentification sociale (Google, Apple)
- [ ] Rate limiting par utilisateur
- [ ] Cache Redis
- [ ] WebSockets pour données temps réel
- [ ] Notifications push
- [ ] Recommandations IA personnalisées

---

## 📞 Support

Pour toute question ou problème :
- **Email** : support@healthai-coach.com
- **Documentation Swagger** : http://localhost:8080/api/swagger-ui.html

---

**Version** : 1.0.0  
**Dernière mise à jour** : 2024-01-20  
**Mainteneurs** : EPSI Team
