package aquarion.world.graphics.shaders;

import arc.graphics.Color;
import mindustry.graphics.Shaders;

public class ColorFillShader extends Shaders.LoadShader {

    Color targetColor;

    public ColorFillShader(){
        this(Color.black);
    }

    public ColorFillShader(Color targetColor) {
        super("fillColor", "screenspace");
        this.targetColor = targetColor;
    }

    @Override
    public void apply() {
        setUniformf("u_target_color", targetColor.r, targetColor.g, targetColor.b, targetColor.a);
    }
}
