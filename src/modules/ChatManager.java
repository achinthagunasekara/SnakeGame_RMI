/**
 * @author: Archie Gunasekara
 * @date: 2013.07.02
 */

package modules;

import java.io.Serializable;
import java.util.ArrayList;

public class ChatManager implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private ArrayList<String> messages;

	public ChatManager() {
		
		messages = new ArrayList<String>();
	}
	
	public void addMessage(String userName, String msg) {
		
		messages.add("<" + userName + " Said> : " + msg);
	}
	
	public ArrayList<String> getMessages() {
		
		return messages;
	}
}
