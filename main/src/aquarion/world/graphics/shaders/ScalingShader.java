package aquarion.world.graphics.shaders;

import arc.Core;
import arc.Events;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import mindustry.game.EventType;
import mindustry.graphics.Shaders;

public class ScalingShader extends Shaders.LoadShader {

    public ScalingShader() {
        super("tileTexScaler", "screenspace");
    }

    @Override
    public void apply() {
        setUniformf("u_campos", Core.camera.position.x - Core.camera.width / 2, Core.camera.position.y - Core.camera.height / 2);
        setUniformf("u_resolution", Core.camera.width, Core.camera.height);

    }
}
