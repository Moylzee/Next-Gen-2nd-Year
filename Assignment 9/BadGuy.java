import java.util.ArrayList;
import java.util.List;
import java.awt.Graphics;

public class BadGuy {
	java.awt.Image myImage;
	int x = 0, y = 0;
	ArrayList<Node> surroundingCells = new ArrayList<>(); // ArrayList for Surrounding cells 
	List<Node> closedList = new ArrayList<>(); // Closed List for Nodes

	public BadGuy(java.awt.Image img) {
		myImage = img;
		x = 30;
		y = 10;
	}

	public void reCalcPath(boolean map[][], int targx, int targy) {
		surroundingCells.clear();

		// Loop through cells around the bad guy's cell
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if (i == 0 && j == 0) {
					continue;
				}
				int cellX = x + i;
				int cellY = y + j;
				// Skip Walls
				if (map[cellX][cellY]) {
					continue;
				}
				// Set the variable value of the surrounding cell based on its position
				int gCost = 0;
				if (i == 0 || j == 0) { // Horizontal or vertical cell
					gCost = 10;
				} else { // Diagonal cell
					gCost = 14;
				}
				// hCost
				int dx = Math.abs(cellX - targx);
				int dy = Math.abs(cellY - targy);
				int hCost = dx + dy;
				// fCost
				int fCost = gCost + hCost;
				// Add the cell to the list of surrounding cells with its variable values
				surroundingCells.add(new Node(cellX, cellY, gCost, hCost, fCost));
			}
		}
	}

	public void move(boolean map[][], int targx, int targy) {
		int newx = x, newy = y;
		if (targx < x)
			newx--;
		else if (targx > x)
			newx++;
		if (targy < y)
			newy--;
		else if (targy > y)
			newy++;

		// Has new cell Been Visited?
		boolean alreadyVisited = false;
		for (Node node : closedList) {
			if (node.x == newx && node.y == newy) {
				alreadyVisited = true;
				break;
			}
		}

		// Move to New cell if it hasnt been visited 
		if (!alreadyVisited && !map[newx][newy]) {
			x = newx;
			y = newy;
			// Add the current cell to the closed list
			closedList.add(new Node(x, y, 0, 0, 0));
		} else {
			reCalcPath(map, targx, targy);
			// Find the lowest fCost in the surrounding cells that haven't been visited yet
			int lowestFCost = Integer.MAX_VALUE;
			Node lowestFCostNode = null;
			for (Node node : surroundingCells) {
				boolean visited = false;
				for (Node closedNode : closedList) {
					if (node.x == closedNode.x && node.y == closedNode.y) {
						visited = true;
						break;
					}
				}
				if (!visited && node.f < lowestFCost) {
					lowestFCost = node.f;
					lowestFCostNode = node;
				}
			}
			// Move to node with lowest fCost
			if (lowestFCostNode != null) {
				x = lowestFCostNode.x;
				y = lowestFCostNode.y;
				// Add current cell to closed list
				closedList.add(new Node(x, y, 0, 0, 0));
			}
		}
	}

	public void paint(Graphics g) {
		g.drawImage(myImage, x * 20, y * 20, null);
	}
}