package aquarion.world.graphics.shaders;

import arc.graphics.Color;
import mindustry.graphics.Shaders;

public class ColorStripShader extends Shaders.LoadShader {

    Color targetColor;

    public ColorStripShader(){
        this(Color.black);
    }

    public ColorStripShader(Color targetColor) {
        super("stripColor", "screenspace");
        this.targetColor = targetColor;
    }

    @Override
    public void apply() {
        setUniformf("u_target_color", targetColor.r, targetColor.g, targetColor.b, targetColor.a);
    }
}
