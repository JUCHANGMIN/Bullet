package BulletProject;

/**
 * Created by Joft on 2015-06-09.
 */

import java.awt.Point;

public class Bullet {

    Point displayPoint;
    Point point;	//총알의 계산 좌표
    int angle;		//총알의 진행 방향 (각도)
    int speed;		//총알의 이동 속도


    Bullet(int x, int y, int angle, int speed){

        point = new Point(x,y);
        displayPoint = new Point(x/100,y/100);
        this.angle = angle;
        this.speed = speed;
    }

    public void move(){

        point.x += ( speed * Math.sin( Math.toRadians( angle-90 ) ) * 100 );
        point.y += ( speed * Math.cos( Math.toRadians( angle-90 ) ) * 100 );
        displayPoint.x = point.x / 100;
        displayPoint.y = point.y / 100;

    }
}
