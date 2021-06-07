package buildings;

import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;

public class Market extends EconomicBuilding {
	public Market() {
		super(1500,700);
	}
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException{
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Building is cooling down");
		int level = this.getLevel();
		if(level == 1)
			this.setUpgradeCost(1000);
		if(level == 3)
			throw new MaxLevelException("Maximum level possible");
		
		this.setLevel(level+1);
		this.setCoolDown(true);
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
