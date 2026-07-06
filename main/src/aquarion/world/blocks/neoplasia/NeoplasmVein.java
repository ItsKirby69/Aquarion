package aquarion.world.blocks.neoplasia;

import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.gen.Building;
import mindustry.type.Item;

import static mindustry.Vars.content;

public class NeoplasmVein extends NeoplasiaproductionBlock {
    public NeoplasmVein(String name) {
        super(name);
    }

    public class NeoplasmVeinBuild extends NeoplasiaProductionBlockBuild {
        float activity = 0f;

        @Override
        public void created() {
            super.created();
            activity = 60f;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return true;
        }

        @Override
        public void handleItem(Building source, Item item) {
            items.add(item, 1);
            itemTraffic = Math.min(100f, itemTraffic + 1f);
        }

        @Override
        public void updateTile() {
            super.updateTile();
            if (tile == null || tile.build != this) return;
            if (amount > 50f) {
                for (Building neighbor : proximity) {
                    if (!(neighbor instanceof NeoplasiaProductionBlockBuild pb)) continue;
                    if (neighbor instanceof NeoplasmVeinBuild) continue;
                    NeoplasiaproductionBlock prodBlock = (NeoplasiaproductionBlock) pb.block();
                    if (prodBlock.shouldCraft && pb.amount < prodBlock.craftCost) {
                        float need = prodBlock.craftCost - pb.amount;
                        float transfer = Math.min(amount * 0.15f, need);
                        if (transfer > 0f) {
                            amount -= transfer;
                            pb.amount += transfer;
                        }
                    }
                }
            }
            if (items.total() > 0) {
                activity = Math.min(100f, activity + delta() * 5f);
            } else {
                activity = Math.max(0f, activity - delta() * 0.05f);
            }
            if (activity <= 0f && items.total() <= 0 && base != null) {
                upgradeTo(base);
            }
        }

        @Override
        void pushItems() {
            for (Item item : content.items()) {
                while (items.get(item) > 0) {
                    items.remove(item, 1);
                    boolean didPush = false;
                    for (Building neighbor : proximity) {
                        if (!(neighbor instanceof NeoplasiaBuild n)) continue;
                        if (!n.acceptItem(this, item)) continue;
                        n.handleItem(this, item);
                        didPush = true;
                        break;
                    }
                    if (!didPush) {
                        items.add(item, 1);
                        break;
                    }
                }
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(activity);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            activity = read.f();
        }
    }
}
