package aquarion.world.graphics;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.content.Items;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.entities.effect.MultiEffect;
import mindustry.entities.effect.ParticleEffect;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.units.UnitAssembler.*;

import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import mindustry.entities.*;
import mindustry.entities.abilities.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.units.UnitAssembler.*;

import static arc.graphics.g2d.Draw.rect;
import static arc.graphics.g2d.Draw.*;
import static arc.graphics.g2d.Lines.*;
import static arc.math.Angles.*;
import static mindustry.Vars.*;
public class AquaFx {
    //I tried... I couldnt
    public static final Rand rand = new Rand();
    public static final Vec2 v = new Vec2();

    public static final Effect strutBulletTrail = new Effect(16, e -> {
        color(Color.white, e.color, e.fin());
        stroke(0.5f + e.fout() * 1.2f);
        rand.setSeed(e.id);

        for(int i = 0; i < 2; i++){
            float rot = e.rotation + rand.range(90f) + 180f;
            v.trns(rot, rand.random(e.fin() * 27f));
            lineAngle(e.x + v.x, e.y + v.y, rot, e.fout() * rand.random(2f, 7f) + 1.5f);
        }
    }),
            shootHori = new Effect(12, e -> {
                color(Color.white, Pal.lightOrange, e.fin());
                float w = 1.2f + 7 * Interp.pow2Out.apply(e.fout());
                Drawf.tri(e.x, e.y, w/1.5f, 30f * e.fout(), e.rotation - 90);
                Drawf.tri(e.x, e.y, w/1.5f, 5f * e.fout(), e.rotation - 90f);
                Drawf.tri(e.x, e.y, w/1.5f, 30f * e.fout(), e.rotation + 90);
                Drawf.tri(e.x, e.y, w/1.5f * e.fout(), 5f * e.fout(), e.rotation + 90f);
                Drawf.tri(e.x, e.y, w, 45f * Interp.pow5Out.apply(e.fout()), e.rotation);
                Drawf.tri(e.x, e.y, w, 5f * Interp.pow5Out.apply(e.fout()), e.rotation + 180);
            }),
    shootLong = new Effect(12, e -> {
        color(Color.white, Pal.lightOrange, e.fin());
        float w = 1.2f + 7 * e.fout();
        Drawf.tri(e.x, e.y, w, 45f * e.fout(), e.rotation);
        Drawf.tri(e.x, e.y, w, 5f * e.fout(), e.rotation + 180f);
    }),
            ionizing = new Effect(35f, e -> {
                color(Color.valueOf("ffab84"), Color.valueOf("ba3838"), e.fin());

                randLenVectors(e.id, 3, 2f + e.fin() * 7f, (x, y) ->
                    Fill.poly(e.x + x, e.y + y, 4, 0.1f + e.fout() * 1.4f)
                );
            }),
            t1TrailZoarcid = new MultiEffect(
                    new ParticleEffect(){{
                        //it's too much of a pain to do the usual fx
                        lifetime = 35;
                        sizeFrom = 3f;
                        baseRotation = 180;
                        rotWithParent = true;
                        sizeTo = 0;
                        particles = 3;
                        colorFrom = Color.valueOf("181b1c");
                        colorTo = Color.valueOf("181b1c");
                        sizeInterp = Interp.pow10Out;
                        randLength = true;
                        length = 14;
                        interp = Interp.linear;
                        cone = 12;
                        layer = Layer.flyingUnitLow - 2f;
                    }},
                    new ParticleEffect(){{
                        lifetime = 30;
                        sizeFrom = 2f;
                        baseRotation = 180;
                        rotWithParent = true;
                        sizeTo = 0;
                        particles = 3;
                        colorFrom = Color.valueOf("2e3235");
                        colorTo = Color.valueOf("2e3235");
                        randLength = true;
                        length = 8;
                        interp = Interp.linear;
                        sizeInterp = Interp.pow10Out;
                        cone = 8;
                        layer = Layer.flyingUnitLow - 1.9f;
                    }},
                    new ParticleEffect(){{
                        lifetime = 30;
                        sizeFrom = 1f;
                        sizeTo = 0;
                        particles = 2;
                        baseRotation = 180;
                        rotWithParent = true;
                        colorFrom = Color.valueOf("6d89dd");
                        colorTo = Color.valueOf("444b5e");
                        randLength = true;
                        length = 4;
                        interp = Interp.linear;
                        sizeInterp = Interp.pow10Out;
                        cone = 6;
                        layer = Layer.flyingUnitLow - 1.8f;
                    }}

            ),
            t2TrailAnguilli = new MultiEffect(
                    new ParticleEffect(){{
                        //it's too much of a pain to do the usual fx
                        lifetime = 90;
                        sizeFrom = 10f;
                        baseRotation = 180;
                        rotWithParent = true;
                        sizeTo = 0;
                        particles = 3;
                        colorFrom = Color.valueOf("181b1c");
                        colorTo = Color.valueOf("181b1c");
                        sizeInterp = Interp.pow10Out;
                        randLength = false;
                        length = 18;
                        interp = Interp.linear;
                        cone = 13;
                        layer = Layer.flyingUnitLow - 2f;
                    }},
                    new ParticleEffect(){{
                        lifetime = 70;
                        sizeFrom = 6f;
                        rotWithParent = true;
                        sizeTo = 0;
                        particles = 3;
                        baseRotation = 180;
                        colorFrom = Color.valueOf("2e3235");
                        colorTo = Color.valueOf("2e3235");
                        randLength = true;
                        length = 10;
                        interp = Interp.linear;
                        sizeInterp = Interp.pow10Out;
                        cone = 9;
                        layer = Layer.flyingUnitLow - 1.9f;
                    }},
                    new ParticleEffect(){{
                        lifetime = 50;
                        sizeFrom = 4f;
                        sizeTo = 0;
                        particles = 2;
                        baseRotation = 180;
                        rotWithParent = true;
                        colorFrom = Color.valueOf("6d89dd");
                        colorTo = Color.valueOf("444b5e");
                        randLength = true;
                        length = 8;
                        interp = Interp.linear;
                        sizeInterp = Interp.pow10Out;
                        cone =7;
                        layer = Layer.flyingUnitLow - 1.8f;
                    }}

            ),
    fomentShootSmoke = new MultiEffect(new Effect(85f, e -> {
        color(Color.gray, Color.black, e.color, e.fin());

        randLenVectors(e.id, 9, Interp.pow5Out.apply(e.finpow() )* 25f, e.rotation, 35f, (x, y) -> {
                    Draw.alpha(0.5f);
                    Fill.circle(e.x + x / Interp.pow2Out.apply(e.fout()), e.y + y / Interp.pow2Out.apply(e.fout()), Interp.pow2In.apply(e.fout()) * 4f + 0.1f);
                    Draw.alpha(1);
                }
        );

    }),new Effect(45f, e -> {
        color(Color.white, AquaPal.fireLight1, Color.black, e.fin());

        randLenVectors(e.id, 12, Interp.pow5Out.apply(e.finpow() )* 25f, e.rotation, 26f, (x, y) ->
            Fill.circle(e.x + x, e.y + y, Interp.pow2Out.apply(e.fout())  * 2.5f + 0.1f)
        );

    }), new Effect(40, e->{
        color(AquaPal.fireLight2, AquaPal.fireLight1, e.color, e.fin());
        stroke(2f *e.fout());
        rand.setSeed(e.id);

        for(int i = 0; i < 6; i++){
            float rot = e.rotation + rand.range(34f);
            v.trns(rot, rand.random(e.fin() * 27f));
            lineAngle(e.x + v.x, e.y + v.y, rot, Interp.pow2In.apply(e.fout() )* rand.random(4f, 12f) + 2.5f);
        }
    })),
            fomentHitColor = new Effect(18, e -> {
                color(Color.white, e.color, e.fin());

                e.scaled(7f, s -> {
                    stroke(0.5f + s.fout());
                    Lines.circle(e.x, e.y, s.fin() * 5f);
                });
                color(Color.gray, Color.black, e.color, e.fin());
                stroke(0.5f + e.fout());

                randLenVectors(e.id, 5, e.fin() * 17f, (x, y) -> {
                    Fill.circle(e.x + x, e.y + y, e.fout() * 4.2f);
                });
                color(AquaPal.fireLight2, AquaPal.fireLight1, e.color, e.fin());
                randLenVectors(e.id + 1, 7, e.fin() * 17f, (x, y) -> {
                    Fill.poly(e.x + x, e.y + y,  3, Interp.pow2Out.apply(e.fout()) * 2.2f, rand.random(360f));
                });
                Drawf.light(e.x, e.y, 20f, e.color, 0.6f * e.fout());
            }),
    shootSmokeFormentGallium = new Effect(35f, e -> {
        color(Color.white, e.color, e.fin());

        randLenVectors(e.id, 6, e.finpow() * 29f, e.rotation, 26f, (x, y) ->
            Fill.circle(e.x + x, e.y + y, e.fout() * 2f + 0.1f)
        );
    }),
            smallShockwave = new Effect(25f, 80f, e -> {
                color(e.color, Color.lightGray , e.fin());
                stroke(e.fout() * 2f + 0.4f);
                Lines.circle(e.x, e.y, e.fin() * 18f);
            }).layer(Layer.debris),

            pentagonShootSmoke = new Effect(65f, e -> {
                color(Color.valueOf("ffbfc8"),Color.valueOf("e8586c"), e.fin());

                rand.setSeed(e.id);
                for(int i = 0; i < 3; i++){
                    float rot = e.rotation + rand.range(15f);
                    v.trns(rot, rand.random(e.finpow() * 15f));
                    float randomRotationSpeed = rand.random(180f, 360f);
                    float slowRotation = Interp.pow2In.apply(randomRotationSpeed * e.fout());
                    Fill.poly(e.x + v.x, e.y + v.y, 5, e.fout() * 2.5f, rand.random(700f) + slowRotation);
                }
            }),
                    heatEngineGenerate = new Effect(180f, e -> {
                        color(Pal.lightOrange, AquaPal.smoke, Interp.pow2In.apply(e.fin()));
                        alpha(Interp.pow2In.apply(e.fout()));
                        randLenVectors(e.id, 4,Interp.pow5In.apply(e.finpow()) * 56f, 32f, 26f, (x, y) ->
                                Fill.circle(e.x + x, e.y + y, Interp.pow5In.apply(e.finpow()) * 3.5f + 0.1f)
                        );
                    }),
                    hydroxideReactorGenerate = new Effect(220f, e -> {
                        color(Pal.sap, AquaPal.smoke, Interp.pow2In.apply(e.fin()));
                        alpha(Interp.pow2In.apply(e.fout()));
                        randLenVectors(e.id, 5,Interp.pow5In.apply(e.finpow()) * 70f, 32f, 28f, (x, y) ->
                                Fill.circle(e.x + x, e.y + y, Interp.pow5In.apply(e.finpow()) * 5f + 0.1f)
                        );
                    }),
            GyreShootSmoke = new Effect(48, e -> {
                color(Color.valueOf("e8586c"), e.fin());
                stroke(0.8f + e.fout() * 2.7f);
                rand.setSeed(e.id);

                for(int i = 0; i < 2; i++){
                    float rot = e.rotation + rand.range(15f);
                    v.trns(rot, rand.random(e.fin() * 32f));
                    lineAngle(e.x + v.x, e.y + v.y, rot, e.fout() * rand.random(6f, 7f) + 1.5f);
                }
            }),

    shootSmokeTri = new Effect(45f, e -> {
        color(e.color, e.color, e.fin());

        rand.setSeed(e.id);
        for(int i = 0; i < 3; i++){
            float rot = e.rotation + rand.range(35f);
            v.trns(rot, rand.random(e.finpow() * 30f));
            Fill.poly(e.x + v.x, e.y + v.y, 3, e.fout() * 3.8f + 0.2f, rand.random(360f));
        }
    }),
                    SiliconHearthSmoke = new Effect(65f, e ->
                        randLenVectors(e.id, 0.65f + e.fin(), 6, 14.3f, (x, y, fin, out) -> {
                            color(Color.darkGray, Pal.coalBlack, e.finpowdown());
                            Fill.circle(e.x + x, e.y + y, out * 8.5f + 0.45f);
                        })
                    ),
                    cuproNickelSmeltSmoke = new Effect(190f, e -> {
                        color(Color.valueOf("df9c887f"));
                        rand.setSeed(e.id);
                        for(int i = 0; i < 3; i++){
                            float len = rand.random(12f);

                            e.scaled(e.lifetime * rand.random(0.45f, 1f), b ->
                                Fill.circle(e.x + v.x, e.y + v.y, 3.5f * b.fslope() + 0.4f)
                            );
                        }
                    }),
            parzilDebrisSmall = new Effect(85, e -> {
                rand.setSeed(e.id);
                color( Color.valueOf("ffffff"), Color.valueOf("ffffff").a(e.fin()), e.fin());

                for (int i = 0; i < 25; i++) {
                    float rot = e.rotation + rand.range(360f); // Similar to shootSmokeSquare
                    int regionId = rand.random(1, 3);
                    TextureRegion region = Core.atlas.find("aquarion-parzil-debris" + regionId);
                    v.trns(rot, rand.random(e.finpow() * 21f));
                    float fout = Math.max(e.fout(), 0.1f);
                    float size = fout * 24f + 0.2f;
                    Draw.rect(region, e.x + v.x, e.y + v.y, size, size, rand.random(45) + Interp.pow2In.apply(rand.random(10f, 20f) * e.fout()));
                }
            }).layer(Layer.debris),
            parzilDebrisLarge = new Effect(120, e -> {
                rand.setSeed(e.id);
                color( Color.valueOf("ffffff"), Color.valueOf("ffffff").a(e.fin()), e.fin());

                for (int i = 0; i < 40; i++) {
                    float rot = e.rotation + rand.range(360f);
                    int regionId = rand.random(1, 3);
                    TextureRegion region = Core.atlas.find("aquarion-parzil-debris" + regionId);
                    v.trns(rot, rand.random(e.finpow() * 30f));
                    float fout = Math.max(e.fout(), 0.1f);
                    float size = fout * 28f + 0.2f;
                    Draw.rect(region, e.x + v.x, e.y + v.y, size, size, rand.random(45) + Interp.pow2In.apply(rand.random(10f, 20f) * e.fout()));
                }
            }).layer(Layer.debris),
            azuriteSmelt = new Effect(45, e -> {
                color(Color.valueOf("9eb8f5"), Color.lightGray, e.fin());
                randLenVectors(e.id, 4, e.fin() * 5f, (x, y) ->
                    Fill.square(e.x + x, e.y + y, e.fout() + 0.5f, 0)
                );
            });

}