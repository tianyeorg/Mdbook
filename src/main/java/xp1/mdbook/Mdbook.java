package xp1.mdbook;

import net.fabricmc.api.ModInitializer;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xp1.mdbook.client.item.MarkdownBookItem;

public class Mdbook implements ModInitializer {
	public static final String MOD_ID = "mdbook";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	
	// The Markdown Book item
	public static final MarkdownBookItem MARKDOWN_BOOK = new MarkdownBookItem(new Item.Settings());

	@Override
	public void onInitialize() {
		// Register the Markdown Book item
		Registry.register(Registries.ITEM, Identifier.of(MOD_ID, "markdown_book"), MARKDOWN_BOOK);
		
		LOGGER.info("Hello Fabric world! Mdbook initialized with Markdown Book item.");
	}
}