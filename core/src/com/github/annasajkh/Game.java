package com.github.annasajkh;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.annasajkh.entities.Player;
import com.github.annasajkh.shapes.Line;

public class Game extends ApplicationAdapter
{
	
	ShapeRenderer shapeRenderer;
	OrthographicCamera camera;
	public static Vector3 mousePos3D;
	Player player;
	public static List<Line> walls;
	
	public List<Line> createRectangle(float x, float y, float width, float height)
	{
		List<Line> lines = new ArrayList<>(4);
		
		lines.add(new Line(	new Vector2(x - width / 2,y - height / 2),
							new Vector2(x + width / 2,y - height / 2)));

		lines.add(new Line(	new Vector2(x - width / 2,y - height / 2),
							new Vector2(x - width / 2,y + height / 2)));
		
		lines.add(new Line(	new Vector2(x + width / 2,y + height / 2),
							new Vector2(x + width / 2,y - height / 2)));

		lines.add(new Line(	new Vector2(x + width / 2,y + height / 2),
							new Vector2(x - width / 2,y + height / 2)));
		
		return lines;
		
	}
	
	@Override
	public void create()
	{
		shapeRenderer = new ShapeRenderer();
		camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		camera.position.x = Gdx.graphics.getWidth() / 2;
		camera.position.y = Gdx.graphics.getHeight() / 2;
		camera.update();
		player = new Player(new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2));
		walls = new ArrayList<>();

		walls.add(new Line(new Vector2(Gdx.graphics.getWidth()/2,0),new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight())));
		walls.add(new Line(new Vector2(0,Gdx.graphics.getHeight()),new Vector2(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight())));
		
		walls.add(new Line(new Vector2(0,0),new Vector2(Gdx.graphics.getWidth()/2,0)));
		walls.add(new Line(new Vector2(0,0),new Vector2(0,Gdx.graphics.getHeight())));
		
		walls.addAll(createRectangle(Gdx.graphics.getWidth()/8,Gdx.graphics.getHeight()/2,100,100));

		walls.addAll(createRectangle(Gdx.graphics.getWidth()/8 * 3,Gdx.graphics.getHeight()/2,100,100));
	}
	
	public void getInput()
	{
		if(Gdx.input.isKeyPressed(Keys.D))
		{
			player.velocity.x += Player.speed;
		}
		else if(Gdx.input.isKeyPressed(Keys.A))
		{
			player.velocity.x -= Player.speed;
		}
		
		if(Gdx.input.isKeyPressed(Keys.W))
		{
			player.velocity.y += Player.speed;
		}
		else if(Gdx.input.isKeyPressed(Keys.S))
		{
			player.velocity.y -= Player.speed;
		}
		
		player.velocity.x -= player.velocity.x * Player.friction;
		player.velocity.y -= player.velocity.y * Player.friction;
		
		
	}
	
	public void update()
	{
		mousePos3D = camera.unproject(new Vector3(Gdx.input.getX(),Gdx.input.getY(),0));
		player.update();
	}

	@Override
	public void render()
	{
		getInput();
		update();
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(Color.WHITE);
		for(Line wall : walls)
		{
			wall.draw(shapeRenderer);
		}
		player.draw(shapeRenderer);
		shapeRenderer.end();
	
	}

	@Override
	public void dispose()
	{
		
	}
}
