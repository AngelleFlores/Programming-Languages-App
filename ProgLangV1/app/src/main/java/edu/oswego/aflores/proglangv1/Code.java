package edu.oswego.aflores.proglangv1;

/**
 * Created by aflores on 2/9/2017.
 */

public class Code {
    private String topic;
    private String language;
    private String syntax;
    private String example;

    public Code(String t, String l, String s, String e) {
        this.topic = t;
        this.language = l;
        this.syntax = s;
        this.example = e;
    }

    public String getTopic() {
        return this.topic;
    }

    public String getLang() {
        return this.language;
    }

    public String getSyntax() {
        return this.syntax;
    }

    public String getExample() {
        return this.example;
    }

}
