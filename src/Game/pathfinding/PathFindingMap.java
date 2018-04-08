/*
    Copyright (C) 2012 http://software-talk.org/ (developer@software-talk.org)

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * // TODO
 * possible optimizations:
 * - calculate f as soon as g or h are set, so it will not have to be
 *      calculated each time it is retrieved
 * - store nodes in openList sorted by their f value.
 */

package Game.pathfinding;

import Game.mapworks.Map;

import java.util.LinkedList;
import java.util.List;


public class PathFindingMap<T extends AbstractNode> {

    protected static boolean CANMOVEDIAGONALY = true;
    private T[][] nodes;
    private Map map;
    private boolean land;
    protected int width;
    protected int higth;
    private NodeFactory nodeFactory;

    public PathFindingMap(Map map, int width, int higth, NodeFactory nodeFactory) {
        // TODO check parameters. width and higth should be > 0.
        this.nodeFactory = nodeFactory;
        nodes = (T[][]) new AbstractNode[width][higth];
        this.width = width - 1;
        this.higth = higth - 1;
        this.map = map;
        initEmptyNodes();
    }

    public PathFindingMap(Map map, int width, int higth, NodeFactory nodeFactory, int k) {
        // TODO check parameters. width and higth should be > 0.
        this.nodeFactory = nodeFactory;
        nodes = (T[][]) new AbstractNode[width][higth];
        this.width = width - 1;
        this.higth = higth - 1;
        this.map = map;
        //initEmptyNodes();
        initEmptyNodesWater();
    }


    private void initEmptyNodes() {
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= higth; j++) {
                nodes[i][j] = (T) nodeFactory.createNode(i, j);
                nodes[i][j].setWalkable(map.getTiles()[i][j].isWalkableOn());

            }
        }
    }

    private  void initEmptyNodesWater() {
        for (int i = 0; i <= width; i++) {
            for (int j = 0; j <= higth; j++) {
                nodes[i][j] = (T) nodeFactory.createNode(i, j);
                nodes[i][j].setWalkable(map.getTiles()[i][j].isSailableOn());

            }
        }
    }

    public void setWalkable(int x, int y, boolean bool) {
        nodes[x][y].setWalkable(bool);
    }

    public final T getNode(int x, int y) {
        // TODO check parameter.
        return nodes[x][y];
    }

    public void drawMap() {
        for (int i = 0; i <= width; i++) {
            print(" _");
        }
        print("\n");

        for (int j = higth; j >= 0; j--) {
            print("|");
            for (int i = 0; i <= width; i++) {
                if (nodes[i][j].isWalkable()) {
                    print("  ");
                } else {
                    print(" #");
                }
            }
            print("|\n");
        }

        for (int i = 0; i <= width; i++) {
            print(" _");
        }
    }


    private void print(String s) {
        System.out.print(s);
    }


    private List<T> openList;
    private List<T> closedList;
    private boolean done = false;

    public final List<T> findPath(int oldX, int oldY, int newX, int newY) {
        // TODO check input
        openList = new LinkedList<T>();
        closedList = new LinkedList<T>();
        openList.add(nodes[oldX][oldY]);

        done = false;
        T current;
        while (!done) {
            current = lowestFInOpen();
            closedList.add(current);
            openList.remove(current);

            if ((current.getxPosition() == newX)
                    && (current.getyPosition() == newY)) {
                return calcPath(nodes[oldX][oldY], current);
            }

            List<T> adjacentNodes = getAdjacent(current);
            for (int i = 0; i < adjacentNodes.size(); i++) {
                T currentAdj = adjacentNodes.get(i);
                if (!openList.contains(currentAdj)) {
                    currentAdj.setPrevious(current);
                    currentAdj.sethCosts(nodes[newX][newY]);
                    currentAdj.setgCosts(current);
                    openList.add(currentAdj);
                } else {
                    if (currentAdj.getgCosts() > currentAdj.calculategCosts(current)) {
                        currentAdj.setPrevious(current);
                        currentAdj.setgCosts(current);
                    }
                }
            }

            if (openList.isEmpty()) {
                return new LinkedList<T>();
            }
        }
        System.out.println("returning null in the pathfindingmap");
        return null;
    }


    private List<T> calcPath(T start, T goal) {
        LinkedList<T> path = new LinkedList<T>();

        T curr = goal;
        boolean done = false;
        while (!done) {
            path.addFirst(curr);
            curr = (T) curr.getPrevious();

            if (curr.equals(start)) {
                done = true;
            }
        }
        return path;
    }


    private T lowestFInOpen() {
        T cheapest = openList.get(0);
        for (int i = 0; i < openList.size(); i++) {
            if (openList.get(i).getfCosts() < cheapest.getfCosts()) {
                cheapest = openList.get(i);
            }
        }
        return cheapest;
    }

    private List<T> getAdjacent(T node) {
        // TODO make loop
        int x = node.getxPosition();
        int y = node.getyPosition();
        List<T> adj = new LinkedList<T>();

        T temp;
        if (x > 0) {
            temp = this.getNode((x - 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (x < width) {
            temp = this.getNode((x + 1), y);
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (y > 0) {
            temp = this.getNode(x, (y - 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }

        if (y < higth) {
            temp = this.getNode(x, (y + 1));
            if (temp.isWalkable() && !closedList.contains(temp)) {
                temp.setIsDiagonaly(false);
                adj.add(temp);
            }
        }
        if (CANMOVEDIAGONALY) {
            if (x < width && y < higth) {
                temp = this.getNode((x + 1), (y + 1));
                if (temp.isWalkable() && !closedList.contains(temp)) {
                    temp.setIsDiagonaly(true);
                    adj.add(temp);
                }
            }

            if (x > 0 && y > 0) {
                temp = this.getNode((x - 1), (y - 1));
                if (temp.isWalkable() && !closedList.contains(temp)) {
                    temp.setIsDiagonaly(true);
                    adj.add(temp);
                }
            }

            if (x > 0 && y < higth) {
                temp = this.getNode((x - 1), (y + 1));
                if (temp.isWalkable() && !closedList.contains(temp)) {
                    temp.setIsDiagonaly(true);
                    adj.add(temp);
                }
            }

            if (x < width && y > 0) {
                temp = this.getNode((x + 1), (y - 1));
                if (temp.isWalkable() && !closedList.contains(temp)) {
                    temp.setIsDiagonaly(true);
                    adj.add(temp);
                }
            }
        }
        return adj;
    }
}