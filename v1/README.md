Coded by: Jose Antonio Rodriguez Rivera

# Compilation and Execution
To run this program, use your terminal window and locate the src folder in this project.

To compile the program, type:

	javac p2Main.java
To run the program, type:

	java p2Main
	
# Brief explanation of this program:
This program is a command-based system that lets you do the following things:

- *help* : shows the command list and formats for each one

- *showdisks* : shows a list of existing DiskUnits

- *createdisk disk_name* : creates a disk with the name disk_name 

- *deletedisk disk_name* : deletes the disk with the name disk_name

- *formatdisk disk_name nblocks bsize* : formats the disk with name disk_name as a disk that has nblocks blocks and each block is of size bsize

- *mount disk_name* : mounts a DiskUnit as the current working unit

- *unmount* : unmounts the current DiskUnit

- *loadfile file_name ext_file_name* : loads a file located in the src folder with name ext_file_name into the DiskUnit as a file named file_name 

- *cd dir_name* : goes to the subdirectory dir_name. If dir_name = .. , it goes to the parent directory 

- *mkdir dir_name* : creates a new directory with name dir_name

- *rmdir dir_name* :  removes the directory with name dir_name

- *cp file_name_1 file_name_2* : copies two internal files (from file_name_1 to file_name_2)

- *rm file_name* : removes the file with name file_name

- *ls* : shows the current subdirectories and files

- *cat file_name* : shows the contents of a file

- *exit* : exits the program
