package disk;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Scanner;

/**
 * Object that represents a file in the DiskUnit.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class DiskFile implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private File file;
	private static File path = new File("src" + File.separator + "DiskUnits" + File.separator);
	int size;
	
	/**
	 * Constructs a DiskFile with the given size.
	 * @param size the size of the file
	 */
	public DiskFile(int size){
		this.size = size;
	}
	
	/**
	 * Loads an external file into this DiskFile
	 * @param diskName the name of the disk this DiskFile belongs to
	 * @param name the name of the file (internally)
	 * @param f the file to load from
	 * @throws FileNotFoundException if the file does not exist
	 */
	public void loadFile(String diskName, String name, File f) throws FileNotFoundException{
		file = new File(path, diskName +  name + ".txt");		
		Scanner sc = new Scanner(f);
		PrintWriter pw = new PrintWriter(file);
		
		while(sc.hasNextLine())
			pw.write(sc.nextLine());
			
		sc.close();
		pw.close();
		
	}

	/**
	 * Returns the content of this DiskFile.
	 * @return the contents of this file
	 * @throws FileNotFoundException of the file does not exist
	 */
	public String showFileContents() throws FileNotFoundException{
		String s = "";
		Scanner sc = new Scanner(file);
		
		while(sc.hasNext())
			s = s + sc.nextLine();
		
		return s;
	}
	
	/**
	 * Returns the size of this DiskFile.
	 * @return the size of this file
	 */
	public int getSize(){
		return size;
	}
	
	/**
	 * Returns the file.
	 * @return the file
	 */
	public File getFile(){
		return file;
	}
	
	/**
	 * Deletes this file.
	 */
	public void delete(){
		if(file.exists())
			file.delete();
	}
}
