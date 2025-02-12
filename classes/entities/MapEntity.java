package classes.entities;

public abstract class MapEntity extends PanelEntity {
    private String name;
    private int hit_points;
    private int max_hit_points;
    private int attack_stat = 0;
    private float haste = 0;
    private int defense_stat;
    private long id;
    private static final int INFINITE = Integer.MAX_VALUE; // Infinite health and defense for NPCs

    protected void setName(String name){
        this.name = name;
    }

    public void setHit_points(int hit_points){
        this.hit_points = hit_points;
    }

    protected void setAttack_stat(int attack_stat){
        this.attack_stat = attack_stat;
    }

    protected void setHaste(float haste){
        this.haste = haste;
    }

    protected void setDefense_stat(int defense_stat){
        this.defense_stat = defense_stat;
    }

    protected void setId(long id){
        this.id = id;
    }

    public void setMax_hit_points(int max_hit_points){
        this.max_hit_points = max_hit_points;
    }

    public MapEntity(){
        super();
        this.name = "Default";
        this.hit_points = INFINITE;
        max_hit_points = hit_points;
        this.attack_stat = 1;
        this.haste = 1;
        this.defense_stat = 1;
        this.id = 1;
    }

    public int attack(){
        return attack_stat;
    }

    public String getName() {
        return name;
    }

    public int getHit_points() {
        return hit_points;
    }

    public int getMax_hit_points(){
        return max_hit_points;
    }

    //hit point test - dym
    public void damage(int damage) {
        hit_points -= damage;
    }

    public long getId() {
        return id;
    }

    public void show_stats(){
        //to check if its a player, NPCs shouldn't call these
        if(hit_points != INFINITE){
            System.out.println(
            "Showing stats of " + name + ":\n"
            + "HP    : " + hit_points + "\n"
            + "ATK   : " + attack_stat + "\n"
            + "HASTE : " + haste + "\n"
            + "DEF   : " + defense_stat
            );
        }
    }
}
