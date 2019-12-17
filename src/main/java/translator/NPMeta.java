package translator;

public class NPMeta extends Metadata{
	
	public String instance = null;
	public boolean isPronoun = false;
	public boolean isPerson = false;
	public boolean isProper = false;
	public NPMeta pronounReference = null;
	public boolean hasGender = false;
	public boolean masculine = true;
	
	public boolean hasGender() {
		if(isPronoun && pronounReference != null) {
			return pronounReference.hasGender();
		}
		return hasGender;
	}
	
	public boolean isMasculine() {
		if(isPronoun && pronounReference != null) {
			return pronounReference.hasGender();
		}
		return hasGender;
	}
}
