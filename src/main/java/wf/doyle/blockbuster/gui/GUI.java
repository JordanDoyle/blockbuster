package wf.doyle.blockbuster.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.util.Collections;
import java.util.List;

import javax.swing.JTabbedPane;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import wf.doyle.blockbuster.App;
import wf.doyle.blockbuster.item.LibraryItem;
import wf.doyle.blockbuster.item.items.printed.Book;
import wf.doyle.blockbuster.item.items.printed.Periodical;
import wf.doyle.blockbuster.util.EnumLineType;

/**
 * Provides a nice (yet somewhat sketchy) front-end for BlockBuster.
 * 
 * @author Jordan Doyle
 */
public class GUI extends JFrame {

	/**
	 * Serial Version UID
	 */
    private static final long serialVersionUID = 1L;

	/**
	 * Content Pane
	 */
	private JPanel contentPane;
	
	/**
	 * Tabbed Pane
	 */
	private final JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);

	/**
	 * Create the frame.
	 */
	public GUI()
	{
		this.setResizable(false);
		this.setTitle("BlockBuster");

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e)
		{
			e.printStackTrace();
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 712, 613);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(this.contentPane);
		this.contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		this.contentPane.add(this.tabbedPane);

		List<LibraryItem> items = App.items;
		Collections.sort(items, (arg0, arg1) -> {
			return arg0.getType().toString().compareTo(arg1.getType().toString());
		});

		EnumLineType header = null;

		int top = 17;

		JPanel panel = null;

		for(LibraryItem item : items)
		{
			if(header != item.getType())
			{
				top = 17;

				panel = new JPanel();
				this.tabbedPane.addTab(item.getClass().getSimpleName(), null, panel, null);
				panel.setLayout(null);

				header = item.getType();
			}

			JButton btnNewButton = new JButton("Borrow");

			if(item.getLoan())
			{
				btnNewButton.setEnabled(false);
				btnNewButton.setText("Unavailable");
			}

			btnNewButton.setBounds(570, top - 4, 97, 25);
			panel.add(btnNewButton);

			JLabel lblNewLabel = new JLabel(item.getName());
			lblNewLabel.setBounds(12, top, 170, 16);
			panel.add(lblNewLabel);

			JLabel lblNewLabel_2;
			JLabel lblNewLabel_3;

			final boolean borrow;

			if(item instanceof Book)
			{
				lblNewLabel_2 = new JLabel("" + ((Book) item).getISBN());
				lblNewLabel_3 = new JLabel( ((Book) item).getAuthor());
				borrow = false;
			}
			else
			{
				lblNewLabel_2 = new JLabel(item.getItemCode());

				if(item instanceof Periodical)
				{
					lblNewLabel_3 = new JLabel( ((Periodical) item).getPublisher());
					borrow = false;
				}
				else
				{
					lblNewLabel_3 = new JLabel("Borrowed: " + item.getTimesBorrowed());
					borrow = true;
				}
			}
			
			btnNewButton.addActionListener((e) -> {
				if(!App.USER.hasItem() && item.toggleItem())
				{	
					if(borrow)
						lblNewLabel_3.setText("Borrowed: " + item.getTimesBorrowed());
					
					App.USER.setItem(item);
					
					btnNewButton.setText("Return");
				} else if(App.USER.hasItem(item)) {
					btnNewButton.setText("Borrow");
					App.USER.removeItem();
				}
			});

			lblNewLabel_2.setBounds(220, top, 100, 16);
			panel.add(lblNewLabel_2);

			lblNewLabel_3.setBounds(370, top, 100, 16);
			panel.add(lblNewLabel_3);

			JLabel lblNewLabel_4 = new JLabel(item.getCost());
			lblNewLabel_4.setBounds(520, top, 100, 16);
			panel.add(lblNewLabel_4);

			top += 38;
		}
	}
}
