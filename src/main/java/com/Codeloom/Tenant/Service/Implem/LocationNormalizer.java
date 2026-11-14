package com.Codeloom.Tenant.Service.Implem;

import com.Codeloom.Tenant.Enum.Location;
import com.Codeloom.Tenant.ExceptionHandler.InvalidLocationException;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


@Component
public class LocationNormalizer {

    private static final int MAX_DISTANCE = 2;
    private static final int MIN_SEARCH_LENGTH = 4;

    private final LevenshteinDistance levenshteinDistance =
            LevenshteinDistance.getDefaultInstance();

    //this method coverts the location enum to be searched as fuzzy and case-insensitive
    public Location normalize(String input) {

        if (input == null || input.isBlank()) {
            throw new InvalidLocationException(
                    "Location cannot be empty."
            );
        }

        String normalized = normalizeInput(input);

        // 1. Exact matching
        for (Location location : Location.values()) {

            if (location.name().equals(normalized)) {
                return location;
            }
        }

        // 2. Prevent unreliable matching of very short inputs
        if (normalized.replace("_", "").length() < MIN_SEARCH_LENGTH) {
            throw new InvalidLocationException(
                    "Location search must contain at least 4 characters."
            );
        }

        // 3. Calculate fuzzy matching scores
        List<LocationMatch> matches = Arrays.stream(Location.values())
                .map(location -> new LocationMatch(
                        location,
                        calculateDistance(
                                normalized,
                                location.name()
                        )
                ))
                .sorted(Comparator.comparingInt(LocationMatch::distance))
                .toList();

        // 4. Get the closest match
        LocationMatch bestMatch = matches.get(0);

        // Reject if no sufficiently close match exists
        if (bestMatch.distance() > MAX_DISTANCE) {
            throw new InvalidLocationException(
                    buildErrorMessage(input)
            );
        }

        // 5. Avoid ambiguous matches
        if (matches.size() > 1 &&
                matches.get(1).distance() == bestMatch.distance()) {

            throw new InvalidLocationException(
                    "Your search is ambiguous. Please specify the location. "
                            + "Possible matches: "
                            + matches.stream()
                            .filter(match ->
                                    match.distance() == bestMatch.distance())
                            .map(match -> match.location().name())
                            .toList()
            );
        }

        return bestMatch.location();
    }

    private String normalizeInput(String input) {

        return input.trim()
                .replaceAll("[\\s-]+", "_")
                .toUpperCase(Locale.ROOT);
    }

    private int calculateDistance(
            String input,
            String candidate
    ) {

        // Normal Levenshtein distance
        int fullDistance = levenshteinDistance.apply(
                input,
                candidate
        );

        // Also support incomplete location names
        int prefixDistance = Integer.MAX_VALUE;

        if (input.length() < candidate.length()) {

            String candidatePrefix =
                    candidate.substring(0, input.length());

            prefixDistance = levenshteinDistance.apply(
                    input,
                    candidatePrefix
            );
        }

        return Math.min(fullDistance, prefixDistance);
    }

    private String buildErrorMessage(String input) {

        return "No matching location found for '" + input
                + "'. Available locations: "
                + Arrays.toString(Location.values());
    }

    private record LocationMatch(
            Location location,
            int distance
    ) {}
}