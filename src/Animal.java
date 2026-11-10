public abstract class Animal {
    private String name;
    private int age;
    private String colour;
    private double weight;

    public Animal(String name, int age, String colour, double weight) {
        this.name = name;
        this.age = age;
        this.colour = colour;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public abstract String makeSound();

    public String getDetails() {
        return String.format("%s;%s;%d;%s;%.2f;%s", getClass().getSimpleName(), name, age, colour, weight, getAdditionalDetail());
    }

    protected abstract String getAdditionalDetail();

    @Override
    public String toString() {
        return String.format("%s: %s, age: %d, colour: %s, weight(kg): %.2f, %s", getClass().getSimpleName(), name, age, colour, weight, getAdditionalDetail());
    }
}
