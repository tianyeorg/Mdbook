package xp1.mdbook.client.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.ScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import xp1.mdbook.client.screen.MarkdownBookScreen;

/**
 * Custom item class for the Markdown Book.
 * This book displays HTML content (exported from Typora) using MCEF browser when right-clicked.
 */
public class MarkdownBookItem extends Item {
    
    public static final String HTML_CONTENT_KEY = "HtmlContent";
    
    public MarkdownBookItem(Settings settings) {
        super(settings.maxCount(1));
    }
    
    /**
     * Sets the HTML content for this book item.
     * The HTML content should be exported from Typora or another Markdown editor.
     * 
     * @param itemStack The item stack to set content on
     * @param htmlContent The HTML content string
     */
    public static void setHtmlContent(ItemStack itemStack, String htmlContent) {
        NbtCompound nbt = itemStack.getOrCreateNbt();
        nbt.putString(HTML_CONTENT_KEY, htmlContent);
    }
    
    /**
     * Gets the HTML content from this book item.
     * 
     * @param itemStack The item stack to get content from
     * @return The HTML content string, or empty string if not set
     */
    public static String getHtmlContent(ItemStack itemStack) {
        NbtCompound nbt = itemStack.getNbt();
        if (nbt != null && nbt.contains(HTML_CONTENT_KEY)) {
            return nbt.getString(HTML_CONTENT_KEY);
        }
        return getDefaultHtmlContent();
    }
    
    /**
     * Returns default HTML content for demonstration purposes.
     * In a real scenario, this would be replaced with actual Typora-exported HTML.
     */
    public static String getDefaultHtmlContent() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Helvetica, Arial, sans-serif;
                        line-height: 1.6;
                        padding: 20px;
                        max-width: 800px;
                        margin: 0 auto;
                        background-color: #fefefe;
                        color: #333;
                    }
                    h1, h2, h3, h4, h5, h6 {
                        color: #2c3e50;
                        margin-top: 24px;
                        margin-bottom: 16px;
                    }
                    code {
                        background-color: #f4f4f4;
                        padding: 2px 6px;
                        border-radius: 3px;
                        font-family: 'Courier New', Courier, monospace;
                    }
                    pre {
                        background-color: #f4f4f4;
                        padding: 16px;
                        border-radius: 6px;
                        overflow-x: auto;
                    }
                    blockquote {
                        border-left: 4px solid #ddd;
                        padding-left: 16px;
                        color: #666;
                    }
                    table {
                        border-collapse: collapse;
                        width: 100%;
                        margin: 16px 0;
                    }
                    th, td {
                        border: 1px solid #ddd;
                        padding: 8px;
                        text-align: left;
                    }
                    th {
                        background-color: #f4f4f4;
                        font-weight: bold;
                    }
                    a {
                        color: #3498db;
                        text-decoration: none;
                    }
                    a:hover {
                        text-decoration: underline;
                    }
                </style>
            </head>
            <body>
                <h1>Markdown Book</h1>
                <p>This is a <strong>Markdown Book</strong> that displays HTML content rendered from Markdown.</p>
                
                <h2>Features</h2>
                <ul>
                    <li>Displays Typora-exported HTML content</li>
                    <li>Uses MCEF for in-game browser rendering</li>
                    <li>Supports full HTML/CSS styling</li>
                    <li>Interactive content support</li>
                </ul>
                
                <h2>How to Use</h2>
                <ol>
                    <li>Create your content in <em>Typora</em></li>
                    <li>Export as HTML</li>
                    <li>Use commands or data generator to set content</li>
                    <li>Right-click the book to view</li>
                </ol>
                
                <h2>Code Example</h2>
                <pre><code>// Set HTML content programmatically
MarkdownBookItem.setHtmlContent(itemStack, htmlContent);
                </code></pre>
                
                <blockquote>
                    <p><strong>Note:</strong> This mod requires MCEF (Minecraft Chromium Embedded Framework) to render HTML content.</p>
                </blockquote>
                
                <h2>Table Example</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Feature</th>
                            <th>Status</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>HTML Rendering</td>
                            <td>✓ Complete</td>
                        </tr>
                        <tr>
                            <td>CSS Styling</td>
                            <td>✓ Complete</td>
                        </tr>
                        <tr>
                            <td>JavaScript</td>
                            <td>⚠ Limited</td>
                        </tr>
                    </tbody>
                </table>
                
                <p>Created with ❤️ using Fabric API and MCEF</p>
            </body>
            </html>
            """;
    }
    
    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        
        if (!world.isClient) {
            // Server side - just return success
            return ActionResult.SUCCESS;
        } else {
            // Client side - open the screen
            String htmlContent = getHtmlContent(itemStack);
            
            // Open the Markdown Book screen
            user.getClient().send(() -> {
                MarkdownBookScreen screen = new MarkdownBookScreen(htmlContent);
                user.getClient().setScreen(screen);
            });
            
            return ActionResult.SUCCESS;
        }
    }
    
    @Override
    public Text getName(ItemStack stack) {
        return Text.literal("Markdown Book");
    }
}
