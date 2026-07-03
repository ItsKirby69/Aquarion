package aquarion.world.blocks.neoplasia;

import mindustry.gen.Building;
import mindustry.world.Block;

public class NeoplasmHeart extends Block {
    public NeoplasmHeart(String name) {
        super(name);
        update = true;
        solid = true;
        destructible = true;
        rebuildable = false;
    }

    public class NeoplasmHeartBuild extends Building {
        @Override
        public void created() {
            super.created();
            NeoplasiaGraph.hearts.add(this);
            NeoplasiaGraph.gracePeriod = true;
            NeoplasiaGraph.buildPulseIds.clear();
        }

        @Override
        public void onRemoved() {
            super.onRemoved();
            NeoplasiaGraph.hearts.remove(this);
        }
    }
}
