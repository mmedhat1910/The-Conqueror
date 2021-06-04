package buildings;

import exceptions.*;


public class Farm extends EconomicBuilding {
	public Farm() {
		super(1000, 500);
	}
	
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException{
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Building is cooling down");
		int level = this.getLevel();
		if(level == 3)
			throw new MaxLevelException("Maximum level possible");
		
		this.setLevel(++level);
		this.setUpgradeCost(700);
		this.setCoolDown(true);
	}

	@Override
	public int harvest() {
		int level = this.getLevel();
		switch(level) {
		case 1: return 500;
		case 2: return 700;
		case 3: return 1000;
		
		}
		return -1;
	}
}
