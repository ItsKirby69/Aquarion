package aquarion.world.graphics.shaders;

import arc.Core;
import arc.graphics.Texture;
import mindustry.graphics.Shaders;

public class MaskBaseTextureShader extends Shaders.LoadShader {

    String textureName = "white";

    public Texture maskTex;

    public MaskBaseTextureShader(){
        this("");
    }

    public MaskBaseTextureShader(String textureName) {
        super("maskBaseTexture", "screenspace");
        this.textureName = textureName;
    }

    @Override
    public void apply() {

        maskTex.bind(1);
        setUniformi("u_texture_mask", 1);

    }

    @Override
    public void dispose() {
        super.dispose();
        maskTex.dispose();
    }
}
