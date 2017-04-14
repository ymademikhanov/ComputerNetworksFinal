class FailMailFile {
    private String name;
    private String type;
    private int size;
    private String lastModifiedDate, IPAddress;
    private int port;

    public FailMailFile(String name, String type, int size, String lastModifiedDate, String IPAddress, int port) {
        this.name = name;
        this.type = type;
        this.size = size;
        this.lastModifiedDate = lastModifiedDate;
        this.IPAddress = IPAddress;
        this.port = port;
    }

    public String toString() {
        String message = "<";
        message += this.name + ",";
        message += this.type + ",";
        message += this.size + ",";
        message += this.lastModifiedDate + ",";
        message += this.IPAddress + ",";
        message += this.port + ">";
        return message;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
    public String getIPAddress() {
        return IPAddress;
    }
    public int getPort() {
        return port;
    }
}
