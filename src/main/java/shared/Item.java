package shared;

import lombok.ToString;

@ToString
public class Item {
    private String key;

    public Item(String key) {
        this.key = key;
    }

}
