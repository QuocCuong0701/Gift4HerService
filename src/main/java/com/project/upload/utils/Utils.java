package com.project.upload.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static ResponseEntity responseData(HttpStatus status, boolean error, String messages, Object responseData) {
        Map<String, Object> map = new HashMap<>();
        map.put("timestamp", new Date());
        map.put("status", status.value());
        map.put("isSuccess", error);
        map.put("messages", messages);
        map.put("data", responseData);

        return new ResponseEntity<>(map, status);
    }

    public static String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    public static String getFileType(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String generateFileNameCrop(String fileName) {
        String name = fileName.substring(0, fileName.lastIndexOf(".")) + "_crop";
        String ext = getExtension(fileName);
        return name + ext;
    }

    public static String millisecondToDateString(Long millisecond) {
        Date convertDate = new Date(millisecond);
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(convertDate);
    }

    public static Date millisecondToDate(Long millisecond) {
        return new Date(millisecond);
    }

    /**
     * Crop image function
     *
     * @param image
     * @return
     */
    public static BufferedImage cropImageSquare(byte[] image) {
        try {
            // Get a BufferedImage object from a byte array
            InputStream in = new ByteArrayInputStream(image);
            BufferedImage originalImage = ImageIO.read(in);

            //get image dimensions
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            if (width == height)
                return originalImage;

            int squareSize = height > width ? width : height;
            // Coordinates of the image's middle
            int xc = width / 2;
            int yc = height / 2;

            // Crop
            BufferedImage cropImage = originalImage.getSubimage(
                    xc - (squareSize / 2),  // x coordinate of the upper-left corner
                    yc - (squareSize / 2),  // y coordinate of the upper-left corner
                    squareSize,                // width
                    squareSize                 // height
            );

            return cropImage;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
