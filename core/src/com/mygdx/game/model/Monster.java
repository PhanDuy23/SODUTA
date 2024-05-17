package com.mygdx.game.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.controller.CheckCollision;
import com.mygdx.game.controller.Direction;
import com.mygdx.game.controller.movement.Monster_Movement;
import com.mygdx.game.view.GameScreen;

public class Monster extends Entity{
    public GameScreen gameScreen;
    // ROLLS:
    private Texture texture_walking;
  //  private Texture texture_shooting;
   // private Texture texture_stabbing;
    private Animation[] walking;
    private TextureRegion[] idle; // Ta chỉ set 1 số ảnh để làm IDLE thôi, Ko cần 1 cái Standing riêng, vì nó sẽ bị giật giật khi chuyển qua lại các status.

    // TẤN COONG:
    public Attack_Status attackStatus;
   // private Animation[] shootting;
  //  private Animation[] stabbing;

    public Monster(TiledMapTileLayer collsionLayer, GameScreen gameScreen, String direction_Static) {
        this.gameScreen = gameScreen;

        //image
            this.texture_walking = new Texture("Apocalypse Character Pack/Zombie/Walk.png");
            //this.texture_shooting = new Texture("basic/character/Shoot.png");
            //this.texture_stabbing = new Texture("basic/character/Stab.png");
        // position
            this.setPlaceGen();
        //speed
            this.setSpeed_Stright(120);
            this.setSpeed_Cross((float) Math.sqrt(120*120 / 2));

        // first setting:
        this.direction_Static = direction_Static;
        if(direction_Static.equals("vertical")) {
            direction = Direction.DOWN;
            this.xMin = this.getX();
            this.yMin = this.getY() - 32;
            this.xMax = this.getX();
            this.yMax = this.getY() + 32;
        }
        else if(direction_Static.equals("horizontal")){
            direction = Direction.RIGHT;
            this.xMin = this.getX() - 32;
            this.yMin = this.getY();
            this.xMax = this.getX() + 32;
            this.yMax = this.getY();
        }

        status = Entity_Status.WALKING;
        this.setWidth(32);
        this.setHeight(32);
        this.setAnimation();

        //collsion:
        this.collisionLayer = collsionLayer;

        // quanr ly di chuyen
        this.moving = Monster_Movement.getInstance();

        // attack:
       // this.attackStatus = Attack_Status.STAB; // Mặc định là ban đầu sẽ chém


        //health:

    }

    private void setAnimation(){
        walking = new Animation[15];
     //   stabbing = new Animation[10];
       // shootting = new Animation[10];
        idle = new TextureRegion[15];
        TextureRegion[][] region1 = TextureRegion.split(this.texture_walking, this.getWidth(), this.getHeight());
     //   TextureRegion[][] region2 = TextureRegion.split(this.texture_stabbing, this.getWidth(), this.getHeight());
     //   TextureRegion[][] region3 = TextureRegion.split(this.texture_shooting, this.getWidth(), this.getHeight());
        for(int i = 0; i < 4; ++i){
            walking[i] = new Animation(0.2f, region1[i]);
        //    stabbing[i] = new Animation(0.2f, region2[i]);
        //    shootting[i] = new Animation(0.2f, region3[i]);

            idle[i] = region1[i][1];
        }
    }
    public void update(){
        this.moving.move(this,this.gameScreen);
    }


    public void draw(SpriteBatch batch, float stateTime, ShapeRenderer shapeRenderer){
        int index;
        if(direction == Direction.DOWN) index = 0;
        else if(direction == Direction.LEFT || direction == Direction.DOWNLEFT || direction == Direction.UPLEFT) index = 3;
        else if(direction == Direction.RIGHT || direction == Direction.DOWNRIGHT || direction == Direction.UPRIGHT) index = 2;
        else index = 1;

        //  System.out.println(this.gameScreen.knight.getX() + "-" + this.getX() + "-" + this.gameScreen.knight.screenX);
        float screenX = this.getX() - this.gameScreen.knight.getX() + this.gameScreen.knight.screenX;
        float screenY = this.getY() - this.gameScreen.knight.getY() + this.gameScreen.knight.screenY;
        drawHealthBar(shapeRenderer, stateTime, screenX, screenY, index);
        drawMonster(batch, stateTime, screenX, screenY, index);
    }
    private void drawHealthBar(ShapeRenderer shapeRenderer, float stateTime, float screenX, float screenY, int index){
        shapeRenderer.setColor(Color.RED);
        shapeRenderer.rect(screenX, screenY + this.getHeight() * 2, this.getWidth() * 2 , 8);
    }
    private void drawMonster(SpriteBatch batch, float stateTime, float screenX, float screenY, int index){
        // System.out.println("Knight: (" + this.gameScreen.knight.getX() + "," + this.gameScreen.knight.getY() + ")  " + screenX + " - " + screenY);
        if(status == Entity_Status.IDLE){
            batch.draw(idle[index], screenX, screenY,  this.getWidth()*2, this.getHeight()*2);
        }
        else if(status == Entity_Status.WALKING){
            // Khác 1 chút so với Knight, Khi knight nó luôn ở giữa màn hinhf.
            // Còn cái tk này là nó phải set dựa vào vị trí của tk knight so với bản đồ nữa. => Lại phải toán à :vvvv
            batch.draw((TextureRegion) walking[index].getKeyFrame(stateTime, true), screenX, screenY,  this.getWidth() * 2, this.getHeight() * 2);
        }
    }
    public void setPlaceGen(){
        int rong = 600, cao = 600, kc = 100; // screen
        int x = MathUtils.random(1, 4); // trái - phải
        if(x == 1){
            setPosision(kc, cao-kc);
        }else if(x == 2){
            setPosision(rong-kc, cao-kc);
        } else if(x == 3){
            setPosision(rong-kc, kc);
        }else if(x == 4){
            setPosision(kc, kc);
        }
    }
}
