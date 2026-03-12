package com.dateplan.entity;

import java.util.List;

public record DatePlan(DatePlanRequest request, WeatherInfo weather, List<Restaurant> restaurants, String planText) {
}
