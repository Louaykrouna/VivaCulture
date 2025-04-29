package tn.esprit.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class QRCodeUtil {

    /**
     * Génère une Image JavaFX contenant le QR-Code représentant la chaîne `data`.
     *
     * @param data la chaîne à encoder
     * @param width largeur en pixels du QR
     * @param height hauteur en pixels du QR
     * @return une Image JavaFX
     */
    public static Image generateQRCodeImage(String data, int width, int height) {
        try {
            BitMatrix matrix = new MultiFormatWriter()
                    .encode(data, BarcodeFormat.QR_CODE, width, height);
            BufferedImage buffered = MatrixToImageWriter.toBufferedImage(matrix);
            return SwingFXUtils.toFXImage(buffered, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
