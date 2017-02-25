

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;

import de.stefan1200.jts3serverquery.JTS3ServerQuery;
import de.stefan1200.jts3serverquery.TS3ServerQueryException;
import de.stefan1200.jts3serverquery.TeamspeakActionListener;

public class ServerQuery  implements TeamspeakActionListener  {
	List<String> auth = new ArrayList<String>();
	String mypseudo;
    HashMap<String,Integer> pt = new HashMap<String,Integer>();
	//HashMap<String, Integer> pt = new HashMap();
	static JTS3ServerQuery query;
	String last;
	int chan;
	boolean debug = false; 
	
	public void addtext(String massg) {
		Main.ChatBox.append(massg+"\n");
	}
	
	void outputHashMap(HashMap<String, String> hm, PrintStream stream) {
		if (hm == null) {
			return;
		}

		Collection<String> cValue = hm.values();
		Collection<String> cKey = hm.keySet();
		Iterator<String> itrValue = cValue.iterator();
		Iterator<String> itrKey = cKey.iterator();

		while (itrValue.hasNext() && itrKey.hasNext()) {
			stream.println(itrKey.next() + ": " + itrValue.next());
		}
	}

	public void teamspeakActionPerformed(String eventType, HashMap<String, String> eventInfo) {
		if (debug)
			System.out.println(eventType + " received");
		if (debug)
			outputHashMap(eventInfo, System.out);

		if (eventType.equals("notifytextmessage")) {
			addtext(eventInfo.get("invokername") + " - " + eventInfo.get("msg"));
			if (auth.contains(eventInfo.get("invokerid"))) {

				if (eventInfo.get("msg").startsWith("!shutdown")) // Quit
																		// this
																		// program
				{
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Bye Bye!");
						query.removeTeamspeakActionListener();
						query.closeTS3Connection();
					} catch (Exception e) {
						e.printStackTrace();
						try {
							query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
									JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
						} catch (Exception e2) {
							/* do nothing */ }
					}
					System.exit(0);

				}
			
			if (eventInfo.get("msg").startsWith("a")) {
				try {
					if (last == null) {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Aucune demande en attente !");
						return;
					}
					auth.add(last);
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Vous avez accepté la demande !");
					query.sendTextMessage(Integer.parseInt(last), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT,
							"Votre demande a été accepté ! o/");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }
				}
			}
			if (eventInfo.get("msg").startsWith("b")) {
				try {
					if (last == null) {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Aucune demande en attente !");
						return;
					}

					last = null;
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Vous avez refusé la demande !");
					query.sendTextMessage(Integer.parseInt(last), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT,
							"Votre demande a été refusé ! :'c");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						 }
				}
			}if (eventInfo.get("msg").startsWith("!add")) {
				try {
					String comm = eventInfo.get("msg");
					comm = comm.replaceFirst("!add ", "");
					if(!pt.containsKey(comm)){
						pt.put(comm, 1);
					}
					else{
						pt.put(comm, pt.get(comm)+1);
					}
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Vous avez ajouté 1 point à "+comm);
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }
				}
			}
			if(eventInfo.get("msg").startsWith("!channlist")){
				Vector<HashMap<String, String>> dataChannelList;
				
				try {
					dataChannelList = query.getList(JTS3ServerQuery.LISTMODE_CHANNELLIST);
				
				StringBuffer sb = new StringBuffer();
				for (HashMap<String, String> hashMap : dataChannelList)
				{
					if (debug)
						outputHashMap(hashMap, System.out);
					if (sb.length() > 0)
					{
						sb.append(" - "+hashMap.get("cid")+"\n ");
					}
					sb.append(hashMap.get("channel_name"));

				}
				query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")), JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Channel List : " + sb.toString());
				} catch (TS3ServerQueryException e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }				}
			}
			if (eventInfo.get("msg").startsWith("!join")) {
				try {
						
					query.moveClient(query.getCurrentQueryClientID(), chan, null);
					
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "J'ai rejoin votre channel !");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }
				}
			}
			if (eventInfo.get("msg").startsWith("!bt")) {
				try {

				    Set<Entry<String, Integer>> set = pt.entrySet();

				    List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
				            set);

				    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

				        @Override
				        public int compare(Entry<String, Integer> o1,
				                Entry<String, Integer> o2) {

				            return o2.getValue().compareTo(o1.getValue());
				        }

				    });
				    String top = list.subList(0, pt.size()).toString();
				    top = top.replaceAll(",", "\n");
				    top = top.replaceAll("=", " - ");
				    top = top.replace("[", "");
				    top = top.replace("]", "");
					query.sendTextMessage(chan,
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, "\n_____________\n \n"+top+"\n____");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(chan,
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CHANNEL, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }
				}
		}
		} 
			if (eventInfo.get("msg").startsWith("!help")) {
				try {
						
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "\n------\n  - !auth - Avoir les permissions pour m'administrer \n  - !top - Voir le TOP\n  ---- Commande avec permission: \n  - a - Accepter la dernière demande d'autorisation\n  - b - Refuser la dernière demande d'autorisation\n  - !join - Me faire rejoindre votre channel\n  - !add pseudo - Ajoute un point à pseudo\n  - !shutdown - M'éteindre\n  - !channlist - Liste des channels ainsi que leur ID\n  - !bt - Afficher le top dans le chat du channel\n----");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }
				}
			}
			if (eventInfo.get("msg").startsWith("!auth")) // Quit this
																// program
			{
				try {
					if (auth.contains(mypseudo)){
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Vous avez déjà les autorisations...");
						return;
					}
					if (eventInfo.get("invokername").equals(mypseudo)){
						auth.add(eventInfo.get("invokerid"));
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Autorisation accepté !");
						return;
					}
					

					last = eventInfo.get("invokerid");
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "Attente des autorisations...");
					query.sendTextMessage(3, JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, eventInfo.get("invokername")
							+ " souhaite avoir les autorisations... \na - pour accepter\nb - pour refuser");// 2452
					last = eventInfo.get("invokerid");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }
				}

			}if (eventInfo.get("msg").startsWith("!top")) {
				try {

				    Set<Entry<String, Integer>> set = pt.entrySet();

				    List<Entry<String, Integer>> list = new ArrayList<Entry<String, Integer>>(
				            set);

				    Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {

				        @Override
				        public int compare(Entry<String, Integer> o1,
				                Entry<String, Integer> o2) {

				            return o2.getValue().compareTo(o1.getValue());
				        }

				    });
				    String top = list.subList(0, pt.size()).toString();
				    top = top.replaceAll(",", "\n");
				    top = top.replaceAll("=", " - ");
				    top = top.replace("[", "");
				    top = top.replace("]", "");
					query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
							JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "\n_____________\n \n"+top+"\n____");
				} catch (Exception e) {
					e.printStackTrace();
					try {
						query.sendTextMessage(Integer.parseInt(eventInfo.get("invokerid")),
								JTS3ServerQuery.TEXTMESSAGE_TARGET_CLIENT, "An error occurred: " + e.toString());
					} catch (Exception e2) {
						/* do nothing */ }
				}
		}
		}
	}

	void runServerMod(String srv, String user, String mdp, String name,String pseudo,int cc) throws Exception {
		query = new JTS3ServerQuery();

		try {
			// Connect to TS3 Server, set your server data here
			query.connectTS3Query(srv, 10011);
			chan = cc;
			// Login with an server query account. If needed, uncomment next
			// line!
			query.loginTS3(user, mdp);

			// Set our class for receiving events
			query.setTeamspeakActionListener(this);

			// Select virtual Server
			query.selectVirtualServer(1);

			// Register some events
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTSERVER, 0); // Server
																			// Chat
																			// event
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTCHANNEL, 0); // Channel
																				// Chat
																				// event
			query.addEventNotify(JTS3ServerQuery.EVENT_MODE_TEXTPRIVATE, 0); // Private
																				// Chat
																				// event
			query.setDisplayName(name);
			mypseudo = pseudo;
		} catch (TS3ServerQueryException sqe) {
			System.err
					.println("An error occurred while connecting to the TS3 server, stopping now! More details below.");

			if (sqe.getFailedPermissionID() >= 0) {
				HashMap<String, String> permInfo = null;
				try {
					permInfo = query.getPermissionInfo(sqe.getFailedPermissionID());
					Main.ChatBox.append("Missing permission\n ");
					outputHashMap(permInfo, System.err);
				} catch (Exception e) {
				}
			}

			throw sqe;
		} catch (Exception e) {
			System.err
					.println("An error occurred while connecting to the TS3 server, stopping now! More details below.");
			throw e;
		}

		
	}
}