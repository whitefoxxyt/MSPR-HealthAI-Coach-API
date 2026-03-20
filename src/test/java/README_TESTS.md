# Tests API HealthAI Coach

## Structure des Tests

Tous les tests sont des **tests unitaires** utilisant **Mockito** pour mocker les dépendances (repositories et services).

### Tests Disponibles

1. **AuthControllerTest** - Tests du contrôleur d'authentification
   - Enregistrement d'utilisateur
   - Connexion
   - Gestion des erreurs (email existant, credentials invalides)

2. **HealthControllerTest** - Tests du health check
   - Vérification du statut de l'API

3. **BiometricControllerTest** - Tests des données biométriques
   - CRUD complet sur les entrées biométriques
   - Mock du BiometricEntryRepository

4. **ExerciseControllerTest** - Tests du catalogue d'exercices
   - Récupération paginée d'exercices
   - Récupération d'un exercice par ID
   - Mock du ExerciseRepository

5. **NutritionControllerTest** - Tests du suivi nutritionnel
   - CRUD complet sur les entrées nutritionnelles
   - Mock du NutritionEntryRepository

6. **UserControllerTest** - Tests de gestion des utilisateurs
   - Récupération du profil utilisateur
   - Mise à jour du profil
   - Mise à jour de la dernière activité
   - Mock du UserService et SecurityContext

7. **WorkoutControllerTest** - Tests des séances d'exercice
   - CRUD complet sur les séances d'exercice
   - Mock du ExerciseEntryRepository

## Technologie

- **JUnit 5** - Framework de test
- **Mockito** - Framework de mocking
- **Spring Boot Test** - Utilitaires de test Spring

## Exécution des Tests

```bash
# Exécuter tous les tests
mvn test

# Exécuter un test spécifique
mvn test -Dtest=AuthControllerTest

# Exécuter les tests avec rapport de couverture
mvn test jacoco:report
```

## Points Importants

### Types de Données
- Les entités utilisent `BigDecimal` pour les valeurs numériques décimales
- Utiliser `BigDecimal.valueOf()` pour la conversion dans les tests

### Dates
- Les entités utilisent `createdAt` (LocalDateTime) géré automatiquement
- Pas de `recordedAt` ou `consumedAt` dans les entités

### Sécurité
- Les tests du UserController mockent le `SecurityContext` pour simuler un utilisateur authentifié
- Les autres contrôleurs ne testent pas directement la sécurité JWT (tests unitaires, pas d'intégration)

## Structure d'un Test Type

```java
@ExtendWith(MockitoExtension.class)
class ControllerTest {
    
    @Mock
    private Repository repository;  // ou Service
    
    @InjectMocks
    private Controller controller;
    
    @BeforeEach
    void setUp() {
        // Initialisation des données de test
    }
    
    @Test
    @DisplayName("Description du test")
    void testMethodName() {
        // Given - Configuration des mocks
        when(repository.method()).thenReturn(expectedValue);
        
        // When - Appel de la méthode à tester
        ResponseEntity<?> response = controller.method();
        
        // Then - Vérifications
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(repository, times(1)).method();
    }
}
```

## Prochaines Étapes

Pour compléter la couverture de tests, il faudrait ajouter :
1. Tests d'intégration avec base de données de test (H2 ou Testcontainers)
2. Tests de sécurité JWT
3. Tests de validation des DTOs
4. Tests des services (AuthService, UserService)
5. Tests des repositories
