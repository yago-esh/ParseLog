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
	
	public Main_PL() {
		initialize();
		explorador = new JFileChooser();
		explorador.setDialogTitle("Abrir documento...");
		explorador.setFileFilter(new FileNameExtensionFilter("Logs", "log"));
		salir=false;
	}

	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnAbrirLog = new JButton("Abrir Log");
		btnAbrirLog.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int seleccion = explorador.showDialog(null, "Abrir!");
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
			}
		});
		btnAbrirLog.setBounds(167, 30, 97, 25);
		frame.getContentPane().add(btnAbrirLog);
		
		JPanel panel = new JPanel();
		panel.setBounds(73, 91, 285, 51);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblFiltro = new JLabel("Filtro:");
		lblFiltro.setBounds(12, 16, 34, 16);
		panel.add(lblFiltro);
		
		txtEsbrirFiltroAqui = new JTextField();
		txtEsbrirFiltroAqui.setText("Esbrir filtro aqui...");
		txtEsbrirFiltroAqui.setBounds(67, 13, 202, 22);
		panel.add(txtEsbrirFiltroAqui);
		txtEsbrirFiltroAqui.setColumns(10);
		
		JButton btnCrearArchivoDe = new JButton("Crear Archivo de Salida");
		btnCrearArchivoDe.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					btnCrearArchivoDe.setEnabled(false);
					btnCrearArchivoDe.setText("Filtro ejecutandose...");
					(new Thread(window)).start();

			}
		});
		btnCrearArchivoDe.setBounds(132, 184, 167, 25);
		frame.getContentPane().add(btnCrearArchivoDe);
	}
	
	public void readLog() throws IOException{
		int x=0;
		boolean escrito=false;
		if(ruta != "null") {
			System.out.println("Vamos por "+ x);
			fichero = new FileWriter("C:\\Users\\sechave\\Desktop\\file.txt");
            pw = new PrintWriter(fichero);
			FileReader fr = new FileReader (archivo);
			BufferedReader br = new BufferedReader(fr);
			String linea;
			while(true) {
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
					fichero.close();
					fichero = new FileWriter("C:\\Users\\sechave\\Desktop\\file.txt",true);
					pw = new PrintWriter(fichero);
					}
				}
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
