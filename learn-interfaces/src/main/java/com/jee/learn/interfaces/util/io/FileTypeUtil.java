package com.jee.learn.interfaces.util.io;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import org.apache.commons.lang3.StringUtils;

import com.jee.learn.interfaces.util.text.EncodeUtil;

/**
 * 
 * 文件类型工具
 * 
 * @author ccp
 * @version 1.0<br/>
 *          修改记录:<br/>
 *          1.2018年11月10日 上午11:18:58 ccp 新建
 */
public class FileTypeUtil {

    private static final int BYTE_SIZE = 28;

    /**
     * 以流方式获取文件类型
     * 
     * @param file
     * @return 小写的文件类型
     * @throws IOException
     * @see {@link FileTypeUtil#getFileType(Path)}
     */
    public static String getFileType(File file) throws IOException {
        if (file == null) {
            return StringUtils.EMPTY;
        }
        return getFileType(file.toPath());
    }

    /**
     * 以流方式获取文件类型
     * 
     * @param path
     * @return 小写的文件类型, 若无法识别文件头或在{@link FileTypeUtil.FileType}字典没有匹配时返回{@link StringUtils#EMPTY}
     * @throws IOException
     * @see 若需要更新匹配字典, 请修改{@link FileTypeUtil.FileType}
     */
    public static String getFileType(Path path) throws IOException {
        byte[] b = new byte[BYTE_SIZE];

        InputStream inStream = FileUtil.asInputStream(path);
        inStream.read(b, 0, BYTE_SIZE);
        String fileHead = EncodeUtil.encodeHex(b);

        if (StringUtils.isBlank(fileHead)) {
            return StringUtils.EMPTY;
        }
        fileHead = fileHead.toUpperCase();
        FileType[] fileTypes = FileType.values();
        String typeName = StringUtils.EMPTY;

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getValue())) {
                typeName = type.name;
                break;
            }
        }
        return typeName.toLowerCase();
    }

    /**
     * 文件类型匹配字典
     * 
     * @author ccp
     * @version 1.0<br/>
     *          修改记录:<br/>
     *          1.2018年11月10日 下午12:01:30 ccp 新建
     */
    enum FileType {
        /**
         * JPEG.
         */
        JPEG("jpeg", "FFD8FF"),

        /**
         * JPG.
         */
        JPG("jpg", "FFD8FF"),

        /**
         * PNG.
         */
        PNG("PNG", "89504E47"),

        /**
         * GIF.
         */
        GIF("GIF", "47494638"),

        /**
         * TIFF.
         */
        TIFF("TIFF", "49492A00"),

        /**
         * Windows Bitmap.
         */
        BMP("BMP", "424D"),

        /**
         * CAD.
         */
        DWG("DWG", "41433130"),

        /**
         * Adobe Photoshop.
         */
        PSD("PSD", "38425053"),

        /**
         * Rich Text Format.
         */
        RTF("RTF", "7B5C727466"),

        /**
         * XML.
         */
        XML("XML", "3C3F786D6C"),

        /**
         * HTML.
         */
        HTML("HTML", "68746D6C3E"),

        /**
         * Email [thorough only].
         */
        EML("EML", "44656C69766572792D646174653A"),

        /**
         * Outlook Express.
         */
        DBX("DBX", "CFAD12FEC5FD746F"),

        /**
         * Outlook (pst).
         */
        PST("PST", "2142444E"),

        /**
         * MS Word/Excel.
         */
        XLS_DOC("DOCX", "D0CF11E0"),

        /**
         * MS Access.
         */
        MDB("MDB", "5374616E64617264204A"),

        /**
         * WordPerfect.
         */
        WPD("WPD", "FF575043"),

        /**
         * Postscript.
         */
        EPS("EPS", "252150532D41646F6265"),

        /**
         * Adobe Acrobat.
         */
        PDF("PDF", "255044462D312E"),

        /**
         * Quicken.
         */
        QDF("QDF", "AC9EBD8F"),

        /**
         * Windows Password.
         */
        PWL("PWL", "E3828596"),

        /**
         * ZIP Archive.
         */
        ZIP("ZIP", "504B0304"),

        /**
         * RAR Archive.
         */
        RAR("RAR", "52617221"),

        /**
         * Wave.
         */
        WAV("WAV", "57415645"),

        /**
         * AVI.
         */
        AVI("AVI", "41564920"),

        /**
         * Real Audio.
         */
        RAM("RAM", "2E7261FD"),

        /**
         * Real Media.
         */
        RM("RM", "2E524D46"),

        /**
         * MPEG (mpg).
         */
        MPG("MPG", "000001BA"),

        /**
         * Quicktime.
         */
        MOV("MOV", "6D6F6F76"),

        /**
         * Windows Media.
         */
        ASF("ASF", "3026B2758E66CF11"),

        /**
         * MIDI.
         */
        MID("MID", "4D546864");

        private String name = "";
        private String value = "";

        /**
         * Constructor.
         *
         * @param type
         */
        private FileType(String name, String value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
