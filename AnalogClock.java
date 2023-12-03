import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

public class AnalogClock 
{

    public static void main(String[] args) 
    {
        JFrame frame = new JFrame("Аналоговые Часы");
        ClockPanel clockPanel = new ClockPanel();

        JButton addHour = new JButton("Час +");
        JButton subtractHour = new JButton("Час -");
        JButton addMinute = new JButton("Минута +");
        JButton subtractMinute = new JButton("Минута -");

        addHour.addActionListener(e -> clockPanel.changeTime(1, 0));
        subtractHour.addActionListener(e -> clockPanel.changeTime(-1, 0));
        addMinute.addActionListener(e -> clockPanel.changeTime(0, 1));
        subtractMinute.addActionListener(e -> clockPanel.changeTime(0, -1));

        JPanel buttonsPanel = new JPanel(new GridLayout(1, 4));
        buttonsPanel.add(addHour);
        buttonsPanel.add(subtractHour);
        buttonsPanel.add(addMinute);
        buttonsPanel.add(subtractMinute);

        frame.add(clockPanel, BorderLayout.CENTER);
        frame.add(buttonsPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 450);
        frame.setVisible(true);
    }
}

class ClockPanel extends JPanel 
{
    private Calendar calendar;

    public ClockPanel() 
    {
        calendar = Calendar.getInstance();
        new Timer(1000, e -> repaint()).start(); // Таймер для обновления часов каждую секунду
    }

    public void changeTime(int hours, int minutes) 
    {
        calendar.add(Calendar.HOUR, hours);
        calendar.add(Calendar.MINUTE, minutes);
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) 
    {
        super.paintComponent(g);

        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        int height = getHeight();
        int width = getWidth();
        int clockRadius = Math.min(width, height) / 2 - 10;
        int centerX = width / 2;
        int centerY = height / 2;

        g.setColor(Color.BLACK);
        g.drawOval(centerX - clockRadius, centerY - clockRadius, 2 * clockRadius, 2 * clockRadius);

        Font font = new Font("Arial", Font.BOLD, 16);
        g.setFont(font);
        for (int i = 1; i <= 12; i++) {
            String number = Integer.toString(i);
            double angle = Math.toRadians(i * 30 - 90);
            int numX = (int) (centerX + (clockRadius - 30) * Math.cos(angle)) - g.getFontMetrics().stringWidth(number) / 2;
            int numY = (int) (centerY + (clockRadius - 30) * Math.sin(angle)) + g.getFontMetrics().getHeight() / 4;
            g.drawString(number, numX, numY);
        }

        double hourAngle = Math.toRadians((hours % 12 + minutes / 60.0) * 30 - 90);
        double minuteAngle = Math.toRadians(minutes * 6 - 90);

        Graphics2D g2d = (Graphics2D) g;
        Stroke oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(2));
        drawHand(g2d, centerX, centerY, hourAngle, clockRadius / 2, Color.BLACK); // Часовая стрелка
        drawHand(g2d, centerX, centerY, minuteAngle, clockRadius * 2 / 3, Color.BLACK); // Минутная стрелка
        g2d.setStroke(oldStroke);
    }

    private void drawHand(Graphics2D g, int x, int y, double angle, int length, Color color) 
    {
        int endX = (int) (x + length * Math.cos(angle));
        int endY = (int) (y + length * Math.sin(angle));
        g.setColor(color);
        g.drawLine(x, y, endX, endY);
    }
}

