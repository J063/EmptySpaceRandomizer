import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Random;

public class Randomizer {
    private static final String BIGFILENAME = "bigFile";
    private static final String SMALLFILENAME = "smallFile";
    private static final String ROOTFILENAME = "/";

    /**
     * Main function that kicks off random byte writing
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        int spaceLimit = 0;
        if (args.length == 1) {
            try {
                spaceLimit = Integer.parseInt(args[0]);
            } catch (NumberFormatException ex) {
                printHelp();
            }
        } else if (args.length > 1) {
            printHelp();
        }

        System.out.println("Starting randomization...");
        printRemainingDiskspace();
        writeSmallFile();
        writeBigFile(spaceLimit);

        System.out.println("Cleaning up files...");
        if (!deleteSmallFile()) {
            System.out.println("Cleaning up small file failed!");
        }

        if (!deleteBigFile()) {
            System.out.println("Cleaning up big file failed!");
        }

        System.out.printf("The operation took %d min\n", (System.currentTimeMillis() - timeStart)/60000);
    }

    /**
     * Return the remaining disk apace in raw double format
     *
     * @return double
     */
    private static double getRemainingDiskspace() {
        File file = new File(ROOTFILENAME);

        return file.getFreeSpace();
    }

    /**
     * Helper function to print uit disk space in human readable format
     */
    private static void printRemainingDiskspace() {
        System.out.printf("%.3f GB\n", getRemainingDiskspace() / (1024.0 * 1024 * 1024));
    }

    /**
     * Helper function to print uit disk space in human readable format with a speed element
     */
    private static void printRemainingDiskspaceAndSpeed(double oldSize, double newSize, Long timeDelta) {
        double sizeDelta = oldSize - newSize;
        double mbps = 0.00;
        if (sizeDelta > 0 && timeDelta > 0) {
            mbps = sizeDelta / timeDelta;
        }

        System.out.printf("%.3f GB @%.2f\n", getRemainingDiskspace() / (1024.0 * 1024 * 1024), mbps/1024);
    }

    /**
     * Create an in-memory byte[] of 1GB containing random characters
     *
     * @return byte[]
     */
    private static byte[] getRandomGb() {
        byte[] gbyte = new byte[1024 * 1024 * 1024];
        Random rnd = new Random();
        rnd.nextBytes(gbyte);

        return gbyte;
    }

    /**
     * Write 'small' 1GB file in order to have a quick way of freeing up space once the disk is completely full and
     * the system becomes unresponsive
     */
    private static void writeSmallFile() {
        try (FileOutputStream output = new FileOutputStream(SMALLFILENAME, true)) {
            output.write(getRandomGb());
            printRemainingDiskspace();

        } catch (IOException e) {
            System.err.println("problem opening/closing the smaller file");
        }
    }

    /**
     * Write to large file in 1GB blocks until the system disk contains less than the lowerDiskSpaceLimit amount of GB
     *
     * @param lowerDiskSpaceLimit int
     */
    private static void writeBigFile(int lowerDiskSpaceLimit) {
        try (FileOutputStream output = new FileOutputStream(BIGFILENAME, true)) {
          byte[] randomGb = getRandomGb();
            while (getRemainingDiskspace() / (1024.0 * 1024 * 1024) > lowerDiskSpaceLimit) {
                double sizeBefore = getRemainingDiskspace();
                Long timeBefore = System.currentTimeMillis();
                output.write(randomGb);
                printRemainingDiskspaceAndSpeed(sizeBefore, getRemainingDiskspace(), (System.currentTimeMillis() - timeBefore));
            }

        } catch (IOException e) {
            System.err.println("Disk full!");
        }
    }

    /**
     * Remove smaller file.
     * Doing this before removing the larger file makes the system responsive again.
     *
     * @return boolean
     */
    private static boolean deleteSmallFile() {
        File file = new File(SMALLFILENAME);
        boolean result;
        try {
            result = Files.deleteIfExists(file.toPath());
        } catch (IOException ex) {
            result = false;
        }

        return result;
    }

    /**
     * Remove larger file
     *
     * @return boolean
     */
    private static boolean deleteBigFile() {
        File file = new File(BIGFILENAME);
        boolean result;
        try {
            result = Files.deleteIfExists(file.toPath());
        } catch (IOException ex) {
            result = false;
        }

        return result;
    }

    /**
     * Print usage
     */
    private static void printHelp() {
        System.out.println("Useage: randomizer <min remaining disk size>");
    }

}
