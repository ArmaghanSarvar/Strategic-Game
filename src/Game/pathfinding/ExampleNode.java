
package Game.pathfinding;


public class ExampleNode extends AbstractNode {

        public ExampleNode(int xPosition, int yPosition) {
            super(xPosition, yPosition);
        }

        public void sethCosts(AbstractNode endNode) {
            this.sethCosts((absolute(this.getxPosition() - endNode.getxPosition())
                    + absolute(this.getyPosition() - endNode.getyPosition()))
                    * BASICMOVEMENTCOST);
        }

        private int absolute(int a) {
            return a > 0 ? a : -a;
        }
}
