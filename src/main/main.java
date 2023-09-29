package main;

import java.util.Scanner;

public class main {
	static Inventory inv;
	public static void main(String[] args) throws InvalidCardException {
		// TODO Auto-generated method stub
		//to avoid errors in the middle
		inv = new Inventory();
		inv.fromFile();
		choices();
		}
	private static void choices() {
		
		System.out.println("Functions:\n1.Add cards to inventory\n2.See if you have the cards in a deck\n3.Print inventory to file\n4.Print inv");
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		int choice = sc.nextInt();
		//add to inv
		
		String input = "";
		sc.nextLine();
		switch(choice) {
		case 1:
			System.out.println("Enter the names of the cards make sure to press enter between card names");
			System.out.println("press enter without typing to end");
			do {
				input = sc.nextLine();
				if(!input.equals("")) {
					inv.add(input);
				}
			}while(!(input.equals("")));
			choices();
			break;
		case 2:
			System.out.println("input ydk file path");
			inv.compareYDK(sc.nextLine());
			break;
		case 3:
			inv.toFile();
			choices();
			break;
		case 4:
			inv.tester();
			choices();
			break;
		default:
			System.out.println("invalid choice");
			choices();
			break;
		}
		
			
	}
}

