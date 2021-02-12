package domain;

/**
 * This ADT represents a category associated to movements and transactions.
 */
public class Tag {
    private String name;
    private String description;

    /**
     * Creates a new tag with the given name and description.<br>
     * Raises NullPointerException if name or description are null.<br>
     * Raises IllegalArgumentException if name or description represent the empty string<br>
     *
     * @param name The name given to the new tag
     * @param description The description given to the new tag
     */
    public Tag(String name, String description) {
        if(name == null || description == null) {
            throw new NullPointerException("Name or description are null");
        }
        if(name.equals("") || description.equals("")){
            throw new IllegalArgumentException("Name or description must not be empty");
        }

        this.name = name;
        this.description = description;
    }

    /**
     * @return The name of this tag
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of this tag
     */
    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return  getName().equals(tag.getName()) &&
                getDescription().equals(tag.getDescription());
    }
}
