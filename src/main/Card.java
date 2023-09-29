package main;


import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.MediaType;


public class Card {
	int passcode;
	String name;
	Client client;
	Response response;
	String RESPONSE;
	public Card(String input) throws InvalidCardException {
		try {
			if (input.equals("7")) {
				throw new NumberFormatException("uwu");
			}
			passcode= Integer.parseInt(input);
			getDataPasscode();
            // Print and display
		}catch (NumberFormatException ex) {
			name = input;
			getDataName();
		}
	}

	private void getDataName() throws InvalidCardException {

		try {
			 client = ClientBuilder.newClient();
			Response response = client.target("https://db.ygoprodeck.com/api/v7/cardinfo.php?name="+name)
			  .request(MediaType.TEXT_PLAIN_TYPE)
			  .get();
			RESPONSE = response.readEntity(String.class);
			if (RESPONSE.contains("No card matching your query was found in the database")) {
				throw new InvalidCardException("this is not a valid card");
			}
			name = readName(RESPONSE);
		}catch(Exception E) {
			throw new InvalidCardException("this is not a valid card");
		}
		
	}
	

	private void getDataPasscode() throws InvalidCardException {

		try {
			client = ClientBuilder.newClient();
			response = client.target("https://db.ygoprodeck.com/api/v7/cardinfo.php?id="+passcode)
			  .request(MediaType.TEXT_PLAIN_TYPE)
			  .get();
			 RESPONSE = response.readEntity(String.class);
			 if (RESPONSE.contains("No card matching your query was found in the database")) {
					throw new InvalidCardException("this is not a valid card");
				}
			name = readName(RESPONSE);
		}catch(Exception E) {
			throw new InvalidCardException("this is not a valid card");
		}
		
	}
	private String readName(String Input) {
		int substringIndexStart = Input.indexOf("\"name\"");
		int substringIndexEnd = Input.indexOf(',', substringIndexStart);
		String cardName = Input.substring(substringIndexStart+8, substringIndexEnd-1);
		return cardName;
	}
	public String getName() {
		return name;
	}
	public Integer getPass() {
		return passcode;
	}
	
	
}
