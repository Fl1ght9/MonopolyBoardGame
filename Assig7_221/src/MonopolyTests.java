import org.junit.*;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import java.util.*;

import swen221.monopoly.*;
import swen221.monopoly.GameOfMonopoly.InvalidMove;

public class MonopolyTests {
	// this is where you must write your tests; do not alter the package, or the
	// name of this file. An example test is provided for you.

	//==================================================================
	@Rule
	public ExpectedException except = ExpectedException.none();

	//==================================================================
	// Checks for a invalid street name
	@Test
	public void boardInvalidName_1() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location newLoc = board.findLocation("Cool Street");
		assertTrue(newLoc == null);
	}

	//==================================================================
	// Check start is go
	@Test
	public void boardStart_1() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location newLoc = board.findLocation("Go");
		Location loc = board.getStartLocation();
		assertTrue(newLoc == loc);
	}

	//==================================================================
	// Check go has no owner
	@Test
	public void specialArea_1() {
		RuntimeException e = null;
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location newLoc = board.findLocation("Go");
		SpecialArea spec = (SpecialArea) newLoc;
		assertTrue(spec.hasOwner() == false);
		try {
			spec.getOwner();
		} catch (RuntimeException ex) {
			e = ex;
		}
		assertTrue(e instanceof RuntimeException);
	}

	// Check go has no rent
	@Test
	public void specialArea_2() {
		RuntimeException e = null;
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location newLoc = board.findLocation("Go");
		SpecialArea spec = (SpecialArea) newLoc;
		try {
			spec.getRent();
		} catch (RuntimeException ex) {
			e = ex;
		}
		assertTrue(e instanceof RuntimeException);
	}

	//==================================================================
	// Check player who owns multiple stations is correct
	@Test
	public void testStation_1() throws GameOfMonopoly.InvalidMove {		
		Player player = newP("Dave", "Dog", 1500, "Kings Cross Station");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		game.buyProperty(player);
		Location station1 = board.findLocation("Kings Cross Station");
		Location station2 = board.findLocation("Marylebone Station");
		player.setLocation(station2);
		game.buyProperty(player);
		Station stat1 = (Station) station1;
		Station stat2 = (Station) station2;
		stat1.setOwner(player);
		stat2.setOwner(player);
		assertTrue(stat1.getRent() == 100);
	}

	@Test
	// Check the station rent is correct
	public void testStation_2() throws GameOfMonopoly.InvalidMove {
		Player player = newP("Dave", "Dog", 1500, "Kings Cross Station");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		game.buyProperty(player);
		Location location = board.findLocation("Kings Cross Station");
		Station stat = (Station) location;
		stat.setOwner(player);
		assertTrue(stat.getRent() == 50);
	}

	//==================================================================
	@Test
	// Check utility rent is correct
	public void testUtility_1() throws GameOfMonopoly.InvalidMove {
		Player player = newP("Dave", "Dog", 1500, "Electric Company");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		game.buyProperty(player);
		Location location = board.findLocation("Electric Company");
		Utility stat = (Utility) location;
		stat.setOwner(player);
		assertTrue(stat.getRent() == 75);
	}

	@Test
	// Check player owning multiple utilities has the right rent
	public void testUtility_2() throws GameOfMonopoly.InvalidMove {
		Player player = newP("Dave", "Dog", 1500, "Electric Company");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		game.buyProperty(player);
		Location location = board.findLocation("Electric Company");
		Location location2 = board.findLocation("Water Works");
		player.setLocation(location2);
		game.buyProperty(player);
		Utility stat = (Utility) location;
		Utility stat2 = (Utility) location2;
		stat.setOwner(player);
		stat2.setOwner(player);
		assertTrue(stat.getRent() == 150);
	}

	//==================================================================
	// Buy new property
	@Test
	public void testValidBuyProperty_1() {
		try {
			tryAndBuy(1500, "Park Lane");
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	//==================================================================
	// Check player location, piece, and balance
	@Test
	public void testPlayerInvalid_1() {
		Player player = newP("Dave", "Dog", 1500, "Park Lane");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location newLoc = board.findLocation("Mayfair");
		player.setLocation(newLoc);
		assertTrue(player.getLocation().equals(newLoc));
		assertTrue(player.getName().equals("Dave"));
		assertTrue(player.getToken().equals("Dog"));
		int sum = 500;
		player.deduct(sum);
		assertTrue(player.getBalance() == 1000);
	}

	// Check the move is correct
	@Test
	public void testPlayerInvalid_2() {
		Player player = newP("Dave", "Dog", 1500, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		game.movePlayer(player, 4);
		Location newLoc = board.findLocation("Whitechapel Road");
		assertTrue(player.getLocation().equals(newLoc));
	}

	// Check player cant buy twice
	@Test
	public void testPlayerInvalid_3() throws GameOfMonopoly.InvalidMove {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Player player = newP("Dave", "Dog", 1500, "Old Kent Road");
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		player.buy(prop);

		IllegalArgumentException e = null;
		try {
			player.buy(prop);
		} catch (IllegalArgumentException ex) {
			e = ex;
		}
		assertTrue(e instanceof IllegalArgumentException);
	}

	// Check player cant buy another property
	@Test
	public void testPlayerInvalid_4() throws GameOfMonopoly.InvalidMove {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Player player = newP("Dave", "Dog", 1500, "Old Kent Road");
		Player player2 = newP("Zyzz", "Car", 1500, "Old Kent Road");
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		player2.buy(prop);

		IllegalArgumentException e = null;
		try {
			player.sell(prop);
		} catch (IllegalArgumentException ex) {
			e = ex;
		}
		assertTrue(e instanceof IllegalArgumentException);
	}

	//==================================================================
	// Check player cant buy another players property
	@Test
	public void testInvalidPurchase_1() throws InvalidMove {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1500, "Whitechapel Road");
		Player player2 = newP("Zyzz", "Dog", 1500, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		game.buyProperty(player);
		Location loc = board.findLocation("Whitechapel Road");
		Property prop = (Property) loc;
		prop.setOwner(player);
		game.movePlayer(player2, 4);
		try {
			game.buyProperty(player2);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant buy with no money
	@Test
	public void testInvalidPurchase_2() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 0, "Whitechapel Road");
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			game.buyProperty(player);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant buy a special spot
	@Test
	public void testInvalidPurchase_3() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Go");
		GameOfMonopoly game = new GameOfMonopoly();
		try {
			game.buyProperty(player);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	//==================================================================
	@Test
	public void testInvalidSelling_1() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.setOwner(player);
		try {
			game.buyProperty(player);
			game.sellProperty(player, location);
		} catch (InvalidMove ex) {
			fail(ex.getMessage());
		}
	}

	// Check player cant sell a special
	@Test
	public void testInvalidSelling_2() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Go");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Go");
		try {
			game.sellProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant sell another players property
	@Test
	public void testInvalidSelling_3() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		Player player2 = newP("Zyzz", "Car", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.setOwner(player2);
		try {
			game.sellProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant sell a mortgaged property
	@Test
	public void testInvalidSelling_4() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.setOwner(player);
		prop.mortgage();
		try {
			game.sellProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}
	
	//==================================================================
	// Check player cant mortgage a special spot
	@Test
	public void testValidMortgage_1() {
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.setOwner(player);
		try {
			game.mortgageProperty(player, location);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	//==================================================================
	// Check player cant mortgage a mortgaged property
	@Test
	public void testInvalidMortgage_1() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.setOwner(player);
		prop.mortgage();
		try {
			game.mortgageProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant mortgage another players property
	@Test
	public void testInvalidMortgage_2() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		Player player2 = newP("Zyzz", "Car", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.setOwner(player2);
		try {
			game.mortgageProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant mortgage a special spot
	@Test
	public void testInvalidMortgage_3() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Jail");
		try {
			game.mortgageProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	//==================================================================
	// Check player cant mortgage a special spot
	@Test
	public void testValidUnmortgage_1() {
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.mortgage();
		prop.setOwner(player);
		try {
			game.unmortgageProperty(player, location);
		} catch (GameOfMonopoly.InvalidMove e) {
			fail(e.getMessage());
		}
	}

	//==================================================================
	// Check player cant unmortgage an unmortgaged property
	@Test
	public void testInvalidUnmortgage_1() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.setOwner(player);
		try {
			game.unmortgageProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant unmortgage another players property
	@Test
	public void testInvalidUnmortgage_2() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		Player player2 = newP("Zyzz", "Car", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.mortgage();
		prop.setOwner(player2);
		try {
			game.unmortgageProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant unamortgage a special spot
	@Test
	public void testInvalidUnmortgage_3() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Jail");
		try {
			game.unmortgageProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	// Check player cant unamortgage with insufficient funds
	@Test
	public void testInvalidUnmortgage_4() {
		Throwable e = null;
		Player player = newP("Dave", "Dog", 5, "Old Kent Road");
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Property prop = (Property) location;
		prop.mortgage();
		prop.setOwner(player);
		try {
			game.unmortgageProperty(player, location);
		} catch (InvalidMove ex) {
			e = ex;
		}
		assertTrue(e instanceof InvalidMove);
	}

	//==================================================================
	// Check street color matches
	@Test
	public void testStreetColor() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Street s = (Street) location;
		assertTrue(s.getColourGroup().getColour().equals("Brown"));
	}

	//==================================================================
	// Check the rent matches
	@Test
	public void testStreetRent() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Street s = (Street) location;
		assertTrue(s.getRent() == 2);
	}

	//==================================================================
	// Check the price matches
	@Test
	public void testStreetPrice() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Street s = (Street) location;
		assertTrue(s.getPrice() == 60);
	}

	//==================================================================
	// Check the num of houses
	@Test
	public void testStreetHouse() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Street s = (Street) location;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		assertTrue(s.getHouses() == 0);
	}

	//==================================================================
	// Check the num of hotels
	@Test
	public void testStreetHotels() {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation("Old Kent Road");
		Street s = (Street) location;
		Player player = newP("Dave", "Dog", 1000, "Old Kent Road");
		assertTrue(s.getHotels() == 0);
	}

	/**
	 * This is a helper function. Feel free to modify this as you wish.
	 */
	private void tryAndBuy(int cash, String locationName)
			throws GameOfMonopoly.InvalidMove {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation(locationName);
		Player player = new Player("Dave", "Dog", 1500, location);
		game.buyProperty(player);
	}

	// New player helper function
	private Player newP(String name, String token, int cash, String loc) {
		GameOfMonopoly game = new GameOfMonopoly();
		Board board = game.getBoard();
		Location location = board.findLocation(loc);
		Player player = new Player(name, token, cash, location);
		return player;
	}
}
