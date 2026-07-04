package aquarion.world.graphics;

import aquarion.world.graphics.shaders.ColorFillShader;
import aquarion.world.graphics.shaders.ColorStripShader;
import aquarion.world.graphics.shaders.MaskBaseTextureShader;
import aquarion.world.graphics.shaders.util.CaptureBuffer;
import aquarion.world.graphics.shaders.util.ShaderWrapper;
import arc.graphics.Color;
import arc.graphics.gl.Shader;
import mindustry.graphics.CacheLayer;
import mindustry.graphics.Shaders;

import static mindustry.Vars.renderer;

public class LiquidUnderFloorLayer extends CacheLayer.ShaderLayer {

    /**shader for stripping marker colors*/
    public Shader stripShader;

    /**shader for masking the base texture*/
    public Shader baseShader;

    /***/

    public CaptureBuffer intermediateBuffer = new CaptureBuffer();

    public LiquidUnderFloorLayer(Shader shader, Color baseColor) {
        this(shader, baseColor, "white");
    }

    public LiquidUnderFloorLayer(Shader shader, Color targetColor, String baseTexName) {
        super(shader);
        this.shader = new ShaderWrapper(shader) {
            @Override
            public void apply() {
                super.apply();
                intermediateBuffer.getTexture().bind(0);
            }
        };
        stripShader = new ColorStripShader(targetColor);
        baseShader = new MaskBaseTextureShader(baseTexName);
    }

    @Override
    public void end(){
        if(!renderer.animateWater) return;

        //finish capturing floors
        renderer.effectBuffer.end();
        renderer.blocks.floor.beginDraw();

        //preprocess floors to create base area for the liquid texture
        intermediateBuffer.capture();
        renderer.effectBuffer.blit(baseShader);
        intermediateBuffer.stopCapture();

        renderer.blocks.floor.beginDraw();

        //draw liquid to the screen
        intermediateBuffer.blit(shader);

        //finally draw the floors
        renderer.effectBuffer.blit(stripShader);
        renderer.blocks.floor.beginDraw();
    }
}
