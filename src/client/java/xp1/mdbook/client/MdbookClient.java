package xp1.mdbook.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import xp1.mdbook.Mdbook;
import xp1.mdbook.client.command.MarkdownBookCommands;

public class MdbookClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		
		// Register client commands
		MarkdownBookCommands.register();
		
		// Add tooltip to Markdown Book item
		ItemTooltipCallback.EVENT.register((itemStack, context, lines) -> {
			if (itemStack.getItem() == Mdbook.MARKDOWN_BOOK) {
				lines.add(Text.literal("").formatted(Formatting.GRAY));
				lines.add(Text.literal("Markdown Book - Right-click to view").formatted(Formatting.GREEN));
				lines.add(Text.literal("Contains HTML content from Typora").formatted(Formatting.YELLOW));
				
				if (Screen.hasShiftDown()) {
					lines.add(Text.literal("").formatted(Formatting.GRAY));
					lines.add(Text.literal("Commands: /mdbook help").formatted(Formatting.AQUA));
					lines.add(Text.literal("Export Typora → HTML → /mdbook sethtml").formatted(Formatting.DARK_GREEN));
				} else {
					lines.add(Text.literal("(Hold Shift for more info)").formatted(Formatting.DARK_GRAY));
				}
			}
		});
	}
}