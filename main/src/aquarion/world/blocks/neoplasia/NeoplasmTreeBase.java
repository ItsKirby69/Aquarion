package aquarion.world.blocks.neoplasia;

import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.Reads;
import arc.util.io.Writes;
import mindustry.content.UnitTypes;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.game.Team;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.world.Tile;
import mindustry.world.meta.Env;

import static mindustry.Vars.tilesize;
import static mindustry.Vars.world;

public class NeoplasmTreeBase extends GenericNeoplasiaBlock {
    public UnitType unitType = UnitTypes.flare;
    public float unitGrowTime = 10f;
    public float podCost = 100f;
    public float branchCost = 200f;
    public int maxPodsPerBranch = 2;
    public ItemStack[] unitItemCost;
    public float minVisualSize = 12f;
    public float maxVisualSize = 44f;
    public int[] maxBranchesByLevel = {5, 8, 4};
    public float[] branchLengthByLevel = {36f, 22f, 12f};
    public float[] branchThicknessByLevel = {2.5f, 1.6f, 0.8f};
    public float spacing = 5f;

    public NeoplasmTreeBase(String name) {
        super(name);
        shouldEmptyUpgrade = false;
        shouldEmpty2Upgrade = false;
        envDisabled = Env.none;
        hasItems = true;
    }

    @Override
    public boolean canPlaceOn(Tile tile, Team team, int rotation) {
        if (tile.floor().isDeep()) return false;
        if (!tile.floor().isFloor()) return false;
        for (int dx = -(int) spacing; dx <= spacing; dx++) {
            for (int dy = -(int) spacing; dy <= spacing; dy++) {
                Tile other = world.tile(tile.x + dx, tile.y + dy);
                if (other != null && other.build instanceof NeoplasmTreeBaseBuild) return false;
            }
        }
        return true;
    }

    public class LeafPod {
        float position;
        float progress;
        int side;

        void write(Writes write) {
            write.f(position);
            write.f(progress);
            write.b(side);
        }

        void read(Reads read) {
            position = read.f();
            progress = read.f();
            side = read.b();
        }
    }

    public class BranchNode {
        int level;
        float angle, length, thickness, phase, attachPos;
        int parentIndex;
        Seq<LeafPod> pods = new Seq<>();

        BranchNode(int level, float angle, float length, float thickness, float phase, float attachPos, int parentIndex) {
            this.level = level;
            this.angle = angle;
            this.length = length;
            this.thickness = thickness;
            this.phase = phase;
            this.attachPos = attachPos;
            this.parentIndex = parentIndex;
        }

        void write(Writes write) {
            write.b(level);
            write.f(angle);
            write.f(length);
            write.f(thickness);
            write.f(phase);
            write.f(attachPos);
            write.s(parentIndex);
            write.b(pods.size);
            for (LeafPod pod : pods) pod.write(write);
        }

        void read(Reads read) {
            level = read.b();
            angle = read.f();
            length = read.f();
            thickness = read.f();
            phase = read.f();
            attachPos = read.f();
            parentIndex = read.s();
            int pc = read.b();
            pods.clear();
            for (int i = 0; i < pc; i++) {
                LeafPod pod = new LeafPod();
                pod.read(read);
                pods.add(pod);
            }
        }
    }

    public class NeoplasmTreeBaseBuild extends NeoplasiaBuild {
        public Seq<BranchNode> branches = new Seq<>();
        public float treeAngle = Mathf.random(360f);

        @Override
        public void updateTile() {
            super.updateTile();
            growBranches();
            managePods();
            tickPods();
        }

        void growBranches() {
            float s = Mathf.clamp(amount / maxAmount);
            int targetL0 = Math.min(maxBranchesByLevel[0], (int) (s * maxBranchesByLevel[0]));
            while (countBranches(0) < targetL0 - 1 && amount >= branchCost * 2f) {
                amount -= branchCost * 2f;
                int idx = countBranches(0);
                float base = treeAngle + 360f / maxBranchesByLevel[0] * idx;
                float spread = 20f + Mathf.random(10f);
                float len = branchLengthByLevel[0] * (0.7f + Mathf.random(0.6f));
                float thick = branchThicknessByLevel[0] * (0.8f + Mathf.random(0.4f));
                branches.add(new BranchNode(0, base - spread, len, thick, Mathf.random(360f), 0f, -1));
                branches.add(new BranchNode(0, base + spread, len, thick, Mathf.random(360f), 0f, -1));
            }
            if (s > 0.4f) {
                for (int i = 0; i < branches.size; i++) {
                    BranchNode p = branches.get(i);
                    if (p.level != 0) continue;
                    int targetL1 = Math.min(maxBranchesByLevel[1] / maxBranchesByLevel[0], 1 + (int) ((s - 0.4f) * 3f));
                    int haveL1 = countChildren(i);
                    while (haveL1 < targetL1 - 1 && amount >= branchCost * 3f) {
                        amount -= branchCost * 3f;
                        float spread = 25f + Mathf.random(15f);
                        float base = p.angle + Mathf.random(-10f, 10f);
                        float len = branchLengthByLevel[1] * (0.6f + Mathf.random(0.8f));
                        float thick = branchThicknessByLevel[1] * (0.7f + Mathf.random(0.6f));
                        float pos = 0.3f + Mathf.random(0.4f);
                        branches.add(new BranchNode(1, base - spread, len, thick, Mathf.random(360f), pos, i));
                        branches.add(new BranchNode(1, base + spread, len, thick, Mathf.random(360f), pos, i));
                        haveL1 += 2;
                    }
                }
            }
            if (s > 0.7f) {
                int l1count = countBranches(1);
                for (int i = 0; i < branches.size; i++) {
                    BranchNode p = branches.get(i);
                    if (p.level != 1) continue;
                    int targetL2 = Math.min(maxBranchesByLevel[2] / Math.max(1, l1count), 1);
                    int haveL2 = countChildren(i);
                    while (haveL2 < targetL2 - 1 && amount >= branchCost * 4f) {
                        amount -= branchCost * 4f;
                        float spread = 20f + Mathf.random(20f);
                        float base = p.angle + Mathf.random(-10f, 10f);
                        float len = branchLengthByLevel[2] * (0.5f + Mathf.random(1f));
                        float thick = branchThicknessByLevel[2] * (0.6f + Mathf.random(0.8f));
                        float pos = 0.5f + Mathf.random(0.4f);
                        branches.add(new BranchNode(2, base - spread, len, thick, Mathf.random(360f), pos, i));
                        branches.add(new BranchNode(2, base + spread, len, thick, Mathf.random(360f), pos, i));
                        haveL2 += 2;
                    }
                }
            }
        }

        int countBranches(int level) {
            int c = 0;
            for (BranchNode b : branches) if (b.level == level) c++;
            return c;
        }

        int countChildren(int parentIndex) {
            int c = 0;
            for (BranchNode b : branches) if (b.parentIndex == parentIndex) c++;
            return c;
        }

        void managePods() {
            for (BranchNode branch : branches) {
                while (branch.pods.size < maxPodsPerBranch && amount >= podCost) {
                    amount -= podCost;
                    LeafPod pod = new LeafPod();
                    pod.position = 0.2f + Mathf.random(0.7f);
                    pod.progress = 0f;
                    pod.side = Mathf.randomBoolean() ? 1 : -1;
                    branch.pods.add(pod);
                }
            }
        }

        float[] leafPos(BranchNode branch, LeafPod pod) {
            float[] ep = branchEndpoints(branch);
            float bx = ep[0], by = ep[1], tx = ep[2], ty = ep[3];
            float alongX = bx + (tx - bx) * pod.position;
            float alongY = by + (ty - by) * pod.position;
            float branchAngle = Mathf.atan2(ty - by, tx - bx) * Mathf.radDeg + 90f;
            float leafSway = Mathf.sin(Time.time / 60f + pod.position * 10f + branch.phase) * 2f;
            float offset = 3f + leafSway;
            float px = alongX + Mathf.cosDeg(branchAngle) * offset * pod.side;
            float py = alongY + Mathf.sinDeg(branchAngle) * offset * pod.side;
            return new float[]{px, py};
        }

        void tickPods() {
            for (BranchNode branch : branches) {
                for (int pi = 0; pi < branch.pods.size; pi++) {
                    LeafPod pod = branch.pods.get(pi);
                    pod.progress += delta();
                    if (pod.progress >= unitGrowTime) {
                        pod.progress = 0f;
                        float[] lp = leafPos(branch, pod);
                        float px = lp[0], py = lp[1];
                        if (unitItemCost != null) {
                            boolean hasAll = true;
                            for (ItemStack stack : unitItemCost) {
                                if (items.get(stack.item) < stack.amount) { hasAll = false; break; }
                            }
                            if (!hasAll) continue;
                            for (ItemStack stack : unitItemCost) {
                                items.remove(stack.item, stack.amount);
                            }
                        }
                        float angle = Mathf.random(360f);
                        float dist = Mathf.random(2f, 6f);
                        unitType.spawn(team, px + Mathf.cosDeg(angle) * dist, py + Mathf.sinDeg(angle) * dist);
                    }
                }
            }
        }

        float[] branchEndpoints(BranchNode branch) {
            float sway = Mathf.sin(Time.time / 80f + branch.phase) * 4f;
            float a = branch.angle + sway;
            float bx, by;
            if (branch.parentIndex < 0) {
                bx = x;
                by = y;
            } else {
                BranchNode parent = branches.get(branch.parentIndex);
                float pa = parent.angle + Mathf.sin(Time.time / 80f + parent.phase) * 4f;
                float pl = parent.length * branchScale(parent);
                bx = x + Mathf.cosDeg(pa) * pl * parent.attachPos;
                by = y + Mathf.sinDeg(pa) * pl * parent.attachPos;
            }
            float len = branch.length * branchScale(branch);
            float tx = bx + Mathf.cosDeg(a) * len;
            float ty = by + Mathf.sinDeg(a) * len;
            return new float[]{bx, by, tx, ty};
        }

        float branchScale(BranchNode branch) {
            return 0.5f + 0.5f * Mathf.clamp(amount / maxAmount);
        }

        @Override
        public void draw() {
            float s = Mathf.clamp(amount / maxAmount);
            float drawSize = minVisualSize + (maxVisualSize - minVisualSize) * s;
            float saved = baseSize;
            baseSize = drawSize;
            super.draw();
            baseSize = saved;
            Color col = block().colTo;
            for (BranchNode branch : branches) {
                float[] ep = branchEndpoints(branch);
                float bx = ep[0], by = ep[1], tx = ep[2], ty = ep[3];
                float thick = branch.thickness * branchScale(branch);
                Lines.stroke(thick, col);
                Lines.line(bx, by, tx, ty);
                for (LeafPod pod : branch.pods) {
                    if (pod.progress <= 0f) continue;
                    float[] lp = leafPos(branch, pod);
                    float px = lp[0], py = lp[1];
                    float podProgress = Mathf.clamp(pod.progress / unitGrowTime);
                    float podSize = 1.5f + podProgress * 4f;
                    Draw.color(podProgress < 0.8f ? col : Color.scarlet);
                    Fill.circle(px, py, podSize);
                }
            }
            Draw.color();
            Lines.stroke(1f);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.s(branches.size);
            for (BranchNode branch : branches) branch.write(write);
            write.f(treeAngle);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            int bc = read.s();
            branches.clear();
            for (int i = 0; i < bc; i++) {
                BranchNode branch = new BranchNode(0, 0f, 0f, 0f, 0f, 0f, -1);
                branch.read(read);
                branches.add(branch);
            }
            treeAngle = read.f();
        }
    }
}
