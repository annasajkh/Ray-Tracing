package com.github.annasajkh.entities;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.github.annasajkh.Game;
import com.github.annasajkh.GameObject;
import com.github.annasajkh.shapes.Line;

public class Player extends GameObject
{

	public Vector2 velocity = new Vector2();

	private float rotation;
	public static float size = 20;
	public static float speed = 100;
	public static float friction = 0.3f;
	
	float far = 5;
	float near = 0;

	private Vector2[] vertices;
	Line[] rays;
	int fov = 25;
	float pointSpawnDst = 0.1f; 
	float maxDistance;
	
	public Player(Vector2 position)
	{
		super(position);
		vertices = new Vector2[3];
		
		vertices[0] = position.cpy().add(new Vector2(1, 0).rotateDeg(rotation).scl(size));
		vertices[1] = position.cpy().add(new Vector2(1, 0).rotateDeg(135 + rotation).scl(size));
		vertices[2] = position.cpy().add(new Vector2(1, 0).rotateDeg(225 + rotation).scl(size));
//		
//		rays = new Line[fov * 2];
//		
//		for(int i = 0; i < rays.length; i++)
//		{
//			rays[i] = new Line(new Vector2(),new Vector2());
//		}
		
	}

	@Override
	public void update()
	{
		rotation = new Vector2(Game.mousePos3D.x,Game.mousePos3D.y).sub(position).angleDeg(Vector2.X);

		vertices[0] = position.cpy().add(new Vector2(1, 0).rotateDeg(rotation).scl(size));
		vertices[1] = position.cpy().add(new Vector2(1, 0).rotateDeg(135 + rotation).scl(size));
		vertices[2] = position.cpy().add(new Vector2(1, 0).rotateDeg(225 + rotation).scl(size));
		
		position.x += velocity.x * Gdx.graphics.getDeltaTime(); 
		position.y += velocity.y * Gdx.graphics.getDeltaTime();
		
		position.x = MathUtils.clamp(position.x,1,Gdx.graphics.getWidth()/2 - 1);
		position.y = MathUtils.clamp(position.y,1,Gdx.graphics.getHeight() - 1);
		
		rays = new Line[(int)(fov * 2 / pointSpawnDst)];
		
		for(int i = 0; i < rays.length; i++)
		{
			rays[i] = new Line(position,position.cpy().add(new Vector2(1,0).rotateDeg(rotation + (i - (rays.length / 2)) * pointSpawnDst).scl(Gdx.graphics.getWidth())));
			List<Vector2> intersectionPoints = new ArrayList<>();

			
			for(Line wall : Game.walls)
			{
				Vector2 result = wall.getIntersectionPoint(rays[i]);
				
				if(result != null)
				{
					intersectionPoints.add(result);
				}
			}
			
			if(!intersectionPoints.isEmpty())
			{
				Vector2 nearestIntersectionPoint = intersectionPoints.get(0);
						
				for(Vector2 intersectionPoint : intersectionPoints)
				{
					if(position.dst2(intersectionPoint) < position.dst2(nearestIntersectionPoint))
					{
						nearestIntersectionPoint= intersectionPoint;
					}
				}
				
				rays[i].b = nearestIntersectionPoint;
				
			}
			
		}
	}

	@Override
	public void draw(ShapeRenderer shapeRenderer)
	{
		float maxDistance = (float)Math.sqrt(Math.pow(Gdx.graphics.getWidth() * 0.5f, 2) + Math.pow(Gdx.graphics.getHeight(),2));
	
		shapeRenderer.setColor(Color.GREEN);
		
		for(Line ray : rays)
		{
			ray.draw(shapeRenderer);
		}

		shapeRenderer.setColor(Color.WHITE);
		
		for(int i = 0; i < rays.length; i++)
		{

			float halfHeight = Gdx.graphics.getHeight() * 0.5f;
			float heightDivN = Gdx.graphics.getHeight() * 0.25f;

			
			float distance = 	MathUtils.map(	0,maxDistance * maxDistance,
								near,heightDivN - far,rays[i].a.dst2(rays[i].b));
			
			
			shapeRenderer.rectLine(	Gdx.graphics.getWidth() * 0.5f + i,

									halfHeight + (distance - heightDivN),
									
									Gdx.graphics.getWidth() * 0.5f + i,
									
									halfHeight + (heightDivN - distance),
									
									1);
		}
		
		shapeRenderer.triangle(vertices[0].x, vertices[0].y, vertices[1].x, vertices[1].y, vertices[2].x,
				vertices[2].y);
	}

}
