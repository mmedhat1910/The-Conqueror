package buildings;

import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;

public class Market extends EconomicBuilding {
	public Market() {
		super(1500,700, "Market");
	}
	@Override
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException {
		super.upgrade();
		if(getLevel()==1)
		{
			setLevel(2);
			setUpgradeCost(1000);
		}
		else if(getLevel()==2) {
		setLevel(3);
		setUpgradeCost(-1);
		}
		
	}
	@Override
	public int harvest() {
		int level = this.getLevel();
		switch(level) {
		case 1: return 1000;
		case 2: return 1500;
		case 3: return 2000;
	
		}
		return -1;
	}
}
