package buildings;

import exceptions.BuildingInCoolDownException;
import exceptions.MaxLevelException;
import exceptions.MaxRecruitedException;
import units.Archer;
import units.Unit;

public class ArcheryRange extends MilitaryBuilding{
	public ArcheryRange() {
		super(1500, 800, 400);
	}
	
	public void upgrade() throws BuildingInCoolDownException, MaxLevelException{
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Building is cooling down");
		int level = this.getLevel();
		if(level == 3)
			throw new MaxLevelException("Maximum level possible");
		
		this.setLevel(++level);
		this.setUpgradeCost(700);
		int recCost = level==2? 450:500;
		this.setRecruitmentCost(recCost);
		this.setCoolDown(true);		
	}

	@Override
	public Unit recruit() throws BuildingInCoolDownException, MaxRecruitedException {
		int level = this.getLevel();
		if(this.getCurrentRecruit() == this.getMaxRecruit())
			throw new MaxRecruitedException("Maximum number recruited");
		if(this.isCoolDown())
			throw new BuildingInCoolDownException("Cannot recruit as cooling down");
		int maxSoldier =0; double idleKeep =0; double marchKeep = 0; double seigeKeep =0;
		
		switch(level) {
		
		case 1:
			maxSoldier = 60;
			idleKeep = 0.4;
			marchKeep = 0.5;
			seigeKeep = 0.6;
			break;
		
		case 2:
			maxSoldier = 60; 
			idleKeep =  0.4; 
			marchKeep = 0.5;
			seigeKeep = 0.6;
			break;
		
		case 3:
			maxSoldier = 70; 
			idleKeep =  0.5; 
			marchKeep = 0.6;
			seigeKeep = 0.7;
			break;
		}
		this.setCurrentRecruit(this.getCurrentRecruit()+1);
		return new Archer(level, maxSoldier, idleKeep, marchKeep, seigeKeep);
		
		
	}
}








