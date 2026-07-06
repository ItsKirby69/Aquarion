package aquarion.world.graphics.shaders.util;

import arc.Core;
import arc.Events;
import arc.func.Cons;
import arc.graphics.Pixmap;
import arc.graphics.Texture;
import arc.graphics.g2d.TextureRegion;
import arc.util.Log;
import mindustry.game.EventType;

/** A helper class for extracting a texture out of the atlas before the pixmaps used to build it are disposed of. Only useful if instantiated before {@link EventType.AtlasPackEvent} is fired.*/
public class TextureFetcher{

    public Texture fetched;

    public String textureName;

    public Cons<Texture> onFetch = (t)->{};

    public TextureFetcher(String textureName){

        this.textureName = textureName;

        Events.on(EventType.AtlasPackEvent.class, e -> {
                TextureRegion region = Core.atlas.find(textureName);
                Pixmap pix = new Pixmap(region.width, region.height);
                pix.draw(Core.atlas.getPixmap(region));
                fetched = new Texture(pix);
                fetched.setFilter(Texture.TextureFilter.linear);
                fetched.setWrap(Texture.TextureWrap.repeat);
                onFetch.get(fetched);
                Log.info("--------------TextureFetcher");
                Log.info("Name:" + textureName);
                Log.info("Region:" + region);
                Log.info("Pixmap:" + pix);
                pix.dispose();
            }
        );
    }

}
