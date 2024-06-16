import java.util.*;

public class FileSystemSimulator {
    private Directory root;
    private Journal journal;

    public FileSystemSimulator() {
        this.root = new Directory("root");
        this.journal = new Journal();
    }

    private Directory getDirectory(String path) {
        String[] parts = path.split("/");
        Directory current = root;
        for (String part : parts) {
            if (part.isEmpty()) continue;
            Optional<Directory> nextDir = current.getDirectories().stream()
                    .filter(d -> d.getName().equals(part))
                    .findFirst();
            if (nextDir.isPresent()) {
                current = nextDir.get();
            } else {
                return null;
            }
        }
        return current;
    }

    private File getFile(String path) {
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash == -1) return null;
        String dirPath = path.substring(0, lastSlash);
        String fileName = path.substring(lastSlash + 1);
        Directory dir = getDirectory(dirPath);
        if (dir == null) return null;
        return dir.getFiles().stream()
                .filter(f -> f.getName().equals(fileName))
                .findFirst()
                .orElse(null);
    }

    public void createFile(String path, String name) {
        Directory dir = getDirectory(path);
        if (dir != null) {
            File file = new File(name);
            dir.addFile(file);
            journal.record("Criar arquivo " + path + "/" + name);
        }
    }

    public void deleteFile(String path) {
        File file = getFile(path);
        if (file != null) {
            Directory dir = getDirectory(path.substring(0, path.lastIndexOf('/')));
            if (dir != null) {
                dir.removeFile(file);
                journal.record("Deletar arquivo " + path);
            }
        }
    }

    public void renameFile(String oldPath, String newPath) {
        File file = getFile(oldPath);
        if (file != null) {
            String newFileName = newPath.substring(newPath.lastIndexOf('/') + 1);
            file.setName(newFileName);
            journal.record("Renomear arquivo " + oldPath + " para " + newPath);
        }
    }

    public void copyFile(String sourcePath, String destinationPath) {
        File file = getFile(sourcePath);
        if (file != null) {
            Directory destDir = getDirectory(destinationPath);
            if (destDir != null) {
                File newFile = new File(file.getName());
                destDir.addFile(newFile);
                journal.record("Copiar arquivo " + sourcePath + " para " + destinationPath);
            }
        }
    }

    public void createDirectory(String path, String name) {
        Directory dir = getDirectory(path);
        if (dir != null) {
            Directory newDir = new Directory(name);
            dir.addDirectory(newDir);
            journal.record("Criar diretório " + path + "/" + name);
        }
    }

    public void deleteDirectory(String path) {
        Directory dir = getDirectory(path);
        if (dir != null) {
            Directory parentDir = getDirectory(path.substring(0, path.lastIndexOf('/')));
            if (parentDir != null) {
                parentDir.removeDirectory(dir);
                journal.record("Deletar diretório " + path);
            }
        }
    }

    public void renameDirectory(String oldPath, String newPath) {
        Directory dir = getDirectory(oldPath);
        if (dir != null) {
            String newDirName = newPath.substring(newPath.lastIndexOf('/') + 1);
            dir.setName(newDirName);
            journal.record("Renomear diretório " + oldPath + " para " + newPath);
        }
    }

    public List<String> listDirectory(String path) {
        System.out.println("Listando diretório: " + path);
        Directory dir = getDirectory(path);
        if (dir != null) {
            List<String> contents = new ArrayList<>();
            for (Directory subDir : dir.getDirectories()) {
                contents.add(subDir.getName() + "/");
            }
            for (File file : dir.getFiles()) {
                contents.add(file.getName());
            }
            journal.record("Listar diretório " + path);
            contents.forEach(System.out::println);
            return contents;
        } else {
            System.out.println("Diretório não encontrado: " + path);
        }
        return Collections.emptyList();
    }

    public void printJournal() {
        for (String logEntry : journal.getLog()) {
            System.out.println(logEntry);
        }
    }

    public void startShell() {
        Scanner scanner = new Scanner(System.in);
        String command;
        while (true) {
            System.out.print("shell> ");
            command = scanner.nextLine();
            if (command.equals("exit")) break;
            executeCommand(command);
        }
        scanner.close();
    }

    private void executeCommand(String command) {
        String[] parts = command.split(" ");
        switch (parts[0]) {
            case "createFile":
                createFile(parts[1], parts[2]);
                break;
            case "deleteFile":
                deleteFile(parts[1]);
                break;
            case "renameFile":
                renameFile(parts[1], parts[2]);
                break;
            case "copyFile":
                copyFile(parts[1], parts[2]);
                break;
            case "createDirectory":
                createDirectory(parts[1], parts[2]);
                break;
            case "deleteDirectory":
                deleteDirectory(parts[1]);
                break;
            case "renameDirectory":
                renameDirectory(parts[1], parts[2]);
                break;
            case "listDirectory":
                listDirectory(parts[1]).forEach(System.out::println);
                break;
            case "printJournal":
                printJournal();
                break;
            default:
                System.out.println("Comando desconhecido.");
        }
    }

    public static void main(String[] args) {
        FileSystemSimulator simulator = new FileSystemSimulator();
        simulator.startShell();
    }
}