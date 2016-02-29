package swen221.monopoly;

/**
 * Represents any location on the monopoly board. This includes streets,
 * utilities, special areas and stations.
 */
public abstract class Location {
	private String name;

	public Location(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	/**
	 * Check whether this location has an owner or not. Note that not all
	 * locations can actually have owners.
	 */
	public abstract boolean hasOwner();
}
