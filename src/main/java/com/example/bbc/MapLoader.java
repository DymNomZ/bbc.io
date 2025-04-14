package com.example.bbc;

import classes.Sprite;
import classes.Tile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Deprecated
public class MapLoader {

    ArrayList<Tile> loaded_tiles = new ArrayList<>();
    ArrayList<ZipEntry> png_entries = new ArrayList<>();
    int[][] loaded_map_indexes, tile_data_indexes;

    public MapLoader(){

    }

    public ArrayList<Tile> getTiles(){
        return loaded_tiles;
    }

    public int[][] getMapIndexes(){
        return loaded_map_indexes;
    }

    public void loadMapData(String path){

        ZipFile zip_file;
        try {

            zip_file = new ZipFile(path);

            // Get an enumeration of the entries in the zip file
            Enumeration<? extends ZipEntry> entries = zip_file.entries();

            // Iterate over the entries
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                //read operations:
                if(entry.getName().endsWith("data.txt")){
                    readTile_data(zip_file, entry);
                }
                if(entry.getName().endsWith("$.txt")){
                    readMap(zip_file, entry);
                }
            }

            //Store the pngs in an ArrayList first
            entries = zip_file.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                if(entry.getName().endsWith(".png")){
                    png_entries.add(entry);
                }
            }

            //Iterate until png matches index
            for(int i = 0; i < tile_data_indexes.length; i++){
                for(ZipEntry z : png_entries){
                    //extract index at the beginning of the png name
                    String image_index = z.getName().substring(0, z.getName().lastIndexOf('$'));

                    if(Integer.parseInt(image_index) == tile_data_indexes[i][0]){
                        loaded_tiles.add(readImage(zip_file, z, tile_data_indexes, i));
                    }
                }
            }

            //Check print
            //System.out.println(loaded_tiles.size());
            for(Tile t : loaded_tiles){
                // System.out.println(
                //     t.name + " " + t.index + " "
                //     + t.is_solid + " " + t.is_animated);
            }

            zip_file.close();

        } catch (IOException ex) {
        }
    }

    public Tile readImage(ZipFile zip, ZipEntry image, int[][] tile_data_indexes, int curr_idx){

        Sprite tile_image;
        String tile_name = image.getName().substring(image.getName().lastIndexOf("$")+1);

        tile_image = Sprite.load("map_tiles/" + tile_name);

        int tile_index = tile_data_indexes[curr_idx][0];
        boolean solid_state = tile_data_indexes[curr_idx][1] == 1;
        boolean animated_state = tile_data_indexes[curr_idx][2] == 1;

        return new Tile(tile_image, tile_name, tile_index, solid_state, animated_state);
    }

    public void readTile_data(ZipFile zip, ZipEntry tile_data){

        InputStream tile_data_stream;
        BufferedReader reader;
        try {
            tile_data_stream = zip.getInputStream(tile_data);
            reader = new BufferedReader(new InputStreamReader(tile_data_stream));

            String line = reader.readLine();
            int td_h = 0;
            int td_l = line.length() / 2;

            do{
                td_h++;
            }while ((reader.readLine()) != null);

            reader.close();

            tile_data_stream = zip.getInputStream(tile_data);
            reader = new BufferedReader(new InputStreamReader(tile_data_stream));

            tile_data_indexes = new int[td_h][td_l];

            for(int i = 0; i < td_h; i++){

                String[] raw_indexes = reader.readLine().split(" ");

                for(int j = 0; j < td_l; j++) {
                    tile_data_indexes[i][j] = Integer.parseInt(raw_indexes[j]);
                }

            }

            reader.close();
            tile_data_stream.close();

        } catch (IOException ex) {
        }
    }

    public void readMap(ZipFile zip, ZipEntry map){

        InputStream map_data_stream;
        BufferedReader reader;
        try {
            map_data_stream = zip.getInputStream(map);
            reader = new BufferedReader(new InputStreamReader(map_data_stream));

            int map_h = 0;
            int map_l = reader.readLine().split(" ").length; //because of spaces

            do{
                map_h++;
            }while ((reader.readLine()) != null);

            reader.close();

            map_data_stream = zip.getInputStream(map);
            reader = new BufferedReader(new InputStreamReader(map_data_stream));

            loaded_map_indexes = new int[map_h][map_l];

            for(int i = 0; i < map_h; i++){

                String[] raw_indexes = reader.readLine().split(" ");

                for(int j = 0; j < map_l; j++) {
                    loaded_map_indexes[i][j] = Integer.parseInt(raw_indexes[j]);
                }

            }

            reader.close();
            map_data_stream.close();

        } catch (IOException ex) {
        }

    }
}
