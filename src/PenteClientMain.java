
public class PenteClientMain {
	
	public static void main(String[] args) {
		PenteClientView view = new PenteClientView();
		PenteClientModel model = new PenteClientModel();
		PenteClientController controller = new PenteClientController(view, model);
		view.setVisible(true);
		view.requestFocus();
	}
	
}
