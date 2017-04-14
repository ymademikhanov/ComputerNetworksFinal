import java.util.*;

class Manager {
    static private ArrayList<FailMailFile> files;

    public Manager() {
        files = new ArrayList<FailMailFile>();
    }

    public String addFilesFromUser(String information) {
        String[] tokens = information.split("<");
        String feedback = "";
        for (String token : tokens)
            if (token.length() > 0) {
                String[] sub = token.split("[,>\n]+");

                String name = sub[0].trim();
                String type = sub[1].trim();
                int size = Integer.parseInt(sub[2].trim());
                String lastModifiedDate = sub[3].trim();
                String IPAddress = sub[4].trim();
                int port = Integer.parseInt(sub[5].trim());

                files.add(new FailMailFile(name, type, size, lastModifiedDate, IPAddress, port));
                feedback = IPAddress;
            }
        return feedback;
    }

    // This function searches for file from Main List of Files
    public String search(String filename) {
        String results = "";
        for (FailMailFile file : files)
            if (match(file.getName(), filename))
                results += file + ",";
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

    public void delete(String ipaddress) {
        boolean removing = true;
        while (removing) {
            removing = false;
            for (FailMailFile file : files) {
                System.out.println(ipaddress + " and " + file.getIPAddress());
                if (file.getIPAddress().equals(ipaddress)) {
                    System.out.println("removing");
                    files.remove(file);
                    removing = true;
                }
            }
        }
    }

    public String toString() {
        String message = "";
        for (FailMailFile f : files)
            message += f + ",";
        return message;
    }
}
