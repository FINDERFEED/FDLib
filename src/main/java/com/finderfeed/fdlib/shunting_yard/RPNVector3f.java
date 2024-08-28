package com.finderfeed.fdlib.shunting_yard;

import com.finderfeed.fdlib.shunting_yard.sy_base.ExpressionContext;
import com.finderfeed.fdlib.shunting_yard.sy_base.SYStaticValue;
import org.joml.Vector3f;

import java.util.List;

public class RPNVector3f {

    private RPNExpression x;
    private RPNExpression y;
    private RPNExpression z;

    public RPNVector3f(RPNExpression x, RPNExpression y, RPNExpression z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public RPNVector3f(String x, String y, String z) {
        try {
            this.x = ShuntingYard.parse(x);
        }catch (Exception e){
            throw new RuntimeException("Error while reading expression: " + x);
        }
        try {
            this.y = ShuntingYard.parse(y);
        }catch (Exception e){
            throw new RuntimeException("Error while reading expression: " + y);
        }
        try {
            this.z = ShuntingYard.parse(z);
        }catch (Exception e){
            throw new RuntimeException("Error while reading expression: " + z);
        }
    }

    public RPNVector3f(float x,float y,float z){
        this.x = new RPNExpression(List.of(new SYStaticValue(x)));
        this.y = new RPNExpression(List.of(new SYStaticValue(y)));
        this.z = new RPNExpression(List.of(new SYStaticValue(z)));
    }


    public float getX(ExpressionContext context) {
        return x.compute(context);
    }

    public float getY(ExpressionContext context) {
        return y.compute(context);
    }

    public float getZ(ExpressionContext context) {
        return z.compute(context);
    }

    public Vector3f get(ExpressionContext context){
        return new Vector3f(
          this.getX(context),
          this.getY(context),
          this.getZ(context)
        );
    }
}
