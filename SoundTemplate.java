import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
public class SoundTemplate extends JFrame implements Runnable, ActionListener
{
	int rows = 15;
	int cols = 25;

	JToggleButton button[][] = new JToggleButton[rows][cols];
	JPanel panel = new JPanel();
	JScrollPane scrollPane;
	AudioClip soundClip[] = new AudioClip[15];
	boolean notStopped = true;
	JMenuBar menubar;
	JMenu size, pbsongs;
	JMenuItem add, remove, twinkle, under, love;
	JButton clear, random, save, load, pause;
	Thread timing;
	boolean paused;
	String[] names={"C6","B5","A5","G5","F5","E5","D5","C5","B4","A4","G4","F4","E4","D4","C4"};
	int colCounter = 0;
	public SoundTemplate()
	{
		this.setLayout(new BorderLayout());
		try
		{
			for(int x=0;x<names.length;x++)
				soundClip[x] = JApplet.newAudioClip(new URL("file:"+names[x]+".wav"));

		}catch(MalformedURLException mue)
		{
			System.out.println("File not found");
		}
		panel.setLayout(new GridLayout(rows,cols,10,10));
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				button[i][j] = new JToggleButton();
				button[i][j].setPreferredSize(new Dimension(10,10));
				button[i][j].setMargin(new Insets(0, 0, 0, 0));
				button[i][j].setText(names[i]);
				button[i][j].setBorder(new LineBorder(Color.BLACK, 2));//BorderFactory.createStrokeBorder(new BasicStroke(2.0f)));
				panel.add(button[i][j]);
			}
		}
		paused = true;
		scrollPane=new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		menubar = new JMenuBar();
		size = new JMenu ("Size");
		add = new JMenuItem("Add Column");
		add.addActionListener(this);
	 	remove = new JMenuItem("Remove Column");
	 	remove.addActionListener(this);
		size.add(add);
		size.add(remove);
		menubar.add(size);
		pbsongs = new JMenu ("Pre-Built Songs");
		twinkle = new JMenuItem ("Twinkle Twinkle Little Star");
		twinkle.addActionListener(this);
		under = new JMenuItem ("Under the Sea");
		under.addActionListener(this);
		love = new JMenuItem ("Can't Help Falling in Love");
		love.addActionListener(this);
		pbsongs.add(twinkle);
		pbsongs.add(under);
		pbsongs.add(love);
		menubar.add(pbsongs);
		load = new JButton ("Load");
		load.addActionListener(this);
		menubar.add(load);
		menubar.add(new JLabel("  "));
		clear = new JButton ("Clear");
		random = new JButton ("Random");
		save = new JButton ("Save");
		pause = new JButton ("Play");
		clear.addActionListener(this);
		random.addActionListener(this);
		save.addActionListener(this);
		pause.addActionListener(this);
		menubar.add(random);
		menubar.add(new JLabel("  "));
		menubar.add(save);
		menubar.add(new JLabel("  "));
		menubar.add(clear);
		menubar.add(new JLabel("  "));
		menubar.add(pause);
		this.add(menubar, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setPreferredSize(new Dimension(1200,800));
		this.setSize(1200,800);//cols*60,rows*30);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		timing = new Thread(this);
		timing.start();
	}

	public void clear(){
		for (int i = 0; i < rows; i++){
			for (int j = 0; j < cols; j++){
				button[i][j].setSelected(false);
				button[i][j].setBorder(new LineBorder(Color.BLACK, 2));
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
		if (e.getSource() == add){
			resizePanel(rows, cols+1);
		}
		if (e.getSource() == remove){
			resizePanel(rows, cols-1);
		}
		if (e.getSource() == twinkle){
			System.out.println ("twinkle");
			preSelected("Song1.txt");
		}
		if (e.getSource() == under){
			System.out.println ("under");
			preSelected("Song2.txt");
		}
		if (e.getSource() == love){
			System.out.println ("love");
			preSelected("Song3.txt");
		}
		if (e.getSource() == pause){
			if (paused) {
				paused = false;
				pause.setText ("Pause");
			}
			else {
				paused = true;
				pause.setText ("Play");
			}

		}
		if (e.getSource() == save){
			String fileName = JOptionPane.showInputDialog("Enter the file name.");
			String notes = "";
			for (int i = 0; i < rows; i++){
				for (int j = 0; j < cols; j++){
					if (button[i][j].isSelected())
						notes+="X";
					else
						notes+="O";
				}
				notes+="\n";
		 	}
		 	try {
				BufferedWriter OutputStream = new BufferedWriter(new FileWriter(fileName+".txt"));
				OutputStream.write(notes);
				OutputStream.close();
			} catch (IOException io){
			}
		}
		if (e.getSource() == load){
			System.out.println ("load");
			colCounter = 0;
			JFileChooser chooser = new JFileChooser();
			if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
				File f = chooser.getSelectedFile();
				String filePath = f.getPath();
				try {
					preSelected (f.getName());
				} catch (Exception ex){
				}
			}
		}
	}

	public void preSelected(String songText){
		String textInput;
		colCounter = 0;
		try {
			BufferedReader input = new BufferedReader(new FileReader(songText));
			textInput=input.readLine();
			System.out.println(button.length+" "+button[0].length);
			resizePanel (rows, textInput.length());
			int row = 0;
			while(textInput!= null)
			{
				String[] notes = textInput.split("");
				for (int i = 0; i < notes.length; i++){
					if (notes[i].equals("X"))
						button[row][i].setSelected(true);
				}
				row++;
				textInput=input.readLine();
			}

		}
		catch (IOException io)
		{
			System.err.println("File does not exist");
		}
	}

	public void resizePanel(int r, int c){
		rows = r;
		cols = c;
		this.remove(scrollPane);
		panel = new JPanel();
		panel.setLayout(new GridLayout(r,c,10,10));
		panel.setPreferredSize(new Dimension(c*50,r*40));
		button=new JToggleButton[r][c];
		System.out.println(button.length+" "+button[0].length);
		for (int i = 0; i < r; i++){
			for (int j = 0; j < c; j++){
				button[i][j] = new JToggleButton();
				button[i][j].setPreferredSize(new Dimension(10,10));
				button[i][j].setMargin(new Insets(0, 0, 0, 0));
				button[i][j].setText(names[i]);
				panel.add(button[i][j]);
				button[i][j].setBorder(new LineBorder(Color.BLACK, 2));
			}
		}
		scrollPane = new JScrollPane(panel);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.add(scrollPane, BorderLayout.CENTER);
		this.revalidate();
	}

	public void run()
	{
		do {
			try {
				while (!paused && colCounter < cols) {
						for (int i = 0; i < rows; i++) {
							if(button[i][colCounter]!=null && button[i][colCounter].isSelected()){
								soundClip[i].stop();
								soundClip[i].play();
								button[i][colCounter].setBorder(new LineBorder(Color.RED, 2));
							}
						}
						timing.sleep(500);
						for (int i = 0; i < rows; i++)
							button[i][colCounter].setBorder(new LineBorder(Color.BLACK, 2));
						colCounter++;
						if (colCounter == cols)
							colCounter = 0;
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