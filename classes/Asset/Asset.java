package classes.Asset;

import java.lang.ref.WeakReference;
import java.util.HashMap;

public abstract class Asset {
    private static final HashMap<String, WeakReference<Asset>> loaded_assets = new HashMap<>();

    protected static Asset load(String fileName) {
        return loaded_assets.containsKey(fileName) ? loaded_assets.get(fileName).get() : null;
    }

    protected static void add(String fileName, Asset asset) {
        loaded_assets.put(fileName, new WeakReference<Asset>(asset));
    }

    public static void cleanup() {
        System.gc();
    }
}
