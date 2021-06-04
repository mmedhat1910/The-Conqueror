package buildings;

import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;

import units.Cavalry;
import units.Unit;

public class Stable extends MilitaryBuilding{
	public Stable() {
		super(2500, 1500, 600);
	}

	public void upgrade() throws BuildingInCoolDownException, MaxLevelException{
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Building is cooling down");
		int level = this.getLevel();
		if(level == 3)
			throw new MaxLevelException("Maximum level possible");
		
		this.setLevel(++level);
		this.setUpgradeCost(2000);
		int recCost = level==2? 650:700;
		this.setRecruitmentCost(recCost);
		
		
		this.setCoolDown(true);
	}
	
	@Override
	public Unit recruit() throws BuildingInCoolDownException, MaxRecruitedException {
		int level = this.getLevel();
		if(this.getCurrentRecruit() == this.getMaxRecruit())
			throw new MaxRecruitedException("Cannot Recruit more from stable");
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Cannot recruit as cooling down");
		int maxSoldier =0; double idleKeep =0; double marchKeep = 0; double seigeKeep =0;
		
		switch(level) {	
		case 1:
			maxSoldier = 40;
			idleKeep = 0.6;
			marchKeep = 0.7;
			seigeKeep = 0.75;
			break;
		
		case 2:
			maxSoldier = 40; 
			idleKeep =  0.6; 
			marchKeep = 0.7;
			seigeKeep = 0.75;
			break;
		
		case 3:
			maxSoldier = 60; 
			idleKeep =  0.7; 
			marchKeep = 0.8;
			seigeKeep = 0.9;
			break;
		}
		this.setCurrentRecruit(this.getCurrentRecruit()+1);
		return new Cavalry(level, maxSoldier, idleKeep, marchKeep, seigeKeep);
		
		
	}
}
