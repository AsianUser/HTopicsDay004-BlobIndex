
import java.io.File;

public class FileUtils {

    static boolean testMode = false;

    // help me with testing
    static boolean setTestMode() throws Exception {
        testMode = !testMode;
        return testMode;
    }

}
