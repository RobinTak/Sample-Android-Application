package com.housing.employees;

/**
 * The class name represents the database Entity .
 * 
 * @author robin
 *
 */
public class Name {
	/**
	 * This attribute represents the unique identifier of the entity.
	 */
	private long id;
	/**
	 * This attribute represents the name of the entity.
	 */
	private String name;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}
}
