public class LaptopSet extends Equipment {
    private int ramGb;
    private boolean hasDockingStation;

    public LaptopSet(String id, String name, double baseDailyPrice, int ramGb, boolean hasDockingStation) {
        super(id, name, baseDailyPrice);
        this.ramGb = ramGb;
        this.hasDockingStation = hasDockingStation;
    }

    @Override
    public double calculateDailyPrice() {
        double price = getBaseDailyPrice();
        if (hasDockingStation) price += 15;
        if (ramGb >= 32) price += 25;
        return price;
    }

    @Override
    public String getDetails() {
        return "LaptopSet | RAM: " + ramGb + " GB | Stacja dokująca: " + (hasDockingStation ? "tak" : "nie");
    }
}
