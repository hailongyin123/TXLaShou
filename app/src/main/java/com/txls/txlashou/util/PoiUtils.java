package com.txls.txlashou.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Picture;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Table;
import org.apache.poi.hwpf.usermodel.TableCell;
import org.apache.poi.hwpf.usermodel.TableIterator;
import org.apache.poi.hwpf.usermodel.TableRow;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by pc on 2017/3/7.
 */

public class PoiUtils {
    private Range range = null;
    private HWPFDocument hwpf = null;
    private String htmlPath;
    private String picturePath;
    private List pictures;
    private TableIterator tableIterator;
    private int presentPicture = 0;
    private FileOutputStream output;
    private File myFile;
    private int screenWidth;

    public void convert2Html(String filePath,int WindowWidth,Context context) {
        screenWidth = WindowWidth;
        WriteToSD(context);
        getRange(filePath);
        makeFile();
        readAndWrite();
    }

    private void getRange(String filePath) {
        FileInputStream in = null;
        POIFSFileSystem pfs = null;
        try {
            in = new FileInputStream(filePath);
            pfs = new POIFSFileSystem(in);
            hwpf = new HWPFDocument(pfs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        range = hwpf.getRange();
        pictures = hwpf.getPicturesTable().getAllPictures();
        tableIterator = new TableIterator(range);

    }

    public void WriteToSD(Context context) {
        File sdFile = Environment.getExternalStorageDirectory();
        String path = sdFile.getAbsolutePath() + File.separator + "txlashou";
        File dirFile = new File(path);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        File docFile = new File(path + File.separator + "content.doc");
        InputStream inputStream;
        try {
            if (!docFile.exists()) {
                docFile.createNewFile();
            }
            inputStream = context.getResources().getAssets().open("content.doc");
            FileOutputStream fileOutputStream = new FileOutputStream(path + "/content.doc");
            byte[] buffer = new byte[512];
            int count = 0;
            while ((count = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, count);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void makeFile() {
        String sdStateString = Environment.getExternalStorageState();
        if (sdStateString.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File sdFile = Environment.getExternalStorageDirectory();
                String path = sdFile.getAbsolutePath() + File.separator + "txlashou";
                File dirFile = new File(path);
                if (!dirFile.exists()) {
                    dirFile.mkdir();
                }
                File myFile = new File(path + File.separator + "content.html");
                if (!myFile.exists()) {
                    myFile.createNewFile();
                }
                htmlPath = myFile.getAbsolutePath();

            } catch (Exception e) {
            }
        }
    }

    /*用来在sdcard上创建图片*/
    public void makePictureFile() {
        String sdString = Environment.getExternalStorageState();

        if (sdString.equals(Environment.MEDIA_MOUNTED)) {
            try {
                File picFile = Environment.getExternalStorageDirectory();

                String picPath = picFile.getAbsolutePath() + File.separator + "txlashou";

                File picDirFile = new File(picPath);

                if (!picDirFile.exists()) {
                    picDirFile.mkdir();
                }
                File pictureFile = new File(picPath + File.separator + presentPicture + ".jpg");

                if (!pictureFile.exists()) {
                    pictureFile.createNewFile();
                }

                picturePath = pictureFile.getAbsolutePath();

            } catch (Exception e) {
                System.out.println("PictureFile Catch Exception");
            }
        }
    }

    /*读取word中的内容写到sdcard上的.html文件中*/
    public void readAndWrite() {

        try {
            myFile = new File(htmlPath);
            output = new FileOutputStream(myFile);
            String head = "<html><body>";
            String tagBegin = "<p>";
            String tagEnd = "</p>";
            output.write(head.getBytes());
            int numParagraphs = range.numParagraphs();
            for (int i = 0; i < numParagraphs; i++) {
                Paragraph p = range.getParagraph(i);
                if (p.isInTable()) {
                    int temp = i;
                    if (tableIterator.hasNext()) {
                        String tableBegin = "<table style=\"border-collapse:collapse\" border=1 bordercolor=\"black\">";
                        String tableEnd = "</table>";
                        String rowBegin = "<tr>";
                        String rowEnd = "</tr>";
                        String colBegin = "<td>";
                        String colEnd = "</td>";

                        Table table = tableIterator.next();

                        output.write(tableBegin.getBytes());

                        int rows = table.numRows();

                        for (int r = 0; r < rows; r++) {
                            output.write(rowBegin.getBytes());
                            TableRow row = table.getRow(r);
                            int cols = row.numCells();
                            int rowNumParagraphs = row.numParagraphs();
                            int colsNumParagraphs = 0;
                            for (int c = 0; c < cols; c++) {
                                output.write(colBegin.getBytes());
                                TableCell cell = row.getCell(c);
                                int max = temp + cell.numParagraphs();
                                colsNumParagraphs = colsNumParagraphs + cell.numParagraphs();
                                for (int cp = temp; cp < max; cp++) {
                                    Paragraph p1 = range.getParagraph(cp);
                                    output.write(tagBegin.getBytes());
                                    writeParagraphContent(p1);
                                    output.write(tagEnd.getBytes());
                                    temp++;
                                }
                                output.write(colEnd.getBytes());
                            }
                            int max1 = temp + rowNumParagraphs;
                            for (int m = temp + colsNumParagraphs; m < max1; m++) {
                                Paragraph p2 = range.getParagraph(m);
                                temp++;
                            }
                            output.write(rowEnd.getBytes());
                        }
                        output.write(tableEnd.getBytes());
                    }
                    i = temp;
                } else {
                    output.write(tagBegin.getBytes());
                    writeParagraphContent(p);
                    output.write(tagEnd.getBytes());
                }
            }

            String end = "</body></html>";
            output.write(end.getBytes());
            output.close();
        } catch (Exception e) {
            System.out.println("readAndWrite Exception");
        }
    }

    /*以段落的形式来往html文件中写内容*/
    public void writeParagraphContent(Paragraph paragraph) {
        Paragraph p = paragraph;
        int pnumCharacterRuns = p.numCharacterRuns();

        for (int j = 0; j < pnumCharacterRuns; j++) {

            CharacterRun run = p.getCharacterRun(j);

            if (run.getPicOffset() == 0 || run.getPicOffset() >= 1000) {
                if (presentPicture < pictures.size()) {
                    writePicture();
                }
            } else {
                try {
                    String text = run.text();
                    if (text.length() >= 2 && pnumCharacterRuns < 2) {
                        output.write(text.getBytes());
                    } else {
                        int size = run.getFontSize();
                        int color = run.getColor();
                        String fontSizeBegin = "<font size=\"" + decideSize(size) + "\">";
                        String fontColorBegin = "<font color=\"" + decideColor(color) + "\">";
                        String fontEnd = "</font>";
                        String boldBegin = "<b>";
                        String boldEnd = "</b>";
                        String islaBegin = "<i>";
                        String islaEnd = "</i>";

                        output.write(fontSizeBegin.getBytes());
                        output.write(fontColorBegin.getBytes());

                        if (run.isBold()) {
                            output.write(boldBegin.getBytes());
                        }
                        if (run.isItalic()) {
                            output.write(islaBegin.getBytes());
                        }

                        output.write(text.getBytes());

                        if (run.isBold()) {
                            output.write(boldEnd.getBytes());
                        }
                        if (run.isItalic()) {
                            output.write(islaEnd.getBytes());
                        }
                        output.write(fontEnd.getBytes());
                        output.write(fontEnd.getBytes());
                    }
                } catch (Exception e) {
                    System.out.println("Write File Exception");
                }
            }
        }
    }

    /*将word中的图片写入到.jpg文件中*/
    public void writePicture() {
        Picture picture = (Picture) pictures.get(presentPicture);

        byte[] pictureBytes = picture.getContent();

        Bitmap bitmap = BitmapFactory.decodeByteArray(pictureBytes, 0, pictureBytes.length);

        makePictureFile();
        presentPicture++;

        File myPicture = new File(picturePath);

        try {

            FileOutputStream outputPicture = new FileOutputStream(myPicture);

            outputPicture.write(pictureBytes);

            outputPicture.close();
        } catch (Exception e) {
            System.out.println("outputPicture Exception");
        }

        String imageString = "<img src=\"" + picturePath + "\"";

        if (bitmap.getWidth() > screenWidth) {
            imageString = imageString + " " + "width=\"" + screenWidth + "\"";
        }
        imageString = imageString + ">";

        try {
            output.write(imageString.getBytes());
        } catch (Exception e) {
            System.out.println("output Exception");
        }
    }

    /*处理word和html字体的转换*/
    public int decideSize(int size) {

        if (size >= 1 && size <= 8) {
            return 1;
        }
        if (size >= 9 && size <= 11) {
            return 2;
        }
        if (size >= 12 && size <= 14) {
            return 3;
        }
        if (size >= 15 && size <= 19) {
            return 4;
        }
        if (size >= 20 && size <= 29) {
            return 5;
        }
        if (size >= 30 && size <= 39) {
            return 6;
        }
        if (size >= 40) {
            return 7;
        }
        return 3;
    }

    /*处理word和html颜色的转换*/
    private String decideColor(int a) {
        int color = a;
        switch (color) {
            case 1:
                return "#000000";
            case 2:
                return "#0000FF";
            case 3:
            case 4:
                return "#00FF00";
            case 5:
            case 6:
                return "#FF0000";
            case 7:
                return "#FFFF00";
            case 8:
                return "#FFFFFF";
            case 9:
                return "#CCCCCC";
            case 10:
            case 11:
                return "#00FF00";
            case 12:
                return "#080808";
            case 13:
            case 14:
                return "#FFFF00";
            case 15:
                return "#CCCCCC";
            case 16:
                return "#080808";
            default:
                return "#000000";
        }
    }
}
