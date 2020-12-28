import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSlider;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.Timer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.Hashtable;

public class Lebensform extends JFrame implements ActionListener {

    private int width = 800;
    private int height = 500;
    Timer timer;
    JButton startButton;
    JButton stopButton;
    JButton clearButton;
    JButton stepButton;
    JSlider slider;
    JLabel speedLabel;
    Petridish petridish;
    Menubar menuBar;
    CustomMouseAdapter eventListener = new CustomMouseAdapter();

    Lebensform() {
        this.setLayout(new BorderLayout());
        this.setTitle("Lebensform");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.timer = new Timer(1000, this);
        this.menuBar = new Menubar();
        this.startButton = new JButton("Start");
        this.stopButton = new JButton("Stop");
        this.clearButton = new JButton("Clear");
        this.stepButton = new JButton("Step");
        this.slider = new JSlider(JSlider.HORIZONTAL, 0, 20, 1);
        this.speedLabel = new JLabel(Integer.toString(slider.getValue()));

        this.startButton.addMouseListener(eventListener);
        this.stopButton.addMouseListener(eventListener);
        this.clearButton.addMouseListener(eventListener);
        this.stepButton.addMouseListener(eventListener);

        Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
        labelTable.put(0, new JLabel("0"));
        labelTable.put(5, new JLabel("500"));
        labelTable.put(10, new JLabel("1000"));
        labelTable.put(15, new JLabel("1500"));
        labelTable.put(20, new JLabel("2000"));

        this.slider.setMajorTickSpacing(5);
        this.slider.setMinorTickSpacing(1);
        this.slider.setPaintTicks(true);
        this.slider.setPaintLabels(true);
        this.slider.setLabelTable(labelTable);
        this.slider.setSnapToTicks(true);

        this.slider.addChangeListener(new CustomeChangeListener());

        this.menuBar.add(startButton);
        this.menuBar.add(stopButton);
        this.menuBar.add(stepButton);
        this.menuBar.add(clearButton);
        this.menuBar.add(slider);
        this.menuBar.add(speedLabel);

        this.petridish = new Petridish(width, height, 20);
        this.petridish.addMouseListener(eventListener);
        this.petridish.addMouseMotionListener(new CustomMouseMotionAdapter());

        this.add(menuBar, BorderLayout.PAGE_START);
        this.add(petridish, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == timer) {
            petridish.update();
        }
    }

    class CustomMouseAdapter extends MouseAdapter {

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getSource() == startButton) {
                timer.setInitialDelay(slider.getValue());
                timer.setDelay(slider.getValue() * 100);
                timer.start();
            } else if (e.getSource() == stopButton) {
                timer.stop();
            } else if (e.getSource() == clearButton) {
                petridish.clearAll();
            } else if (e.getSource() == stepButton) {
                petridish.update();
            } else if (e.getSource() == petridish) {
                petridish.toggleState(e.getX(), e.getY());
            }
        }
    }

    class CustomeChangeListener implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() == slider) {
                int delay = slider.getValue() * 100;
                speedLabel.setText(Integer.toString(delay));
                timer.setInitialDelay(delay);
                timer.setDelay(delay);
                if (timer.isRunning()) {
                    timer.restart();
                }
            }
        }
    }

    class CustomMouseMotionAdapter extends MouseMotionAdapter{

        @Override
        public void mouseDragged(MouseEvent e) {
           if (e.getSource() == petridish){
               petridish.setAlive(e.getX(), e.getY());
           }
        }
    }
}
