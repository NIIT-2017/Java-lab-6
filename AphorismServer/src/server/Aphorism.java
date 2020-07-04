package server;

public class Aphorism {
    private String aphorism;
    private String author;

    public Aphorism(String aphorism, String author) {
        this.aphorism = aphorism;
        this.author = author;
    }

    public String getAphorism() {
        return aphorism;
    }

    public String getAuthor() {
        return author;
    }
}
