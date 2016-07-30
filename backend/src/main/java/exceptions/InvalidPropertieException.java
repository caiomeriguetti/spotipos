package exceptions;

public class InvalidPropertieException extends Exception {
	private String[] validationResult;
	public String[] getValidationResult() {
		return validationResult;
	}
	public void setValidationResult(String[] validationResult) {
		this.validationResult = validationResult;
	}
	public InvalidPropertieException(String[] validationResult, String message) {
		super(message);
		this.validationResult = validationResult;
		
	}
}
