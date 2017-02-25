

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Main extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3191919612845534135L;
	static JTextArea ChatBox = new JTextArea(9, 45);
	private JScrollPane myChatHistory = new JScrollPane(ChatBox, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
			JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	private JTextArea pseudo = new JTextArea((int)1.2, 11);
	private JTextArea ipw = new JTextArea((int)1.2, 11);
	private JScrollPane ip = new JScrollPane(ipw, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JTextArea usere = new JTextArea((int)1.2, 11);
	private JScrollPane user = new JScrollPane(usere, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JTextArea mpde = new JTextArea((int)1.2, 11);
	private JScrollPane mdp = new JScrollPane(mpde, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JTextArea usrq = new JTextArea((int)1.2, 11);
	private JScrollPane usr = new JScrollPane(usrq, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JTextArea ide = new JTextArea((int)1.2, 11);
	private JScrollPane id = new JScrollPane(ide, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JScrollPane myUserHistory = new JScrollPane(pseudo, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	private JButton Send = new JButton("Stop");
	private JButton exit = new JButton("Exit");
	private JButton Start = new JButton("Start");

	public Main() {
		setTitle("TS3Bot by SweetKebab_ ");
		setSize(560, 400);
		Container cp = getContentPane();
		cp.setLayout(new FlowLayout());
		cp.add(new JLabel("Historique:"));
		cp.add(myChatHistory);
		ChatBox.append("Bienvenue ! En cas de question mp moi sur twitter ( @SweetKebab_ )\n");

		cp.add(new JLabel("Pseudo du bot :"));
		cp.add(myUserHistory);
		cp.add(new JLabel("IP du TeamSpeak : "));
		cp.add(ip);
		cp.add(new JLabel("Utilisateur Query : "));
		cp.add(user);
		cp.add(new JLabel("Mot De Passe Query : "));
		cp.add(mdp);
		cp.add(new JLabel("   Votre pseudo ts : "));
		cp.add(usr);
		cp.add(new JLabel("    ID du channel ts : "));
		cp.add(id);
		cp.add(Send);
		cp.add(Start);
		cp.add(exit);
		exit.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				System.exit(0);				
			}
			
		});
		Start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ServerQuery jts3 = new ServerQuery();

				try {
					jts3.runServerMod(ipw.getText(),usere.getText(), mpde.getText(),pseudo.getText(),usrq.getText(),Integer.parseInt(ide.getText()));
					ChatBox.setText("<Serveur> Started. ");
				} catch (Exception e1) {
					addtext("<Serveur> Erreur : " + e.toString());
				}
			}
		});
		Send.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				addtext("<Serveur> Stoped. ");
				ServerQuery.query.closeTS3Connection();
			}
		});

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);

	}

	public static void main(String[] args) {
		new Main();
	}

	public void addtext(String massg) {
		ChatBox.append(massg+"\n");
	}

}
