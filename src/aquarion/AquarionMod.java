package aquarion;

import mindustry.mod.*;
import aquarion.blocks.*;

public class AquarionMod extends Mod {
//TODO proper load order DO NOT DO UNTIL ALL CONTENT IS PORTED
    //crafters
    //defense
    //effect
    //transport
    //liquidBlocks
    //power
    //production (drills and other)
    //cores
    //turrets
    //unit blocks
    //logic
    @Override
    public void loadContent() {
        AquaItems.loadContent();
        AquaDistribution.loadContent();
        AquaLiquid.loadContent();
        AquaLiquids.loadContent();
        AquaEnv.loadContent();
        AquaDefense.loadContent();
        AquaEffect.loadContent();
        AquaCore.loadContent();
    }
}