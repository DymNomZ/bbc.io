package classes.map;
import classes.Asset.Sprite.Sprite;

import java.awt.image.BufferedImage;

public class Tile {
    public Sprite image;
    public String name;
    public int index = 0;
    public boolean is_solid = false, is_animated = false;

    public Tile(String path, String name, int index, boolean is_solid, boolean is_animated){

        if(path.equals("void.png")){
            image = Sprite.load("default");
        }
        else image = Sprite.load("map_tiles/" + path);

        this.name = name;
        this.index = index;
        this.is_solid = is_solid;
        this.is_animated = is_animated;
    }

    //for loading
    public Tile(Sprite image, String name, int index, boolean is_solid, boolean is_animated){
        this.image = image;
        this.name = name;
        this.index = index;
        this.is_solid = is_solid;
        this.is_animated = is_animated;
    }

    public String getName(){
        return name;
    }

    public BufferedImage getSprite() {
        return image.getSprite();
    }

    public void setSprite(String path){
        image = Sprite.load(path);
    }

    @Override
    public String toString(){
        return name;
    }

}
