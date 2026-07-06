package aquarion.world.graphics;

import aquarion.world.graphics.shaders.MaskColorShader;
import aquarion.world.graphics.shaders.ScalingShader;
import aquarion.world.graphics.shaders.util.CaptureBuffer;
import aquarion.world.graphics.shaders.util.ShaderWrapper;
import aquarion.world.graphics.shaders.util.TextureFetcher;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.gl.Shader;
import mindustry.graphics.CacheLayer;

import static mindustry.Vars.renderer;

public class LiquidUnderFloorLayer extends CacheLayer.ShaderLayer {

    /**shader for masking the base texture*/
    public MaskColorShader maskTex;

    /**shader for scaling the base texture to a tile in size*/
    public Shader scalingShader;

    /***/
    public TextureFetcher fetcher;

    public CaptureBuffer scalingBuffer = new CaptureBuffer();

    public CaptureBuffer applyLiquidBuffer = new CaptureBuffer();

    public LiquidUnderFloorLayer(Shader shader, Color baseColor) {
        this(shader, baseColor, "white");
    }

    public LiquidUnderFloorLayer(Shader shader, Color targetColor, String baseTexName) {
        super(shader);
        this.shader = new ShaderWrapper(shader) {
            @Override
            public void apply() {
                super.apply();
                scalingBuffer.getTexture().bind(0);
            }
        };
        maskTex = new MaskColorShader(targetColor);
        scalingShader = new ScalingShader();
        fetcher = new TextureFetcher(baseTexName);
    }

    @Override
    public void end(){
        if(!renderer.animateWater) return;

        //finish capturing floors
        renderer.effectBuffer.end();
        renderer.blocks.floor.beginDraw();

        //scale base floor texture to the block grid
        scalingBuffer.capture();
        Draw.blit(fetcher.fetched, scalingShader);
        scalingBuffer.stopCapture();

        //apply liquid distortion to base
        applyLiquidBuffer.capture();
        scalingBuffer.blit(shader);
        applyLiquidBuffer.stopCapture();

        renderer.blocks.floor.beginDraw();

        //use the drawn layer texture as a mask for the liquid texture
        maskTex.maskTex = renderer.effectBuffer.getTexture();
        applyLiquidBuffer.blit(maskTex);


        renderer.blocks.floor.beginDraw();
    }
}
