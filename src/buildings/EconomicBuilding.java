package buildings;

abstract public class EconomicBuilding extends Building {
	public EconomicBuilding(int cost, int upgradeCost, String type) {
		super(cost, upgradeCost, type);
	}
	
	public abstract int harvest();
}
