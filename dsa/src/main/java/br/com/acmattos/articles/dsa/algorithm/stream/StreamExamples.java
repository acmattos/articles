package br.com.acmattos.articles.dsa.algorithm.stream;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class StreamExamples {

    public static void main(String[] args) {
        System.out.println("count                   : " + countActors());
        System.out.println("count + distinct        : " + countDistinctActors());
        System.out.println("max (AGE)               : " + getOldestActor());
        System.out.println("min (AGE)               : " + getYoungestActor());
        System.out.println("average (AGE)           : " + getAverageAgeOfAllActors());
        System.out.println("average (AGE) + distinct: " + getAverageAgeOfDistinctActors());
        System.out.println("filter (J) + forEach    : ");
        filterActorsByFirstNameStartingWith("J")
            .forEach(actor -> System.out.println("  - " + actor));
        System.out.println("sorted + limit (3)      : ");
        getActorsSortedByFirstNameAscAndLastNameAscLimitedBy(3).stream()
            .forEach(actor -> System.out.println("  - " +actor));
        System.out.println("sorted + skip (9)       : ");
        getActorsSortedByFirstNameAscAndLastNameAscSkipingFirst(9).stream()
            .forEach(actor -> System.out.println("  - " +actor));
        System.out.println("sorted (first+last Name): ");
        getActorsSortedByFirstNameAscAndLastNameAsc().stream()
            .forEach(actor -> System.out.println("  - " +actor));
        System.out.println("unsorted                : ");
        getDistinctActors().stream()
            .forEach(actor -> System.out.println("  - " +actor));

        System.out.println("filter (> 50)           : ");
        getActorsWithAgeGreaterThan(50).stream()
            .forEach(actor -> System.out.println("  - " +actor));
        System.out.println("groupBy (MOVIE)         : ");
        getActorsGroupedByMovie()
            .forEach((movie, actors) -> {
                if(actors.size() > 1){
                    System.out.println("  - " + movie + ":");
                    actors.stream()
                        .forEach(actor -> System.out.println("    - " + actor));
                }
            });
        System.out.println("groupBy(MOVIE) + count(ACTORS): ");
        countActorsGroupedByMovie()
            .forEach((movie, count) ->
                System.out.println("  - " +movie + ": " + count));
        System.out.println("map(MOVIE)                    : ");
        getMoviesFromActors()
            .forEach((movie) ->
                System.out.println("  - " +movie));
        System.out.println("reduce(first+last Name)       : " + getActorsFullNames());
    }

    private static Collection<Actor> getAllActors() {
        return Arrays.asList(
            new Actor("Penelope","Guiness"     , "F", "Angels Life",	 30),
            new Actor("Penelope","Guiness"     , "F", "Angels Life",	 30),
            new Actor("Marta"   ,"Guiness"     , "F", "Bulworth Commandments", 30),
            new Actor("Cuba"    , "Olivier"    , "M", "Angels Life",	 42),
            new Actor("Nick"    ,"Wahlberg"    , "M", "Dracula Crystal",50),
            new Actor("Jennifer","Davis"       , "F", "Angels Life",	28),
            new Actor("Ed"      ,"Chase"       , "M", "Young Language", 10),
            new Actor("Bette"   ,"Nicholson"   , "F", "Crossroads Casualties",87),
            new Actor("Johnny"  ,"Lollobrigida", "M", "Pacific Amistad", 35),
            new Actor("Matthew" ,"Johansson"   , "M", "Conquerer Nuts", 72),
            new Actor("Grace"   ,"Mostel"      , "F", "Angels Life", 63),
            new Actor("Uma"     , "Wood"       , "F", "Clash Freddy", 20),
            new Actor("Joe"     , "Swank"      , "M", "Waterfront Deliverance", 20),
            new Actor("Penelope", "Bergen"     , "F", "Hills Neighbors", 55));
    }

    private static Collection<Actor> getDistinctActors() {
        return getAllActors().stream()
            .distinct()
            .collect(Collectors.toList());
    }

    private static long countActors() {
        return getAllActors().stream()
            .count();
    }

    private static long countDistinctActors() {
        return getDistinctActors().stream()
            .count();
    }

    private static Optional<Actor> getOldestActor() {
        return getAllActors().stream()
            .max(Comparator.comparingInt(Actor::age));
    }

    private static Optional<Actor> getYoungestActor() {
        return getAllActors().stream()
            .min(Comparator.comparingInt(Actor::age));
    }

    private static double getAverageAgeOfAllActors() {
        return getAllActors().stream()
            .collect(Collectors.averagingInt(Actor::age));
    }

    private static double getAverageAgeOfDistinctActors() {
        return getAllActors().stream()
            .distinct()
            .collect(Collectors.averagingInt(Actor::age));
    }

    private static Collection<Actor> filterActorsByFirstNameStartingWith(
        String prefix) {
        return getAllActors().stream()
            .filter(actor -> actor.firstName().startsWith(prefix))
            .collect(Collectors.toList());
    }

    private static Collection<Actor> getActorsSortedByFirstNameAscAndLastNameAsc() {
        return getAllActors().stream()
            .sorted(Comparator.comparing(Actor::firstName)
                .thenComparing(Actor::lastName))
            .collect(Collectors.toList());
    }

    private static Collection<Actor> getActorsSortedByFirstNameAscAndLastNameAscLimitedBy(int maxSize) {
        return getActorsSortedByFirstNameAscAndLastNameAsc().stream()
            .limit(maxSize)
            .collect(Collectors.toList());
    }

    private static Collection<Actor> getActorsSortedByFirstNameAscAndLastNameAscSkipingFirst(int n) {
        return getActorsSortedByFirstNameAscAndLastNameAsc().stream()
            .skip(n)
            .collect(Collectors.toList());
    }

    private static Collection<Actor> getActorsWithAgeGreaterThan(int age){
        return getDistinctActors().stream()
            .filter(actor -> actor.age() > age)
            .collect(Collectors.toList());
    }

    private static Map<String, List<Actor>> getActorsGroupedByMovie(){
        return getAllActors().stream()
            .collect(Collectors.groupingBy(Actor::movie));
    }

    private static Map<String, Long> countActorsGroupedByMovie(){
        return getAllActors().stream()
            .collect(Collectors.groupingBy(Actor::movie, Collectors.counting()));
    }

    private static Collection<String> getMoviesFromActors(){
        return getDistinctActors().stream()
            .map(Actor::movie)
            .collect(Collectors.toList());
    }

    private static String getActorsFullNames() {
        return getActorsSortedByFirstNameAscAndLastNameAsc().stream()
            .distinct()
            .map(actor -> actor.firstName() + " " + actor.lastName())
            .reduce("", (result, name) -> result.isEmpty() ? name : result + ", " + name);
    }
}
