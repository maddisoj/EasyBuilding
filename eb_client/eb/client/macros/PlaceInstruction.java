package eb.client.macros;

import eb.client.GhostBlockHandler;

/**
 * The instruction representing the placing of a block
 * 
 * @author Lerp
 * @license Lesser GNU Public License v3 http://www.gnu.org/licenses/lgpl.html
 */

public class PlaceInstruction implements IInstruction {
	private int itemID;
	private int metadata;
	
	public PlaceInstruction() {
		this(-1, 0);
	}
	
	public PlaceInstruction(int itemID, int metadata) {
		this.itemID = itemID;
		this.metadata = metadata;
	}
	
	@Override
	public void execute() {	
		GhostBlockHandler.instance().placeBlock(itemID, metadata);
	}

	@Override
	public String getParameters() {
		return Integer.toString(itemID) + " " + Integer.toString(metadata);
	}

	@Override
	public boolean parseParameters(String[] parameters) {
		try {
			itemID = Integer.parseInt(parameters[0]);
			
			if(parameters.length > 1) { //to accomodate older macros
				metadata = Integer.parseInt(parameters[1]);
			}
		} catch(Exception e) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public boolean shouldSync() {
		return true;
	}
	
	public int getItemID() {
		return itemID;
	}
	
	public int getMetadata() {
		return metadata;
	}
}
