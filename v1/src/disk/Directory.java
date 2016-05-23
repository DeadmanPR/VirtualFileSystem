package disk;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.nio.file.DirectoryNotEmptyException;
import java.util.ArrayList;

import exceptions.DirectoryNotFoundException;

/**
 * Class that represents a directory inside a disk.
 * @author Jose Antonio Rodriguez Rivera
 *
 */
public class Directory implements Serializable{
	private ArrayList<Pair<String, Directory>> subdirectories;
	private ArrayList<Pair<String, DiskFile>> files;
	private int size;
	private String name;

	/**
	 * Constructs a new Directory.
	 * @param name the name of the directory
	 * @param size the size of the directory
	 */
	public Directory(String name, int size){
		subdirectories = new ArrayList<>();
		files = new ArrayList<>();
		this.name = name;
		this.size = size;

	}

	/**
	 * Creates a new DiskFile inside this directory.
	 * @param name the name of the new file
	 * @param file the file to insert
	 */
	public void createNewFile(String name, DiskFile file){
		Pair<String, DiskFile> p = new Pair<>(name, file);
		files.add(p);
	}

	/**
	 * Creates a new subdirectory inside this directory
	 * @param name the name of the subdirectory
	 * @param dir the Directory to insert
	 */
	public void createNewSubdirectory(String name, Directory dir){
		Pair<String, Directory> p = new Pair<>(name, dir);
		subdirectories.add(p);
	}

	/**
	 * Returns the subdirectory that has the given name.
	 * @param name the name to look for
	 * @return the corresponding Directory
	 * @throws DirectoryNotFoundException if the directory does not exist
	 */
	public Directory getSubdirectory(String name) throws DirectoryNotFoundException{
		for(Pair<String, Directory> p : subdirectories){
			if(p.getFirst().equals(name))
				return p.getSecond();
		}

		throw new DirectoryNotFoundException("Directory does not exist!");
	}

	/**
	 * Returns the DiskFile that has the given name.
	 * @param name the name to look for
	 * @return the corresponding DiskFile
	 * @throws FileNotFoundException if the file does not exist
	 */
	public DiskFile getDiskFile(String name) throws FileNotFoundException{
		for(Pair<String, DiskFile> p : files){
			if(p.getFirst().equals(name))
				return p.getSecond();
		}

		throw new FileNotFoundException("File does not exist!");
	}

	/**
	 * Returns the name of this Directory.
	 * @return the name of this Directory
	 */
	public String getName(){
		return name;
	}

	/**
	 * Returns the size of this Directory.
	 * @return the size of the Directory
	 */
	public int getSize(){
		return size;
	}

	/**
	 * Returns an ArrayList of a pair of names and sizes corresponding to subdirectories.
	 * @return ArrayList of pair of names and sizes of subdirectories
	 */
	public ArrayList<Pair<String,Integer>> getSubdirectoryInfo(){
		ArrayList<Pair<String, Integer>> altr = new ArrayList<>();

		for(Pair<String, Directory> p : subdirectories){
			Pair<String, Integer> p1 = new Pair<>(p.getFirst(), p.getSecond().getSize());
			altr.add(p1);
		}

		return altr;
	}

	/**
	 * Returns an ArrayList of a pair of names and sizes corresponding to files.
	 * @return ArrayList of pair of names and sizes of files
	 */
	public ArrayList<Pair<String,Integer>> getFilesInfo(){
		ArrayList<Pair<String, Integer>> altr = new ArrayList<>();

		for(Pair<String, DiskFile> p : files){
			Pair<String, Integer> p1 = new Pair<>(p.getFirst(), p.getSecond().getSize());
			altr.add(p1);
		}

		return altr;
	}

	/**
	 * Removes the DiskFile with the given name from this Directory.
	 * @param name the name of the DiskFile to delete
	 * @throws FileNotFoundException if the file does not exist
	 */
	public void removeFile(String name) throws FileNotFoundException{
		boolean fileRemoved = false;
		for(int i = 0 ; i < files.size() && !fileRemoved ; i++)
			if(files.get(i).getFirst().equals(name)){
				files.get(i).getSecond().delete();
				files.remove(i);
				fileRemoved = true;
			}
		
		if(!fileRemoved)
			throw new FileNotFoundException("File does not exist!");
	}
	
	/**
	 * Empties the contents of this Directory and its subdirectories,
	 */
	public void deleteEverything(){
		for(int i  = 0; i < files.size() ; i ++)
			files.get(i).getSecond().delete();
		
		for(int i = 0; i < subdirectories.size() ; i++)
			subdirectories.get(i).getSecond().deleteEverything();
	}

	/**
	 * Removes a subdirectory from this Directory
	 * @param name the name of the subdirectory to be removed
	 * @throws DirectoryNotFoundException if the directory does not exist
	 */
	public void removeDirectory(String name) throws DirectoryNotFoundException{
		boolean dirRemoved = false;
		for(int i = 0 ; i < subdirectories.size()  && !dirRemoved ; i++)
			if(subdirectories.get(i).getFirst().equals(name)){
				subdirectories.get(i).getSecond().deleteEverything();
				subdirectories.remove(i);
				dirRemoved = true;
			}
		
		if(!dirRemoved)
			throw new DirectoryNotFoundException("Directory does not exist!");
		
	}



}
