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
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URL;

public class Systray {
	
	private MenuItem exitItem, showItem;
	private TrayIcon trayIcon;
	private CheckboxMenuItem OpcMinimize, OpcSilence;
	
	public Systray() {
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
	        OpcSilence = new CheckboxMenuItem("Silenciar voz");
	       
	        //Add components to pop-up menu
	        popup.add(showItem);
	        popup.addSeparator();
	        popup.add(options);
	        options.add(OpcMinimize);
	        options.add(OpcSilence);
	        popup.addSeparator();
	        popup.add(exitItem);
	       
	        trayIcon.setPopupMenu(popup);
	       
	        try {
	            tray.add(trayIcon);
	        } catch (AWTException e) {
	            System.out.println("TrayIcon could not be added.");
	        }
        }
	}
	
	public void saveOptions() {
		FileWriter fichero = null;
		try
        {
			new File("C:\\Users\\"+System.getProperty("user.name")+"\\Documents\\Virtual_Totem").mkdirs();
			URL url = System.class.getResource("/options/options.txt");
			System.out.println("What URL: "+url);
			System.out.println("What URL.toString: "+url.toString().substring(6));
			fichero = new FileWriter(url.toString().substring(6));
            PrintWriter pw = new PrintWriter(fichero);
            
            pw.println("que pasa loco");
            pw.println("que pasa loco");
            pw.println("que pasa loco");
            pw.close();
//            for (int x=0; x<options.length; x++) {
//                pw.println(options[x]);
//            }

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

}
