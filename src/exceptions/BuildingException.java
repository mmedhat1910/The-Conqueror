package exceptions;

abstract public class BuildingException extends EmpireException {
	public BuildingException() { super(); }
	
	public BuildingException(String s) {
		super(s);
	}
}
