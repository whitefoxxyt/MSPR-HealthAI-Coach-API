package fr.epsi.healthaicoachapi.dto;

import java.util.List;
import java.util.Map;

public class AnalyticsOverviewDTO {

    private List<DistributionItem> ageDistribution;
    private List<DistributionItem> objectiveDistribution;
    private Map<String, Double> progressionRateByPeriod;
    private Map<String, List<TrendPoint>> userProgressionTrend;
    private Map<String, List<TrendPoint>> foodTrends;
    private List<NutritionBalance> nutritionBalanceByProfile;
    private List<DistributionItem> topExercises;
    private List<DistributionItem> intensityLevels;
    private BusinessKpis businessKpis;

    public AnalyticsOverviewDTO() {}

    public static class DistributionItem {
        private String label;
        private double value;
        public DistributionItem() {}
        public DistributionItem(String label, double value) { this.label = label; this.value = value; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
    }

    public static class TrendPoint {
        private String label;
        private double value;
        public TrendPoint() {}
        public TrendPoint(String label, double value) { this.label = label; this.value = value; }
        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }
        public double getValue() { return value; }
        public void setValue(double value) { this.value = value; }
    }

    public static class NutritionBalance {
        private String profile;
        private double deficit;
        private double excess;
        public NutritionBalance() {}
        public NutritionBalance(String profile, double deficit, double excess) {
            this.profile = profile; this.deficit = deficit; this.excess = excess;
        }
        public String getProfile() { return profile; }
        public void setProfile(String profile) { this.profile = profile; }
        public double getDeficit() { return deficit; }
        public void setDeficit(double deficit) { this.deficit = deficit; }
        public double getExcess() { return excess; }
        public void setExcess(double excess) { this.excess = excess; }
    }

    public static class BusinessKpis {
        private double engagementRate;
        private double premiumConversionRate;
        private double satisfactionRate;
        public BusinessKpis() {}
        public BusinessKpis(double engagementRate, double premiumConversionRate, double satisfactionRate) {
            this.engagementRate = engagementRate;
            this.premiumConversionRate = premiumConversionRate;
            this.satisfactionRate = satisfactionRate;
        }
        public double getEngagementRate() { return engagementRate; }
        public void setEngagementRate(double engagementRate) { this.engagementRate = engagementRate; }
        public double getPremiumConversionRate() { return premiumConversionRate; }
        public void setPremiumConversionRate(double premiumConversionRate) { this.premiumConversionRate = premiumConversionRate; }
        public double getSatisfactionRate() { return satisfactionRate; }
        public void setSatisfactionRate(double satisfactionRate) { this.satisfactionRate = satisfactionRate; }
    }

    public List<DistributionItem> getAgeDistribution() { return ageDistribution; }
    public void setAgeDistribution(List<DistributionItem> ageDistribution) { this.ageDistribution = ageDistribution; }
    public List<DistributionItem> getObjectiveDistribution() { return objectiveDistribution; }
    public void setObjectiveDistribution(List<DistributionItem> objectiveDistribution) { this.objectiveDistribution = objectiveDistribution; }
    public Map<String, Double> getProgressionRateByPeriod() { return progressionRateByPeriod; }
    public void setProgressionRateByPeriod(Map<String, Double> progressionRateByPeriod) { this.progressionRateByPeriod = progressionRateByPeriod; }
    public Map<String, List<TrendPoint>> getUserProgressionTrend() { return userProgressionTrend; }
    public void setUserProgressionTrend(Map<String, List<TrendPoint>> userProgressionTrend) { this.userProgressionTrend = userProgressionTrend; }
    public Map<String, List<TrendPoint>> getFoodTrends() { return foodTrends; }
    public void setFoodTrends(Map<String, List<TrendPoint>> foodTrends) { this.foodTrends = foodTrends; }
    public List<NutritionBalance> getNutritionBalanceByProfile() { return nutritionBalanceByProfile; }
    public void setNutritionBalanceByProfile(List<NutritionBalance> nutritionBalanceByProfile) { this.nutritionBalanceByProfile = nutritionBalanceByProfile; }
    public List<DistributionItem> getTopExercises() { return topExercises; }
    public void setTopExercises(List<DistributionItem> topExercises) { this.topExercises = topExercises; }
    public List<DistributionItem> getIntensityLevels() { return intensityLevels; }
    public void setIntensityLevels(List<DistributionItem> intensityLevels) { this.intensityLevels = intensityLevels; }
    public BusinessKpis getBusinessKpis() { return businessKpis; }
    public void setBusinessKpis(BusinessKpis businessKpis) { this.businessKpis = businessKpis; }
}
