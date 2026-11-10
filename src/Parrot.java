public class Parrot extends Animal implements Flyable {
    private int vocabularySize;

    public Parrot(String name, int age, String colour, double weight, int vocabularySize) {
        super(name, age, colour, weight);
        this.vocabularySize = vocabularySize;
    }

    public int getVocabularySize() {
        return vocabularySize;
    }

    public void setVocabularySize(int vocabularySize) {
        this.vocabularySize = vocabularySize;
    }

    @Override
    public String makeSound() {
        return String.format("Squawk! I am %s, a %d year old parrot and I know %d words.", getName(), getAge(), vocabularySize);
    }

    @Override
    public void checkWings() {
        System.out.println(getName() + " is having wing health checked.");
    }

    @Override
    public void inspectFlightSuitability() {
        System.out.println(getName() + " is being inspected for flight suitability.");
    }

    @Override
    protected String getAdditionalDetail() {
        return "vocabulary size: " + vocabularySize;
    }
}
