package server.game_structure;


import server.model.ServerEntity;

import java.util.ArrayList;
import java.util.List;
/**
 * ------------------   QUAD TREE EXPLANATION   ------------------
 * QuadTree uses QuadRectangle as its "bounds" showing what coordinates counts as being "inside" the QuadTree
 *
 * Each Quadtree has a list of objects and a maximum capacity for said list defined in the constructor
 *
 * Each QuadTree also has 4 sub QuadTrees, namely: Northwest (NW), Northeast(NE), Southwest(SW) and Southeast(SE)
 *
 * Lastly each QuadTree has a boolean divided showing if the QuadTree has been divided into the four QuadTrees
 * mentioned above.
 *
 * Insert works like the insert of a normal tree, where it is recursive. It starts at the root and checks for 3 cases
 *         Case 1: Object DOES NOT fit in the bounds / coordinates of the object is not within the bounds
 *              - In this case, we just terminate immediately no further f**king questions
 *
 *         //SINCE WE HAVE CHECKED FOR OBJECTS NOT FITTING IN BOUNDS, THE FF ARE NOW CASES WHERE OBJECTS ARE WITHIN THE BOUNDS//
 *
 *         Case 2: The list of objects is not full and is not divided
 *              - This basically means we are at a leaf node that is not full. In this case, we add the object into the list
 *
 *         Case 3: This means that we are full, or we are not in a leaf node
 *              - It first checks if it is subdivided:
 *              1. If it is NOT subdivided, this means we are in a FULL LEAF NODE.
 *                      -> In this case, we subdivide the node, and we "reinsert" the objects, so that the objects stay at the leaf
 *              2. If it IS subdivided, we are in a BRANCH/INNER NODE
 *                      -> In this case, we recursively call the insert into the 4 subQuadTrees using the insertIntoQuads() function
 *
 *      insertIntoQuads Function
 *          -> Has GameObject as a parameter, and will insert the object into one* of the QuadTrees
 *          [Problem: What if the object is placed in a way that a part of it is outisde the bounds?
 *              Proposed solution: Instead of adding to only one quad tree, we add it to MULTIPLE subQuadTrees]
 *
 * Query Function
 *  - Accepts a RangeCircle, and returns a list of GameObjects that are "in range" of the circle (aka inside the circle)
 *  - It is also a recursive function
 *      First, it creates an empty list of objects. then checks for 2 cases
 *          Case 1: RangeCircle is currently out of the scope of the bounds
 *              - In this case we return an EMPTY list of objects
 *          Case 2: RangeCircle is IN SCOPE of the bounds
 *              - it will add ALL the objects in the QuadTree, and recursively calls query for the subtrees, gets the list they return and adds it into its own list
 *              [It adds all objects first because a previous version of the QuadTree has objects IN THE INNER NODES which is weird. Might change this later but for now
 *               query still works]
 *
 *      After the cases are checked, the list is then returned
 * Clear function
 * - Recursively calls clear into the subtrees, then deletes all the subtree pointers and deletes all list of GameObjects
 *
 * Runtime of functions [may be wrong as I am too lazy to calculate exactly]
 * do note all the log here are log base 4 because of the nature of the tree being divided into 4
 * Insert - O(logn)
 * Query - O(nlogn)
 *
 * Yours Truly - Set H
 */
public class QuadTree {
    int capacity;
    public List<ServerEntity> entities;
    QuadRectangle bounds;
    QuadTree northwest;
    QuadTree southwest;
    QuadTree northeast;
    QuadTree southeast;
    boolean divided;

    public QuadTree(QuadRectangle bounds, int capacity) {
        entities = new ArrayList<ServerEntity>();
        this.capacity = capacity;
        this.bounds = bounds;
        northwest = null;
        southwest = null;
        northeast = null;
        southeast = null;
    }

    public void subdivide() {

        QuadRectangle nw = new QuadRectangle(bounds.x, bounds.y, bounds.w/2, bounds.h/2);

        QuadRectangle sw = new QuadRectangle(bounds.x,bounds.y + bounds.h/2, bounds.w/2, bounds.h/2);

        QuadRectangle ne = new QuadRectangle(bounds.x + bounds.w/2, bounds.y, bounds.w/2, bounds.h/2);

        QuadRectangle se = new QuadRectangle(bounds.x + bounds.w/2, bounds.y + bounds.h/2, bounds.w/2, bounds.h/2);

        northwest = new QuadTree(nw, capacity);
        southwest = new QuadTree(sw, capacity);
        northeast = new QuadTree(ne, capacity);
        southeast = new QuadTree(se, capacity);
        for (ServerEntity obj : entities) {
            insertIntoQuads(obj);
        }
        entities.clear(); //ANIMALLLLLLLL
        divided = true;

    }

    private void insertIntoQuads(ServerEntity entity) {
        if (northwest.bounds.canContain(entity)) {
            northwest.insert(entity);
        } else if (northeast.bounds.canContain(entity)) {
            northeast.insert(entity);
        } else if (southwest.bounds.canContain(entity)) {
            southwest.insert(entity);
        } else if (southeast.bounds.canContain(entity)) {
            southeast.insert(entity);
        }
    }

    public void insert(ServerEntity entity) {
        if(!bounds.canContain(entity)) {
            return;
        }
        if(entities.size() < capacity && !divided) {
            entities.add(entity);
        } else {
            if(!divided) {
                subdivide();
            }
            insertIntoQuads(entity);
        }
    }

    public List<ServerEntity> query(RangeCircle c){
        List<ServerEntity> found = new ArrayList<>();
        if(!bounds.inRange(c)){
            return found;
        } else {
            for(ServerEntity entity : entities) {
                if(c.canContain(entity)) {
                    found.add(entity);
                }
            }
            if(divided) {
                found.addAll(northeast.query(c));
                found.addAll(southeast.query(c));
                found.addAll(northwest.query(c));
                found.addAll(southwest.query(c));
            }

            return found;
        }
    }

    public void clear() {
        if (northwest != null) northwest.clear();
        if (northeast != null) northeast.clear();
        if (southwest != null) southwest.clear();
        if (southeast != null) southeast.clear();

        northwest = null;
        northeast = null;
        southwest = null;
        southeast = null;
        divided = false;

        entities.clear();
    }

}
