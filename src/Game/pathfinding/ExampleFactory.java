

package Game.pathfinding;


public class ExampleFactory implements NodeFactory {

        @Override
        public AbstractNode createNode(int x, int y) {
            return new ExampleNode(x, y);
        }

}
