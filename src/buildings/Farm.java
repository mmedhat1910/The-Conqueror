package buildings;

import exceptions.*;


public class Farm extends EconomicBuilding {
	public Farm() {
		super(1000, 500, "Farm");
	}
	
	@Override
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException {
		super.upgrade();
		if(getLevel()==1)
		{
			setLevel(2);
			setUpgradeCost(700);
		}
		else if(getLevel()==2)
		{
			setLevel(3);
			
		}
		
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
