package main;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Scanner;

public class Inventory {
	HashMap<String, Integer> inventory;
	File myObj;
	String filepath;
	List<String> list;
	int temp;
	FileWriter myWriter;
	String name;
	int passcode;
	Client client;
	Response response;
	String RESPONSE;
	public Inventory() {
		try {
			list = new ArrayList<String>();
			myObj = new File("inventory.txt");
			myObj.createNewFile();
			filepath =myObj.getName();
			inventory = new HashMap<String, Integer>();
	    } catch (IOException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }

		
		
	}
	private boolean isValid(String urlusable) {
		String input = urlusable.replaceAll(" ", "%20");
		try {
			if (input.equals("7")) {
				throw new NumberFormatException("uwu");
			}
			passcode= Integer.parseInt(input);
			try {
				getDataPasscode();
			}catch (InvalidCardException e) {
				return false;
			}
			
            // Print and display
		}catch (NumberFormatException ex) {
			name = input;
			try {
				getDataName();
			}catch (InvalidCardException e) {
				return false;
			}
		}
		return true;
	}
	private String readName(String Input) {
		int substringIndexStart = Input.indexOf("\"name\"");
		int substringIndexEnd = Input.indexOf(',', substringIndexStart);
		String cardName = Input.substring(substringIndexStart+8, substringIndexEnd-1);
		return cardName;
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
	public void add(String urlusable) {
		
		if (isValid(urlusable)) {
			temp = 0;
			if (inventory.containsKey(name)) {
				temp = inventory.get(name);
				temp ++;
				if(inventory.put(name,temp) == temp-1) {
					System.out.println("added successfully");
				}			
			}
			else {
				temp =1;
				if(inventory.put(name,temp) == null) {
					System.out.println("added successfully");
				}
			}
		}
				
	}
	public void tester() {
		for (Map.Entry<String,Integer> mapElement : inventory.entrySet()) {
            String key = mapElement.getKey();
            
            // Adding some bonus marks to all the students
            int value = (mapElement.getValue());
 
            // Printing above marks corresponding to
            // students names
            System.out.println(key + " : " + value);
        }
	}
	public void toFile(){
		try {
			myWriter = new FileWriter(filepath);
			for (Entry<String, Integer> mapElement : inventory.entrySet()) {
				System.out.println(mapElement.getKey()+":"+mapElement.getValue());
	            myWriter.write(mapElement.getKey()+":"+mapElement.getValue()+"\n");
	            
	        }
			myWriter.close();
			System.out.println("printed successfully");
		}catch(IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
		
		
		
	}
	public void fromFile() {
		try {
		Scanner myReader = new Scanner(myObj);
	      while (myReader.hasNextLine()) {
	        String data = myReader.nextLine();
	        int substringIndexStart = data.indexOf(":");
	        name = data.substring(0, substringIndexStart);
	        String temp = data.substring(substringIndexStart+1,data.length());
	        int count = Integer.parseInt(temp);
	        if (isValid(name)) {
	        	inventory.put(name, count);
	        }
	        
	      }
	      myReader.close();
	    } catch (FileNotFoundException e) {
	      System.out.println("An error occurred.");
	      e.printStackTrace();
	    }
	}
	public void compareYDK(String path) {
		HashMap<String, Integer> inputDeck = new HashMap<String, Integer>();
		ArrayList<String> removals = new ArrayList<String>();
		File f = new File(path);
		Scanner sc;
		try {
			sc = new Scanner (f);
			while(sc.hasNextLine()) {
				String fileline = sc.nextLine();
				try {
					passcode = Integer.parseInt(fileline);
					if (isValid(Integer.toString(passcode))) {
						temp = 0;
						if (inputDeck.containsKey(name)) {
							temp = inputDeck.get(name);
							temp ++;
							if(inputDeck.put(name,temp) == temp-1) {
							}			
						}
						else {
							temp =1;
							if(inputDeck.put(name,temp) == null) {
							}
						}
						
					}
				}catch(NumberFormatException ex) {
					continue;
				}
			}
			for (Entry<String, Integer> mapElement : inputDeck.entrySet()) {
				if ((inventory.containsKey(mapElement.getKey()))) {
					if(inventory.get(mapElement.getKey())>=mapElement.getValue()) {
						removals.add(mapElement.getKey());
					}
					else {
						inputDeck.put(mapElement.getKey(),(mapElement.getValue()-inventory.get(mapElement.getKey())));
					}
				}
	            
			}
			for (String i : removals) {
				inputDeck.remove(i);
			}
			if (!(inputDeck.isEmpty())) {
				System.out.println("found 1 or more missing cards");
				cardDifference(inputDeck);
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("file not found");
			e.printStackTrace();
		}	
	}
	private void printHashmap(HashMap<String,Integer> input) {
		for (Map.Entry<String,Integer> mapElement : input.entrySet()) {
            String key = mapElement.getKey();
            int value = (mapElement.getValue());
 
            System.out.println(value + " x " + key);
        }
	}
	private void openTCG(HashMap<String,Integer> input) {
		String urlbuilderstart = "https://www.tcgplayer.com/massentry?c=";
		String urlbuilderend = "&productline=YuGiOh";
		String urlbuildermiddle = "";
		for (Map.Entry<String,Integer> mapElement : input.entrySet()) {
            urlbuildermiddle+=Integer.toString(mapElement.getValue())+" "+mapElement.getKey()+"%7C%7C";
        }
		String finalmiddle = urlbuildermiddle.replaceAll(" ", "%20");
		Desktop desk = Desktop.getDesktop();
        
        // now we enter our URL that we want to open in our
        // default browser
        try {
			desk.browse(new URI(urlbuilderstart+finalmiddle+urlbuilderend));
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void cardDifference(HashMap<String,Integer> input) {
		System.out.println("Choose a function\n1.View cards\n2.Open in TCG player");
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		switch(choice) {
		case 1:
			printHashmap(input);
			cardDifference(input);
			break;
		case 2:
			openTCG(input);
			cardDifference(input);
			break;
			
			
		}
	}

	
	
}
