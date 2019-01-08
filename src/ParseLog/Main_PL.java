package ParseLog;

import java.awt.Choice;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

public class Main_PL implements Runnable{

	private JFrame frame;
	private static Main_PL window;
	private JFileChooser explorador;
	private String ruta,file_name;
	private File archivo;
	private boolean start_filter, all_filter;
	private FileWriter fichero = null;
    private PrintWriter pw = null;
    private JTextField open_text,save_text,filter_txt;
    private Thread thread_filter;
    private JButton btnOpenFile, btnExecuteFilter,btnSaveIn,btnStopFilter;
    private JPanel panel_CBs;
    private ArrayList<String> filter_list, exclude_list;
    private JTextField Exclude_txt;
    private BufferedReader br;
	private JButton SimpleModeBtn;
	private String myIP;
	private boolean SimpleMode, escrito, ignore;
	private ArrayList<String> TrazaCode;
	private ArrayList<String> TrazaCodeAll;
	private ArrayList<String> FirstLine;
	private JButton CreatedBy;
	private Choice choice;
	private JButton clearBtn;
	private static Systray systray;
	private Utils utils;
	private JPanel panel_2;
	

	public static void main(String[] args) {
		
		window = new Main_PL();
		window.frame.setVisible(true);
	}

	public Main_PL() {
		
		//------------------------------------Frame------------------------------------------//
		
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Img/Parse_log.png")));
		frame.setBounds(700, 350, 590, 324);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("ParseLog");
		
		
		
		//------------------------------------Panels------------------------------------------//
		
		JPanel panel = new JPanel();
		panel.setBounds(25, 116, 539, 25);
		panel.setOpaque(false);
		panel.setLayout(null);
		frame.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(25, 178, 539, 25);
		panel_1.setOpaque(false);
		frame.getContentPane().add(panel_1);
		
		panel_2 = new JPanel();
		panel_2.setBounds(316, 216, 248, 33);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		panel_2.setVisible(false);
		
		JLabel lblCargando = new JLabel("Cargando...");
		lblCargando.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 19));
		lblCargando.setBounds(61, 0, 147, 33);
		panel_2.add(lblCargando);
		
		panel_CBs = new JPanel();
		panel_CBs.setBounds(25, 144, 539, 33);
		panel_CBs.setOpaque(false);
		frame.getContentPane().add(panel_CBs);
		
		
		//------------------------------------Labels------------------------------------------//
		
		JLabel lblFiltro = new JLabel("Filtro:");
		lblFiltro.setForeground(Color.BLACK);
		lblFiltro.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFiltro.setBounds(10, 3, 34, 16);
		panel.add(lblFiltro);
		
		JLabel lblExcluir = new JLabel("Excluir:");
		lblExcluir.setForeground(Color.BLACK);
		lblExcluir.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcluir.setBounds(10, 3, 44, 16);
		panel_1.add(lblExcluir);
		
		JLabel Version = new JLabel("Versi\u00F3n 1.3.3");
		Version.setForeground(Color.BLACK);
		Version.setFont(new Font("Tahoma", Font.BOLD, 13));
		Version.setBounds(10, 260, 110, 16);
		frame.getContentPane().add(Version);
		
		
		//------------------------------------TextFields---------------------------------------//
		
		filter_txt = new JTextField();
		filter_txt.setBounds(46, 0, 493, 22);
		filter_txt.setColumns(10);
		panel.add(filter_txt);
		
		open_text = new JTextField();
		open_text.setText(FindMostRecentLog());
		open_text.setFont(new Font("Tahoma", Font.PLAIN, 10));
		open_text.setBounds(144, 46, 420, 25);
		frame.getContentPane().add(open_text);
		open_text.setColumns(10);
		
		save_text = new JTextField();
		save_text.setFont(new Font("Tahoma", Font.PLAIN, 10));
		save_text.setColumns(10);
		save_text.setBounds(144, 82, 420, 25);
		frame.getContentPane().add(save_text);
		
		Exclude_txt = new JTextField();
		Exclude_txt.setColumns(10);
		Exclude_txt.setBounds(64, 0, 475, 22);
		panel_1.add(Exclude_txt);
		
		//------------------------------------Buttons------------------------------------------//
		
		btnOpenFile = new JButton("Abrir Log");
		btnOpenFile.setBounds(25, 46, 110, 25);
		frame.getContentPane().add(btnOpenFile);
		
		btnExecuteFilter = new JButton("Ejecutar Filtro");
		btnExecuteFilter.setBounds(322, 224, 115, 25);
		frame.getContentPane().add(btnExecuteFilter);
		
		btnSaveIn = new JButton("Guardar en...");
		btnSaveIn.setBounds(25, 81, 110, 25);
		frame.getContentPane().add(btnSaveIn);
		
		btnStopFilter = new JButton("Parar Filtro");
		btnStopFilter.setBounds(449, 224, 115, 25);
		frame.getContentPane().add(btnStopFilter);
		
		SimpleModeBtn = new JButton("Modo Simplificado");
		SimpleModeBtn.setBounds(181, 8, 170, 25);
		frame.getContentPane().add(SimpleModeBtn);
		
		CreatedBy = new JButton("");
		CreatedBy.setBorder(null);
		CreatedBy.setOpaque(false);
		CreatedBy.setIcon(new ImageIcon(Main_PL.class.getResource("/javax/swing/plaf/metal/icons/ocean/info.png")));
		CreatedBy.setBackground(Color.LIGHT_GRAY);
		CreatedBy.setBounds(532, 253, 32, 32);
		CreatedBy.setMargin(new Insets(0, 0, 0, 0));
		frame.getContentPane().add(CreatedBy);
		
		clearBtn  = new JButton("Limpiar");
		clearBtn.setFont(new Font("Tahoma", Font.BOLD, 8));
		clearBtn.setBounds(240, 255, 70, 15);
		frame.getContentPane().add(clearBtn);
		
		
		//------------------------------------CheckBoxes------------------------------------------//
		
		JCheckBox CB_JF = new JCheckBox("Juntar Filtros");
		CB_JF.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_JF.setForeground(Color.BLUE);
		CB_JF.setOpaque(false);
		panel_CBs.add(CB_JF);
		
		JCheckBox CB_error = new JCheckBox("error");
		CB_error.setForeground(Color.BLACK);
		CB_error.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_error.setOpaque(false);
		panel_CBs.add(CB_error);
		
		JCheckBox CB_wav = new JCheckBox(".wav");
		CB_wav.setForeground(Color.BLACK);
		CB_wav.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_wav.setOpaque(false);
		panel_CBs.add(CB_wav);
		
		JCheckBox CB_aspx = new JCheckBox(".aspx");
		CB_aspx.setForeground(Color.BLACK);
		CB_aspx.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_aspx.setOpaque(false);
		panel_CBs.add(CB_aspx);
		
		JCheckBox CB_goto = new JCheckBox("goto :");
		CB_goto.setForeground(Color.BLACK);
		CB_goto.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_goto.setOpaque(false);
		panel_CBs.add(CB_goto);
		
		JCheckBox CB_log = new JCheckBox("log ");
		CB_log.setForeground(Color.BLACK);
		CB_log.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_log.setOpaque(false);
		panel_CBs.add(CB_log);
		
		JCheckBox CB_codi = new JCheckBox("CODIFI");
		CB_codi.setForeground(Color.BLACK);
		CB_codi.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_codi.setOpaque(false);
		CB_codi.setToolTipText("");
		panel_CBs.add(CB_codi);
		
		JCheckBox CB_js = new JCheckBox(".js");
		CB_js.setForeground(Color.BLACK);
		CB_js.setFont(new Font("Tahoma", Font.BOLD, 11));
		CB_js.setOpaque(false);
		panel_CBs.add(CB_js);
		
		choice = new Choice();
		choice.setBounds(25, 229, 285, 20);
		frame.getContentPane().add(choice);
		
		JLabel lblSeleccionarLlamada = new JLabel("Seleccionar llamada:");
		lblSeleccionarLlamada.setForeground(Color.BLACK);
		lblSeleccionarLlamada.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSeleccionarLlamada.setBounds(25, 209, 212, 14);
		frame.getContentPane().add(lblSeleccionarLlamada);
		
		//-----------------------------------------Background--------------------------------------//
		
		JLabel background = new JLabel("New label");
		background.setIcon(new ImageIcon(Main_PL.class.getResource("/Img/background.jpg")));
		background.setBounds(0, 0, 574, 339);
		frame.getContentPane().add(background);
		
		//------------------------------------Initialize Variables--------------------------------//
		initialize();
		listeners();
	}

	
	
	public JFrame getFrame() {
		return frame;
	}

	public void setSave_text(String save_text) {
		this.save_text.setText(save_text);
	}

	private void initialize() {
		
		utils = new Utils();
		systray = new Systray(this);
		systray.loadFiles();
		save_text.setText(systray.getPath());
		TrazaCode = new ArrayList<String>();
		TrazaCodeAll = new ArrayList<String>();
		file_name="new_file";
		start_filter=false;
		all_filter=false;
		filter_list = new ArrayList<String>();
		exclude_list = new ArrayList<String>();
		FirstLine = new ArrayList<String>();
		myIP = "";
		SimpleMode=false;
		choice.add("Todas");
		chargeChoice();
		
		//------------------------------------File Chosser--------------------------------//
		
		explorador = new JFileChooser("\\\\10.0.1.95\\gvp_logs\\GVP_MCP");
		explorador.setDialogTitle("Abrir documento...");
		explorador.setFileFilter(new FileNameExtensionFilter("Logs", "log"));
		
	}

	private void listeners() {
		
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if(systray.getOpcMinimize()) {
					frame.setVisible(false);
				}
				else {
					exit();
				}
			}
		});
		
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path=readfile();
				if(path!=null) {
					open_text.setText(path);
				}
				panel_2.setVisible(true);
				btnExecuteFilter.setEnabled(false);
				btnStopFilter.setEnabled(false);
				cleanChoicelist();
				new Thread() {
		            public void run() {
		            	chargeChoice();
		            }
		        }.start();
			}
		});
		
		btnExecuteFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					btnExecuteFilter.setEnabled(false);
					btnExecuteFilter.setText("Ejecutando...");
					setFilter();
					start_filter=true;
					thread_filter = new Thread(window);
					thread_filter.start();
			}
		});
		
		btnSaveIn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path=readDirectory();
				if(path!=null) {
					save_text.setText(path);
				}
			}
		});
		
		btnStopFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnExecuteFilter.setEnabled(true);
				btnExecuteFilter.setText("Ejecutar Filtro");
				clear();
			}
		});
		
		SimpleModeBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(SimpleMode) {
					SimpleModeOff();
				}
				else {
					SimpleModeOn();
				}
			}
		});
		
		CreatedBy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null,"Created by: Yago Echave-Sustaeta Hernán");
			}
		});
		
		clearBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {	
				cleanChoicelist();
			}
		});
	}
	
	public void cleanChoicelist() {
		choice.removeAll();
		choice.add("Todas");
	}
	
	public String FindMostRecentLog() {
		
		File folder = new File("\\\\10.0.1.95\\gvp_logs\\GVP_MCP");
		File[] listOfFiles = folder.listFiles();
		
		return "\\\\10.0.1.95\\gvp_logs\\GVP_MCP\\" + listOfFiles[listOfFiles.length-1].getName();

	}
	
	public void SimpleModeOn() {
		SimpleModeBtn.setForeground(Color.RED);
		SimpleMode=true;
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
			while(e.hasMoreElements()){
			    NetworkInterface n = (NetworkInterface) e.nextElement();
			    Enumeration<InetAddress> ee = n.getInetAddresses();
			    while (ee.hasMoreElements())
			    {
			        InetAddress i = (InetAddress) ee.nextElement();
			        if(i.getHostAddress().indexOf("10.1.")!=-1) {
			        	myIP=i.getHostAddress();
			        }
			        
			    }
			}
		} catch (SocketException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println(myIP);
	}
	
	public void SimpleModeOff() {
		SimpleMode=false;
		SimpleModeBtn.setForeground(Color.DARK_GRAY);
	}
	
	public void chargeChoice(){
		try {
        archivo = new File(open_text.getText());
		FileReader fr = new FileReader (archivo);
		br = new BufferedReader(fr);
		String linea;
			while((linea=br.readLine()) != null){
				System.out.println(linea);
				checkChoice(linea);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		panel_2.setVisible(false);
		btnExecuteFilter.setEnabled(true);
		btnStopFilter.setEnabled(true);
	}
	
	public void readLog() throws IOException{
		escrito=false;
		ignore=true;
		if(ruta != "null") {
			fichero = new FileWriter(save_text.getText());
            pw = new PrintWriter(fichero);
            archivo = new File(open_text.getText());
			FileReader fr = new FileReader (archivo);
			br = new BufferedReader(fr);
			String linea;
			while(start_filter) {
				if((linea=br.readLine()) != null){
					if(linea.indexOf("Int")!=-1 || linea.indexOf("Std")!=-1) {
						if(SimpleMode) {
							readLogSimpleMode(linea);
						}
						else if(readFilter(linea)) {
							readLogBody(linea);
						}
					}
				}
				else {
					if(escrito) {
					System.out.println("cerrar fichero");
					fichero.close();
					escrito=false;
					fichero = new FileWriter(save_text.getText(),true);
					pw = new PrintWriter(fichero);
					}
				}
			}
		}
	}

	public void readLogSimpleMode(String linea){
		String[] parts = linea.split(" ");
		if(!lookForTrazaCodeAll(parts[3])) {
			if(linea.indexOf("incall_initiated")!=-1) {
				FirstLine.add(linea);
			}
			else {
				TrazaCodeAll.add(parts[3]);
				ignore=(linea.indexOf(myIP)==-1);
				if(!ignore) {
					TrazaCode.add(parts[3]);
					pw.println(PrintFirstLine(parts[3]));
					if(readFilter(linea)){
						pw.println(linea);
					}
				}
				escrito=true;
			}
		}
		else if (lookForTrazaCode(parts[3])){
			if(readFilter(linea)){
				checkChoice(linea);
				pw.println(linea);
			}
			escrito=true;
		}
	}
	
	public void readLogBody(String linea) {
		
		checkChoice(linea);
		System.out.println(linea);
		pw.println(linea);
		escrito=true;
	}
	
	public void checkChoice(String linea) {
		
		if(linea.indexOf("incall_initiated")!=-1) {
			String[] parts = linea.split(" ");
			String string = parts[0].substring(0, 8) + " // " + "unknow"+ " // " + parts[3];
			if(!utils.isInList(choice, parts[3])){
				choice.add(string);
			}
		}
		if(linea.indexOf("Environment|vdf-")!=-1) {
			String[] parts = linea.split(" ");
			if(utils.isInList(choice, parts[3])){
				if(utils.removeChoice(choice, parts[3])) {
					String[] parts2 = linea.split("Environment\\|");
					String[] splitName = parts2[1].split("\\-");
					choice.add(parts[0].substring(0, 8)+" // "+ splitName[1]+" // "+parts[3]);
				}
			}
		}
		
	}
	
	public String PrintFirstLine(String code) {
		for (String linea : FirstLine) {
			String[] parts = linea.split(" ");
			if (code.equals(parts[3])) {
				return linea;
			}
		}
		return null;
	}
	
	public boolean lookForTrazaCode(String code) {
		for(String SavedCode: TrazaCode) {
			if(SavedCode.equals(code)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean lookForTrazaCodeAll(String code) {
		for(String SavedCode: TrazaCodeAll) {
			if(SavedCode.equals(code)) {
				return true;
			}
		}
		return false;
	}
	
	public void clear() {
		start_filter=false;
		all_filter=false;
		filter_list.clear();
		exclude_list.clear();
	}
	
	public String readDirectory() {
		explorador = new JFileChooser(systray.getPath());
		explorador.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int seleccion = explorador.showDialog(null, "Abrir!");
		switch(seleccion) {
		case JFileChooser.APPROVE_OPTION:
			archivo = explorador.getSelectedFile();
			systray.setPath(archivo.getPath());
		 //seleccionó abrir
		 break;

		default: return null;
		}
		return systray.getPath();
	}
	
	public String readfile() {
		
		explorador.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int seleccion = explorador.showDialog(null, "Abrir!");
		String ruta="";
		switch(seleccion) {
		case JFileChooser.APPROVE_OPTION:
			archivo = explorador.getSelectedFile();
			file_name=archivo.getName();
			file_name = file_name.replaceAll(".log", "_ParseLog.txt");
			ruta = archivo.getPath();
		 //seleccionó abrir
		 break;

		default: return null;
		}
		return ruta;
	}
	
	public boolean readFilter(String linea) {
		
		boolean toReturn=false;
		if(filter_list.isEmpty()) {
			if(exclude_list != null && exclude_list.size() != 0) {
				for(String y:exclude_list) {
					if(linea.indexOf(y) != -1) {
						return false;
					}
				}
			}
			return true;
		}
		for(String x:filter_list) {
			if(linea.indexOf(x) != -1) {
				if(all_filter) {
					if(x == filter_list.get(filter_list.size()-1)) {
						toReturn= true;
						break;
					}
				}
				else {
					toReturn=true;
					break;
				}
			}
			else{
				if(all_filter) {
					if(x != filter_list.get(filter_list.size()-1)) {
						break;
					}
				}
			}
		}
		
		if(exclude_list != null && exclude_list.size() != 0) {
			for(String y:exclude_list) {
				if(linea.indexOf(y) != -1) {
					toReturn=false;
					break;
				}
			}
		}

		return toReturn;
	}
	
	public void setFilter() {
		
		JCheckBox checkBox = (JCheckBox)panel_CBs.getComponent( 0 );
		  if( checkBox.isSelected() ) { 
		     all_filter=true;
		  }
		
		for( int i=1; i<panel_CBs.getComponentCount(); i++ ) {
			  checkBox = (JCheckBox)panel_CBs.getComponent( i );
			  if( checkBox.isSelected() ) { 
			     filter_list.add(checkBox.getText());
			  }
			}
		
		if(!filter_txt.getText().isEmpty()) {
			String delims = "[,]+";
			String[] filters = filter_txt.getText().split(delims);
			for(String filt: filters){
				filter_list.add(filt);
			}
		}
		
		if(!Exclude_txt.getText().isEmpty()) {
			String delims = "[,]+";
			String[] excludes = Exclude_txt.getText().split(delims);
			for(String filt: excludes){
				exclude_list.add(filt);
			}
		}
		
		if(!choice.getSelectedItem().equals("Todas")) {
			String[] parts = choice.getSelectedItem().split(" // ");
			filter_list.add(parts[2]);
		}
	}
	
	public void exit() {
		systray.saveOptions();
		System.exit(0);
	}
	
	public void run() {
		try {
			readLog();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null,"Archivo Log no encontrado");
			e.printStackTrace();
		}
	}
}
