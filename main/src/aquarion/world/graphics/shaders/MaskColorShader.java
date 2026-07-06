package aquarion.world.graphics.shaders;

import arc.graphics.Color;
import arc.graphics.Texture;
import mindustry.graphics.Shaders;

public class MaskColorShader extends Shaders.LoadShader {

    Color targetColor;

    public Texture maskTex;

    public MaskColorShader(){
        this(Color.black);
    }

    public MaskColorShader(Color targetColor) {
        super("maskBaseTexture", "screenspace");
        this.targetColor = targetColor;
    }

    @Override
    public void apply() {
        setUniformf("u_target_color", targetColor.r, targetColor.g, targetColor.b, targetColor.a);

        maskTex.bind(1);
        setUniformi("u_texture_mask", 1);

    }

    @Override
    public void dispose() {
        super.dispose();
        maskTex.dispose();
    }
}
