package xp1.mdbook.client.screen;

import com.keksuccino.mcef.api.MCEFApi;
import com.keksuccino.mcef.api.browser.Browser;
import com.keksuccino.mcef.api.browser.BrowserConfig;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

/**
 * Screen that displays HTML content rendered from Markdown using MCEF browser.
 * This screen is designed to show Typora-generated HTML content in-game.
 */
public class MarkdownBookScreen extends Screen {
    
    @Nullable
    private Browser browser;
    
    private final String htmlContent;
    private int browserWidth;
    private int browserHeight;
    
    /**
     * Creates a new MarkdownBookScreen with the specified HTML content.
     * 
     * @param htmlContent The HTML content to display (typically exported from Typora)
     */
    public MarkdownBookScreen(String htmlContent) {
        super(Text.literal("Markdown Book"));
        this.htmlContent = htmlContent;
    }
    
    @Override
    protected void init() {
        super.init();
        
        // Calculate browser dimensions (leave some padding)
        this.browserWidth = this.width - 40;
        this.browserHeight = this.height - 40;
        
        // Initialize MCEF browser if available
        if (MCEFApi.isAvailable()) {
            try {
                // Create browser configuration
                BrowserConfig config = new BrowserConfig.Builder()
                    .setSize(browserWidth, browserHeight)
                    .setUrl("data:text/html," + java.net.URLEncoder.encode(htmlContent, "UTF-8"))
                    .build();
                
                // Create and initialize the browser
                this.browser = MCEFApi.createBrowser(config);
                
                if (this.browser != null) {
                    this.browser.initialize();
                }
            } catch (Exception e) {
                MarkdownBookScreen.LOGGER.error("Failed to create MCEF browser for markdown content", e);
            }
        } else {
            LOGGER.warn("MCEF is not available, cannot render HTML content");
        }
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render background
        this.renderBackground(context, mouseX, mouseY, delta);
        
        // Render title
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 10, 0xFFFFFF);
        
        // Render browser content if available
        if (this.browser != null && this.browser.isReady()) {
            int x = (this.width - browserWidth) / 2;
            int y = (this.height - browserHeight) / 2;
            
            // Render the browser texture
            this.browser.render(context, x, y, browserWidth, browserHeight);
        } else {
            // Show fallback message if browser is not ready
            String message = this.browser == null ? 
                "MCEF not available - install MCEF mod" : 
                "Loading content...";
            context.drawCenteredTextWithShadow(this.textRenderer, message, this.width / 2, this.height / 2, 0xFFFF5555);
        }
        
        // Render instructions
        context.drawCenteredTextWithShadow(this.textRenderer, 
            "Press ESC to close | Scroll to navigate", 
            this.width / 2, 
            this.height - 15, 
            0xAAAAAA);
        
        super.render(context, mouseX, mouseY, delta);
    }
    
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.browser != null && this.browser.isReady()) {
            int x = (this.width - browserWidth) / 2;
            int y = (this.height - browserHeight) / 2;
            
            // Forward mouse events to browser
            if (mouseX >= x && mouseX <= x + browserWidth && 
                mouseY >= y && mouseY <= y + browserHeight) {
                this.browser.mouseClicked(mouseX - x, mouseY - y, button);
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (this.browser != null && this.browser.isReady()) {
            int x = (this.width - browserWidth) / 2;
            int y = (this.height - browserHeight) / 2;
            
            // Forward mouse events to browser
            if (mouseX >= x && mouseX <= x + browserWidth && 
                mouseY >= y && mouseY <= y + browserHeight) {
                this.browser.mouseReleased(mouseX - x, mouseY - y, button);
                return true;
            }
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.browser != null && this.browser.isReady()) {
            int x = (this.width - browserWidth) / 2;
            int y = (this.height - browserHeight) / 2;
            
            // Forward scroll events to browser
            if (mouseX >= x && mouseX <= x + browserWidth && 
                mouseY >= y && mouseY <= y + browserHeight) {
                this.browser.mouseScrolled(verticalAmount);
                return true;
            }
        }
        return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.browser != null && this.browser.isReady()) {
            // Forward keyboard events to browser
            if (this.browser.keyPressed(keyCode)) {
                return true;
            }
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        if (this.browser != null && this.browser.isReady()) {
            // Forward keyboard events to browser
            if (this.browser.keyReleased(keyCode)) {
                return true;
            }
        }
        return super.keyReleased(keyCode, scanCode, modifiers);
    }
    
    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.browser != null && this.browser.isReady()) {
            // Forward character events to browser
            if (this.browser.charTyped(chr)) {
                return true;
            }
        }
        return super.charTyped(chr, modifiers);
    }
    
    @Override
    public void close() {
        // Clean up browser resources
        if (this.browser != null) {
            this.browser.close();
            this.browser = null;
        }
        super.close();
    }
    
    @Override
    public void removed() {
        // Clean up when screen is removed
        if (this.browser != null) {
            this.browser.close();
            this.browser = null;
        }
        super.removed();
    }
}
