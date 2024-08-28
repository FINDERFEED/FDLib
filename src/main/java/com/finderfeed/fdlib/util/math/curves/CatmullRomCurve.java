package com.finderfeed.fdlib.util.math.curves;

import com.mojang.datafixers.util.Pair;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

//Not tested, same for all curves
public class CatmullRomCurve extends MultipointCurve {

    private List<Pair<Vector3f, Vector3f>> neighbors;

    public CatmullRomCurve(Collection<Vector3f> points) {
        super(points);
        neighbors = new ArrayList<>();
        for (int i = 0; i < this.points.size();i++){
            neighbors.add(generateNeighbors(
                    this.getPoint(i - 1),this.getPoint(i),this.getPoint(i + 1)
            ));
        }
    }


    @Override
    public void addPoint(int index, Vector3f point) {
        if (index == 0){
            var nn = generateNeighbors(point,points.get(0),points.get(1));
            var nc = generateNeighbors(null,point,points.get(0));
            neighbors.set(0,nn);
            neighbors.add(0,nc);
        }else if (index == this.points.size()){
            var nn = generateNeighbors(points.get(points.size() - 2),points.get(points.size() - 1),point);
            var nc = generateNeighbors(points.get(points.size()-1),point,null);
            neighbors.set(neighbors.size() - 1,nn);
            neighbors.add(nc);
        }else{
            var np = generateNeighbors(
                    this.getPoint(index - 2),
                    this.getPoint(index - 1),
                    point
            );
            var nc = generateNeighbors(
                    this.getPoint(index - 1),
                    point,
                    this.getPoint(index)
            );
            var nn = generateNeighbors(
                    point,
                    this.getPoint(index),
                    this.getPoint(index + 1)
            );
            neighbors.set(index - 1,np);
            neighbors.set(index,nn);
            neighbors.add(index,nc);
        }
        this.points.add(point);
    }

    @Override
    public Vector3f getLocalCurvePoint(int pointIndex, float percent) {
        var ns1 = this.neighbors.get(pointIndex);
        var ns2 = this.neighbors.get(pointIndex + 1);
        Vector3f n1 = this.points.get(pointIndex);
        Vector3f n2 = ns1.getSecond();
        Vector3f n3 = ns2.getFirst();
        Vector3f n4 = this.points.get(pointIndex + 1);
        return new Vector3f(
                bernstein(n1.x,n2.x,n3.x,n4.x,percent),
                bernstein(n1.y,n2.y,n3.y,n4.y,percent),
                bernstein(n1.z,n2.z,n3.z,n4.z,percent)
        );
    }

    /*
     "Speeds" are already sanitized so don't care about actual catmullrom
     */
    private float bernstein(float x1,float x2,float x3,float x4,float t){
        float t3 = (float) Math.pow(t,3);
        float t2 = (float) Math.pow(t,2);
        return x1 * (-t3 + 3*t2 - 3*t  + 1) +
                x2 * (3*t3 - 6*t2 + 3*t) +
                x3 * (-3*t3 + 3*t2) +
                x4 * t3;
    }


    private Pair<Vector3f,Vector3f> generateNeighbors(Vector3f previous,Vector3f current,Vector3f next){

        if (previous == null){
            Vector3f b = next.sub(current,new Vector3f()).mul(2/3f);
            Vector3f n2 = current.add(b,new Vector3f());
            Vector3f n1 = current.add(b.mul(-1,new Vector3f()),new Vector3f());
            return new Pair<>(n1,n2);
        }else if (next == null){
            Vector3f b = current.sub(previous,new Vector3f()).mul(2/3f);
            Vector3f n2 = current.add(b,new Vector3f());
            Vector3f n1 = current.add(b.mul(-1,new Vector3f()),new Vector3f());
            return new Pair<>(n1,n2);
        }else{
            Vector3f b = next.sub(previous,new Vector3f()).mul(1/3f);
            Vector3f n2 = current.add(b,new Vector3f());
            Vector3f n1 = current.add(b.mul(-1,new Vector3f()),new Vector3f());
            return new Pair<>(n1,n2);
        }

    }
}
