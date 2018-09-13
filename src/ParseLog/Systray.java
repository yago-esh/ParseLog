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

}
