import java.io.IOException;
import systemGeneralClasses.SystemController;

/**
 *  Main class of this project.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class p2Main {


	public static void main(String[] args) throws IOException  {
		SystemController system = new SystemController(); 
		system.start(); 
	
		System.out.println("+++++ SYSTEM SHUTDOWN +++++"); 
	}

}
