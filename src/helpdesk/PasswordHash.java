package helpdesk;

//imports
import org.mindrot.jbcrypt.BCrypt;

/**
 * Contains the hashPassword() method and checkPassword() method
 * @author Shameemah Fuseini-Codjoe
 * @version NetBeans IDE 8.2 (Build 201609300101)
 */
public class PasswordHash {
    // Define the BCrypt workload to use when generating password hashes.
    private static int workload = 12;
   
    /**
     * Takes the user's password and hashes it to prevent hacking
     * @param password the password entered in the database
     * @return hashedPassword the hashed version of the password
     */
    public static String hashPassword(String password) {
		String salt = BCrypt.gensalt(workload);
		String hashedPassword = BCrypt.hashpw(password, salt);

		return(hashedPassword);
	}
    
    /**
     * Checks that the password entered at login matches the hash password stored 
     * in the database
     * @param password_plaintext the password entered at login
     * @param stored_hash the hash used to has the password
     * @return password_verified true or false depending on if password matches or not
     */
    public static boolean checkPassword(String password_plaintext, String stored_hash) {
		boolean password_verified = false;

		if(null == stored_hash || !stored_hash.startsWith("$2a$"))
			throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

		password_verified = BCrypt.checkpw(password_plaintext, stored_hash);

		return(password_verified);
	}
   
        
}
