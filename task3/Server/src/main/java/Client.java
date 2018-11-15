class Client {
    private boolean isOnline;

    Client() {
        this.isOnline = false;
    }

    boolean isOnline() {
        return isOnline;
    }

    void setOnline(boolean online) {
        isOnline = online;
    }
}
