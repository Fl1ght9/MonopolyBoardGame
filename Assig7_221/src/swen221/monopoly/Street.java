package swen221.monopoly;

public class Street extends Property {
	private int numHouses;
	private int numHotels;
	private int rent; // in $
	private ColourGroup colourGroup;

	public Street(String name, int price, int rent) {
		super(name, price);
		this.rent = rent;
		colourGroup = null;
	}

	public void setColourGroup(ColourGroup group) {
		colourGroup = group;
	}

	/**
	 * Get colour group to which this street belongs. Will return null if
	 * setColourGroup not already called.
	 */
	public ColourGroup getColourGroup() {
		return colourGroup;
	}

	public int getRent() {
		return rent;
	}

	public int getHouses() {
		return numHouses;
	}

	public int getHotels() {
		return numHotels;
	}
}
