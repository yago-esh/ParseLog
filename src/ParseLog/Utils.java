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
	
	public boolean removeChoice(Choice choice, String string) {
		
		for(int x=0; x<choice.getItemCount(); x++) {
			if(choice.getItem(x).indexOf(string)!=-1){
				if(choice.getItem(x).indexOf("unknow")!=-1) {
					choice.remove(x);
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
}
