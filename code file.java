import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Main {

    static final int K = 3;
    static final int MAX_ITERATIONS = 20;

    static double[][] pixels;
    static double[][] centers;
    static int[] labels;

    public static void main(String[] args) {

        try {
            System.out.println("======================================");
            System.out.println(" COMPUTER VISION - K MEANS");
            System.out.println(" IMAGE SEGMENTATION PROJECT");
            System.out.println("======================================");

            // Try to find the input image in the project folder.
            File inputFile = findInputImage();

            // If no image is found, create a test image automatically.
            // This prevents the 'Can't read input file!' error.
            if (inputFile == null) {
                System.out.println("No input image found.");
                System.out.println("Creating sample.jpg automatically...");
                createSampleImage("sample.jpg");
                inputFile = new File("sample.jpg");
            }

            System.out.println("Input image: " + inputFile.getAbsolutePath());

            BufferedImage image = ImageIO.read(inputFile);

            if (image == null) {
                throw new Exception(
                    "The selected file is not a valid JPG/PNG image."
                );
            }

            int width = image.getWidth();
            int height = image.getHeight();

            System.out.println("Width  : " + width);
            System.out.println("Height : " + height);
            System.out.println("Pixels : " + (width * height));
            System.out.println("K      : " + K);

            convertImageToPixels(image);
            initializeCenters();
            kMeans();

            BufferedImage segmented =
                    createSegmentedImage(width, height);

            File outputFolder = new File("output");
            if (!outputFolder.exists()) {
                outputFolder.mkdirs();
            }

            ImageIO.write(
                    image,
                    "jpg",
                    new File(outputFolder, "original.jpg")
            );

            ImageIO.write(
                    segmented,
                    "jpg",
                    new File(outputFolder, "segmented.jpg")
            );

            BufferedImage comparison =
                    createComparisonImage(image, segmented);

            ImageIO.write(
                    comparison,
                    "jpg",
                    new File(outputFolder, "comparison.jpg")
            );

            System.out.println();
            System.out.println("======================================");
            System.out.println(" PROJECT COMPLETED SUCCESSFULLY!");
            System.out.println("======================================");
            System.out.println("Check the 'output' folder:");
            System.out.println("1. original.jpg");
            System.out.println("2. segmented.jpg");
            System.out.println("3. comparison.jpg");

        } catch (Exception e) {
            System.out.println();
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Finds common image names in the current project folder.
    static File findInputImage() {
        String[] names = {
            "sample.jpg",
            "sample.jpeg",
            "sample.png",
            "input.jpg",
            "input.jpeg",
            "input.png",
            "image.jpg",
            "image.jpeg",
            "image.png"
        };

        for (String name : names) {
            File file = new File(name);
            if (file.exists() && file.isFile()) {
                return file;
            }
        }

        return null;
    }

    // Creates a simple image automatically if the user has no image.
    static void createSampleImage(String fileName) throws Exception {

        int width = 600;
        int height = 400;

        BufferedImage image =
                new BufferedImage(
                    width,
                    height,
                    BufferedImage.TYPE_INT_RGB
                );

        Graphics2D g = image.createGraphics();

        g.setColor(new Color(80, 150, 220));
        g.fillRect(0, 0, width, height / 2);

        g.setColor(new Color(100, 190, 90));
        g.fillRect(0, height / 2, width, height / 2);

        g.setColor(new Color(220, 70, 70));
        g.fillOval(80, 180, 180, 150);

        g.setColor(new Color(240, 190, 60));
        g.fillRect(350, 200, 160, 120);

        g.setColor(Color.DARK_GRAY);
        int[] x = {270, 330, 390};
        int[] y = {150, 80, 150};
        g.fillPolygon(x, y, 3);

        g.dispose();

        ImageIO.write(image, "jpg", new File(fileName));
    }

    static void convertImageToPixels(BufferedImage image) {

        int width = image.getWidth();
        int height = image.getHeight();

        pixels = new double[width * height][3];
        labels = new int[width * height];

        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                Color color =
                        new Color(image.getRGB(x, y));

                pixels[index][0] = color.getRed();
                pixels[index][1] = color.getGreen();
                pixels[index][2] = color.getBlue();

                index++;
            }
        }
    }

    static void initializeCenters() {

        centers = new double[K][3];

        Random random = new Random(42);

        for (int i = 0; i < K; i++) {

            int randomPixel =
                    random.nextInt(pixels.length);

            centers[i][0] =
                    pixels[randomPixel][0];

            centers[i][1] =
                    pixels[randomPixel][1];

            centers[i][2] =
                    pixels[randomPixel][2];
        }
    }

    static void kMeans() {

        for (int iteration = 0;
             iteration < MAX_ITERATIONS;
             iteration++) {

            System.out.println(
                    "Iteration " + (iteration + 1)
            );

            assignClusters();

            boolean changed = updateCenters();

            if (!changed) {
                System.out.println("Clusters converged.");
                break;
            }
        }
    }

    static void assignClusters() {

        for (int i = 0; i < pixels.length; i++) {

            double minimumDistance =
                    Double.MAX_VALUE;

            int nearestCluster = 0;

            for (int j = 0; j < K; j++) {

                double distance =
                        calculateDistance(
                            pixels[i],
                            centers[j]
                        );

                if (distance < minimumDistance) {
                    minimumDistance = distance;
                    nearestCluster = j;
                }
            }

            labels[i] = nearestCluster;
        }
    }

    static double calculateDistance(
            double[] pixel,
            double[] center) {

        double r = pixel[0] - center[0];
        double g = pixel[1] - center[1];
        double b = pixel[2] - center[2];

        return Math.sqrt(
                r * r +
                g * g +
                b * b
        );
    }

    static boolean updateCenters() {

        double[][] newCenters =
                new double[K][3];

        int[] count = new int[K];

        for (int i = 0; i < pixels.length; i++) {

            int cluster = labels[i];

            newCenters[cluster][0] += pixels[i][0];
            newCenters[cluster][1] += pixels[i][1];
            newCenters[cluster][2] += pixels[i][2];

            count[cluster]++;
        }

        for (int i = 0; i < K; i++) {

            if (count[i] > 0) {

                newCenters[i][0] /= count[i];
                newCenters[i][1] /= count[i];
                newCenters[i][2] /= count[i];

            } else {

                newCenters[i][0] = centers[i][0];
                newCenters[i][1] = centers[i][1];
                newCenters[i][2] = centers[i][2];
            }
        }

        boolean changed = false;

        for (int i = 0; i < K; i++) {

            double difference =
                    Math.abs(centers[i][0] - newCenters[i][0]) +
                    Math.abs(centers[i][1] - newCenters[i][1]) +
                    Math.abs(centers[i][2] - newCenters[i][2]);

            if (difference > 0.5) {
                changed = true;
            }
        }

        centers = newCenters;

        return changed;
    }

    static BufferedImage createSegmentedImage(
            int width,
            int height) {

        BufferedImage segmented =
                new BufferedImage(
                    width,
                    height,
                    BufferedImage.TYPE_INT_RGB
                );

        int index = 0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                int cluster = labels[index];

                int red =
                        clamp((int) centers[cluster][0]);

                int green =
                        clamp((int) centers[cluster][1]);

                int blue =
                        clamp((int) centers[cluster][2]);

                Color color =
                        new Color(red, green, blue);

                segmented.setRGB(
                        x,
                        y,
                        color.getRGB()
                );

                index++;
            }
        }

        return segmented;
    }

    static BufferedImage createComparisonImage(
            BufferedImage original,
            BufferedImage segmented) {

        int width = original.getWidth();
        int height = original.getHeight();

        BufferedImage comparison =
                new BufferedImage(
                    width * 2,
                    height,
                    BufferedImage.TYPE_INT_RGB
                );

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                comparison.setRGB(
                        x,
                        y,
                        original.getRGB(x, y)
                );

                comparison.setRGB(
                        x + width,
                        y,
                        segmented.getRGB(x, y)
                );
            }
        }

        return comparison;
    }

    static int clamp(int value) {

        if (value < 0) return 0;
        if (value > 255) return 255;

        return value;
    }
}
