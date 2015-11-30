import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.XMPPException;
import java.io.IOException;
import java.util.HashMap;
import javax.security.auth.callback.CallbackHandler;
import javax.security.sasl.Sasl;
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.Base64;

/*
public class hello {
	
	public static void main(String[] args) throws NoSuchAlgorithmException
	{
		
		
		System.out.println("hello");
		
		SASLDigestMD5Mechanism mySASLDigestMD5Mechanism= new SASLDigestMD5Mechanism(null);
		//sASLDigestMD5Mechanism.
		
		
		
		
		
		
		
		
		//Connection connection = new XMPPConnection("jabber.org");
		//Connection connection = new XMPPConnection("chat.facebook.com");
		try
		{
			
			SASLAuthentication.registerSASLMechanism("DIGEST-MD5", mySASLDigestMD5Mechanism.getClass());
			ConnectionConfiguration config = new ConnectionConfiguration("chat.facebook.com", 5222);
			Connection connection = new XMPPConnection(config);
			connection.connect();
			System.out.println("hello2");
			connection.login("atul.pandey2", "myaccount");
			
			//connection.connect();
			System.out.println("hello3");
			
			// MD5 sum
			/*MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
		    digest.update("myaccount".getBytes());
		    //digest.up
		    byte[] hash = digest.digest();*/
			/*StringBuffer hexString = new StringBuffer();
			byte[] defaultBytes = "myaccount".getBytes();
			try{
				MessageDigest algorithm = MessageDigest.getInstance("MD5");
				algorithm.reset();
				algorithm.update(defaultBytes);
				byte messageDigest[] = algorithm.digest();
			            
				
				for (int i=0;i<messageDigest.length;i++) {
					hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
				}
				String foo = messageDigest.toString();
			//	System.out.println("sessionid "+sessionid+" md5 version is "+hexString.toString());
			//	sessionid=hexString+"";
			}catch(NoSuchAlgorithmException nsae){
			            
			}
		    
			//connection.login("testatul2", "testtest");
			System.out.println(hexString.toString());
			connection.login("atulpandey88@rediffmail.com", hexString.toString());*/
		/*	System.out.println("hello3");
			Chat chat = connection.getChatManager().createChat("testatul@jabber.org", 
				new MessageListener() 
				{
			    	public void processMessage(Chat chat, Message message)
			    	{
			    		System.out.println("Received message: " + message);
			    	}
				});
			System.out.println("hello");
			chat.sendMessage("Neeraj darling!");
			
			Roster roster = connection.getRoster();
			Collection<RosterEntry> entries = roster.getEntries();
			// System.out.println(roster.getPresences(null));
			//Presence pr=roster.getPresence(null);
			//System.out.println(pr.getStatus();
			for (RosterEntry entry : entries)
			{
				//entry.
			    System.out.println(entry.getUser());
			   // System.out.println(roster.getPresence(""));
			    
			    System.out.println(entry.getStatus()); //TODO:: Get status 
			}
			
			
			/*Message newMessage = new Message();
			newMessage.setBody("Howdy!");
			newMessage.setProperty("favoriteColor", "red");
			chat.sendMessage(newMessage);
			*/
			// presence
		/*	Presence presence = new Presence(Presence.Type.unavailable);
			presence.setStatus("Gone fishing");
			// Send the packet (assume we have a Connection instance called "con").
			connection.sendPacket(presence);
					
					
		}
		catch(XMPPException x)
		{
			System.out.println("Exception"+x.getLocalizedMessage());
		}
	}

}
*/

public class MySASLDigestMD5Mechanism extends SASLMechanism
{	
    public MySASLDigestMD5Mechanism(SASLAuthentication saslAuthentication)
    {
        super(saslAuthentication);
    }
 
    protected void authenticate() throws IOException, XMPPException
    {
        String mechanisms[] = {
            getName()
        	};
        java.util.Map props = new HashMap();
        sc = Sasl.createSaslClient(mechanisms, null, "xmpp", hostname, props, this);
        super.authenticate();
    }
 
    public void authenticate(String username, String host, String password)
        throws IOException, XMPPException
    {
        authenticationId = username;
        this.password = password;
        hostname = host;
        String mechanisms[] = {
            getName()
        };
        java.util.Map props = new HashMap();
        sc = Sasl.createSaslClient(mechanisms, null, "xmpp", host, props, this);
        super.authenticate();
    }
 
    public void authenticate(String username, String host, CallbackHandler cbh)
        throws IOException, XMPPException
    {
        String mechanisms[] = {
            getName()
        };
        java.util.Map props = new HashMap();
        sc = Sasl.createSaslClient(mechanisms, null, "xmpp", host, props, cbh);
        super.authenticate();
    }
 
    protected String getName()
    {
        return "DIGEST-MD5";
    }
 
    public void challengeReceived(String challenge)
        throws IOException
    {
        byte response[];
        if(challenge != null)
            response = sc.evaluateChallenge(Base64.decode(challenge));
        else
            response = sc.evaluateChallenge(new byte[0]);
 
        Packet responseStanza;
        if (response == null)
        {
        	responseStanza = new Response();
        }
        else
        {
                responseStanza = new Response(Base64.encodeBytes(response,Base64.DONT_BREAK_LINES));   
        }
        getSASLAuthentication().send(responseStanza);
    }
}



