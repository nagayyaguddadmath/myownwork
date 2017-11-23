package com.bskyb.internettv.parental_control_service;


public enum ParentalControl {
	LEVEL_U("U", 1),
	LEVEL_PG("PG", 2),
	LEVEL_12("12", 3),
	LEVEL_15("15", 4),
	LEVEL_18("18", 5);

	private String level;
	private int priority;

	private ParentalControl(String level, int priority) {
		this.level = level;
		this.priority = priority;
	}

	public String getLevel() {
		return level;
	}

	public int getPriority() {
		return priority;
	}

	public static ParentalControl getControlevel(String level) {
		if (level == null)
			return null;
		for (ParentalControl l : values()) {
			if (l.getLevel().equals(level))
				return l;
		}
		return null;
	}

}
