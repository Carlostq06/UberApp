package com.example.uber_carlos.model

data class Ride(
    val firestoreId:  String = "",
    val userId:       String = "",
    val originLat:    Double = 0.0,
    val originLng:    Double = 0.0,
    val originName:   String = "",
    val destLat:      Double = 0.0,
    val destLng:      Double = 0.0,
    val destName:     String = "",
    val driverName:   String = "",
    val price:        Double = 0.0,
    val status:       String = "completed",
    val date:         String = "",
    val durationMins: Int    = 0,
    val distanceKm:   Double = 0.0
)
