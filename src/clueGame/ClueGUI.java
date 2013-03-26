package clueGame;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ClueGUI extends JFrame {

	private JPanel contentPane;
	private JPanel turnPane;
	private JPanel diePane;
	private JPanel guessPane;
	private JPanel guessResultPane;
	private JTextField turn;
	private JTextField dieRoll = new JTextField(3);
	private JTextField guess = new JTextField(30);
	private JTextField guessResult = new JTextField(10);
	private JButton nextPlayer = new JButton("Next player");
	private JButton makeAccusation = new JButton("Make an accusation");
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClueGUI frame = new ClueGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ClueGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 670, 300);
		setTitle("Clue Control GUI");
		contentPane = new JPanel();
		turnPane = new JPanel();
		diePane = new JPanel();
		guessPane = new JPanel();
		guessResultPane = new JPanel();
		turnPane.setLayout(new BorderLayout());
		diePane.setLayout(new FlowLayout());
		guessPane.setLayout(new BorderLayout());
		guessResultPane.setLayout(new FlowLayout());
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new FlowLayout());
		setContentPane(contentPane);
		
		JLabel turnLabel = new JLabel("Whose turn?");
		turn = new JTextField(20);
		nextPlayer.setPreferredSize(new Dimension(160,40));
		makeAccusation.setPreferredSize(new Dimension(160,40));
		
		turnPane.add(turnLabel, BorderLayout.NORTH);
		turnPane.add(turn, BorderLayout.CENTER);
		contentPane.add(turnPane);
		contentPane.add(nextPlayer);
		contentPane.add(makeAccusation);
		
		diePane.add(new JLabel("Roll"));
		diePane.add(dieRoll);
		diePane.setBorder(new TitledBorder(new EtchedBorder(), "Die"));
		contentPane.add(diePane);
		
		guessPane.add(new JLabel("Guess"),BorderLayout.NORTH);
		guessPane.add(guess);
		guessPane.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		contentPane.add(guessPane);
		
		guessResultPane.add(new JLabel("Response"));
		guessResultPane.add(guessResult);
		guessResultPane.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		contentPane.add(guessResultPane);
	}

}
