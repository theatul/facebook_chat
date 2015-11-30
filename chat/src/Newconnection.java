import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HashMap;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.Connection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterListener;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;

import utilities.ConstantReader;

/**
*  @Name:  Newconnection.java          
*  @Programmer:  Atul Pandey
*  @Creation Date:  Jan 11, 2012
*
*  This class is responsible for creating new connections and listeners.
*
*/

public class Newconnection 
{
	Connection connection;
	HashMap availableChatMap = new HashMap(); // userid,chat
	HashMap busyChatMap = new HashMap(); // userid,chat
	HashMap userChatMap=new HashMap(); // userid,userid
	
	Newconnection()
	{
		//Dummy for now
	}
	
	void createconnection() throws XMPPException
    {
	 	SASLAuthentication.registerSASLMechanism("DIGEST-MD5",MySASLDigestMD5Mechanism. class);
        ConnectionConfiguration config = new ConnectionConfiguration("chat.facebook.com",5222);
 
        config.setSASLAuthenticationEnabled(true);
        config.setRosterLoadedAtLogin (true);
 
        connection = new XMPPConnection(config);
        connection.connect();
        System.out.println("hello");
        
        // Presence Listener
        Roster roster = connection.getRoster();
        roster.addRosterListener(new RosterListener() {
            // Ignored events public void entriesAdded(Collection<String> addresses) {}
            public void entriesDeleted(Collection<String> addresses) {}
            public void entriesUpdated(Collection<String> addresses) {}
            public void presenceChanged(Presence presence)
            {
                System.out.println("Presence changed: " + presence.getFrom() + " " + presence);
                if (presence.toString().contentEquals(ConstantReader.AVAILABLE))
                {
                	createChatObject(presence.getFrom().toString());
                }
                else if (presence.toString().contentEquals(ConstantReader.UNAVAILABLE))
                {
                	if (availableChatMap.containsKey(presence.getFrom()))
                		availableChatMap.remove(presence.getFrom());
                	if (busyChatMap.containsKey(presence.getFrom()))
                	{
                		busyChatMap.remove(presence.getFrom());
                		// TODO : Remove from user map too and appropriate message to other user
                	}
                }
            }
			@Override
			public void entriesAdded(Collection<String> arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        connection.login("atul.tes", "myaccount");
        System.out.println("hello2");
    }

    void createChatObject(String userid)
    {
    	// Send message
    	System.out.println("hello3");
    	MessageListener messageListener=new MessageListener() 
		{
	    	public void processMessage(Chat chat, Message message)
	    	{
	    		
	    		if(!message.getBody().toString().isEmpty())
	    		{
	    			System.out.println("Received message: " + message.getBody()+"from "+message.getFrom());
	    			if (availableChatMap.containsKey(message.getFrom()))
	    			{
	    				System.out.println("1");
	    				Chat ch= (Chat) availableChatMap.get(message.getFrom());
	    				if (availableChatMap.size()>=2)
	    				{	
	    					System.out.println("2");
		    				busyChatMap.put(message.getFrom(), ch);
		    				availableChatMap.remove(message.getFrom());
		    				
		    				String user2= availableChatMap.keySet().toArray()[0].toString(); //TODO : A Random algo to get users
		    				userChatMap.put(message.getFrom(),user2);
		    				busyChatMap.put(user2,availableChatMap.get(user2));
		    				availableChatMap.remove(user2);
		    				
		    				System.out.println("3");
		    				Chat chUser2=(Chat) busyChatMap.get(user2);
		    				try {
								chUser2.sendMessage(message.getBody());
							} catch (XMPPException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
		    				
		    				try {
								ch.sendMessage("Paired");
							} catch (XMPPException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	    				}
	    			}
	    		}
	    		try
	    		{
					chat.sendMessage(message.getBody());
				}
	    		catch (XMPPException e)
	    		{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	    	}
		};
		
		// Create chat object for each online user
		Chat chat = connection.getChatManager().createChat(userid, messageListener);	
		availableChatMap.put(userid, chat);
		System.out.println("Size::"+availableChatMap.size());
		
			try 
			{
				System.out.println("hello");
				if(false)
				{
					chat.sendMessage("testtest!");	
				}
			}
			catch (XMPPException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }
    
	public static void main(String[] args) throws NoSuchAlgorithmException, XMPPException
	{     
		Newconnection newconnection =new Newconnection();
		newconnection.createconnection();
		while(true){}
		//newconnection.createChatObject("-100002392965888@chat.facebook.com");
	}
	
}
