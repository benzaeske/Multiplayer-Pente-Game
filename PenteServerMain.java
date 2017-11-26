import java.io.IOException;


public class PenteServerMain {
	
	private static final int DEFAULT_PORT = 45017;
	
	public static void main(String[] args) {
		try {
			new PenteServerController(DEFAULT_PORT);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}
