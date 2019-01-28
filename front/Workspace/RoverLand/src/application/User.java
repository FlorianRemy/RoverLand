package application;

/**
 * Classe User : Classe qui definit un utilisateur
 * @author karim
 *
 */
public class User {
	/** id : Identifiant de l'utilisateur */
	private int id;
	
	public User(int id){
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
