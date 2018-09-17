package ParseLog;

import java.awt.Choice;

public class Utils {

	public boolean isInList(Choice choice, String string) {
		
		for(int x=0; x<choice.getItemCount(); x++) {
			if(choice.getItem(x).equals(string)) {
				return true;
			}
		}
		return false;
	}
	
}
