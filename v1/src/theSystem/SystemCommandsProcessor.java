package theSystem;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.DirectoryNotEmptyException;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import listsManagementClasses.DiskUnitListsManager;
import listsManagementClasses.NamedDiskUnit;
import stack.IntStack;
import systemGeneralClasses.Command;
import systemGeneralClasses.CommandActionHandler;
import systemGeneralClasses.CommandProcessor;
import systemGeneralClasses.FixedLengthCommand;
import systemGeneralClasses.SystemCommand;
import disk.Directory;
import disk.DiskFile;
import disk.DiskUnit;
import disk.Pair;
import exceptions.DirectoryNotFoundException;
import exceptions.ExistingDirectoryException;
import exceptions.NonExistingDiskException;


/**
 * 
 * @author Pedro I. Rivera-Vega
 *
 */
public class SystemCommandsProcessor extends CommandProcessor { 
	private ArrayList<String> resultsList; 
	SystemCommand attemptedSC; 
	boolean stopExecution; 
	private NamedDiskUnit currentNDU;
	private DiskUnit currentDiskUnit;
	private DiskUnitListsManager listManager = new DiskUnitListsManager();

	/**
	 *  Initializes the list of possible commands for each of the
	 *  states the system can be in. 
	 */
	public SystemCommandsProcessor() {
		currentState = new IntStack();
		currentState.push(GENERALSTATE); 
		createCommandList(2);    

		//add(... commands with command processor)
		add(GENERALSTATE, SystemCommand.getFLSC("help", new HelpProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("exit", new ExitProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("createdisk disk_name", new CreateDiskProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("deletedisk disk_name", new DeleteDiskProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("showdisks", new ShowDisksProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("formatdisk disk_name nblocks bsize", new FormatDiskProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("mount disk_name", new MountDiskProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("unmount", new UnmountDiskProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("cd dir_name", new CDProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("mkdir dir_name", new MakeDirProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("ls", new LSProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("loadfile file_name ext_file_name", new LoadFileProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("cat file_name", new CATProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("rm file_name", new RMProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("rmdir file_name", new RMDirectoryProcessor()));

		add(GENERALSTATE, SystemCommand.getFLSC("cp file_name file_name", new CPProcessor()));

		
		stopExecution = false; 
		listManager.loadInfoFromFile();;
	}

	/**
	 * Returns the output.
	 * @return the output
	 */
	public ArrayList<String> getResultsList() { 
		return resultsList; 
	}
	
	
	//****************************************************************************************************************
	//***************************Inner classes for each command's processor*****************************
	//****************************************************************************************************************

	private class ExitProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			resultsList = new ArrayList<String>();
			resultsList.add("\tSystem is shutting down!");

			if(currentDiskUnit != null)
				currentDiskUnit.shutdown();

			listManager.saveInfoToFile();;
			stopExecution = true;
			return resultsList;
		}
	}

	private class CreateDiskProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<>();
			FixedLengthCommand fc = (FixedLengthCommand)c;

			String name = fc.getOperand(1);
			if(!listManager.nameExists(name)){
				listManager.createNewDisk(name);
				resultsList.add("\tDiskUnit " + name + " was successfully created.");
			}
			else{
				resultsList.add("\tA DiskUnit already exists with name: " + name);
			}
			return resultsList;
		}
	}

	private class DeleteDiskProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<>();
			FixedLengthCommand fc = (FixedLengthCommand)c;

			String name = fc.getOperand(1);

			int diskIndex = listManager.getListIndex(name);

			if(diskIndex != -1){
				listManager.removeDisk(diskIndex);
				resultsList.add("\tDiskUnit " + name + " was successfully removed.");
			}
			else
				resultsList.add("\tDiskUnit " + name + " does not exist!");

			return resultsList;
		}

	}

	private class ShowDisksProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<>();

			String s1 = "\tList of existing (active) DiskUnits:\n";
			String s2 = "";

			int numberOfDisks = listManager.getNumberOfDisks();

			for(int i = 0; i < numberOfDisks ; i++){
				s2 = s2 +  "\t\t" +  listManager.getName(i) + ": Number of blocks: " + listManager.getNumberOfBlocks(i) + " | " + "Size of each block: " + listManager.getBlockSize(i) + " | ";
				if(listManager.isMounted(i))
					s2 = s2 + " mounted\n";
				else
					s2 = s2 + " unmounted\n";
			}

			resultsList.add(s1 + s2);
			return resultsList;
		}

	}

	private class FormatDiskProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String name = fc.getOperand(1);
			int nblocks = Integer.parseInt(fc.getOperand(2));
			int bsize = Integer.parseInt(fc.getOperand(3));

			if(bsize < 16){
				resultsList.add("\tBlock Size cannot be less than 16!");
				return resultsList;
			}
			int diskIndex = listManager.getListIndex(name);

			if(diskIndex == -1){
				resultsList.add("\tDiskUnit with name " + name + " does not exist!");
				return resultsList;
			}


			try {
				listManager.formatDisk(diskIndex, nblocks, bsize);
			} catch (InvalidParameterException e) {
				// TODO Auto-generated catch block
				resultsList.add("\tParameters not valid. Block Size and Number of Blocks must be a power of 2!");
				return resultsList;
			} catch (NonExistingDiskException e) {
				// TODO Auto-generated catch block
				resultsList.add("\tDiskUnit with name " + name + " does not exist!");
				return resultsList;

			}


			resultsList.add("\tDiskUnit " + name + " was successfully formatted.");

			if(currentNDU != null){
				if(name == currentNDU.getName()){
					currentNDU = listManager.getNamedDiskUnit(diskIndex);
					currentDiskUnit = currentNDU.getDiskUnit();
				}
			}
			return resultsList;
		}

	}

	private class MountDiskProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU != null){
				resultsList.add("\tThere's a DiskUnit already mounted!");
				return resultsList;
			}

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String name = fc.getOperand(1);

			int diskIndex = listManager.getListIndex(name);

			if(diskIndex == -1){
				resultsList.add("\tDiskUnit " + name + " does not exist!");
				return resultsList;
			}

			currentNDU = listManager.getNamedDiskUnit(diskIndex);
			currentNDU.setMounted(true);

			currentDiskUnit = currentNDU.getDiskUnit();

			resultsList.add("\tDiskUnit " + name + " was successfully mounted.");
			return resultsList;
		}

	}


	private class UnmountDiskProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			currentDiskUnit.shutdown();
			currentNDU.setMounted(false);
			currentDiskUnit = null;
			currentNDU = null;

			resultsList.add("\tDiskUnit was successfully unmounted.");
			return resultsList;
		}

	}


	private class CDProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String name = fc.getOperand(1);

			Directory currentDir = currentDiskUnit.getCurrentDirectory();

			Directory dest;
			if(name.equals(".."))
				currentDiskUnit.goToParentDirectory();
			else{
				dest = currentDir.getSubdirectory(name);

				if(dest == null){
					resultsList.add("\tDirectory " + name + " does not exist!");
					return resultsList;
				}

				currentDiskUnit.goToSubdirectory(name);
			}
			return resultsList;
		}

	}

	private class MakeDirProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}


			FixedLengthCommand fc = (FixedLengthCommand)c;

			String name = fc.getOperand(1);

			if(name.length() > 20){
				resultsList.add("\tDirectory name " + name + " is too long. It can't be more than 20 characters!");
				return resultsList;
			}


			try{
				currentDiskUnit.createNewDirectory(name, 256);
			}
			catch(ExistingDirectoryException e)
			{
				resultsList.add("\tDirectory with name " + name + " already exists!");
				return resultsList;
			}

			return resultsList;
		}

	}

	private class LSProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			String s = "";

			ArrayList<Pair<String, Integer>> al = currentDiskUnit.getCurrentDirectory().getSubdirectoryInfo();
			s = "\t" + "Directories\n";
			for(Pair<String, Integer> p : al)
				s = s + "\t\t" + p.getFirst() + "\t" + "Size: " + p.getSecond() + "\n";

			al = currentDiskUnit.getCurrentDirectory().getFilesInfo();
			s = s + "\t" + "Files\n";
			for(Pair<String, Integer> p : al)
				s = s + "\t\t" + p.getFirst() + "\t" + "Size: " + p.getSecond() + "\n";

			resultsList.add(s);

			return resultsList;
		}

	}

	private class LoadFileProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String fileName = fc.getOperand(1);
			String extFileName = fc.getOperand(2);

			File f = new File("src" + File.separator + extFileName);
			if(!f.exists()){
				resultsList.add("\tFile " + extFileName + " does not exist!");
				return resultsList;
			}

			currentNDU.loadFile(fileName, 256, f);

			resultsList.add("\tFile " + fileName + " was successfully loaded!");
			return resultsList;
		}

	}

	private class CATProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String fileName = fc.getOperand(1);

			DiskFile df;
			try {
				df = currentDiskUnit.accessFile(fileName);
				resultsList.add("\t" + df.showFileContents());
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				resultsList.add("\tFile " + fileName + " does not exist!");
				return resultsList;
			}


			return resultsList;
		}

	}

	private class RMProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String fileName = fc.getOperand(1);

			try {
				currentDiskUnit.removeFile(fileName);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				resultsList.add("\tFile " + fileName + " does not exist!");
				return resultsList;
			}
			resultsList.add("\tFile " + fileName + " was successfully removed.");
			return resultsList;
		}

	}

	private class RMDirectoryProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();

			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String dirName = fc.getOperand(1);

			try{
				currentDiskUnit.removeDirectory(dirName);
			}catch(DirectoryNotFoundException e){
				resultsList.add("\tDirectory " + dirName + " does not exist!");
				return resultsList;
			}

			resultsList.add("\tDirectory " + dirName + " successfully removed");
			return resultsList;
		}

	}

	private class CPProcessor implements CommandActionHandler{

		@Override
		public ArrayList<String> execute(Command c) {
			// TODO Auto-generated method stub
			resultsList = new ArrayList<String>();
			if(currentNDU == null){
				resultsList.add("\tThere's no DiskUnit currently mounted!");
				return resultsList;
			}

			FixedLengthCommand fc = (FixedLengthCommand)c;

			String fileNameSrc = fc.getOperand(1);
			String fileNameDest = fc.getOperand(2);

			DiskFile df;
			try {
				df = currentDiskUnit.accessFile(fileNameSrc);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				resultsList.add("\tFile " + fileNameSrc + " does not exist!");
				return resultsList;
			}
			
			File f = df.getFile();
			currentNDU.loadFile(fileNameDest, 256, f);

			resultsList.add("\tFile " + fileNameSrc + " was successfully copied into " + fileNameDest);
			return resultsList;

		}

	}

	/**
	 * Returns if the system is shutting down.
	 * @return true if the system is shutting down, false otherwise
	 */
	public boolean inShutdownMode() {
		return stopExecution;
	}

}	






