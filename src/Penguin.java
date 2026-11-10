public class Penguin extends Animal implements Swimmable {
    private double swimSpeed;

    public Penguin(String name, int age, String colour, double weight, double swimSpeed) {
        super(name, age, colour, weight);
        this.swimSpeed = swimSpeed;
    }

    public double getSwimSpeed() {
        return swimSpeed;
    }

    public void setSwimSpeed(double swimSpeed) {
        this.swimSpeed = swimSpeed;
    }

    @Override
    public String makeSound() {
        return String.format("Honk! I am %s, a %d year old penguin and I can swim at %.2f mph.", getName(), getAge(), swimSpeed);
    }

    @Override
    public void checkFins() {
        System.out.println(getName() + " is having fin flexibility checked.");
    }

    @Override
    public void inspectWaterComfort() {
        System.out.println(getName() + " is being observed in water for comfort.");
    }

    @Override
    protected String getAdditionalDetail() {
        return "swim speed(mph): " + swimSpeed;
    }
}
