package woodcutting;

import node.Node;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.Calculations;

import java.awt.*;

public class IndCut extends Node {
	
	
	private static final int CHOPPING_ANIMATION = 871;
	int chopped = 0;
	private Locations currentLocation;
	
	public IndCut(AbstractScript as) {
		super(as);
	}
	
	public enum Locations {
		VARROCK(new Area(3139,3433,3190,3470),
				new Area(3181, 3433, 3185, 3448),
				new Area(3150, 3447, 3162, 3465),
				new String("Tree")), 
				
		DRAYNOR(new Area(3070,3220,3110,3251),
				new Area(3092, 3240, 3095, 3246),
				new Area(3080, 3225, 3091, 3239),
				new String("Willow"));
		

		public Area location;
		public Area bankArea;
		public Area treeArea;
		public String tree;
		
		Locations(Area location, Area bankArea, Area treeArea, String tree) {
			this.location = location;
			this.bankArea = bankArea;
			this.treeArea = treeArea;
			this.tree = tree;
		}
	}
	
	public void onStart() {
		for(Locations location: Locations.values()) {
			if(location.location.contains(as.getLocalPlayer())) {
				currentLocation = location;
			}
		}
	}

	public int execute() {
		if(!as.getInventory().isFull()) {
			if(currentLocation.treeArea.contains(as.getLocalPlayer())) {
				chop(currentLocation.tree);
				return Calculations.random(789,1430);
			} else {
				if(as.getWalking().walk(currentLocation.treeArea.getRandomTile())) {
					return Calculations.random(2400, 6300);
				}
			}
		} else {
			if(currentLocation.bankArea.contains(as.getLocalPlayer())) {
				bank();
				return Calculations.random(1140,2330);
			} else {
				if(as.getWalking().walk(currentLocation.bankArea.getRandomTile())) {
					return Calculations.random(2300, 6800);
				}
			}
		}
		return Calculations.random(789,1430);
	}
	
	public void bank() {
		GameObject booth = as.getGameObjects().closest(x -> x != null && x.hasAction("Bank"));
		if(as.getBank().isOpen()) {
			as.getBank().depositAllExcept(axe -> axe != null && axe.getName().contains("axe"));
				return;
		} else {
			if(booth.interact("Bank")) {
				as.sleepUntil(() -> as.getBank().isOpen(), 9000);
				return;
			}
		}
	}
	
	
	public void chop(String tre) {
		GameObject tree = as.getGameObjects().closest(x -> x != null && x.getName().equals(tre));
		if(as.getLocalPlayer().getAnimation() == CHOPPING_ANIMATION) {
			return;
		}
		if(tree.interact("Chop Down")) {
			int countLog = as.getInventory().count(item -> item.getName().contains("ogs"));
			if(as.sleepUntil(() -> as.getLocalPlayer().getAnimation() == CHOPPING_ANIMATION, 10000)) {
				as.sleepUntil(() -> as.getLocalPlayer().getAnimation() == -1, 300000);
				if(countLog <  as.getInventory().count(item -> item.getName().contains("ogs"))) {
					chopped+= as.getInventory().count(item -> item.getName().contains("ogs")) - countLog;
				}
			}
		}
	}
	
	
	public void onPaint(Graphics graphics) {
		as.onPaint(graphics);
		graphics.drawString(currentLocation + "", 10, 80);
		graphics.drawString(currentLocation.tree + "", 10, 90);
		graphics.drawString(chopped + "", 10, 100);
	}

	@Override
	public boolean activate() throws InterruptedException {
		// TODO Auto-generated method stub
		return true;
	}
}