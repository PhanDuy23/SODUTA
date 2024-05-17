package com.mygdx.game.view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.TimeUtils;
import com.mygdx.game.SpaceGame;
import com.mygdx.game.model.Knight;
import com.mygdx.game.model.Monster;

import com.badlogic.gdx.utils.Array;
import java.util.Iterator;


public class GameScreen implements Screen {
    private SpaceGame spaceGame;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private float stateTime = 0;
    private float tile_Size = 32;

//NHÂN VẬT:
    // DI CHUYEN NHAN VAT
    public float speed;
    public Knight knight;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;
    public int roll;
    // VA CHAM
    private TiledMapTileLayer collsionLayer;
    private TmxMapLoader loader;
    private TiledMap map;
// QUÁI VẬT:
    private Array<Monster> monsters ;
    private int sprites_Counting = 0;
    private int sprites_Num = 1;
    private long timeGenBabyMonster;

    private Status_UI statusUI;
    public GameScreen(SpaceGame spaceGame) {
        this.spaceGame = spaceGame;
        batch = spaceGame.getBatch();
        shapeRenderer = spaceGame.shapeRenderer;
    }

    @Override
    public void show() {
        loader = new TmxMapLoader();
        map = loader.load("basic/map/Mini_Map.tmx");
        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
   //     camera.zoom = .8f;

        this.collsionLayer = (TiledMapTileLayer) map.getLayers().get(1);
       // System.out.println(collsionLayer.getName());
        this.speed = 250;
        this.knight = new Knight(this,tile_Size * 3,tile_Size * 3, this.speed, collsionLayer);
        monsters = new Array<Monster>();
        Monster monster = new Monster( collsionLayer, this,"vertical");
        monsters.add(monster);
        timeGenBabyMonster = (Long)TimeUtils.nanoTime();
        this.statusUI = new Status_UI(this);
    }

    float cnt = 0;
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.113f, 0.102f, 0.16f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.x = knight.getX();
        camera.position.y = knight.getY();
        camera.update();

        renderer.setView(camera);
        renderer.render();

        //update
        knight.update();
        if (TimeUtils.nanoTime() - timeGenBabyMonster >= 2000000000){
            Monster monster = new Monster(  collsionLayer, this,"vertical");
            monsters.add(monster);
            timeGenBabyMonster = (Long)TimeUtils.nanoTime();
        }
        statusUI.update();
 //       Monster monster = new Monster(  collsionLayer, this,"vertical");
        for(Monster monster : monsters){
            monster.update();
        }
        stateTime += delta;

        //draw , shape trc, batch sau.
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        batch.begin();
        knight.draw(batch, stateTime, shapeRenderer);
        for(Monster monster : monsters){
            monster.draw(batch, stateTime, shapeRenderer);
        }
        statusUI.draw(batch,shapeRenderer);
        batch.end();
        shapeRenderer.end();
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
    }


}