import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by bing on 1/14/16.
 */
public class FiveChessFrame extends JFrame implements MouseListener, Runnable{

    int width = Toolkit.getDefaultToolkit().getScreenSize().width;
    int height = Toolkit.getDefaultToolkit().getScreenSize().height;

    BufferedImage bgImage = null;

    int x = 0;
    int y = 0;

    int[][] allChess = new int[19][19];

    boolean isBlack = true;

    boolean canPlay = true;

    String message = "黑方先行";

    int maxTime = 0;

    Thread t = new Thread( this );

    int blackTime = 0;
    int whiteTime = 0;

    String blackMessage = "无限制";
    String whiteMessage = "无限制";

    public FiveChessFrame() {
        this.setTitle( "五子棋" );
        this.setSize( 500, 500 );
        this.setLocation( (width - 500) / 2, (height - 500) / 2 );
        this.setResizable( false );
        this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        this.addMouseListener( this );
        this.setVisible( true );

        t.start();
        t.suspend();

        this.repaint();
        String imagePath = "";
        try{
            imagePath = System.getProperty( "user.dir" ) + "/resources/background.jpg";
            bgImage = ImageIO.read( new File( imagePath.replaceAll( "\\\\", "/" ) ) );
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void paint( Graphics g ) {
        BufferedImage bufferedImage = new BufferedImage( 500, 500, BufferedImage.TYPE_3BYTE_BGR );
        Graphics g2 = bufferedImage.createGraphics();
        g2.setColor(Color.BLACK);
        g2.drawImage( bgImage, 1, 20, this );
        g2.setFont( new Font( "黑体", Font.BOLD, 20 ) );
        g2.drawString( "游戏信息： " + message, 130, 60 );
        g2.setFont( new Font( "宋体", Font.BOLD, 14 ) );
        g2.drawString( "黑方时间： " + blackMessage, 30, 470);
        g2.drawString( "白方时间： " + whiteMessage, 260, 470);
        for( int i = 0; i < 19; i ++ ){
            g2.drawLine( 10, 70+20*i, 370, 70+20*i);
            g2.drawLine( 10+20*i, 70, 10+20*i, 430);
        }
        g2.fillOval( 68, 128, 4, 4 );
        g2.fillOval( 308, 128, 4, 4 );
        g2.fillOval( 308, 368, 4, 4 );
        g2.fillOval( 68, 368, 4, 4 );
        g2.fillOval( 68, 368, 4, 4 );
        g2.fillOval( 308, 248, 4, 4 );
        g2.fillOval( 188, 128, 4, 4 );
        g2.fillOval( 68, 248, 4, 4 );
        g2.fillOval( 188, 368, 4, 4 );
        g2.fillOval( 188, 248, 4, 4 );

        for( int i = 0; i < 19; i ++ ) {
            for( int j = 0; j < 19; j ++ ) {
                if( allChess[i][j] == 1 ) {
                    g2.fillOval( i * 20 + 10 - 7, j * 20 + 70 - 7, 14, 14);
                }
                if( allChess[i][j] == 2 ) {
                    g2.setColor( Color.WHITE );
                    g2.fillOval( i * 20 + 10 - 7, j * 20 + 70 - 7, 14, 14);
                    g2.setColor( Color.BLACK );
                    g2.drawOval( i * 20 + 10 - 7, j * 20 + 70 - 7, 14, 14);
                }
            }
        }

        g.drawImage( bufferedImage, 0, 0, this );
    }

    public void mouseClicked( MouseEvent e ) {
        // TODO
    }

    public void mouseEntered( MouseEvent e ) {
        // TODO
    }

    public void mouseExited( MouseEvent e ) {
        // TODO
    }

    public void mousePressed( MouseEvent e ) {
        if( canPlay == true ) {
            x = e.getX();
            y = e.getY();
            if (x >= 10 && x <= 370 && y >= 70 && y <= 430) {
                x = (x - 10) / 20;
                y = (y - 70) / 20;
                if (allChess[x][y] == 0) {
                    if (isBlack == true) {
                        allChess[x][y] = 1;
                        isBlack = false;
                        message = "轮到白方";
                    } else {
                        allChess[x][y] = 2;
                        isBlack = true;
                        message = "轮到黑方";
                    }
                    boolean winFlag = this.checkWin();
                    if (winFlag == true) {
                        JOptionPane.showMessageDialog(this, "游戏结束，" +
                                (allChess[x][y] == 1 ? "黑方" : "白方") + "获胜！");
                        canPlay = false;
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "当前位置已经有棋子，请重新落子！");
                }
                this.repaint();
            }
        }
        if( e.getX() >= 400 && e.getX() <= 470 &&
                e.getY() >=70 && e.getY() <= 100 ) {
            int result = JOptionPane.showConfirmDialog( this, "是否重新开始游戏" );
            if( result == 0 ) {
                for( int i = 0; i < 19; i ++ ){
                    for( int j = 0; j < 19; j ++ ){
                        allChess[i][j] = 0;
                    }
                }
                message = "黑方先行";
                isBlack = true;
                blackTime = maxTime;
                whiteTime = maxTime;
                if( maxTime > 0 ) {
                    blackMessage = blackTime / 3600 + ":"
                                    + ( blackTime / 60 - blackTime / 3600 * 60 )
                                    + ( blackTime - blackTime / 60 * 60 );
                    whiteMessage = whiteTime / 3600 + ":"
                            + ( whiteTime / 60 - whiteTime / 3600 * 60 )
                            + ( whiteTime - whiteTime / 60 * 60 );
                    t.resume();
                } else {
                    blackMessage = "无限制";
                    whiteMessage = "无限制";
                }
                this.canPlay = true;
                this.repaint();
            }
        }

        if( e.getX() >= 400 && e.getX() <= 470 &&
                e.getY() >= 120 && e.getY() <= 150 ) {
            String input = JOptionPane.showInputDialog(this,
                    "请输入游戏的最大时间（单位：分钟），如果输入0，表示没有时间限制：" );
            try {
                maxTime = Integer.parseInt( input ) * 60;
                if( maxTime < 0 ) {
                    JOptionPane.showMessageDialog( this, "请输入正确信息，不允许输入负数！" );
                }
                if( maxTime == 0 ) {
                    int result = JOptionPane.showConfirmDialog( this,
                            "设置完成，是否重新开始游戏？" );
                    if( result == 0 ) {
                        for( int i = 0; i < 19; i ++ ) {
                            for( int j = 0; j < 19; j ++ ) {
                                allChess[i][j] = 0;
                            }
                        }
                        message = "黑方先行";
                        isBlack = true;
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = "无限制";
                        whiteMessage = "无限制";
                        this.canPlay = true;
                        this.repaint();
                    }
                }
                if( maxTime > 0 ) {
                    int result = JOptionPane.showConfirmDialog( this,
                            "设置完成，是否重新开始游戏？" );
                    if( result == 0 ) {
                        for (int i = 0; i < 19; i++) {
                            for (int j = 0; j < 19; j++) {
                                allChess[i][j] = 0;
                            }
                        }
                        message = "黑方先行";
                        isBlack = true;
                        blackTime = maxTime;
                        whiteTime = maxTime;
                        blackMessage = blackTime / 3600 + ":"
                                + (blackTime / 60 - blackTime / 3600 * 60)
                                + (blackTime - blackTime / 60 * 60);
                        whiteMessage = whiteTime / 3600 + ":"
                                + (whiteTime / 60 - whiteTime / 3600 * 60)
                                + (whiteTime - whiteTime / 60 * 60);
                        t.resume();
                        this.canPlay = true;
                        this.repaint();
                    }
                }
            } catch ( NumberFormatException e1 ) {
                JOptionPane.showMessageDialog( this, "请正确输入信息！" );
            }
        }

        if( e.getX() >= 400 && e.getX() <= 470 && e.getY() >= 170 &&
                e.getY() <= 200 ) {
            JOptionPane.showMessageDialog( this,
                    "这是一个五子棋游戏程序，黑白双方轮流下棋，当某一方连到五子时，游戏结束。" );
        }

        if( e.getX() >= 400 && e.getX() <= 470 && e.getY() >= 270
                && e.getY() <= 300 ) {
            int result = JOptionPane.showConfirmDialog( this, "是否确认认输？" );
            if( result == 0 ) {
                if( isBlack ) {
                    JOptionPane.showMessageDialog( this, "黑方已经认输，游戏结束！" );
                } else {
                    JOptionPane.showMessageDialog( this, "白方已经认输，游戏结束！" );
                }
                canPlay = false;
            }
        }

        if( e.getX() >= 400 && e.getX() <= 470 &&
                e.getY() >= 320 && e.getY() <= 350 ) {
            JOptionPane.showMessageDialog( this, "本游戏由我制作" );
        }

        if( e.getX() >= 400 && e.getX() <= 470 && e.getY() >= 370
                && e.getY() <= 400 ) {
            JOptionPane.showMessageDialog( this, "游戏结束" );
            System.exit( 0 );
        }
    }

    public void mouseReleased( MouseEvent e ) {
        // TODO
    }

    private boolean checkWin() {
        boolean flag = false;
        int count = 1;
        int color = allChess[x][y];

        count = this.checkCount( 1, 0, color );
        if( count >=5 ){
            flag = true;
        } else {
            count = this.checkCount( 0, 1, color );
            if( count >=5 ){
                flag = true;
            } else {
                count = this.checkCount( 1, -1, color );
                if( count >=5 ){
                    flag = true;
                } else {
                    count = this.checkCount( 1, 1, color );
                    if (count >= 5) {
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    private int checkCount( int xChange, int yChange, int color ) {
        int count = 1;
        int tempX = xChange;
        int tempY = yChange;
        while( x + xChange >= 0 && x + xChange <=18 && y + yChange >= 0 && y + yChange <=18 &&
                color == allChess[x + xChange][y + yChange]) {
            ++ count;
            if( xChange != 0 ) {
                ++ xChange;
            }
            if( yChange != 0 ) {
                if( yChange > 0 ){
                    ++ yChange;
                } else {
                    -- yChange;
                }
            }
        }
        xChange = tempX;
        yChange = tempY;
        while( x - xChange >= 0 && x - xChange <=18 && y - yChange >= 0 && y - yChange <=18 &&
                color == allChess[x - xChange][y - yChange]) {
            ++ count;
            if( xChange != 0 ) {
                ++ xChange;
            }
            if( yChange != 0 ) {
                if( yChange > 0 ){
                    ++ yChange;
                } else {
                    -- yChange;
                }
            }
        }
        return count;
    }

    public void run() {
        if( maxTime > 0 ) {
            while( true ) {
                if( isBlack ) {
                    -- blackTime;
                    if( blackTime == 0 ) {
                        JOptionPane.showMessageDialog( this, "黑方超时，游戏结束！" );
                    }
                } else {
                    -- whiteTime;
                    if( whiteTime == 0 ) {
                        JOptionPane.showMessageDialog( this, "白方超时，游戏结束！" );
                    }
                }
                blackMessage = blackTime / 3600 + ":"
                        + ( blackTime / 60 - blackTime / 3600 * 60 )
                        + ( blackTime - blackTime / 60 * 60 );
                whiteMessage = whiteTime / 3600 + ":"
                        + ( whiteTime / 60 - whiteTime / 3600 * 60 )
                        + ( whiteTime - whiteTime / 60 * 60 );
                this.repaint();
                try{
                    Thread.sleep( 1000 );
                } catch ( InterruptedException e ) {
                    e.printStackTrace();
                }
                System.out.println( blackTime + "--" + whiteTime);
            }
        }
    }
}
