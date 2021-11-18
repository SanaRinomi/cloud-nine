package codes.sana.cloudnine.client;

import com.mojang.blaze3d.matrix.MatrixStack;

import codes.sana.cloudnine.CloudNineClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.SliderPercentageOption;

public class CloudNineConfigScreen extends Screen {
    private final Screen parentScreen;

    private static final int TITLE_HEIGHT = 8;

    /** Distance from top of the screen to the options row list's top */
    private static final int OPTIONS_LIST_TOP_HEIGHT = 24;
    /** Distance from bottom of the screen to the options row list's bottom */
    private static final int OPTIONS_LIST_BOTTOM_OFFSET = 32;
    /** Height of each item in the options row list */
    private static final int OPTIONS_LIST_ITEM_HEIGHT = 25;

    /** Width of a button */
    private static final int BUTTON_WIDTH = 200;
    /** Height of a button */
    private static final int BUTTON_HEIGHT = 20;
    /** Distance from bottom of the screen to the "Done" button's top */
    private static final int DONE_BUTTON_TOP_OFFSET = 26;

    /** List of options rows shown on the screen */
    // Not a final field because this cannot be initialized in the constructor,
    // as explained below
    private OptionsRowList optionsRowList;

    public CloudNineConfigScreen(Screen parentScreen) {
        super(new TranslationTextComponent("cloudnine.config.title",
            CloudNineClient.NAME));
        this.parentScreen = parentScreen;
    }

    @Override
    protected void init() {
        // Create the options row list
        // It must be created in this method instead of in the constructor,
        // or it will not be displayed properly
        this.optionsRowList = new OptionsRowList(
                this.minecraft, this.width, this.height,
                OPTIONS_LIST_TOP_HEIGHT,
                this.height - OPTIONS_LIST_BOTTOM_OFFSET,
                OPTIONS_LIST_ITEM_HEIGHT
        );

        this.optionsRowList.addBig(new SliderPercentageOption(
            "cloudnine.config.cloudOffset.title",
            // Range: 0 to width of game window
            -100.0, 100.0,
            // This is an integer option, so allow whole steps only
            1.0F,
            // Getter and setter are similar to those in BooleanOption
            unused -> (double) CloudNineClient.getCloudOffset(),
            (unused, newValue) -> CloudNineClient.setCloudOffset(newValue.floatValue()),
            // BiFunction that returns a string text component
            // in format "<name>: <value>"
            (gs, option) -> new StringTextComponent(
                    // Use I18n.get(String) to get a translation key's value
                    I18n.get("cloudnine.config.cloudOffset.title")
                    + ": "
                    + (int) option.get(gs)
            )
        ));

        // Add the options row list as this screen's child
        // If this is not done, users cannot click on items in the list
        this.children.add(this.optionsRowList);

        // Add the "Done" button
        this.addButton(new Button(
                (this.width - BUTTON_WIDTH) / 2,
                this.height - DONE_BUTTON_TOP_OFFSET,
                BUTTON_WIDTH, BUTTON_HEIGHT,
                // Text shown on the button
                new TranslationTextComponent("gui.done"),
                // Action performed when the button is pressed
                button -> this.onClose()
        ));
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        // First draw the background of the screen
        this.renderBackground(matrixStack);
        this.optionsRowList.render(matrixStack, mouseX, mouseY, partialTicks);
        // Draw the title
        drawCenteredString(matrixStack, this.font, this.title.getString(),
                this.width / 2, TITLE_HEIGHT, 0xFFFFFF);
        // Call the super class' method to complete rendering
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void onClose() {
        CloudNineClient.save();
        this.minecraft.setScreen(parentScreen);
    }
}
