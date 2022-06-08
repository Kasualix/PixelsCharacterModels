package me.pixeldots.pixelscharactermodels.other;

import me.pixeldots.pixelscharactermodels.gui.GuiHandler;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class Node {

    public NodeType type;
    public String[] args;
    public boolean changed = false;

    public Node(NodeType t) {
        type = t;
        args = new String[t.argument_count];
    }
        
    public Node(String _type, String[] _args, int args_index) {
        type = NodeType.valueOf(_type.toUpperCase());
        args = new String[_args.length-args_index];

        for (int i = args_index; i < _args.length; i++) {
            args[i-1] = _args[i];
        }
    }

    public String toScript() {
        return type.tofunc.run(this);
    }

    public String argsToString() {
        String out = "";
        for (String arg : args) {
            out += arg + " ";
        }
        return out.trim();
    }

    public void init(GuiHandler gui, int x, int y) {
        this.type.func.init(gui, x, y, this);
    }

    public static void threeButton(GuiHandler gui, int x, int y, Node node) {
        TextFieldWidget bX = new TextFieldWidget(gui.textRendererGUI, x, y, 30, 10, Text.of(""));
        TextFieldWidget bY = new TextFieldWidget(gui.textRendererGUI, x+35, y, 30, 10, Text.of(""));
        TextFieldWidget bZ = new TextFieldWidget(gui.textRendererGUI, x+69, y, 30, 10, Text.of(""));

        bX.setChangedListener((s) -> { node.args[0] = s; node.changed = true; });
        bY.setChangedListener((s) -> { node.args[1] = s; node.changed = true; });
        bZ.setChangedListener((s) -> { node.args[2] = s; node.changed = true; });

        bX.setText(node.args[0]); bY.setText(node.args[1]); bZ.setText(node.args[2]);
        gui.addTextField(bX); gui.addTextField(bY); gui.addTextField(bZ);
    }

    public enum NodeType {
        PARTICLE((gui, x, y, node) -> {}, (node) -> { return "particle " + node.argsToString(); }, 7),
        TRANSLATE((gui, x, y, node) -> { threeButton(gui, x, y, node); }, (node) -> { return "translate " + node.argsToString(); }, 3),
        SCALE((gui, x, y, node) -> { threeButton(gui, x, y, node); }, (node) -> { return "scale " + node.argsToString(); }, 3),
        ROTATE((gui, x, y, node) -> { threeButton(gui, x, y, node); }, (node) -> { return "rotate " + node.argsToString(); }, 3),
        ANGLE((gui, x, y, node) -> { threeButton(gui, x, y, node); }, (node) -> { return "angle " + node.argsToString(); }, 3),
        VERTEX((gui, x, y, node) -> {}, (node) -> { return "vertex " + node.argsToString(); }, 12),
        CUBE((gui, x, y, node) -> {

        }, (node) -> { return ""; }, 0),
        MESH((gui, x, y, node) -> {

        }, (node) -> { return ""; }, 0),
        CANCEL((gui, x, y, node) -> {}, (node) -> { return "cancel"; }, 0);

        NodeFunc func;
        ScriptFunc tofunc;
        public int argument_count;

        private NodeType(NodeFunc _func, ScriptFunc _tofunc, int _argumentcount) {
            this.func = _func;
            this.tofunc = _tofunc;
            this.argument_count = _argumentcount;
        }
    }

    public interface NodeFunc {
        void init(GuiHandler gui, int x, int y, Node node);
    }

    public interface ScriptFunc {
        String run(Node node);
    }
}