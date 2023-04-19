package entite;

import java.util.Objects;

public class Food {
    private String name;
    private float quantity;
    private String unit;
    private String type;
    private String size;

    public Food(String name, float quantity, String unit, String type, String size) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.type = type;
        this.size = size;
    }
    
    public Food(String name, float quantity, String unit, String type) {
        this.name = name;
        this.quantity = quantity;
        this.unit = unit;
        this.type = type;
        this.size = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float newQuantity) {
        this.quantity = newQuantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return Float.compare(food.quantity, quantity) == 0 && Objects.equals(name, food.name) && Objects.equals(unit, food.unit) && Objects.equals(type, food.type) && Objects.equals(size, food.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, quantity, unit, type, size);
    }
}