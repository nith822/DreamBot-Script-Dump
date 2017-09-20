package woodcutting;

import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;

import woodcutting.IndCut;
import node.Node;


@ScriptManifest(category = Category.WOODCUTTING, name = "NODE INDUSTRIAL CUT", author = "Himouto",
version = 1.1)
public class IntCutManager extends AbstractScript {
	private final Node[] tasks = {new IndCut(this)};
	private boolean started = false;
	@Override
	public int onLoop() {
		for(final Node node: tasks) {
			try {
				if(node.activate()) {
					if(!started) {
						node.getAs().onStart();
						started = true;
					} else {
						node.execute();
					}
				} else {
					continue;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
}
