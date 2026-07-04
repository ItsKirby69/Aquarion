package aquarion.world.graphics.shaders;

import aquarion.world.graphics.AquaShaders;
import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.Pixmap;
import arc.graphics.Pixmaps;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import arc.util.Time;
import mindustry.game.EventType;
import mindustry.graphics.Shaders;

import static arc.Core.app;
import static arc.Core.assets;

public class MaskBaseTextureShader extends Shaders.LoadShader {

    String textureName = "white";

    Texture baseTex;

    public MaskBaseTextureShader(){
        this("");
    }

    public MaskBaseTextureShader(String textureName) {
        super("maskBaseTexture", "screenspace");
        this.textureName = textureName;

        Events.on(EventType.AtlasPackEvent.class, e -> {
            TextureRegion region = Core.atlas.find(textureName);
            Pixmap pix = new Pixmap(region.width, region.height);
            pix.draw(Core.atlas.getPixmap(region));
            baseTex = new Texture(pix);
            baseTex.setFilter(Texture.TextureFilter.linear);
            baseTex.setWrap(Texture.TextureWrap.repeat);
            Log.info("--------------MaskbaseShader");
            Log.info("Name:" + textureName);
            Log.info("Region:" + region);
            Log.info("Pixmap:" + pix);
            pix.dispose();
            }
        );
    }

    @Override
    public void apply() {
        setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
        setUniformf("u_resolution", Core.camera.width, Core.camera.height);
        //setUniformf("u_time", Time.time);

        baseTex.bind(1);
        setUniformi("u_texture_base", 1);

    }

    @Override
    public void dispose() {
        super.dispose();
        baseTex.dispose();
    }
}
