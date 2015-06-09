package BulletProject;

/**
 * Created by Joft on 2015-06-09.
 */

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class StartFrame extends JFrame implements KeyListener, Runnable{

    private int frameWidth; // 프레임 가로 크기
    private int frameHeight; // 프레임 세로 크기

    boolean keyCheck = true; // 버튼 체크용

    public Thread th; // 쓰레드

    // 더블 버퍼링 용
    private Image bufferImage;
    private Graphics bufferG;

    // 각종 이미지
    private String location = "X:\\Dropbox\\Project\\IntelliJ\\JOFT\\Bullet\\img";
    private Image backGround;
    private Image titleImg;
    private Image startImgOn;
    private Image startImgOff;
    private Image exitImgOn;
    private Image exitImgOff;

    GameFrame gfm; // 게임 화면

    StartFrame(){

        init();
        start();

        setTitle("Bullet 피하기");
        setSize(frameWidth, frameHeight);
        setLocationRelativeTo(null); // 화면 가운데
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);

        addKeyListener(this);

    }

    // 각종 변수 초기화
    public void init(){

        frameWidth = 500;
        frameHeight = 700;

        backGround = Toolkit.getDefaultToolkit().getImage(location + "/back.jpg");
        titleImg = Toolkit.getDefaultToolkit().getImage(location + "/title.png");
        startImgOn = Toolkit.getDefaultToolkit().getImage(location + "/start.png");
        startImgOff = Toolkit.getDefaultToolkit().getImage(location + "/start2.png");
        exitImgOn = Toolkit.getDefaultToolkit().getImage(location + "/exit.png");
        exitImgOff = Toolkit.getDefaultToolkit().getImage(location + "/exit2.png");
    }

    // 메인 화면 시작
    public void start(){

        th = new Thread(this);
        th.start();

    }

    // 쓰레드 내용
    public void run(){

        try{
            while(true){

                repaint();

                Thread.sleep(20);
            }
        }catch(Exception e){}
    }

    // 그리기
    public void paint(Graphics g){

        bufferImage = createImage(frameWidth, frameHeight);
        bufferG = bufferImage.getGraphics();

        update(g);

    }

    public void update(Graphics g){

        drawImageCenter(backGround, frameWidth/2, frameHeight/2);
        drawImageCenter(titleImg, 250, 150);

        startBtn();
        exitBtn();

        g.drawImage(bufferImage, 0, 0, frameWidth, frameHeight, this);

    }

    public void drawImageCenter(Image img, int x, int y){

        x = x - ( img.getWidth(this) / 2 );
        y = y - ( img.getHeight(this) / 2 );

        bufferG.drawImage(img, x, y, this);
    }

    public void startBtn(){

        if(keyCheck) drawImageCenter(startImgOn, 250, 450);
        else  drawImageCenter(startImgOff, 250, 450);
    }

    public void exitBtn(){

        if(!keyCheck) drawImageCenter(exitImgOn, 250, 550);
        else  drawImageCenter(exitImgOff, 250, 550);
    }

    // KeyListener
    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub
        switch(e.getKeyCode()){
            case KeyEvent.VK_UP: keyCheck=true; break;
            case KeyEvent.VK_DOWN: keyCheck=false ; break;
            case KeyEvent.VK_ENTER: if(keyCheck){ this.dispose();  gfm = new GameFrame();} else { System.exit(0);} th.stop(); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }
}
