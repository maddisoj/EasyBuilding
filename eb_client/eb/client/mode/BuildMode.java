package eb.client.mode;

import net.minecraft.src.ItemStack;
import eb.client.Direction;
import eb.client.GhostBlockHandler;
import eb.client.macros.Macro;
import eb.client.macros.instructions.MoveInstruction;
import eb.client.macros.instructions.UseInstruction;
import eb.common.EBHelper;
import eb.common.network.PacketPlaceBlock;

public class BuildMode extends GhostBlockMode {
	public static class BuildUseInstruction extends UseInstruction {
		private int itemID;
		private int itemMetadata;

		public BuildUseInstruction() {
			this(0, 0);
		}

		public BuildUseInstruction(int itemID, int itemMetadata) {
			this.itemID = itemID;
			this.itemMetadata = itemMetadata;
		}

		@Override
		public void execute() {
			GhostBlockMode mode = GhostBlockHandler.instance().getMode();

			if(!(mode instanceof BuildMode)) {
				return;
			}

			((BuildMode)mode).use(itemID, itemMetadata);
		}

		@Override
		public String getParameters() {
			return Integer.toString(itemID) + " " + Integer.toString(itemMetadata);
		}

		@Override
		public boolean parseParameters(String[] parameters) {
			try {
				itemID = Integer.parseInt(parameters[0]);
				itemMetadata = Integer.parseInt(parameters[1]);
			} catch(Exception e) {
				return false;
			}

			return true;
		}
	}

	@Override
	public UseInstruction use() {
		if(isGhostPlaced()) {
			ItemStack currentItem = EBHelper.getCurrentItem();

			if(currentItem != null) {
				return use(currentItem.itemID, currentItem.getItemDamage());
			}
		}

		return null;
	}

	public UseInstruction use(int itemID, int itemMetadata) {
		if(isGhostPlaced()) {
			EBHelper.sendToServer(new PacketPlaceBlock(x, y, z, itemID, itemMetadata));
			return new BuildUseInstruction(itemID, itemMetadata);
		}

		return null;
	}

	@Override
	public boolean allowsMacros() {
		return true;
	}

	public static Macro translateRawData(short width, short length, short height, byte[] blocks, byte[] meta) {
		Macro macro = new Macro(BuildMode.class);
		boolean leftToRight = true;
		int progress = 0;

		for(int y = 0; y < height; ++y) {
			for(int z = 0; z < length; ++z) {
				for(int x =  (leftToRight ? 0 : width - 1);
				x != (leftToRight ? width : -1);
				x += (leftToRight ? 1 : -1)) {

					int index = y * width * length + z * width + x;

					if(blocks[index] != 0) {
						macro.addInstruction(new BuildUseInstruction(blocks[index], meta[index]));
					}

					if(leftToRight && x != width - 1) {
						macro.addInstruction(new MoveInstruction(Direction.RIGHT));
					} else if(!leftToRight && x != 0) {
						macro.addInstruction(new MoveInstruction(Direction.LEFT));
					}
				}

				leftToRight = !leftToRight;
				macro.addInstruction(new MoveInstruction(Direction.FORWARD));
			}

			macro.addInstruction(new MoveInstruction(Direction.UP));

			for(int i = 0; i < length; ++i) {
				macro.addInstruction(new MoveInstruction(Direction.BACKWARD));
			}
		}

		return macro;
	}
}
