package buildings;

import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import units.Infantry;
import units.Unit;

public class Barracks extends MilitaryBuilding{
	public Barracks() {
		super(2000, 1000, 500);
	}
	
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException{
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Building is cooling down");
		int level = this.getLevel();
		if(level == 1) {
			this.setRecruitmentCost(550);
			this.setUpgradeCost(1500);
		}
		if(level == 2) {
			this.setRecruitmentCost(600);
		}if(level ==3 ) {
			throw new MaxLevelException("Maximum level possible");
		}
		this.setLevel(level+1);		
		this.setCoolDown(true);
	}

	@Override
	public Unit recruit() throws BuildingInCoolDownException, MaxRecruitedException {
		int level = this.getLevel();
		if(this.getCurrentRecruit() == this.getMaxRecruit())
			throw new MaxRecruitedException("Cannot recruit more from barracks");
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Cannot recruit as cooling down");
		int maxSoldier =0; double idleKeep =0; double marchKeep = 0; double seigeKeep =0;
		
		switch(level) {	
		
		
		case 1:
			maxSoldier = 50;
			idleKeep = 0.5;
			marchKeep = 0.6;
			seigeKeep = 0.7;
			break;
		
		case 2:
			maxSoldier = 50; 
			idleKeep =  0.5; 
			marchKeep = 0.6;
			seigeKeep = 0.7;
			break;
		
		case 3:
			maxSoldier = 60; 
			idleKeep =  0.6; 
			marchKeep = 0.7;
			seigeKeep = 0.8;
			break;
		}
		this.setCurrentRecruit(this.getCurrentRecruit()+1);
		return new Infantry(level, maxSoldier, idleKeep, marchKeep, seigeKeep);
		
		
	}
}
