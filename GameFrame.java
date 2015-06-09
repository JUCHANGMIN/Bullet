package BulletProject;

/**
 * Created by Joft on 2015-06-09.
 */

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;
import java.util.Timer;

public class GameFrame extends JFrame implements KeyListener, Runnable{

    private int frameWidth; // 프레임 가로 크기
    private int frameHeight; // 프레임 세로 크기

    private int userXpos; // 유저 X 좌표
    private int userYpos; // 유저 Y 좌표

    private int enemyXpos; // 적 X 좌표
    private int enemyYpos; // 적 Y 좌표

    private int cnt; // 반복문 체크용
    public static int fireSpeed; // bullet 발사 간격
    public static int bulletSpeed; // bullet 속도
    public static int[] time = new int[7]; // 시간 체크용

    private byte userHP = 3; // 유저 체력

    // 키보드 입력 체크용
    private boolean KeyUp = false;
    private boolean KeyDown = false;
    private boolean KeyLeft = false;
    private boolean KeyRight = false;

    // 유저 - bullet 체크용
    private Rectangle userRec;
    private Rectangle bulletRec;

    private Thread th;
    private Timer tm;
    private TimeCheck tc;

    private String location = "X:\\Dropbox\\Project\\IntelliJ\\JOFT\\Bullet\\img";
    private Image userImg; // 유저 이미지
    private Image bulletImg; //bullet 이미지
    private Image enemyImg; // 적 이미지
    private Image backGround; // 배경
    private Image titleMini; // 작은 타이틀
    private Image gameOver; // 게임 오버 이미지
    private Image Hp; // 체력 이미지
    private Image record; // 레코드 이미지
    private Image[] numImg = new Image[11]; // 숫자 이미지

    private ArrayList bulletList = new ArrayList(); // bullet 저장용

    // 더블 버퍼링 용
    private Image bufferImage;
    private Image bufferImageMini;
    private Graphics bufferG;
    private Graphics bufferGmini;

    private Bullet bl; // bullet 객체

    GameFrame(){

        init();
        start();
        timeProcess();

        setTitle("Bullet 피하기");
        setSize(frameWidth+100, frameHeight);
        setBackground(new Color(0).black);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        addKeyListener(this);
        setResizable(false);
        setVisible(true);
    }

    // 각종 변수 초기화
    public void init(){

        frameWidth = 500;
        frameHeight = 700;

        userXpos = 250;
        userYpos = 600;

        enemyXpos = 250;
        enemyYpos = 150;

        fireSpeed = 7;
        bulletSpeed = 4;

        for(int i=0; i<time.length; i++){
            time[i] = 0;
        }

        userImg = Toolkit.getDefaultToolkit().getImage(location + "/user.png");
        bulletImg = Toolkit.getDefaultToolkit().getImage(location + "/bullet.png");
        enemyImg = Toolkit.getDefaultToolkit().getImage(location + "/enemy.png");
        backGround = Toolkit.getDefaultToolkit().getImage(location + "/back.jpg");
        titleMini = Toolkit.getDefaultToolkit().getImage(location + "/titleMini.png");
        Hp = Toolkit.getDefaultToolkit().getImage(location + "/hp.png");
        gameOver = Toolkit.getDefaultToolkit().getImage(location + "/gameover.png");
        record = Toolkit.getDefaultToolkit().getImage(location + "/record.png");

        for(int i=0; i<numImg.length; i++){
            numImg[i] = Toolkit.getDefaultToolkit().getImage(location + "/num/" + Integer.toString(i) + ".png");
        }
    }

    // 시간 체크용
    public void timeProcess(){

        tm = new Timer();
        tc = new TimeCheck();
        tm.scheduleAtFixedRate(tc, 0, 10);
    }

    // 게임 시작
    public void start(){

        th = new Thread(this);
        th.start();
    }

    // 쓰레드 내용
    public void run(){

        try{
            while(true){

                KeyProcess();
                BulletProcess();

                repaint();

                if(userHP==0){

                    Thread.sleep(3000);
                    StartFrame sfm = new StartFrame();
                    this.dispose();
                    tm.cancel();
                    tc.cancel();
                    th.stop();
                }

                cnt++;

                Thread.sleep(10);

            }
        }catch (Exception e){ System.out.println("Thread Error"); }
    }

    // 랜덤 각도
    public int RAND(){

        int rnd;

        Random rand = new Random();

        rnd = rand.nextInt(130) + 30;

        return rnd;
    }

    // bullet 생성
    public void BulletCreate(){

        int i=1;

        if(cnt%fireSpeed==0)
        {
            bl = new Bullet(enemyXpos, enemyYpos, RAND(), bulletSpeed);
            bulletList.add(bl);
        }
    }

    // bullet 동작
    public void BulletProcess() {

        int bulletXpos;
        int bulletYpos;

        BulletCreate();

        for(int i=0; i<bulletList.size(); i++){
            bl = (Bullet) (bulletList.get(i));
            bl.move();

            bulletXpos = enemyXpos + bl.displayPoint.x;
            bulletYpos = enemyYpos + bl.displayPoint.y;

            bulletRec = new Rectangle(bulletXpos, bulletYpos, 20, 20);
            userRec = new Rectangle(userXpos, userYpos-10, 20, 30);

            if ( bulletYpos > frameHeight+10 || bulletYpos < -10 || bulletXpos > frameWidth+10 || bulletXpos < -10 )
                bulletList.remove(i);

            if(Crash(bulletXpos, bulletYpos, bulletRec, userXpos, userYpos, userRec)){
                bulletList.remove(i);
                userHP -= 1;
            }
        }
    }

    // 충돌 체크
    public boolean Crash(int x1, int y1, Rectangle rect1, int x2, int y2, Rectangle rect2){

        boolean check = false;

        if(rect1==null)
            return false;

        if(rect2==null)
            return false;

        Rectangle _rect1 = new Rectangle(x1+rect1.x, y1+rect1.y, rect1.width, rect1.height);
        Rectangle _rect2 = new Rectangle(x2+rect2.x, y2+rect2.y, rect2.width, rect2.height);

        if(	_rect1.x < (_rect2.x+_rect2.width) &&
                _rect2.x < (_rect1.x+_rect1.width) &&
                _rect1.y < (_rect2.y+_rect2.height) &&
                _rect2.y < (_rect1.y+_rect1.height) )

            check = true;

        return check;
    }


    // 각종 이미지 그리기
    public void paint(Graphics g){

        bufferImage = createImage(frameWidth, frameHeight);
        bufferG = bufferImage.getGraphics();
        bufferImageMini = createImage(100, frameHeight);
        bufferGmini = bufferImageMini.getGraphics();

        update(g);
    }

    public void update(Graphics g){

        drawBackGround();

        drawChar();

        drawEnemy();

        drawBullet();

        drawRightBar();

        drawOver();

        g.drawImage(bufferImage, 0, 0, frameWidth, frameHeight, this);
        g.drawImage(bufferImageMini, 500, 0, 100, frameHeight, this);
    }

    public void drawOver(){

        int Xpos = 150;
        int Ypos = 550;

        if(userHP==0){
            drawImageCenter(gameOver, frameWidth/2, frameHeight/2);
            drawImageCenter(record, 250, 480);
            bufferG.drawImage(numImg[time[5]], Xpos, Ypos, this);
            bufferG.drawImage(numImg[time[4]], Xpos+30, Ypos, this);
            bufferG.drawImage(numImg[10], Xpos+65, Ypos, this);
            bufferG.drawImage(numImg[time[3]], Xpos+75, Ypos, this);
            bufferG.drawImage(numImg[time[2]], Xpos+105, Ypos, this);
            bufferG.drawImage(numImg[10], Xpos+140, Ypos, this);
            bufferG.drawImage(numImg[time[1]], Xpos+150, Ypos, this);
            bufferG.drawImage(numImg[time[0]], Xpos+180, Ypos, this);
        }
    }

    public void drawRightBar(){
        drawString();
        drawTime();
        drawHp();
    }

    public void drawTime(){

        bufferGmini.setColor(new Color(0).white);
        bufferGmini.drawImage(numImg[time[5]], 20, 100, this);
        bufferGmini.drawImage(numImg[time[4]], 55, 100, this);
        bufferGmini.drawImage(numImg[time[3]], 20, 160, this);
        bufferGmini.drawImage(numImg[time[2]], 55, 160, this);
        bufferGmini.drawImage(numImg[time[1]], 20, 220, this);
        bufferGmini.drawImage(numImg[time[0]], 55, 220, this);
    }

    public void drawString(){

        bufferGmini.drawImage(titleMini, 4, 50, this);

        bufferGmini.setColor(new Color(0).white);
        bufferGmini.drawString("─ 이동 한계선", 1, 304);

        bufferGmini.drawString("Press the ESC", 7, 350);
        bufferGmini.drawString("   to EXIT   ", 20, 370);
    }

    public void drawHp(){

        int temp=0;
        for(int i=0; i<userHP; i++,temp+=60)
            bufferGmini.drawImage(Hp, 25, 630-temp, this);
    }

    public void drawImageCenter(Image img, int x, int y){

        x = x - ( img.getWidth(this) / 2 );
        y = y - ( img.getHeight(this) / 2 );

        bufferG.drawImage(img, x, y, this);
    }

    public void drawBackGround(){

        bufferG.drawImage(backGround, 0, 0, this);
        bufferG.setColor(new Color(0).red);
        bufferG.drawLine(0,300,500,300);
    }

    public void drawChar(){

        drawImageCenter(userImg, userXpos, userYpos);
    }


    public void drawBullet(){

        int bulletXpos;
        int bulletYpos;

        for (int i = 0 ; i < bulletList.size() ; i++){
            bl = (Bullet) (bulletList.get(i));

            bulletXpos = enemyXpos + bl.displayPoint.x;
            bulletYpos = enemyYpos + bl.displayPoint.y;

            drawImageCenter(bulletImg, bulletXpos, bulletYpos);
        }
    }

    public void drawEnemy(){

        drawImageCenter(enemyImg, enemyXpos, enemyYpos);

    }

    // KeyListener
    public void keyPressed(KeyEvent e){

        String anykey = null;

        switch(e.getKeyCode()){

            case KeyEvent.VK_UP :
                KeyUp = true;
                break;
            case KeyEvent.VK_DOWN :
                KeyDown = true;
                break;
            case KeyEvent.VK_LEFT :
                KeyLeft = true;
                break;
            case KeyEvent.VK_RIGHT :
                KeyRight = true;
                break;
            case KeyEvent.VK_ESCAPE :
                userHP=0;
                break;
        }
    }

    public void keyReleased(KeyEvent e){

        switch(e.getKeyCode()){

            case KeyEvent.VK_UP :
                KeyUp = false;
                break;
            case KeyEvent.VK_DOWN :
                KeyDown = false;
                break;
            case KeyEvent.VK_LEFT :
                KeyLeft = false;
                break;
            case KeyEvent.VK_RIGHT :
                KeyRight = false;
                break;
        }
    }

    public void keyTyped(KeyEvent e){}

    // 유저 움직임 제어
    public void KeyProcess(){

        if(KeyUp) if(userYpos>315) userYpos -= 3;
        if(KeyDown) if(userYpos<frameHeight-15) userYpos += 3;
        if(KeyLeft)  if(userXpos>15) userXpos -= 3;
        if(KeyRight) if(userXpos<frameWidth-15) userXpos += 3;

    }
}
