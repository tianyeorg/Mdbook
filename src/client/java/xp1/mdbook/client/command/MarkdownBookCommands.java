package xp1.mdbook.client.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import xp1.mdbook.Mdbook;
import xp1.mdbook.client.item.MarkdownBookItem;

/**
 * Registers client-side commands for setting HTML content on Markdown Book items.
 * This allows players to easily set Typora-exported HTML content on their books.
 */
public class MarkdownBookCommands {
    
    public static void register() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            // Command: /mdbook sethtml <html_content>
            // Sets the HTML content of the held Markdown Book item
            dispatcher.register(
                ClientCommandManager.literal("mdbook")
                    .then(ClientCommandManager.literal("sethtml")
                        .then(ClientCommandManager.argument("html_content", StringArgumentType.greedyString())
                            .executes(context -> {
                                String htmlContent = StringArgumentType.getString(context, "html_content");
                                
                                var player = context.getSource().getPlayer();
                                ItemStack heldStack = player.getMainHandStack();
                                
                                if (!(heldStack.getItem() instanceof MarkdownBookItem)) {
                                    player.sendMessage(Text.literal("§cYou must hold a Markdown Book!"), false);
                                    return 0;
                                }
                                
                                MarkdownBookItem.setHtmlContent(heldStack, htmlContent);
                                player.sendMessage(Text.literal("§aHTML content set successfully!"), true);
                                
                                return 1;
                            })
                        )
                    )
                    .then(ClientCommandManager.literal("give")
                        .executes(context -> {
                            var player = context.getSource().getPlayer();
                            ItemStack bookStack = new ItemStack(Mdbook.MARKDOWN_BOOK);
                            
                            // Set default HTML content
                            MarkdownBookItem.setHtmlContent(bookStack, MarkdownBookItem.getDefaultHtmlContent());
                            
                            player.getInventory().offerOrDrop(bookStack);
                            player.sendMessage(Text.literal("§aGiven Markdown Book with default content!"), true);
                            
                            return 1;
                        })
                    )
                    .then(ClientCommandManager.literal("help")
                        .executes(context -> {
                            var player = context.getSource().getPlayer();
                            player.sendMessage(Text.literal("§6=== Markdown Book Commands ==="), false);
                            player.sendMessage(Text.literal("§e/mdbook sethtml <html> §7- Set HTML content on held book"), false);
                            player.sendMessage(Text.literal("§e/mdbook give §7- Give yourself a Markdown Book"), false);
                            player.sendMessage(Text.literal("§e/mdbook help §7- Show this help message"), false);
                            player.sendMessage(Text.literal("§7Tip: Export your Typora markdown as HTML and paste it!"), false);
                            
                            return 1;
                        })
                    )
            );
        });
        
        Mdbook.LOGGER.info("Registered Markdown Book client commands");
    }
}
