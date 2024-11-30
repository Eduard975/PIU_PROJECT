package map;

import graphic.Shader;
import graphic.VertexArray;

public class Level {

    private VertexArray background;

    private Tile[][] tiles;

    public Level() {
        int rows = 18; // Corresponding to the -9.0 to 9.0 range
        int cols = 32; // Corresponding to the -16.0 to 16.0 range
        float tileSize = 1.0f; // Each tile is 1x1 units

        tiles = new Tile[rows][cols];

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                float tileX = -16.0f + x * tileSize;
                float tileY = -9.0f + y * tileSize;
                int type = (x + y) % 2;
                tiles[y][x] = new Tile(tileX, tileY, tileSize, type);
            }
        }
    }

    public void render() {
        Shader.BACKGROUND.enable();
        for (Tile[] row : tiles) {
            for (Tile tile : row) {
                tile.render();
            }
        }
        Shader.BACKGROUND.disable();
    }
}
