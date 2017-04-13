import java.util.*;

class Manager {
    private ArrayList<FailMailFile> files;

    public Manager() {
        files = new ArrayList<FailMailFile>();
    }

    public void addFilesFromUser(String information) {
        String[] tokens = information.split("<");
        for (String token : tokens)
            if (token.length() > 0) {
                String[] sub = token.split("[,>\n]+");

                String name = sub[0];
                String type = sub[1];
                int size = Integer.parseInt(sub[2]);
                String lastModifiedDate = sub[3];
                String IPAddress = sub[4];
                int port = Integer.parseInt(sub[5]);

                files.add(new FailMailFile(name, type, size, lastModifiedDate, IPAddress, port));
            }
    }

    // This function searches for file from Main List of Files
    public String search(String filename) {
        String results = "";
        for (FailMailFile file : files)
            if (match(file.getName(), filename))
                results += file + ",";

        System.out.println("WTF");
        if (results.length() > 0)
            return "FOUND: " + results.substring(0, results.length() - 1);;
        return "NOT FOUND";
    }

    // This function checks whether file names considered to be same
    // (PARTIAL: if every word of required file name is found in filename)
    // not case sensitive
    public static boolean match(String a, String b) {
        String[] atokens = a.toLowerCase().split(" ");
        String[] btokens = b.toLowerCase().split(" ");
        int counter = 0;

        for (int i = 0; i < btokens.length; i++)
            for (int j = 0; j < atokens.length; j++)
                if (btokens[i].equals(atokens[j])) {
                    counter++;
                    System.out.println(counter);
                    break;
                }

        return (counter == btokens.length);
    }

    public String toString() {
        String message = "";
        for (FailMailFile f : files)
            message += f + ",";
        return message;
    }
}
