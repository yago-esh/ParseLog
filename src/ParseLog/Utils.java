package ParseLog;

import java.awt.Choice;

public class Utils {

	public boolean isInList(Choice choice, String string) {
		
		for(int x=0; x<choice.getItemCount(); x++) {
			if(choice.getItem(x).indexOf(string)!=-1){
				return true;
			}
		}
		return false;
	}
	
	public void removeChoice(Choice choice, String string) {
		
		for(int x=0; x<choice.getItemCount(); x++) {
			if(choice.getItem(x).indexOf(string)!=-1){
				choice.remove(x);
				break;
			}
		}
	}
	
}
