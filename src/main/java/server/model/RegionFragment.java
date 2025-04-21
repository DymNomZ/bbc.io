package server.model;

@Deprecated
public abstract class RegionFragment {
    // Relative to the base of the region (not the map)
    public double x, y;
    public RegionFragment(double x, double y) {
        this.x = x;
        this.y = y;
    }
    // Add collision function between regionfragment here

    public class RectangleFragment extends RegionFragment {
        public RectangleFragment(double x, double y, double width, double height) {
            super(x, y);
        }
    }

    // to input moree subclasses
}
