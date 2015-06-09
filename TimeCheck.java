package BulletProject;

/**
 * Created by Joft on 2015-06-09.
 */

import java.util.TimerTask;

public class TimeCheck extends TimerTask{

    private int i=1;

    public void run() {

        if(i%1500==0) if(GameFrame.fireSpeed>1) GameFrame.fireSpeed-=2; // 15초마다 실행
        if(i%3000==0) if(GameFrame.bulletSpeed<10) GameFrame.bulletSpeed+=1; // 30초마다 실행

        GameFrame.time[0] += 1;	// 0.01초
        if(GameFrame.time[0]==10){ // 0.1초
            GameFrame.time[0] = 0;
            GameFrame.time[1] += 1;

            if(GameFrame.time[1]==10){ // 1초
                GameFrame.time[1] = 0;
                GameFrame.time[2] += 1;

                if(GameFrame.time[2]==10){ // 10초
                    GameFrame.time[2] = 0;
                    GameFrame.time[3] += 1;

                    if(GameFrame.time[3]==6){ // 1분
                        GameFrame.time[3] = 0;
                        GameFrame.time[4] += 1;

                        if(GameFrame.time[4]==10){ // 10분
                            GameFrame.time[4] = 0;
                            GameFrame.time[5] += 1;

                            if(GameFrame.time[5]==6){ // 1시간
                                System.exit(0);
                            }
                        }
                    }
                }

            }
        }

        i++;
        if(i==1000000000) i=0;
    }
}
