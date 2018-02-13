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
	private boolean start_filter;
	private FileWriter fichero = null;
    private PrintWriter pw = null;
    private JTextField open_text,save_text,filter_txt;
    private Thread thread_filter;
    private JButton btnOpenFile, btnExecuteFilter,btnSaveIn,btnStopFilter;
    private JPanel panel_CBs;
    private ArrayList<String> filter_list;
	
	public Main_PL() {
		
		//------------------------------------Frame------------------------------------------//
		frame = new JFrame();
		frame.setBounds(700, 350, 550, 250);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("ParseLog");
		
		//------------------------------------Panels------------------------------------------//
		
		JPanel panel = new JPanel();
		panel.setBounds(25, 88, 499, 25);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		panel_CBs = new JPanel();
		panel_CBs.setBounds(25, 111, 499, 33);
		frame.getContentPane().add(panel_CBs);
		
		//------------------------------------Labels------------------------------------------//
		
		JLabel lblFiltro = new JLabel("Filtro:");
		lblFiltro.setBounds(10, 3, 34, 16);
		panel.add(lblFiltro);
		
		JLabel CreatedBy = new JLabel("Created by Yago Echave-Sustaeta");
		CreatedBy.setForeground(Color.LIGHT_GRAY);
		CreatedBy.setFont(new Font("Tahoma", Font.BOLD, 13));
		CreatedBy.setBounds(10, 191, 258, 16);
		frame.getContentPane().add(CreatedBy);
		
		//------------------------------------TextFields---------------------------------------//
		
		filter_txt = new JTextField();
		filter_txt.setBounds(46, 0, 453, 22);
		filter_txt.setColumns(10);
		panel.add(filter_txt);
		
		open_text = new JTextField();
		open_text.setFont(new Font("Tahoma", Font.PLAIN, 10));
		open_text.setBounds(144, 15, 380, 25);
		frame.getContentPane().add(open_text);
		open_text.setColumns(10);
		
		save_text = new JTextField();
		save_text.setFont(new Font("Tahoma", Font.PLAIN, 10));
		save_text.setColumns(10);
		save_text.setBounds(144, 51, 380, 25);
		frame.getContentPane().add(save_text);
		
		//------------------------------------Buttons------------------------------------------//
		
		btnOpenFile = new JButton("Abrir Log");
		btnOpenFile.setBounds(25, 15, 110, 25);
		frame.getContentPane().add(btnOpenFile);
		
		btnExecuteFilter = new JButton("Ejecutar Filtro");
		btnExecuteFilter.setBounds(75, 155, 167, 25);
		frame.getContentPane().add(btnExecuteFilter);
		
		btnSaveIn = new JButton("Guardar en...");
		btnSaveIn.setBounds(25, 50, 110, 25);
		frame.getContentPane().add(btnSaveIn);
		
		btnStopFilter = new JButton("Parar Filtro");
		btnStopFilter.setBounds(307, 155, 167, 23);
		frame.getContentPane().add(btnStopFilter);
		
		//------------------------------------CheckBoxes------------------------------------------//
		
		JCheckBox CB_error = new JCheckBox("error");
		panel_CBs.add(CB_error);
		
		JCheckBox CB_wav = new JCheckBox(".wav");
		panel_CBs.add(CB_wav);
		
		JCheckBox CB_aspx = new JCheckBox(".aspx");
		panel_CBs.add(CB_aspx);
		
		JCheckBox CB_goto = new JCheckBox("goto :");
		panel_CBs.add(CB_goto);
		
		JCheckBox CB_log = new JCheckBox("log ");
		panel_CBs.add(CB_log);
		
		JCheckBox CB_codi = new JCheckBox("CODIFI");
		CB_codi.setToolTipText("");
		panel_CBs.add(CB_codi);
		
		JCheckBox CB_js = new JCheckBox(".js");
		panel_CBs.add(CB_js);
		
		//------------------------------------Initialize Variables--------------------------------//
		initialize();
		listeners();
	}

	private void initialize() {
		
		file_name="new_file";
		start_filter=false;
		filter_list = new ArrayList<String>();
		
		//------------------------------------File Chosser--------------------------------//
		
		explorador = new JFileChooser("\\\\10.0.1.95\\gvp_logs\\GVP_MCP");
		explorador.setDialogTitle("Abrir documento...");
		explorador.setFileFilter(new FileNameExtensionFilter("Logs", "log"));
		
	}

	private void listeners() {
		
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				open_text.setText(readfile());
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
				save_text.setText(readDirectory()+"\\"+file_name);
			}
		});
		
		btnStopFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnExecuteFilter.setEnabled(true);
				btnExecuteFilter.setText("Ejecutar Filtro");
				start_filter=false;
				filter_list.clear();
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

		case JFileChooser.CANCEL_OPTION:
		 //dio click en cancelar o cerro la ventana
		 break;

		case JFileChooser.ERROR_OPTION:
		 //llega aqui si sucede un error
		 break;
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

		case JFileChooser.CANCEL_OPTION:
		 //dio click en cancelar o cerro la ventana
		 break;

		case JFileChooser.ERROR_OPTION:
		 //llega aqui si sucede un error
		 break;
		}
		return ruta;
	}
	
	public boolean readFilter(String linea) {
		for(String x:filter_list) {
			if(linea.indexOf(x) != -1) {
				return true;
			}
		}
		return false;
	}
	
	public void setFilter() {
		
		for( int i=0; i<panel_CBs.getComponentCount(); i++ ) {
			  JCheckBox checkBox = (JCheckBox)panel_CBs.getComponent( i );
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
	}
	
	public void run() {
		try {
			readLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
