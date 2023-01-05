package proptoyml;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author Shubhasish
 */
// Hold the properties data
final class Prop {

    private int hierarchy;
    private String name;
    private String value;
    private Prop parent;
    private final Set<Prop> children = new HashSet<>();

    public void setChild(Prop children) {
        this.children.add(children);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setHierarchy(int hierarchy) {
        this.hierarchy = hierarchy;
    }

    public Prop getParent() {
        return parent;
    }

    public void setParent(Prop parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append("  ".repeat(hierarchy));
        if (value != null)
            return builder + name + ": " + value + "\n";
        else {
            return builder + name + ":\n" + children.toString().replace("[", " ").replace("]", "").replace(",", "");
        }
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Prop prop)) return false;
        return getName().equals(prop.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
