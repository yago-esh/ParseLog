package ParseLog;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.filechooser.FileNameExtensionFilter;

import javafx.stage.FileChooser;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main_PL {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Main_PL window = new Main_PL();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private JFileChooser explorador;
	private JTextField txtEsbrirFiltroAqui;
	
	public Main_PL() {
		initialize();
		explorador = new JFileChooser();
		explorador.setDialogTitle("Abrir documento...");
		explorador.setFileFilter(new FileNameExtensionFilter("Logs", "log"));
	}

	/**
	 * Initialize the contents of the frame.
	 */
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
					File archivo = explorador.getSelectedFile();
					String ruta = archivo.getPath();
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
		btnCrearArchivoDe.setBounds(132, 184, 167, 25);
		frame.getContentPane().add(btnCrearArchivoDe);
	}
}
