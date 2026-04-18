#!/bin/bash

# Script de configuration des Quality Gates SonarQube pour HealthAI Coach API
# Prérequis: SonarQube doit être démarré et accessible sur localhost:9000

set -e

SONAR_HOST="http://localhost:9000"
SONAR_USER="admin"
SONAR_PASSWORD="admin"  # Mot de passe par défaut (à changer après première connexion)

echo "🔧 Configuration des Quality Gates SonarQube..."
echo ""

# Fonction pour faire des appels API SonarQube
sonar_api() {
    curl -s -u "${SONAR_USER}:${SONAR_PASSWORD}" "$@"
}

# Attendre que SonarQube soit prêt
echo "⏳ Attente du démarrage de SonarQube..."
until $(curl --output /dev/null --silent --head --fail "${SONAR_HOST}/api/system/status"); do
    printf '.'
    sleep 5
done
echo ""
echo "✅ SonarQube est prêt!"
echo ""

# Créer un Quality Gate personnalisé
echo "📊 Création du Quality Gate 'HealthAI Standards'..."

QUALITY_GATE_NAME="HealthAI Standards"

# Supprimer le Quality Gate s'il existe déjà
EXISTING_QG=$(sonar_api "${SONAR_HOST}/api/qualitygates/list" | grep -o "\"name\":\"${QUALITY_GATE_NAME}\"" || true)
if [ ! -z "$EXISTING_QG" ]; then
    echo "⚠️  Quality Gate existant trouvé, suppression..."
    QG_ID=$(sonar_api "${SONAR_HOST}/api/qualitygates/list" | jq -r ".qualitygates[] | select(.name==\"${QUALITY_GATE_NAME}\") | .id")
    sonar_api -X POST "${SONAR_HOST}/api/qualitygates/destroy?id=${QG_ID}"
fi

# Créer le nouveau Quality Gate
QG_RESPONSE=$(sonar_api -X POST "${SONAR_HOST}/api/qualitygates/create?name=${QUALITY_GATE_NAME}")
QG_ID=$(echo $QG_RESPONSE | jq -r '.id')

echo "✅ Quality Gate créé avec l'ID: ${QG_ID}"
echo ""

# Ajouter les conditions du Quality Gate
echo "📋 Configuration des conditions..."

# 1. Code Coverage >= 80%
echo "  ➤ Code Coverage >= 80%"
sonar_api -X POST "${SONAR_HOST}/api/qualitygates/create_condition" \
    -d "gateId=${QG_ID}" \
    -d "metric=coverage" \
    -d "op=LT" \
    -d "error=80" > /dev/null

# 2. Bugs critiques = 0
echo "  ➤ Bugs critiques = 0"
sonar_api -X POST "${SONAR_HOST}/api/qualitygates/create_condition" \
    -d "gateId=${QG_ID}" \
    -d "metric=critical_violations" \
    -d "op=GT" \
    -d "error=0" > /dev/null

# 3. Duplication <= 3%
echo "  ➤ Duplication <= 3%"
sonar_api -X POST "${SONAR_HOST}/api/qualitygates/create_condition" \
    -d "gateId=${QG_ID}" \
    -d "metric=duplicated_lines_density" \
    -d "op=GT" \
    -d "error=3" > /dev/null

# 4. Blocker Issues = 0
echo "  ➤ Blocker Issues = 0"
sonar_api -X POST "${SONAR_HOST}/api/qualitygates/create_condition" \
    -d "gateId=${QG_ID}" \
    -d "metric=blocker_violations" \
    -d "op=GT" \
    -d "error=0" > /dev/null

# 5. Security Hotspots reviewed = 100%
echo "  ➤ Security Hotspots reviewed = 100%"
sonar_api -X POST "${SONAR_HOST}/api/qualitygates/create_condition" \
    -d "gateId=${QG_ID}" \
    -d "metric=security_hotspots_reviewed" \
    -d "op=LT" \
    -d "error=100" > /dev/null

# 6. Maintainability Rating = A
echo "  ➤ Maintainability Rating = A"
sonar_api -X POST "${SONAR_HOST}/api/qualitygates/create_condition" \
    -d "gateId=${QG_ID}" \
    -d "metric=sqale_rating" \
    -d "op=GT" \
    -d "error=1" > /dev/null

echo ""
echo "✅ Conditions configurées avec succès!"
echo ""

# Définir comme Quality Gate par défaut
echo "🎯 Définition comme Quality Gate par défaut..."
sonar_api -X POST "${SONAR_HOST}/api/qualitygates/set_as_default?id=${QG_ID}" > /dev/null

echo "✅ Quality Gate 'HealthAI Standards' défini par défaut!"
echo ""

# Créer le projet si nécessaire
echo "📦 Création du projet 'healthai-coach-api'..."
PROJECT_EXISTS=$(sonar_api "${SONAR_HOST}/api/projects/search?projects=healthai-coach-api" | jq -r '.components | length')

if [ "$PROJECT_EXISTS" -eq "0" ]; then
    sonar_api -X POST "${SONAR_HOST}/api/projects/create" \
        -d "project=healthai-coach-api" \
        -d "name=HealthAI Coach API" > /dev/null
    echo "✅ Projet créé!"
else
    echo "ℹ️  Projet déjà existant"
fi

echo ""
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║       Quality Gates configurés avec succès! 🎉            ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""
echo "Critères appliqués:"
echo "  ✓ Code Coverage: >= 80%"
echo "  ✓ Bugs critiques: 0"
echo "  ✓ Duplication: <= 3%"
echo "  ✓ Blocker Issues: 0"
echo "  ✓ Security Hotspots reviewed: 100%"
echo "  ✓ Maintainability Rating: A"
echo ""
echo "Accédez à SonarQube: ${SONAR_HOST}"
echo "Login: admin / admin (changez le mot de passe!)"
echo ""
