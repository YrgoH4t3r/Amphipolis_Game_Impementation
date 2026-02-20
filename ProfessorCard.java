package Model;
import java.util.ArrayList;
import java.util.List;

/**
* will be used for the special areas
* */
public class AreaType {
    private List<Tile> tiles;
    private int x;
    private int y;

    public AreaType(int x, int y) {
        this.x = x;
        this.y = y;
        this.tiles = new ArrayList<>();
    }

    public void addTile(Tile tile) {
        tiles.add(tile);
    }

    public List<Tile> removeTiles(int count) {
        List<Tile> removed = new ArrayList<>();
        int toRemove = Math.min(count, tiles.size());
        for (int i = 0; i < toRemove; i++) {
            removed.add(tiles.remove(0));
        }
        return removed;
    }

    public List<Tile> getTiles() {
        return tiles;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public Tile removeTileByIndex(int index) {
        if (index >= 0 && index < tiles.size()) {
            return tiles.remove(index);
        }
        return null;
    }
}
