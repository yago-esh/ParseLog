package ParseLog;

import java.awt.AWTException;
import java.awt.CheckboxMenuItem;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

import javax.swing.JOptionPane;

public class Systray {
	
	private MenuItem exitItem, showItem;
	private TrayIcon trayIcon;
	private CheckboxMenuItem OpcMinimize, OpcPath, OpcName;
	private Main_PL main;
	private String path = null;
	private String pathName = null;

	public Systray(Main_PL main) {
		
		this.main=main;
		//Check the SystemTray is supported
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
        }
        else {
	        final PopupMenu popup = new PopupMenu();
	        URL url = System.class.getResource("/Img/icon_systray.png");
	        Image img = Toolkit.getDefaultToolkit().getImage(url);
	        trayIcon = new TrayIcon(img, "Parse Log", popup);
	        final SystemTray tray = SystemTray.getSystemTray();
	       
	        // Create a pop-up menu components
	        exitItem = new MenuItem("Salir");
	        showItem = new MenuItem("Abrir");
	        Menu options = new Menu("Opciones");
	        OpcMinimize = new CheckboxMenuItem("Minimizar al cerrar");
	        OpcPath = new CheckboxMenuItem("Ruta de fichero personalizada");
	        OpcName = new CheckboxMenuItem("Nombre de fichero personalizado");
	       
	        //Add components to pop-up menu
	        popup.add(showItem);
	        popup.addSeparator();
	        popup.add(options);
	        options.add(OpcMinimize);
	        options.add(OpcPath);
	        options.add(OpcName);
	        popup.addSeparator();
	        popup.add(exitItem);
	       
	        trayIcon.setPopupMenu(popup);
	       
	        try {
	            tray.add(trayIcon);
	        } catch (AWTException e) {
	            System.out.println("TrayIcon could not be added.");
	        }
        }
        listeners();
	}
	
	public void setPath(String path) {
		this.path = path;
		OpcPath.setState(true);
	}

	public String getPath() {
		return path+pathName;
	}
	
	public String getPathName() {
		return pathName;
	}
	
	public void listeners() {
		exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				exit();
            }
        });
		
		showItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.getFrame().setVisible(true);
            }
        });
		
		OpcPath.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie)
            {
            	if(OpcPath.getState()) {
            		path = JOptionPane.showInputDialog("Escriba la ruta donde quiere guardar el fichero");
            		if(path != null) {
            			main.setSave_text(path+pathName);
            		}
            		else {
            			OpcPath.setState(false);
            		}
            	}
            	else {
            		path = ("C:\\Users\\"+System.getProperty("user.name")+"\\Desktop");
            		main.setSave_text(path+pathName);
            	}
            }
        });
		
		OpcName.addItemListener(new ItemListener(){
            public void itemStateChanged(ItemEvent ie)
            {
            	if(OpcName.getState()) {
            		pathName = JOptionPane.showInputDialog("Escriba el nombre del fichero");
            		if(pathName != null) {
            			pathName= "\\"+pathName+".txt";
            			main.setSave_text(path+pathName);
            		}
            		else{
            			OpcName.setState(false);
            		}
            	}
            	else {
            		pathName = ("\\ParseLog.txt");
            		main.setSave_text(path+pathName);
            	}
            }
        });
		
		trayIcon.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	main.getFrame().setVisible(true);
            }
        });
	}
	public boolean getOpcMinimize() {
		return OpcMinimize.getState();
	}

	public void setOpcMinimize(boolean opcMinimize) {
		OpcMinimize.setState(opcMinimize);
	}

	public void saveOptions() {
		FileWriter fichero = null;
		try
        {
			new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\Parse_Log").mkdirs();
            fichero = new FileWriter("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\Parse_Log\\Data_Option.txt");
            PrintWriter pw = new PrintWriter(fichero);
            
            pw.println(OpcMinimize.getState());
            pw.println(OpcPath.getState());
            if(OpcPath.getState()) {
            	pw.println(path);
            }
            else {
            	pw.println("C:\\Users\\"+System.getProperty("user.name")+"\\Desktop");
            }
            pw.println(OpcName.getState());
            if(OpcName.getState()) {
            	pw.println(pathName);
            }
            else {
            	pw.println("\\ParseLog.txt");
            }
            pw.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           try {

           if (null != fichero)
              fichero.close();
           } catch (Exception e2) {
              e2.printStackTrace();
           }
        }
	}

	public void loadFiles() {
		
		FileReader fr = null;
		try {
			
			File archivo = new File ("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\Parse_Log\\Data_Option.txt");
	        fr = new FileReader (archivo);
	        BufferedReader br = new BufferedReader(fr);

	        OpcMinimize.setState(Boolean.valueOf(br.readLine()));
	        OpcPath.setState(Boolean.valueOf(br.readLine()));
            path= br.readLine();
            OpcName.setState(Boolean.valueOf(br.readLine()));
            pathName= br.readLine();
	        br.close();

	      }
	      catch(FileNotFoundException e){
	    	  System.out.println("ARCHIVO NO ENCONTRADO");
	      } catch (IOException e) {
			e.printStackTrace();
		}finally{
			
			if (path==null){
				path = ("C:\\Users\\"+System.getProperty("user.name")+"\\Desktop");
			}
			if(pathName == null) {
				pathName = ("\\ParseLog.txt");
			}
	         try{                    
	            if( null != fr ){   
	               fr.close();     
	            }                  
	         }catch (Exception e2){ 
	            e2.printStackTrace();
	         }
	      }
	}
	
	public void exit() {
		saveOptions();
		System.exit(0);
	}
}
