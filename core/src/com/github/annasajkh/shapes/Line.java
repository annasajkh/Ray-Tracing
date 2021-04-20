package com.github.annasajkh.shapes;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;

public class Line
{
	public Vector2 a;
	public Vector2 b;

	public Line(Vector2 a, Vector2 b)
	{
		this.a = a;
		this.b = b;
	}
	
    private Vector2 lineIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4)
    {

        float uA = ((x4 - x3) * (y1 - y3) - (y4 - y3) * (x1 - x3)) 
        			/ ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));
        
        float uB = ((x2 - x1) * (y1 - y3) - (y2 - y1) * (x1 - x3)) 
        			/ ((y4 - y3) * (x2 - x1) - (x4 - x3) * (y2 - y1));

        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1)
        {
            return new Vector2(x1 + (uA * (x2 - x1)), y1 + (uA * (y2 - y1)));
        }
        return null;
    }

    public Vector2 getIntersectionPoint(Line other)
    {
        return lineIntersection(a.x, a.y, b.x, b.y, other.a.x, other.a.y, other.b.x, other.b.y);
    }
	
	public void draw(ShapeRenderer shapeRenderer)
	{
		shapeRenderer.rectLine(a,b,2);
	}

}
