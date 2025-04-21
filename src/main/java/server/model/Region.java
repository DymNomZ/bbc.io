package server.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Deprecated
public class Region {
    private List<RegionFragment> regions;

    public Region() {
        regions = new ArrayList<>();
    }

    public Region(RegionFragment ... regions) {
        this.regions = new ArrayList<>();
        this.regions.addAll(Arrays.asList(regions));
    }

    public boolean isColliding(ServerEntity entity) {
        return false;
    }
}
