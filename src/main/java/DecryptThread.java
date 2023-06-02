import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.File;

public class DecryptThread extends Thread {

    private GUIForm form;
    private File file;
    private String password;

    public DecryptThread(GUIForm form) {
        this.form = form;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void run() {
        onStart();
        try {
            String outPath = getOutputPath();
            ZipFile zipFile = new ZipFile(file);
            zipFile.setPassword(password);
            zipFile.extractAll(outPath);
        }
        catch (ZipException ex) {
            form.showWarning("Пароль указан неверно");
            onStop();
            System.exit(0);
        }
        onFinish();
    }

    private void onStart() {
        form.setButtonsEnabled(false);
    }

    private void onFinish(){
        form.setButtonsEnabled(true);
        form.showFinished();
    }

    private void onStop() {
        form.showStop();
    }

    private String getOutputPath() {

        String path = file.getAbsolutePath()
                .replaceAll("\\.enc$", "");
        for (int i = 1;; i++) {
            String number = i > 1 ? Integer.toString(i) : "";
            String outPath = path + number;
            if(new File(outPath).exists()) {
                return outPath;
            }
        }
    }
}
