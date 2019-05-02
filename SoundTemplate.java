import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
public class SoundTemplate extends JFrame implements Runnable, ActionListener
{
	int rows = 15;
	int cols = 10;

	JToggleButton button[][] = new JToggleButton[rows][cols];
	JPanel panel = new JPanel();
	AudioClip soundClip[] = new AudioClip[15];
	boolean notStopped = true;
	JFrame frame = new JFrame();
	JMenuBar menubar;
	JMenu size, pbsongs, ucsongs;
	JMenuItem add, remove, twinkle;
	JButton clear, random;
	Thread timing;
	boolean paused=false;
	public SoundTemplate()
	{
		this.setLayout(new BorderLayout());
		try
		{
			soundClip[0] = JApplet.newAudioClip(new URL("file:C6.wav"));
			soundClip[1] = JApplet.newAudioClip(new URL("file:B5.wav"));
			soundClip[2] = JApplet.newAudioClip(new URL("file:A5.wav"));
			soundClip[3] = JApplet.newAudioClip(new URL("file:G5.wav"));
			soundClip[4] = JApplet.newAudioClip(new URL("file:F5.wav"));
			soundClip[5] = JApplet.newAudioClip(new URL("file:E5.wav"));
			soundClip[6] = JApplet.newAudioClip(new URL("file:D5.wav"));
			soundClip[7] = JApplet.newAudioClip(new URL("file:C5.wav"));
			soundClip[8] = JApplet.newAudioClip(new URL("file:B4.wav"));
			soundClip[9] = JApplet.newAudioClip(new URL("file:A4.wav"));
			soundClip[10] = JApplet.newAudioClip(new URL("file:G4.wav"));
			soundClip[11] = JApplet.newAudioClip(new URL("file:F4.wav"));
			soundClip[12] = JApplet.newAudioClip(new URL("file:E4.wav"));
			soundClip[13] = JApplet.newAudioClip(new URL("file:D4.wav"));
			soundClip[14] = JApplet.newAudioClip(new URL("file:C4.wav"));
		}catch(MalformedURLException mue)
		{
			System.out.println("File not found");
		}
		panel.setLayout(new GridLayout(rows,cols,10,10));
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				button[i][j] = new JToggleButton();
				panel.add(button[i][j]);
			}
		}

		menubar = new JMenuBar();
		size = new JMenu ("Size");
		add = new JMenuItem("Add Column");
	 	remove = new JMenuItem("Remove Column");
		size.add(add);
		size.add(remove);
		menubar.add(size);
		pbsongs = new JMenu ("Pre-Built Songs");
		twinkle = new JMenuItem ("Twinkle Twinkle Little Star");
		pbsongs.add(twinkle);
		menubar.add(pbsongs);
		ucsongs = new JMenu ("User Songs");
		menubar.add(ucsongs);
		clear = new JButton ("Clear");
		random = new JButton ("Random");
		clear.addActionListener(this);
		random.addActionListener(this);
		menubar.add(clear);
		menubar.add(new JLabel("  "));
		menubar.add(random);

		this.add(menubar, BorderLayout.NORTH);
		this.add(panel, BorderLayout.CENTER);
		setSize(rows*30,cols*60);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timing = new Thread(this);
		timing.start();
	}

	public void clear(){
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				button[i][j].setSelected(false);
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == clear) {
			clear();
		}
		if (e.getSource() == random) {
			clear();
			for (int i = 0; i < rows; i++){
				for (int j = 0; j < cols; j++){
					if (Math.random() < 0.5)
						button[i][j].setSelected(true);
				}
			}
		}
	}

	public void run()
	{
		do {
			try {
				if(!paused)
				{
					for (int j = 0; j < cols; j++){
						for (int i = 0; i < rows; i++) {
							if(button[i][j].isSelected()){
								soundClip[i].stop();
								soundClip[i].play();
							}
						}
						timing.sleep(500);
					}
				}
				timing.sleep(0);
			}
			catch(InterruptedException e){
			}
		} while(notStopped);
	}

	public static void main(String args[])
	{
		SoundTemplate app=new SoundTemplate();

	}
}