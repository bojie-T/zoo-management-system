import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Zoo {
    private static final String ZOO_NAME = "Ulster Zoo";
    private final List<Animal> animals = new ArrayList<>();

    // Adds a new animal to the zoo if the animal is not null and passes validation.
    public void addAnimal(Animal animal) {
        if (animal != null && isAnimalValid(animal)) {
            animals.add(animal);
            System.out.println(animal.getName() + " has been added.");
        }
    }

    // Removes an animal from the zoo by searching for its name.
    public void removeAnimal(String name) {
        Animal found = findByName(name);
        if (found != null) {
            animals.remove(found);
            System.out.println(name + " has been removed.");
        } else {
            System.out.println("No animal found with name " + name + ".");
        }
    }

    // Replaces an existing animal's details with updated information.
    public void modifyAnimal(String name, Animal updated) {
        Animal found = findByName(name);
        if (found != null && isAnimalValid(updated)) {
            animals.remove(found);
            animals.add(updated);
            System.out.println(name + " has been updated.");
        } else {
            System.out.println("Unable to update: animal not found or data invalid.");
        }
    }

    // Searches for an animal by name, ignoring uppercase and lowercase differences.
    public Animal findByName(String name) {
        return animals.stream()
                .filter(a -> a.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    // Finds all animals that match a given colour.
    public List<Animal> findByColour(String colour) {
        return animals.stream()
                .filter(a -> a.getColour().equalsIgnoreCase(colour))
                .collect(Collectors.toList());
    }

    // Performs daily care tasks for every animal in the zoo.
    public void performDailyCare() {
        System.out.println("Performing daily animal care for all animals...");

        for (Animal animal : animals) {
            // Each animal makes its own sound using polymorphism.
            System.out.println(animal.makeSound());

            // If the animal can fly, perform flying-related checks.
            if (animal instanceof Flyable flyable) {
                flyable.checkWings();
                flyable.inspectFlightSuitability();
            }

            // If the animal can swim, perform swimming-related checks.
            if (animal instanceof Swimmable swimmable) {
                swimmable.checkFins();
                swimmable.inspectWaterComfort();
            }
        }
    }

    // Prints a summary report showing zoo information and animal statistics.
    public void printZooReport() {
        System.out.println("Zoo: " + ZOO_NAME);
        System.out.println("Total animals: " + animals.size());

        // Count how many animals exist for each animal type.
        Map<String, Long> counts = animals.stream()
                .collect(Collectors.groupingBy(a -> a.getClass().getSimpleName(), Collectors.counting()));
        counts.forEach((type, count) -> System.out.println(type + ": " + count));

        // Finds the most common animal colour in the zoo.
        String dominantColour = animals.stream()
                .collect(Collectors.groupingBy(Animal::getColour, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
        System.out.println("Dominant colour: " + dominantColour);
    }

    public void printAllAnimals() {
        if (animals.isEmpty()) {
            System.out.println("No animals are currently registered.");
            return;
        }
        animals.forEach(System.out::println);
    }

    // Prints only animals that can fly.
    public void printFlyableAnimals() {
        List<Animal> flyableAnimals = animals.stream()
                .filter(animal -> animal instanceof Flyable)
                .toList();

        if (flyableAnimals.isEmpty()) {
            System.out.println("No flyable animals are currently registered.");
            return;
        }

        flyableAnimals.forEach(System.out::println);
    }

    // Prints only animals that can swim.
    public void printSwimmableAnimals() {
        List<Animal> swimmableAnimals = animals.stream()
                .filter(animal -> animal instanceof Swimmable)
                .toList();

        if (swimmableAnimals.isEmpty()) {
            System.out.println("No swimmable animals are currently registered.");
            return;
        }

        swimmableAnimals.forEach(System.out::println);
    }

    public void saveToDisk() {
        writeZooDetails();
        writeAnimalDetails();
    }

    // Loads saved animal information from the disk.
    public void loadFromDisk() {
        loadAnimalDetails();
    }

    // Writes zoo information to ZooDetails.txt.
    private void writeZooDetails() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("ZooDetails.txt"))) {
            writer.write("ZooName=" + ZOO_NAME);
            writer.newLine();
            writer.write("TotalAnimals=" + animals.size());
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Unable to write ZooDetails.txt: " + e.getMessage());
        }
    }

    // Writes each valid animal's details to AnimalDetails.txt.
    private void writeAnimalDetails() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("AnimalDetails.txt"))) {
            for (Animal animal : animals) {
                if (isAnimalValid(animal)) {
                    writer.write(animal.getDetails());
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to write AnimalDetails.txt: " + e.getMessage());
        }
    }

    // Reads animal records from AnimalDetails.txt and adds them back into the zoo.
    private void loadAnimalDetails() {
        File file = new File("AnimalDetails.txt");

        // If the save file does not exist, there is nothing to load.
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Read each saved animal record one line at a time.
            while ((line = reader.readLine()) != null) {
                try {
                    Animal animal = parseAnimalRecord(line);
                    if (animal != null) {
                        animals.add(animal);
                    }
                } catch (IllegalArgumentException e) {
                    System.err.println("Skipping invalid animal record: " + line);
                    System.err.println("Reason: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Unable to read AnimalDetails.txt: " + e.getMessage());
        }
    }

    // Converts a saved text record back into the correct Animal object.
    private Animal parseAnimalRecord(String record) {
        String[] parts = record.split(";");

        // A valid animal record must contain all required fields.
        if (parts.length < 6) {
            throw new IllegalArgumentException("Record does not contain enough fields.");
        }

        String type = parts[0].trim();
        String name = parts[1].trim();
        int age = Integer.parseInt(parts[2].trim());
        String colour = parts[3].trim();
        double weight = Double.parseDouble(parts[4].trim());
        String detail = parts[5].trim();
        String detailValue = extractDetailValue(detail);

        // Create the correct animal subclass depending on the saved type.
        if (type.equals("Lion")) {
            return new Lion(name, age, colour, weight, detailValue);
        }
        if (type.equals("Penguin")) {
            return new Penguin(name, age, colour, weight, Double.parseDouble(detailValue));
        }
        if (type.equals("Parrot")) {
            return new Parrot(name, age, colour, weight, Integer.parseInt(detailValue));
        }

        throw new IllegalArgumentException("Unknown animal type: " + type);
    }

    private String extractDetailValue(String detail) {
        int equalsIndex = detail.indexOf('=');
        int colonIndex = detail.indexOf(':');

        int separatorIndex;
        if (equalsIndex >= 0 && colonIndex >= 0) {
            separatorIndex = Math.min(equalsIndex, colonIndex);
        } else {
            separatorIndex = Math.max(equalsIndex, colonIndex);
        }

        if (separatorIndex < 0 || separatorIndex == detail.length() - 1) {
            throw new IllegalArgumentException("Animal-specific detail is missing a value.");
        }

        return detail.substring(separatorIndex + 1).trim();
    }

    private boolean isAnimalValid(Animal animal) {
        return animal.getName() != null && !animal.getName().isBlank()
                && animal.getAge() >= 0
                && animal.getColour() != null && animal.getColour().matches("[A-Za-z]+")
                && animal.getWeight() > 0;
    }

    public static void main(String[] args) {
        Zoo zoo = new Zoo();
        zoo.loadFromDisk();
        try (Scanner scanner = new Scanner(System.in)) {
            boolean running = true;
            while (running) {
                System.out.println("\nZoo Management System");
                System.out.println("1. Add animal");
                System.out.println("2. Remove animal");
                System.out.println("3. Modify animal");
                System.out.println("4. View all animals");
                System.out.println("5. Search by name");
                System.out.println("6. Search by colour");
                System.out.println("7. Perform daily care");
                System.out.println("8. Zoo report");
                System.out.println("9. Show flyable");
                System.out.println("10. Show swimmable");
                System.out.println("11. Exit and save");
                System.out.print("Select option: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> addAnimalInteractive(zoo, scanner);
                    case "2" -> removeAnimalInteractive(zoo, scanner);
                    case "3" -> modifyAnimalInteractive(zoo, scanner);
                    case "4" -> zoo.printAllAnimals();
                    case "5" -> searchByNameInteractive(zoo, scanner);
                    case "6" -> searchByColourInteractive(zoo, scanner);
                    case "7" -> zoo.performDailyCare();
                    case "8" -> zoo.printZooReport();
                    case "9" -> zoo.printFlyableAnimals();
                    case "10" -> zoo.printSwimmableAnimals();
                    case "11" -> {
                        zoo.saveToDisk();
                        running = false;
                        System.out.println("Zoo session ended. Details saved.");
                    }
                    default -> System.out.println("Invalid selection.");
                }
            }
        }
    }

    // Handles user input for adding a new animal.
    private static void addAnimalInteractive(Zoo zoo, Scanner scanner) {
        System.out.print("Animal type (Lion/Penguin/Parrot): ");
        String type = scanner.nextLine().trim();
        Animal animal = createAnimalFromInput(type, scanner);
        zoo.addAnimal(animal);
    }

    // Handles user input for removing an animal.
    private static void removeAnimalInteractive(Zoo zoo, Scanner scanner) {
        System.out.print("Enter the animal name to remove: ");
        String name = scanner.nextLine().trim();
        zoo.removeAnimal(name);
    }

    // Handles user input for modifying an existing animal.
    private static void modifyAnimalInteractive(Zoo zoo, Scanner scanner) {
        System.out.print("Enter the name of the animal to modify: ");
        String name = scanner.nextLine().trim();
        Animal existing = zoo.findByName(name);
        if (existing == null) {
            System.out.println("Animal not found.");
            return;
        }
        System.out.println("Enter new details.");
        // Keep the same animal type but allow the user to enter new values.
        Animal updated = createAnimalFromInput(existing.getClass().getSimpleName(), scanner);
        zoo.modifyAnimal(name, updated);
    }

    // Handles searching for an animal by name.
    private static void searchByNameInteractive(Zoo zoo, Scanner scanner) {
        System.out.print("Enter the animal name to search: ");
        String name = scanner.nextLine().trim();
        Animal found = zoo.findByName(name);
        if (found != null) {
            System.out.println(found);
            System.out.println(found.makeSound());
        } else {
            System.out.println("No matching animal found.");
        }
    }

    // Handles searching for animals by colour.
    private static void searchByColourInteractive(Zoo zoo, Scanner scanner) {
        System.out.print("Enter colour to search: ");
        String colour = scanner.nextLine().trim();
        List<Animal> matches = zoo.findByColour(colour);
        if (matches.isEmpty()) {
            System.out.println("No animals found with colour " + colour + ".");
        } else {
            matches.forEach(a -> {
                System.out.println(a);
                System.out.println(a.makeSound());
            });
        }
    }

    private static Animal createAnimalFromInput(String type, Scanner scanner) {
        String name = readRequiredString(scanner, "Name: ");
        int age = readInt(scanner, "Age: ");
        String colour = readRequiredWord(scanner, "Colour: ");
        double weight = readDouble(scanner, "Weight(kg): ");
        switch (type.toLowerCase()) {
            case "lion" -> {
                String favoriteToy = readRequiredString(scanner, "Favorite toy: ");
                return new Lion(name, age, colour, weight, favoriteToy);
            }
            case "penguin" -> {
                double speed = readDouble(scanner, "Swim speed(mph): ");
                return new Penguin(name, age, colour, weight, speed);
            }
            case "parrot" -> {
                int vocabularySize = readInt(scanner, "Vocabulary size: ");
                return new Parrot(name, age, colour, weight, vocabularySize);
            }
            default -> {
                System.out.println("Unknown animal type.");
                return null;
            }
        }
    }

    private static String readRequiredString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isBlank()) {
                return input;
            }
            System.out.println("Input cannot be blank. Please enter a valid value.");
        }
    }

    // Repeatedly asks the user for a word containing letters only.
    private static String readRequiredWord(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();

            if (input.matches("[A-Za-z]+")) {
                return input;
            }

            System.out.println("Colour must be a word using letters only. Please do not use numbers or symbols.");
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Age must be a non-negative integer.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    // Repeatedly asks the user for a number greater than zero.
    private static double readDouble(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                double value = Double.parseDouble(input);
                if (value <= 0) {
                    System.out.println("Value must be greater than zero.");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }
}
