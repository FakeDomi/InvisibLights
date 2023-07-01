package re.domi.invisiblights.config;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.*;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class ConfigScreen extends Screen
{
    private static final Text SCREEN_TITLE = Text.literal("InvisibLights config");

    private static final Text PLACE_VANILLA_LIGHT_SOURCE_BLOCK = Text.translatable("options.invisiblights.place_vanilla_light_source_block");
    private static final Text LIGHT_SOURCE_GLOWSTONE_COST = Text.translatable("options.invisiblights.light_source_glowstone_cost");
    private static final Text POWERED_LIGHT_ROD_CAPACITY = Text.translatable("options.invisiblights.powered_light_rod_capacity");
    private static final Text POWERED_LIGHT_ROD_CHARGE_RATE = Text.translatable("options.invisiblights.powered_light_rod_charge_rate");
    private static final Text POWERED_LIGHT_SOURCE_COST = Text.translatable("options.invisiblights.powered_light_rod_source_cost");

    private boolean placeVanillaLightSourceBlock = Config.PlaceVanillaLightSourceBlock;
    private TextFieldWidget lightSourceGlowstoneCostWidget;
    private TextFieldWidget poweredLightRodCapacityWidget;
    private TextFieldWidget poweredLightRodChargeRateWidget;
    private TextFieldWidget poweredLightSourceCostWidget;

    private final Screen parent;

    protected ConfigScreen(Screen parent)
    {
        super(SCREEN_TITLE);
        this.parent = parent;
    }

    @Override
    protected void init()
    {
        GridWidget grid = new GridWidget();
        grid.getMainPositioner().marginX(5).marginBottom(4).alignRight();
        GridWidget.Adder adder = grid.createAdder(2);

        adder.add(this.createTextWidget(PLACE_VANILLA_LIGHT_SOURCE_BLOCK));
        adder.add(new ButtonWidget.Builder(this.placeVanillaLightSourceBlock ? ScreenTexts.YES : ScreenTexts.NO, button ->
            {
                this.placeVanillaLightSourceBlock = !this.placeVanillaLightSourceBlock;
                button.setMessage(this.placeVanillaLightSourceBlock ? ScreenTexts.YES : ScreenTexts.NO);
            }).dimensions(0, 0, 100, 20).build());

        adder.add(this.createTextWidget(LIGHT_SOURCE_GLOWSTONE_COST));
        this.lightSourceGlowstoneCostWidget = this.createIntTextFieldWidget(Config.LightSourceGlowstoneCost);
        adder.add(this.lightSourceGlowstoneCostWidget);

        adder.add(this.createTextWidget(POWERED_LIGHT_ROD_CAPACITY));
        this.poweredLightRodCapacityWidget = this.createLongTextFieldWidget(Config.PoweredLightRodCapacity);
        adder.add(this.poweredLightRodCapacityWidget);

        adder.add(this.createTextWidget(POWERED_LIGHT_ROD_CHARGE_RATE));
        this.poweredLightRodChargeRateWidget = this.createLongTextFieldWidget(Config.PoweredLightRodChargeRate);
        adder.add(this.poweredLightRodChargeRateWidget);

        adder.add(this.createTextWidget(POWERED_LIGHT_SOURCE_COST));
        this.poweredLightSourceCostWidget = this.createLongTextFieldWidget(Config.PoweredLightSourceCost);
        adder.add(this.poweredLightSourceCostWidget);

        grid.refreshPositions();
        SimplePositioningWidget.setPos(grid, 0, this.height / 6 - 12, this.width, this.height - 50, 0.5F, 0.0F);
        grid.forEachChild(this::addDrawableChild);

        this.addDrawableChild(new ButtonWidget.Builder(ScreenTexts.DONE, button ->
        {
            Config.PlaceVanillaLightSourceBlock = this.placeVanillaLightSourceBlock;

            if (!this.lightSourceGlowstoneCostWidget.getText().isEmpty())
            {
                Config.LightSourceGlowstoneCost = Integer.parseInt(this.lightSourceGlowstoneCostWidget.getText());
            }

            if (!this.poweredLightRodCapacityWidget.getText().isEmpty())
            {
                Config.PoweredLightRodCapacity = Long.parseLong(this.poweredLightRodCapacityWidget.getText());
            }
            if (!this.poweredLightRodChargeRateWidget.getText().isEmpty())
            {
                Config.PoweredLightRodChargeRate = Long.parseLong(this.poweredLightRodChargeRateWidget.getText());
            }
            if (!this.poweredLightSourceCostWidget.getText().isEmpty())
            {
                Config.PoweredLightSourceCost = Long.parseLong(this.poweredLightSourceCostWidget.getText());
            }

            Config.write();
            this.close();
        })
            .dimensions(this.width / 2 - 105, this.height - 40, 100, 20)
            .build());

        this.addDrawableChild(new ButtonWidget.Builder(ScreenTexts.CANCEL, button -> this.close())
            .dimensions(this.width / 2 + 5, this.height - 40, 100, 20)
            .build());
    }

    @Override
    public void close()
    {
        if (this.client != null)
        {
            this.client.setScreen(this.parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
        this.renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
    }

    private Widget createTextWidget(Text text)
    {
        return new TextWidget(0, 0, this.textRenderer.getWidth(text), 20, text, this.textRenderer);
    }

    private TextFieldWidget createIntTextFieldWidget(int initialValue)
    {
        TextFieldWidget w = new TextFieldWidget(this.textRenderer, 0, 0, 100, 18, ScreenTexts.EMPTY);
        w.setText(Integer.toString(initialValue));
        w.setTextPredicate(s ->
        {
            if (s.isEmpty())
            {
                return true;
            }

            try
            {
                int i = Integer.parseInt(s);
                return i >= 0;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        });

        return w;
    }

    private TextFieldWidget createLongTextFieldWidget(long initialValue)
    {
        TextFieldWidget w = new TextFieldWidget(this.textRenderer, 0, 0, 100, 18, ScreenTexts.EMPTY);
        w.setText(Long.toString(initialValue));
        w.setTextPredicate(s ->
        {
            try
            {
                long l = Long.parseLong(s);
                return l >= 0;
            }
            catch (NumberFormatException e)
            {
                return false;
            }
        });

        return w;
    }
}
