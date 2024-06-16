import java.util.*;

public class Directory {
    private String name;
    private List<File> files;
    private List<Directory> directories;

    public Directory(String name) {
        this.name = name;
        this.files = new ArrayList<>();
        this.directories = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<Directory> getDirectories() {
        return directories;
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(File file) {
        files.remove(file);
    }

    public void addDirectory(Directory directory) {
        directories.add(directory);
    }

    public void removeDirectory(Directory directory) {
        directories.remove(directory);
    }
}