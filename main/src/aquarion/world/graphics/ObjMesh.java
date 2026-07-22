package aquarion.world.graphics;

import arc.Core;
import arc.files.Fi;
import arc.graphics.Mesh;
import arc.graphics.VertexAttribute;
import arc.math.geom.Mat3D;
import arc.struct.FloatSeq;
import arc.struct.IntSeq;
import arc.struct.ObjectMap;
import arc.util.Log;
import arc.math.Rand;
import mindustry.Vars;
import mindustry.graphics.Shaders;
import mindustry.graphics.g3d.PlanetMesh;
import mindustry.graphics.g3d.PlanetParams;
import mindustry.type.Planet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ObjMesh extends PlanetMesh {

    public ObjMesh(Planet planet, String objPath) {
        this(planet, objPath, 1f);
    }

    public ObjMesh(Planet planet, String objPath, float scale) {
        this.planet = planet;
        this.shader = Shaders.planet;
        this.mesh = loadObj(planet, objPath, scale);
    }

    private static final int[] STATION_COLORS = {
            0x8da6ab,
            0x697d85,
            0x333f4b,
            0x25303a
    };

    private static Mesh loadObj(Planet planet, String path, float scale) {
        Fi file = Core.files.internal(path);
        if (!file.exists()) {
            file = Vars.tree.get(path);
        }
        if (!file.exists()) {
            Log.warn("ObjMesh: file not found: " + path);
            return null;
        }

        Log.info("ObjMesh: loading " + path);

        FloatSeq positions = new FloatSeq();
        IntSeq indices = new IntSeq();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.read()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");
                    positions.add(Float.parseFloat(parts[1]) * scale);
                    positions.add(Float.parseFloat(parts[2]) * scale);
                    positions.add(Float.parseFloat(parts[3]) * scale);
                } else if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");
                    for (int i = 2; i < parts.length - 1; i++) {
                        indices.add(parseIndex(parts[1], positions.size / 3));
                        indices.add(parseIndex(parts[i], positions.size / 3));
                        indices.add(parseIndex(parts[i + 1], positions.size / 3));
                    }
                }
            }
        } catch (Exception e) {
            Log.err("ObjMesh: error loading " + path, e);
            return null;
        }

        if (positions.size == 0) {
            Log.warn("ObjMesh: no vertices in " + path);
            return null;
        }

        int triCount = indices.size / 3;
        Log.info("ObjMesh: " + (positions.size / 3) + " vertices, " + triCount + " triangles");

        ObjectMap<Long, IntSeq> edgeFaces = new ObjectMap<>();
        for (int t = 0; t < triCount; t++) {
            int a = indices.get(t * 3), b = indices.get(t * 3 + 1), c = indices.get(t * 3 + 2);
            addEdge(edgeFaces, a, b, t);
            addEdge(edgeFaces, b, c, t);
            addEdge(edgeFaces, c, a, t);
        }

        float[][] triColors = new float[triCount][3];
        float[][] smoothed = new float[triCount][3];
        Rand rand = new Rand(indices.hashCode());

        for (int t = 0; t < triCount; t++) {
            int rgb = STATION_COLORS[rand.random(STATION_COLORS.length - 1)];
            triColors[t][0] = ((rgb >> 16) & 0xFF) / 255f;
            triColors[t][1] = ((rgb >> 8) & 0xFF) / 255f;
            triColors[t][2] = (rgb & 0xFF) / 255f;
        }

        List<IntSeq> adj = new ArrayList<>(triCount);
        for (int t = 0; t < triCount; t++) {
            adj.add(new IntSeq());
        }
        for (ObjectMap.Entry<Long, IntSeq> entry : edgeFaces) {
            IntSeq faces = entry.value;
            for (int i = 0; i < faces.size; i++) {
                for (int j = i + 1; j < faces.size; j++) {
                    adj.get(faces.get(i)).add(faces.get(j));
                    adj.get(faces.get(j)).add(faces.get(i));
                }
            }
        }

        int iterations = 5;
        for (int iter = 0; iter < iterations; iter++) {
            for (int t = 0; t < triCount; t++) {
                smoothed[t][0] = triColors[t][0];
                smoothed[t][1] = triColors[t][1];
                smoothed[t][2] = triColors[t][2];
            }
            for (int t = 0; t < triCount; t++) {
                IntSeq neighbors = adj.get(t);
                if (neighbors.size == 0) continue;
                float r = triColors[t][0], g = triColors[t][1], b = triColors[t][2];
                for (int n = 0; n < neighbors.size; n++) {
                    int nb = neighbors.get(n);
                    r += triColors[nb][0];
                    g += triColors[nb][1];
                    b += triColors[nb][2];
                }
                int count = neighbors.size + 1;
                smoothed[t][0] = r / count;
                smoothed[t][1] = g / count;
                smoothed[t][2] = b / count;
            }
            float[][] tmp = triColors;
            triColors = smoothed;
            smoothed = tmp;
        }

        int vertexSize = 3 + 3 + 1;
        float[] vertices = new float[triCount * 3 * vertexSize];

        for (int t = 0; t < triCount; t++) {
            float color = new arc.graphics.Color(triColors[t][0], triColors[t][1], triColors[t][2], 1f).toFloatBits();

            for (int v = 0; v < 3; v++) {
                int vi = indices.get(t * 3 + v);
                int offset = (t * 3 + v) * vertexSize;
                float x = positions.get(vi * 3);
                float y = positions.get(vi * 3 + 1);
                float z = positions.get(vi * 3 + 2);

                vertices[offset] = x;
                vertices[offset + 1] = y;
                vertices[offset + 2] = z;

                float len = (float) Math.sqrt(x * x + y * y + z * z);
                if (len > 0) {
                    vertices[offset + 3] = x / len;
                    vertices[offset + 4] = y / len;
                    vertices[offset + 5] = z / len;
                } else {
                    vertices[offset + 3] = 0;
                    vertices[offset + 4] = 1;
                    vertices[offset + 5] = 0;
                }

                vertices[offset + 6] = color;
            }
        }

        Mesh mesh = new Mesh(true, triCount * 3, 0,
                VertexAttribute.position3, VertexAttribute.normal, VertexAttribute.color);

        mesh.setVertices(vertices);

        return mesh;
    }

    private static int parseIndex(String part, int vCount) {
        String indexStr = part.split("/")[0];
        int idx = Integer.parseInt(indexStr);
        return idx < 0 ? vCount + idx : idx - 1;
    }

    private static void addEdge(ObjectMap<Long, IntSeq> edgeFaces, int a, int b, int face) {
        long key = ((long) Math.min(a, b) << 32) | (long) Math.max(a, b);
        Long boxed = key;
        IntSeq list = edgeFaces.get(boxed);
        if (list == null) {
            list = new IntSeq();
            edgeFaces.put(boxed, list);
        }
        list.add(face);
    }

    @Override
    public void preRender(PlanetParams params) {
        if (!(shader instanceof Shaders.PlanetShader)) return;
        Shaders.PlanetShader s = (Shaders.PlanetShader) shader;
        s.planet = planet;
        s.emissive = planet.generator != null && planet.generator.isEmissive();
        s.lightDir.set(planet.solarSystem.position).sub(planet.position).rotate(arc.math.geom.Vec3.Y, planet.getRotation()).nor();
        s.ambientColor.set(planet.solarSystem.lightColor);
    }

    @Override
    public void render(PlanetParams params, Mat3D projection, Mat3D transform) {
        if (mesh == null || mesh.isDisposed()) return;
        arc.graphics.GL20 gl = arc.Core.gl;
        boolean wasCulling = gl.glIsEnabled(arc.graphics.GL20.GL_CULL_FACE);
        if (wasCulling) gl.glDisable(arc.graphics.GL20.GL_CULL_FACE);
        super.render(params, projection, transform);
        if (wasCulling) gl.glEnable(arc.graphics.GL20.GL_CULL_FACE);
    }
}
