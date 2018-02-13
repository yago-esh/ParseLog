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
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
	private JTextField txtEsbrirFiltroAqui;
	private String ruta;
	private File archivo;
	private boolean salir;
	private FileWriter fichero = null;
    private PrintWriter pw = null;
    private JTextField open_text;
    private JTextField save_text;
    private String file_name;
    private Thread thread_filter;
    private boolean start_filter;
	
	public Main_PL() {
		initialize();
		explorador = new JFileChooser("\\\\10.0.1.95\\gvp_logs\\GVP_MCP");
		explorador.setDialogTitle("Abrir documento...");
		explorador.setFileFilter(new FileNameExtensionFilter("Logs", "log"));
		salir=false;
	}

	private void initialize() {
		file_name="new_file";
		start_filter=false;
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnAbrirLog = new JButton("Abrir Log");
		btnAbrirLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				open_text.setText(readfile());
			}
		});
		btnAbrirLog.setBounds(25, 15, 100, 25);
		frame.getContentPane().add(btnAbrirLog);
		
		JPanel panel = new JPanel();
		panel.setBounds(25, 88, 399, 51);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblFiltro = new JLabel("Filtro:");
		lblFiltro.setBounds(10, 16, 34, 16);
		panel.add(lblFiltro);
		
		txtEsbrirFiltroAqui = new JTextField();
		txtEsbrirFiltroAqui.setBounds(46, 13, 353, 22);
		panel.add(txtEsbrirFiltroAqui);
		txtEsbrirFiltroAqui.setColumns(10);
		
		JButton btnCrearArchivoDe = new JButton("Ejecutar Filtro");
		btnCrearArchivoDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					btnCrearArchivoDe.setEnabled(false);
					btnCrearArchivoDe.setText("Filtro ejecutandose...");
					start_filter=true;
					thread_filter = new Thread(window);
					thread_filter.start();
			}
		});
		btnCrearArchivoDe.setBounds(133, 167, 167, 25);
		frame.getContentPane().add(btnCrearArchivoDe);
		
		JButton btnGuardarEn = new JButton("Guardar en...");
		btnGuardarEn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				save_text.setText(readDirectory()+"\\"+file_name);
			}
		});
		btnGuardarEn.setBounds(25, 50, 100, 25);
		frame.getContentPane().add(btnGuardarEn);
		
		open_text = new JTextField();
		open_text.setBounds(134, 15, 290, 25);
		frame.getContentPane().add(open_text);
		open_text.setColumns(10);
		
		save_text = new JTextField();
		save_text.setColumns(10);
		save_text.setBounds(134, 50, 290, 25);
		frame.getContentPane().add(save_text);
		
		JButton btnNewButton = new JButton("Parar Filtro");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				btnCrearArchivoDe.setEnabled(true);
				btnCrearArchivoDe.setText("Ejecutar Filtro");
				start_filter=false;
			}
		});
		btnNewButton.setBounds(133, 203, 167, 23);
		frame.getContentPane().add(btnNewButton);
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
					
					if(linea.indexOf(txtEsbrirFiltroAqui.getText()) != -1) {
						x++;
						System.out.println("Vamos por "+ x);
						System.out.println("Vamos a escribir "+ linea);
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
	
	public void run() {
		try {
			readLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
