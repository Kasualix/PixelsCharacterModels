package me.pixeldots.pixelscharactermodels.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.pixeldots.pixelscharactermodels.PCMClient;
import me.pixeldots.pixelscharactermodels.PCMMain;
import me.pixeldots.pixelscharactermodels.gui.widgets.NoBackButtonWidget;
import me.pixeldots.pixelscharactermodels.gui.widgets.ToggleWidget;
import me.pixeldots.pixelscharactermodels.skin.SkinHelper;
import me.pixeldots.scriptedmodels.ScriptedModels;
import me.pixeldots.scriptedmodels.platform.PlatformUtils;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.text.Text;

public class SettingsGui extends GuiHandler {

    public LivingEntity entity;
    public EntityModel<?> model;
    public UUID uuid;
    public float entityViewScale = 75;

    public String selectedPreset = "";
    public List<ButtonWidget> presetButtons = new ArrayList<>();

    public static String path_offset = "";

    public SettingsGui(LivingEntity _entity) {
        super("Settings");
        entity = _entity;
        model = PlatformUtils.getModel(_entity);
        uuid = _entity.getUuid();
    }

    public SettingsGui(LivingEntity _entity, float _entityViewScale) {
        this(_entity);
        entityViewScale = _entityViewScale;
    }

    public void setScreen(GuiHandler gui) {
        path_offset = "";
        this.client.setScreen(gui);
    }

    @Override
    public void init() {
        super.init();

        // Top Bar
        addButton(new NoBackButtonWidget(0, 0, 50, 10, Text.of("Presets"), (btn) -> {
            setScreen(new PresetsGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(50, 0, 50, 10, Text.of("Editor"), (btn) -> {
            setScreen(new EditorGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(100, 0, 50, 10, Text.of("Animation"), (btn) -> {
            setScreen(new AnimationGui(entity, this.entityViewScale));
        }));
        addButton(new NoBackButtonWidget(150, 0, 50, 10, Text.of("Settings"), (btn) -> {})).active = false;


        addDrawableElement(new ToggleWidget(5, 15, 110, 10, "Show block under Player", PCMMain.settings.show_block_under_player_ui, (val) -> {
            PCMMain.settings.show_block_under_player_ui = val;
        }));
        addDrawableElement(new ToggleWidget(5, 30, 110, 10, "Player faces Cursor", PCMMain.settings.player_faces_cursor_ui, (val) -> {
            PCMMain.settings.player_faces_cursor_ui = val;
        }));
        addDrawableElement(new ToggleWidget(5, 45, 110, 10, "Keybinding opens Editor", PCMMain.settings.keybinding_opens_editor, (val) -> {
            PCMMain.settings.keybinding_opens_editor = val;
        }));
        addDrawableElement(new ToggleWidget(5, 60, 110, 10, "Preview Preset", PCMMain.settings.preview_preset, (val) -> {
            PCMMain.settings.preview_preset = val;
        }));
        addDrawableElement(new ToggleWidget(5, 75, 110, 10, "Radians instead of Degress", PCMMain.settings.radians_instead_of_degress, (val) -> {
            PCMMain.settings.radians_instead_of_degress = val;
        }));
        addDrawableElement(new ToggleWidget(5, 90, 110, 10, "Show NameTags", PCMMain.settings.show_nametags, (val) -> {
            PCMMain.settings.show_nametags = val;
        }));
        addDrawableElement(new ToggleWidget(5, 105, 110, 10, "Show Armor", PCMMain.settings.show_armor, (val) -> {
            PCMMain.settings.show_armor = val;
        }));

        addButton(new ButtonWidget(5, this.height-15, 110, 10, Text.of("Clear Entity Data"), (btn) -> {
            SkinHelper.clearSkins();
            PCMClient.EntityAnimationList.clear();
            ScriptedModels.EntityScript.clear();
        }));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        if (mouseX >= 240 && mouseX < this.width) {
            entityViewScale += amount*10;
            if (entityViewScale < 1) entityViewScale = 1;
        }
        return super.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        float entityMouseX = (float)(this.width/2)+120;
        float entityMouseY = (float)(this.height/2+37-125);

        if (PCMMain.settings.player_faces_cursor_ui) { 
            entityMouseX -= mouseX;
            entityMouseY -= mouseY;
        } else {
            entityMouseX -= this.width/2-13.5f;
            entityMouseY -= this.height/2+80;
        }

        if (entity != null) {
            if (PCMMain.settings.show_block_under_player_ui) {
                drawEntityOnBlock(this.width/2+120, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, entity);
            } else {
                drawEntity(this.width/2+120, this.height/2+37, Math.round(entityViewScale), entityMouseX, entityMouseY, entity);
            }
        }

        drawColor(matrices, 0, 0, 240, this.height, 0, 4, 17, 222);
        drawVerticalLine(matrices, 240, -1, this.height, 0, 0, 0, 255);
        drawVerticalLine(matrices, 239, -1, this.height, 0, 0, 0, 255);

        drawColor(matrices, 0, 0, this.width, 10, 0, 0, 0, 255);
        super.render(matrices, mouseX, mouseY, delta);
    }

    @Override
    public void close() {
        path_offset = "";
        super.close();
    }

}
