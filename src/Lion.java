public class Lion extends Animal {
    private String favoriteToy;

    public Lion(String name, int age, String colour, double weight, String favoriteToy) {
        super(name, age, colour, weight);
        this.favoriteToy = favoriteToy;
    }

    public String getFavoriteToy() {
        return favoriteToy;
    }

    public void setFavoriteToy(String favoriteToy) {
        this.favoriteToy = favoriteToy;
    }

    @Override
    public String makeSound() {
        return String.format("Roar! I am %s, a %d year old lion and my favourite toy is %s.", getName(), getAge(), favoriteToy);
    }

    @Override
    protected String getAdditionalDetail() {
        return "favourite toy: " + favoriteToy;
    }
}
