package ParseLog;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.stage.FileChooser;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import java.awt.Font;
import java.awt.Color;
import javax.swing.ImageIcon;

public class Main_PL implements Runnable{

	private JFrame frame;
	private static Main_PL window;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		window = new Main_PL();
		window.frame.setVisible(true);

	}

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
	
	public Main_PL() {
		
		//------------------------------------Frame------------------------------------------//
		frame = new JFrame();
		frame.setBounds(700, 350, 550, 290);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("ParseLog");
		
		//------------------------------------Panels------------------------------------------//
		
		JPanel panel = new JPanel();
		panel.setBounds(25, 88, 499, 25);
		panel.setOpaque(false);
		panel.setLayout(null);
		frame.getContentPane().add(panel);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBounds(25, 144, 499, 25);
		panel_1.setOpaque(false);
		frame.getContentPane().add(panel_1);
		
		JLabel lblExcluir = new JLabel("Excluir:");
		lblExcluir.setForeground(Color.BLACK);
		lblExcluir.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExcluir.setBounds(10, 3, 44, 16);
		panel_1.add(lblExcluir);
		
		panel_CBs = new JPanel();
		panel_CBs.setBounds(10, 111, 514, 33);
		panel_CBs.setOpaque(false);
		frame.getContentPane().add(panel_CBs);
		
		//------------------------------------Labels------------------------------------------//
		
		JLabel lblFiltro = new JLabel("Filtro:");
		lblFiltro.setForeground(Color.BLACK);
		lblFiltro.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFiltro.setBounds(10, 3, 34, 16);
		panel.add(lblFiltro);
		
		JLabel CreatedBy = new JLabel("Created by Yago Echave-Sustaeta");
		CreatedBy.setForeground(Color.DARK_GRAY);
		CreatedBy.setFont(new Font("Tahoma", Font.BOLD, 13));
		CreatedBy.setBounds(10, 224, 258, 16);
		frame.getContentPane().add(CreatedBy);
		
		//------------------------------------TextFields---------------------------------------//
		
		filter_txt = new JTextField();
		filter_txt.setBounds(46, 0, 453, 22);
		filter_txt.setColumns(10);
		panel.add(filter_txt);
		
		open_text = new JTextField();
		open_text.setText("\\\\10.0.1.95\\gvp_logs\\GVP_MCP\\GVP_MCP.20180213_124634_933.log");
		open_text.setFont(new Font("Tahoma", Font.PLAIN, 10));
		open_text.setBounds(144, 15, 380, 25);
		frame.getContentPane().add(open_text);
		open_text.setColumns(10);
		
		save_text = new JTextField();
		save_text.setText("C:\\Users\\sechave\\Desktop\\GVP_MCP.20180213_124634_933_ParseLog.txt");
		save_text.setFont(new Font("Tahoma", Font.PLAIN, 10));
		save_text.setColumns(10);
		save_text.setBounds(144, 51, 380, 25);
		frame.getContentPane().add(save_text);
		
		Exclude_txt = new JTextField();
		Exclude_txt.setColumns(10);
		Exclude_txt.setBounds(64, 0, 435, 22);
		panel_1.add(Exclude_txt);
		
		//------------------------------------Buttons------------------------------------------//
		
		btnOpenFile = new JButton("Abrir Log");
		btnOpenFile.setBounds(25, 15, 110, 25);
		frame.getContentPane().add(btnOpenFile);
		
		btnExecuteFilter = new JButton("Ejecutar Filtro");
		btnExecuteFilter.setBounds(75, 188, 167, 25);
		frame.getContentPane().add(btnExecuteFilter);
		
		btnSaveIn = new JButton("Guardar en...");
		btnSaveIn.setBounds(25, 50, 110, 25);
		frame.getContentPane().add(btnSaveIn);
		
		btnStopFilter = new JButton("Parar Filtro");
		btnStopFilter.setBounds(307, 188, 167, 23);
		frame.getContentPane().add(btnStopFilter);
		
		
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
		
		JLabel label = new JLabel("New label");
		label.setIcon(new ImageIcon(Main_PL.class.getResource("/Img/background.jpg")));
		label.setBounds(0, 0, 534, 251);
		frame.getContentPane().add(label);
		
		//------------------------------------Initialize Variables--------------------------------//
		initialize();
		listeners();
	}

	private void initialize() {
		
		file_name="new_file";
		start_filter=false;
		all_filter=false;
		filter_list = new ArrayList<String>();
		exclude_list = new ArrayList<String>();
		
		//------------------------------------File Chosser--------------------------------//
		
		explorador = new JFileChooser("\\\\10.0.1.95\\gvp_logs\\GVP_MCP");
		explorador.setDialogTitle("Abrir documento...");
		explorador.setFileFilter(new FileNameExtensionFilter("Logs", "log"));
		
	}

	private void listeners() {
		
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String path=readfile();
				if(path!=null) {
					open_text.setText(path);
				}
			}
		});
		
		btnExecuteFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					btnExecuteFilter.setEnabled(false);
					btnExecuteFilter.setText("Filtro ejecutandose...");
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
					save_text.setText(path+"\\"+file_name);
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
	}
	
	public void readLog() throws IOException{
		int x=0;
		boolean escrito=false;
		if(ruta != "null") {
			fichero = new FileWriter(save_text.getText());
            pw = new PrintWriter(fichero);
            archivo = new File(open_text.getText());
			FileReader fr = new FileReader (archivo);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while(start_filter) {
				if((linea=br.readLine()) != null){
					
					if(readFilter(linea)) {
						x++;
						System.out.println("Vamos por "+ x);
						System.out.println(linea);
						pw.println(linea);
						escrito=true;
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
	
	public void clear() {
		start_filter=false;
		all_filter=false;
		filter_list.clear();
		exclude_list.clear();
	}
	
	public String readDirectory() {
		explorador.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int seleccion = explorador.showDialog(null, "Abrir!");
		String ruta="";
		switch(seleccion) {
		case JFileChooser.APPROVE_OPTION:
			archivo = explorador.getSelectedFile();
			ruta = archivo.getPath();
		 //seleccionó abrir
		 break;

		default: return null;
		}
		return ruta;
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
		
		for(String x:filter_list) {
			if(linea.indexOf(x) != -1) {
				if(exclude_list != null && exclude_list.size() != 0) {
					for(String y:exclude_list) {
						if(linea.indexOf(y) == -1) {
							if(all_filter) {
								if(x == filter_list.get(filter_list.size()-1)) return true;
							}
							else return true;
						}
					}
				}
				else{
					if(all_filter) {
						if(x == filter_list.get(filter_list.size()-1)) return true;
					}
					else return true;
				}
			}
			else {
				if(all_filter) {
					return false;
				}
			}
		}
		return false;
		
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
	}
	
	public void run() {
		try {
			readLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
