package com.albaniaguessr.service.store;

import com.albaniaguessr.entity.Location;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryLocationStore {

    private final Map<Long, Location> locations = new ConcurrentHashMap<>();
    private long nextId = 1;

    @PostConstruct
    public void init() {
        seedLocations();
    }

    public Location save(Location location) {
        if (location.getId() == null) {
            location.setId(nextId++);
        }
        locations.put(location.getId(), location);
        return location;
    }

    public Optional<Location> findById(Long id) {
        return Optional.ofNullable(locations.get(id));
    }

    public List<Location> findAll() {
        return List.copyOf(locations.values());
    }

    public List<Location> findAllActive() {
        return locations.values().stream()
                .filter(Location::getActive)
                .collect(Collectors.toList());
    }

    public List<Location> findRandomActive(int count) {
        List<Location> active = findAllActive();
        if (active.size() <= count) {
            return new ArrayList<>(active);
        }
        List<Location> shuffled = new ArrayList<>(active);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }

    public void deleteById(Long id) {
        locations.remove(id);
    }

    public long count() {
        return locations.size();
    }

    public List<Location> findRandomActiveExcluding(int count, Set<Long> excludeIds) {
        List<Location> available = locations.values().stream()
                .filter(l -> l.getActive() && !excludeIds.contains(l.getId()))
                .collect(Collectors.toList());
        if (available.size() <= count) {
            return new ArrayList<>(available);
        }
        List<Location> shuffled = new ArrayList<>(available);
        Collections.shuffle(shuffled);
        return shuffled.subList(0, count);
    }

    private void seedLocations() {
        log.info("Seeding 18 Albanian locations...");

        save(createLocation("Kalaja e Beratit", "Berat Castle", Location.LocationCategory.HISTORIC,
                40.7058, 19.9522, 1960, 2,
                "One of the oldest continuously inhabited castles in the world, part of the UNESCO World Heritage site of Berat."));
        save(createLocation("Kalaja e Gjirokastrës", "Gjirokastër Castle", Location.LocationCategory.HISTORIC,
                40.0756, 20.1389, 1985, 2,
                "The largest castle in Albania, now housing the National Museum of Armaments."));
        save(createLocation("Sheshi Skënderbej", "Skanderbeg Square (Tirana)", Location.LocationCategory.LANDMARK,
                41.3275, 19.8187, 1990, 1,
                "The main plaza in Tirana, named after the national hero Gjergj Kastrioti Skënderbeu."));
        save(createLocation("Bunk'Art", "Bunk'Art (Tirana)", Location.LocationCategory.MODERN,
                41.3245, 19.8208, 2015, 3,
                "A massive Cold War bunker turned into a history museum and art gallery."));
        save(createLocation("Ura e Mesit", "Mesi Bridge (Shkodër)", Location.LocationCategory.HISTORIC,
                42.0683, 19.5097, 1970, 3,
                "An Ottoman-era stone bridge built in the 18th century near Shkodër."));
        save(createLocation("Butrint", "Butrint National Park", Location.LocationCategory.HISTORIC,
                39.7450, 20.0203, 1925, 1,
                "UNESCO World Heritage site, an ancient city with remains from Greek, Roman, Byzantine, and Venetian periods."));
        save(createLocation("Llogara Pass", "Llogara Pass", Location.LocationCategory.NATURE,
                40.1969, 19.5917, 2005, 4,
                "A high mountain pass in the Ceraunian Mountains with stunning views of the Ionian Sea."));
        save(createLocation("Theth", "Theth National Park", Location.LocationCategory.NATURE,
                42.3958, 19.7750, 1975, 4,
                "A remote valley in the Albanian Alps known for its stunning landscapes and traditional stone houses."));
        save(createLocation("Sarandë", "Sarandë Promenade", Location.LocationCategory.MODERN,
                39.8750, 20.0050, 2010, 1,
                "A coastal city in southern Albania, known for its beautiful beaches and the ruins of the ancient city of Butrint nearby."));
        save(createLocation("Korçë Cathedral", "Resurrection Cathedral (Korçë)", Location.LocationCategory.LANDMARK,
                40.6167, 20.7833, 2000, 2,
                "The largest Orthodox cathedral in Albania, built in the Byzantine style."));
        save(createLocation("Pogradec / Lake Ohrid", "Lake Ohrid (Pogradec)", Location.LocationCategory.NATURE,
                40.9000, 20.6542, 1980, 2,
                "One of the oldest lakes in the world, shared between Albania and North Macedonia, a UNESCO site."));
        save(createLocation("Kalaja e Krujës", "Krujë Castle", Location.LocationCategory.HISTORIC,
                41.5075, 19.7936, 1955, 2,
                "The castle where national hero Skanderbeg defended Albania against the Ottoman Empire."));
        save(createLocation("Apollonia", "Apollonia Archaeological Park", Location.LocationCategory.HISTORIC,
                40.7197, 19.4733, 1930, 3,
                "An ancient Greek colony founded in the 6th century BC, one of the most important archaeological sites in Albania."));
        save(createLocation("Durrës Amphitheatre", "Durrës Amphitheatre", Location.LocationCategory.HISTORIC,
                41.3111, 19.4450, 1965, 2,
                "The largest Roman amphitheatre in the Balkans, built in the 2nd century AD with a capacity of 15,000-20,000 spectators."));
        save(createLocation("Shkodër Castle (Rozafa)", "Rozafa Castle (Shkodër)", Location.LocationCategory.HISTORIC,
                42.0467, 19.4931, 1972, 2,
                "A historic castle overlooking Shkodër and three rivers, with a legendary tale of a woman walled into its foundations."));
        save(createLocation("Blue Eye (Syri i Kaltër)", "Blue Eye Spring", Location.LocationCategory.NATURE,
                39.9239, 20.1928, 2018, 3,
                "A natural spring with crystal-clear blue water, one of Albania's most beautiful natural attractions."));
        save(createLocation("Ksamil Islands", "Ksamil Islands", Location.LocationCategory.NATURE,
                39.7694, 19.9936, 2020, 1,
                "A small archipelago of four islands in the Ionian Sea, part of the Butrint National Park."));
        save(createLocation("Vlorë Harbor", "Vlorë Harbor", Location.LocationCategory.LANDMARK,
                40.4600, 19.4900, 1995, 1,
                "The site where Albania's independence was declared on November 28, 1912, and an important port city."));

        log.info("Seeded {} locations", locations.size());
    }

    private Location createLocation(String name, String displayName, Location.LocationCategory category,
                                     double lat, double lng, int year, int difficulty, String funFact) {
        Location loc = new Location();
        loc.setLocationName(displayName);
        loc.setImageUrl("/images/locations/" + name.toLowerCase().replaceAll("\\s+", "-").replaceAll("[()]", "") + ".jpg");
        loc.setCorrectLat(lat);
        loc.setCorrectLng(lng);
        loc.setCorrectYear(year);
        loc.setDifficulty(difficulty);
        loc.setCategory(category);
        loc.setFunFact(funFact);
        loc.setActive(true);
        return loc;
    }
}