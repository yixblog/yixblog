package cn.yix.blog.utils.validate;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: yxdave
 * Date: 13-6-18
 * Time: 下午9:47
 */
public class ValidateCodeDesigner {
    private static final String CODE_SOURCE = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final BasicStroke BASIC_STROKE = new BasicStroke(1.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1f, new float[]{1f}, 0);

    public static String generateValidateCode(int length) {
        StringBuilder builder = new StringBuilder();
        Random ran = new Random();
        for (int i = 0; i < length; i++) {
            builder.append(CODE_SOURCE.charAt(ran.nextInt(CODE_SOURCE.length())));
        }
        return builder.toString();
    }

    public static byte[] generateValidateImage(String validateCode, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D graphics = (Graphics2D) image.getGraphics();
        drawImage(graphics, validateCode, width, height);
        graphics.dispose();
        try {
            return getJPGBytes(image);
        } catch (IOException e) {
            return null;
        }
    }

    private static byte[] getJPGBytes(BufferedImage image) throws IOException {
        ByteArrayOutputStream ous = new ByteArrayOutputStream();
        ImageIO.write(image, "JPEG", ous);
        return ous.toByteArray();
    }

    private static void drawImage(Graphics2D graphics, String string, int width, int height) {
        int size = height-5;
        Font font = new Font(null, Font.BOLD, size);
        graphics.setFont(font);
        graphics.setBackground(Color.WHITE);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0,0,width,height);
        graphics.setStroke(BASIC_STROKE);
        int eachWidth = width / (string.length()+2);
        drawString(graphics, string, eachWidth,height);
        drawPuzzlingLines(graphics, width, height);
    }

    private static void drawPuzzlingLines(Graphics2D graphics, int width, int height) {
        Random ran = new Random();
        for (int i = 0; i < 15; i++) {
            Color color = getRandomColor();
            graphics.setColor(color);
            int startX = ran.nextInt(width);
            int startY = ran.nextInt(height);
            int endX = ran.nextInt(width);
            int endY = ran.nextInt(height);
            graphics.drawLine(startX, startY, endX, endY);
        }
    }

    private static void drawString(Graphics2D graphics, String string, int eachWidth,int height) {
        for (int i = 0; i < string.length(); i++) {
            Random ran = new Random();
            int x = ran.nextInt(eachWidth - 1) - eachWidth / 2 + eachWidth * (i+1);
            int y = height-ran.nextInt(5);
            Color color = getRandomColor();
            graphics.setColor(color);
            drawRandomChar(graphics, i, string, x, y);
        }
    }

    private static Color getRandomColor() {
        Random ran = new Random();
        int total = 255;
        int red = ran.nextInt(total);
        total -= red;
        int green = ran.nextInt(total);
        int blue = total - green;
        return new Color(red, green, blue);
    }

    private static void drawRandomChar(Graphics2D graphics, int i, String string, int x, int y) {
        graphics.drawChars(string.toCharArray(), i, 1, x, y);
    }

}
