package buildings;

abstract public class EconomicBuilding extends Building {
	public EconomicBuilding(int cost, int upgradeCost) {
		super(cost, upgradeCost);
	}
	
	public abstract int harvest();
}
