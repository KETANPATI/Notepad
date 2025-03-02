package Notepad;

import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.KeyStroke;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class NotePad 
{

	JFrame frame;
	JTextArea textArea;
	JMenuBar menuBar;
	JMenu fileMenu, languageMenu, formatMenu, commandPrompt;
	JMenuItem itemNew, itemNewWindow, itemOpen, itemSaveAs, itemSave, itemExit;		// File Menu Items
	JMenuItem itemWordWrap, itemFont, itemFontSize;		// Format Menu Item
	JMenuItem itemcmd;		// Command Prompt Menu Item
	String openPath = null;
	String openFileName = null;

	boolean wrap = false;
	//ByDefualt set font with size
	Font arial = new Font("Arial", Font.PLAIN, 24);
	Font timeNewRoman=new Font("Times New Roman", Font.PLAIN, 24);
	Font consolas = new Font("Consolas", Font.PLAIN, 24);
	Font verdana = new Font("Verdana", Font.PLAIN, 24);
	String fontStyle = "consolas";
	

	public NotePad() 
	{
		createFrame();
		createTextArea();
		createScrollBars();
		createMenuBar();
		createFileMenuItems();
		createFormatItem();
		createCommandPrompt();
		createLanguage();
		addFrame();
	}

	public void createFrame() 
	{
		frame = new JFrame("Untitled");
		frame.setSize(900, 700);
	}
	public void addFrame() 
	{
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void createTextArea() 
	{
		textArea = new JTextArea();
		frame.add(textArea);
		textArea.setFont(new Font(fontStyle, Font.PLAIN, 24));
	}

	public void createScrollBars()
	{
		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
		JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		frame.add(scroll);
	}

	public void createMenuBar()
	{
		menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		fileMenu = new JMenu("File");
		menuBar.add(fileMenu);

		languageMenu = new JMenu("Language");
		menuBar.add(languageMenu);

		formatMenu = new JMenu("Format");
		menuBar.add(formatMenu);

		commandPrompt = new JMenu("Command Prompt ");
		menuBar.add(commandPrompt);
	}

	public void createFileMenuItems()
	{

		itemNew = new JMenuItem("New");
		itemNew.addActionListener(new ActionListener()	// Action Listener For New Option Which will Create A New File For Writing
		{			
			@Override
			public void actionPerformed(ActionEvent e)
			{
				textArea.setText("");
				frame.setTitle("Untitled.. NotePad");
				openPath = null;
				openFileName = null;
			}
		});
		fileMenu.add(itemNew);

		itemNewWindow = new JMenuItem("New Window");	// It will open The new Window of Note Pad
		itemNewWindow.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				NotePad n1 = new NotePad();
				n1.frame.setTitle("Untitled....");
			}
		});
		fileMenu.add(itemNewWindow);
		
		itemOpen = new JMenuItem("Open");
		itemOpen.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				FileDialog fd = new FileDialog(frame, "Open", FileDialog.LOAD);
				fd.setVisible(true);

				String file = fd.getFile();
				String path = fd.getDirectory();

				if (file != null) 
				{
					frame.setTitle(file);
					openFileName = file;
					openPath = path;
					readFile(path + file);
				} 
				else
				{
					frame.setTitle("NotePad");
				}
			}
		});
		fileMenu.add(itemOpen);

		itemSaveAs = new JMenuItem("Save As");
		itemSaveAs.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				FileDialog fd = new FileDialog(frame, "Save As", FileDialog.SAVE);
				fd.setVisible(true);

				String path = fd.getDirectory();
				String filename = fd.getFile();

				if (filename != null && path != null) 
				{
					writeDataToFile(path, filename);
				}
			}
		});
		fileMenu.add(itemSaveAs);

		itemSave = new JMenuItem("Save");
//		itemSave.setMnemonic(KeyEvent.VK_0);
		itemSave.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (openFileName != null && openPath != null)
				{
					writeDataToFile(openPath, openFileName);
				} 
				else 
				{
					FileDialog fd = new FileDialog(frame, "Save As", FileDialog.SAVE);
					fd.setVisible(true);
					
					String path = fd.getDirectory();
					String filename = fd.getFile();

					if (filename != null && path != null)
					{
						writeDataToFile(path, filename);
					}
				}
			}
		});
		fileMenu.add(itemSave);

		itemExit = new JMenuItem("Exit");
		itemExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				frame.dispose();
			}
		});
		fileMenu.add(itemExit);
	}

	public void createLanguage()
	{
		JMenuItem itemJava = new JMenuItem("Java");
		languageMenu.add(itemJava);
		
		itemJava.addActionListener(new ActionListener() 
		{
            @Override
            public void actionPerformed(ActionEvent e) 
            {
            	setLanguage("Java");
            }
        });

		JMenuItem itemCpp = new JMenuItem("C++");
		languageMenu.add(itemCpp);
		
		itemCpp.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setLanguage("Cpp");
			}
		});
		
		JMenuItem itemC = new JMenuItem("C");
		languageMenu.add(itemC);
		
		itemC.addActionListener( new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				setLanguage("C");
			}
		});

		JMenuItem itemHtml = new JMenuItem("HTML");
		languageMenu.add(itemHtml);
		
		itemHtml.addActionListener( new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				setLanguage("Html");
			}
		});

		itemNew.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));  //CTRL + N
		itemNewWindow.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK));  //CTRL + Shift + N
		itemOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));//CTRL + O
		itemSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK)); //CTRL + S
		itemSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK | ActionEvent.SHIFT_MASK)); //CTRL + Shift + S
		itemExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK)); // CTRL+Q for exit
	}

	public void createFormatItem()
	{
		itemWordWrap = new JMenuItem("Word Wrap off");
		formatMenu.add(itemWordWrap);

		itemFont = new JMenu("Font");
		formatMenu.add(itemFont);

		JMenuItem itemArial = new JMenuItem("Arial");
		itemFont.add(itemArial);

		JMenuItem itemTimesNewRoman = new JMenuItem("Times New Roman");
		itemFont.add(itemTimesNewRoman);

		JMenuItem itemConsolas = new JMenuItem("Consolas");
		itemFont.add(itemConsolas);

		itemFontSize = new JMenu("Font Size");
		formatMenu.add(itemFontSize);
		
		JMenuItem size12 = new JMenuItem("12");
		itemFontSize.add(size12);

		JMenuItem size14 = new JMenuItem("14");
		itemFontSize.add(size14);
		
		JMenuItem size16 = new JMenuItem("16");
		itemFontSize.add(size16);
		
		JMenuItem size18 = new JMenuItem("18");
		itemFontSize.add(size18);

		JMenuItem size20 = new JMenuItem("20");
		itemFontSize.add(size20);
		
		JMenuItem size22 = new JMenuItem("22");
		itemFontSize.add(size22);

		JMenuItem size24 = new JMenuItem("24");
		itemFontSize.add(size24);
		
		JMenuItem size26 = new JMenuItem("26");
		itemFontSize.add(size26);

		JMenuItem size28 = new JMenuItem("28");
		itemFontSize.add(size28);
		
		JMenuItem size30 = new JMenuItem("30");
		itemFontSize.add(size30);

		JMenuItem size32 = new JMenuItem("32");
		itemFontSize.add(size32);
		
		JMenuItem size34 = new JMenuItem("34");
		itemFontSize.add(size34);

		JMenuItem size36 = new JMenuItem("36");
		itemFontSize.add(size36);
		
		itemWordWrap.addActionListener(itemWordWrapActionListener());

		itemArial.addActionListener(fontStyleActionListener("Arial"));
		itemTimesNewRoman.addActionListener(fontStyleActionListener("Times New Roman"));
		itemConsolas.addActionListener(fontStyleActionListener("Consolas"));
		
		size12.addActionListener(sizeActionListener(12));
		size14.addActionListener(sizeActionListener(14));
		size16.addActionListener(sizeActionListener(16));
		size18.addActionListener(sizeActionListener(18));
		size20.addActionListener(sizeActionListener(20));
		size22.addActionListener(sizeActionListener(22));
		size24.addActionListener(sizeActionListener(24));
		size26.addActionListener(sizeActionListener(26));
		size28.addActionListener(sizeActionListener(28));
		size30.addActionListener(sizeActionListener(30));
		size32.addActionListener(sizeActionListener(32));
		size34.addActionListener(sizeActionListener(34));
		size36.addActionListener(sizeActionListener(36));
		
		itemWordWrap.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, ActionEvent.CTRL_MASK)); //CTRL + W
	}

	public void createCommandPrompt() {
		itemcmd = new JMenuItem("Open cmd ");

		commandPrompt.add(itemcmd);

		itemcmd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					if(openPath!=null)
					{
						Runtime.getRuntime().exec(new String[] {"cmd" , "/C","start"},null,new File(openPath));
					}
					else
					{
						Runtime.getRuntime().exec(new String[] {"cmd" , "/C","start"},null,null);
						
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		itemcmd.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.CTRL_MASK)); // CTRL + M

		
	}
	
	public ActionListener sizeActionListener(int size)
	{
		ActionListener a = new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				setFontSize(size);
			}
		};
		return a;
	}
	
	
	public ActionListener fontStyleActionListener(String fontStyleName)
	{
		ActionListener a = new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				setFontType(fontStyleName);
			}
		};
		return a;
	}
	
	public ActionListener itemWordWrapActionListener()
	{
		ActionListener a = new ActionListener() 
		{
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if (wrap == false)
				{
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);

					wrap = true;
					itemWordWrap.setText("Word Wrap:On");

				} 
				else
				{
					textArea.setLineWrap(false);
					textArea.setWrapStyleWord(false);
					wrap = false;
					itemWordWrap.setText("Word Wrap:Off");
				}
			}
		};
		return a;
	}
	
	public void writeDataToFile(String path, String filename) {
		BufferedWriter bw = null;

		try {
			frame.setTitle(filename);
			bw = new BufferedWriter(new FileWriter(path + filename));

			String text = textArea.getText();

			bw.write(text);

		} catch (IOException e1) {

			System.out.println("Data can not be Written !");
			
		} finally {
			try {
				bw.close();
			} catch (IOException e1) {

				System.out.println("File Can not be closed");
			} catch (NullPointerException e2) {
				System.out.println("File not found ");
			}
		}
	}

	public void readFile(String filePath) {

		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

			String sentence;

			textArea.setText(""); // Clear existing content

			while ((sentence = br.readLine()) != null) {

				textArea.append(sentence + "\n");
			}

		} catch (IOException e) {

			e.printStackTrace();
		}
	}

	public void setFontSize(int size)
	{
		 arial= new Font("Arial", Font.PLAIN, size);
		 timeNewRoman = new Font("Times New Roman", Font.PLAIN, size);
		 consolas = new Font("Consolas", Font.PLAIN, size);
		setFontType(fontStyle);
	}

	public void setFontType(String fontName)
	{
		fontStyle = fontName;
		switch (fontName)
		{
			case "Arial":
				textArea.setFont(arial);
			break;

			case "Times New Roman":
				textArea.setFont(timeNewRoman);
			break;

			case "Consolas":
				textArea.setFont(consolas);
			break;
		
			default :
				textArea.setFont(verdana);
			break;
			
		}
	}


	public void setLanguage(String lang) 
	{
	    String filePath = "/Notepad/BoilerPlate/" + lang + ".txt"; // Relative path inside src folder
	    InputStream inputStream = null;
	    BufferedReader reader = null;

	    try 
	    {
	        inputStream = getClass().getResourceAsStream(filePath);
	        
	        if (inputStream == null) { // Handle case where file is not found
	            System.out.println("File Not Found: " + filePath);
	            return;
	        }

	        reader = new BufferedReader(new InputStreamReader(inputStream));

	        String sentence;
	        textArea.setText(""); // Clear text area

	        while ((sentence = reader.readLine()) != null) 
	        {
	            textArea.append(sentence + "\n");
	        }
	        System.out.println("File Read Successfully.");
	    } 
	    catch (IOException e1) 
	    {
	        System.out.println("Data Could Not be Read: " + e1.getMessage());
	    } 
	    finally 
	    {
	        try 
	        {
	            if (reader != null) reader.close();
	            if (inputStream != null) inputStream.close();
	            System.out.println("Resources Closed.");
	        } 
	        catch (IOException e1) 
	        {
	            System.out.println("File Could Not Close: " + e1.getMessage());
	        }
	    }
	}
	
	public static void main(String[] args) 
	{
		NotePad obj = new NotePad();
	}

}



