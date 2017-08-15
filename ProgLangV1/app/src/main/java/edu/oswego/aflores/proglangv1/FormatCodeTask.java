package edu.oswego.aflores.proglangv1;

import java.util.ArrayList;

/**
 * Created by aflores on 2/13/2017.
 */

public class FormatCodeTask implements Runnable {
    private DbAccess db;
    private String topic;
    private String[] codesFormatted;
    private boolean done;

    public FormatCodeTask(DbAccess db, String topic) {
        this.db = db;
        this.topic = topic;
        done = false;
    }

    @Override
    public void run() {
        setCodesFormatted();
    }

    private void setCodesFormatted() {
        ArrayList<Code> codes = db.getAllCode(topic);
        codesFormatted = new String[codes.size()];
        for (int i = 0; i < codes.size(); i++) {
            codesFormatted[i] = "\nLanguage: " + codes.get(i).getLang() + "\n\nSyntax:\n" +
                    codes.get(i).getSyntax() + "\n\nExample: \n" + codes.get(i).getExample()
                    + "\n";
        }
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public String[] getCodesFormatted() {
        return codesFormatted;
    }

}
