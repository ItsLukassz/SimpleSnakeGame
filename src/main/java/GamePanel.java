import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 40;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
    static final int DELAY = 140;
    final int x[] = new int[GAME_UNITS];
    final int y[] = new int[GAME_UNITS];
    int bodyParts = 6;
    int foodEaten = 0;
    int foodX;
    int foodY;
    char direction = 'R';
    Boolean running = false;
    Timer timer;
    Random random;

    // constructor
    GamePanel() {

        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.GRAY);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();

    }

    public void startGame() {
        spawnFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {

        super.paintComponent(g);
        draw(g);

    }

    public void draw(Graphics g) {

        // checking if the game is running
        if (running) {
            // food color and shape

            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);

            // Snake Body color and shape

            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                } else {
                    g.setColor(new Color(48, 12, 12));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }

            }

            // Score text
            g.setColor(Color.WHITE);
            g.setFont(new Font("Ink Free", Font.BOLD, 25));
            // metrics are used for trying to center the text on the x axis
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score: "+ foodEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+ foodEaten))/2, g.getFont().getSize());
        }
        // if not we're displaying the game over screen
        else{
            gameOver(g);
        }

    }

    public void spawnFood() {
        // randomly spawning food
        // generating x and y randomly
        foodX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
        foodY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;

    }

    public void move() {
        // moving snak
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // setting the directions
        switch (direction) {
            case 'U' -> y[0] = y[0] - UNIT_SIZE;
            case 'D' -> y[0] = y[0] + UNIT_SIZE;
            case 'L' -> x[0] = x[0] - UNIT_SIZE;
            case 'R' -> x[0] = x[0] + UNIT_SIZE;
        }

    }

    public void checkFood() {
        // checking food
        if ((x[0] == foodX) && (y[0] == foodY)) {
            bodyParts++;
            foodEaten++;
            spawnFood();
        }

    }

    public void checkCollisions() {

        for (int i = bodyParts; i > 0; i--) {
            // checks if head colides with any body part
            if ((x[0] == x[i]) && (y[0] == y[i])) {
                running = false;
            }
            //check if head touches left border
            if (x[0] < 0) {
                running = false;
            }
            //check if head touches right border
            if (x[0] > SCREEN_WIDTH) {
                running = false;
            }
            //check if touches top border
            if (y[0] < 0) {
                running = false;
            }
            //check if touches bottom border
            if (y[0] > SCREEN_HEIGHT) {
                running = false;
            }
            // stopping the timer if the snak collided with a border or body part
            if (!running) {
                timer.stop();
            }
        }


    }

    public void gameOver(Graphics g) {
        // Score text
        g.setColor(Color.WHITE);
        g.setFont(new Font("Ink Free", Font.BOLD, 25));
        FontMetrics metrics2 = getFontMetrics(g.getFont());
        g.drawString("Score: "+ foodEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: "+ foodEaten))/2, g.getFont().getSize());
        // Game Over Text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Game Over!", (SCREEN_WIDTH - metrics.stringWidth("Game Over!"))/2, SCREEN_HEIGHT/2);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // checking if the game is running
        if (running) {
            move();
            checkFood();
            checkCollisions();

        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            // deffining keybinds
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                case KeyEvent.VK_A:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                case KeyEvent.VK_D:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                case KeyEvent.VK_W:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                case KeyEvent.VK_S:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }

        }
    }
}
