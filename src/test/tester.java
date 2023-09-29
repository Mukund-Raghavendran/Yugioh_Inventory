package test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class tester {
	int passcode;
	static String name;
	static Client client;
	Response response;
	static String RESPONSE;
	public static void main(String[]args) {
		try {
			client = ClientBuilder.newClient();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Response response = client.target("https://db.ygoprodeck.com/api/v7/cardinfo.php?name="+"me")
		  .request(MediaType.TEXT_PLAIN_TYPE)
		  .get();
		RESPONSE = response.readEntity(String.class);
		System.out.println(RESPONSE);
	}
}
