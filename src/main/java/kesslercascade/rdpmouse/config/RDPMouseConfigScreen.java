package kesslercascade.rdpmouse.config;

import kesslercascade.rdpmouse.RDPMouseClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class RDPMouseConfigScreen extends Screen {
    private final Screen parent;

    public RDPMouseConfigScreen(Screen parent) {
        super(Text.translatable("rdpmouse.config.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 4 + 24;

        RDPMouseConfig config = RDPMouseConfig.getInstance();

        // Cycling button for RDP Mode (ON / OFF)
        this.addDrawableChild(
                CyclingButtonWidget.onOffBuilder(config.rdpModeEnabled)
                        .build(
                                centerX - 100,
                                startY,
                                200,
                                20,
                                Text.translatable("rdpmouse.config.rdp_mode"),
                                (button, value) -> {
                                    config.rdpModeEnabled = value;
                                    RDPMouseConfig.save();
                                    RDPMouseClient.applyMode(this.client, value);
                                }
                        )
        );

        // Done button
        this.addDrawableChild(
                ButtonWidget.builder(ScreenTexts.DONE, button -> close())
                        .dimensions(centerX - 100, startY + 60, 200, 20)
                        .build()
        );
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(this.parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);
        context.drawCenteredTextWithShadow(this.textRenderer, Text.translatable("rdpmouse.config.hint"), this.width / 2, this.height / 4 + 2, 0x888888);
    }
}
