/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package forgel;

import forgel.hearth.HearthComListener;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import jssc.SerialPortList;

/**
 *
 * @author mafolz
 */
public class MainWindow extends JFrame {

	private static MainWindow instance = null;
	private JPanel panel, innerPanel;
	private JTextArea console, comOutConsole, comInConsole;
	private JScrollPane scroller, scrollerComOut, scrollerComIn;
	public JComboBox midiDevice, port;

	private MainWindow() {
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
			Logger.getLogger(MainWindow.class.getName()).log(Level.SEVERE, null, ex);
		}
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		this.add(panel);
		this.setSize(800, 600);
		this.setLocation(150, 50);
		registerCloseHandler();

		innerPanel = new JPanel();
		innerPanel.setLayout(new GridBagLayout());
		panel.add(innerPanel, BorderLayout.NORTH);

		console = new JTextArea();
		scroller = new JScrollPane(console);
		scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		comOutConsole = new JTextArea();
		scrollerComOut = new JScrollPane(comOutConsole);
		scrollerComOut.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		comInConsole = new JTextArea();
		scrollerComIn = new JScrollPane(comInConsole);
		scrollerComIn.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split.add(scroller);

		JSplitPane split2 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		split2.add(scrollerComOut);
		split2.add(scrollerComIn);

		panel.add(split, BorderLayout.CENTER);
		initializeComElements();
		initializeMidiElements();

		this.setVisible(true);
		split.add(split2);
		split.setResizeWeight(0.25);
		split.setDividerLocation(split.getMaximumDividerLocation() / 3);
		split2.setDividerLocation(250);
		split.repaint();
		split2.setResizeWeight(0.25);
		split2.repaint();

	}

	/**
	 *
	 * @return
	 */
	public static MainWindow getInstance() {
		if (instance == null) {
			instance = new MainWindow();
		}
		return instance;
	}

	public static void print(Object object) {
		printTextArea(object, MainWindow.getInstance().console, MainWindow.getInstance().scroller, null);
	}

	public static void printCom(Object object) {
		printTextArea(object, MainWindow.getInstance().comOutConsole, MainWindow.getInstance().scrollerComOut, null);
	}

	public static void printInCom(Object object) {
		printTextArea(object, MainWindow.getInstance().comInConsole, MainWindow.getInstance().scrollerComIn, "");
	}

	public static void clean() {
		MainWindow.getInstance().console.setText("");
		MainWindow.getInstance().comOutConsole.setText("");
		MainWindow.getInstance().comInConsole.setText("");
	}

	private static void printTextArea(Object object, JTextArea con, JScrollPane scroll, String end) {
		con.append(object.toString());
		if (end == null) {
			end = "\n";
		}
		con.append(end);
		if (con.getLineCount() > 500) {
			// todo truncate
		}
		scroll.getVerticalScrollBar().setValue(scroll.getVerticalScrollBar().getMaximum());
	}

	private void initializeComElements() {
		port = new JComboBox(SerialPortList.getPortNames());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.NORTH;
		c.weightx = 1.0;
		innerPanel.add(port, c);

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());

		JButton connect = new JButton("Verbinden");
		connect.addActionListener(new HearthComListener());
		buttons.add(connect);

		JButton refresh = new JButton("Refresh");
		refresh.addActionListener(new ComRefreshListener());
		buttons.add(refresh);

		JButton disconnect = new JButton("Disconnect");
		disconnect.addActionListener(new ComDisconnectListener());
		buttons.add(disconnect);

		JButton hw_options = new JButton("HW Optionen");
		hw_options.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog hw_opt = new forgel.gui.HardwareModalDialog();
				hw_opt.setVisible(true);
			}

		});
		buttons.add(hw_options);

		GridBagConstraints c2 = new GridBagConstraints();
		c2.fill = GridBagConstraints.NORTHEAST;
		c2.gridwidth = GridBagConstraints.REMAINDER;
		c2.weightx = 0.5;
		innerPanel.add(buttons, c2);
	}

	private void registerCloseHandler() {
		/*Some piece of code*/
		this.addWindowListener(new java.awt.event.WindowAdapter() {
			@Override
			public void windowClosing(java.awt.event.WindowEvent windowEvent) {
				Hearth.getInstance().stopAllImediantly();
				System.exit(0);
			}
		});
	}

	private void initializeMidiElements() {
		midiDevice = new JComboBox(MidiListener.getDevices());
		midiDevice.addActionListener(new MidiConnectListener());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.CENTER;
		c.weightx = 1.0;
		innerPanel.add(midiDevice, c);

		JPanel buttons = new JPanel();
		buttons.setLayout(new FlowLayout());

		JButton clean = new JButton("Clean");
		clean.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainWindow.clean();
			}
		});
		buttons.add(clean);

		JButton panic = new JButton("PANIK!");
		panic.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Hearth.getInstance().stopAllImediantly();
			}
		});
		buttons.add(panic);

		GridBagConstraints c1 = new GridBagConstraints();
		c1.fill = GridBagConstraints.CENTER;
		c1.gridwidth = GridBagConstraints.REMAINDER;
		c1.weightx = 1.0;
		innerPanel.add(buttons, c1);
	}
}
