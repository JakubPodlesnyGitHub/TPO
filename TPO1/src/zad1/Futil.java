package zad1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Futil {
    public static void processDir(String dirName, String resultFileName) {
        try {
            FileChannel fileChannelOUT = FileChannel.open(Paths.get(resultFileName), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,StandardOpenOption.WRITE);
            Files.walkFileTree(Paths.get(dirName), new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path path, BasicFileAttributes basicFileAttributes) throws IOException {
                    FileChannel fileChannelIN = FileChannel.open(path);
                    ByteBuffer byteBuffer = ByteBuffer.allocate((int) fileChannelIN.size());
                    fileChannelIN.read(byteBuffer);
                    byteBuffer.flip();
                    CharBuffer charBuffer = Charset.forName("Cp1250").decode(byteBuffer);
                    fileChannelOUT.write(StandardCharsets.UTF_8.encode(charBuffer));
                    fileChannelIN.close();
                    return FileVisitResult.CONTINUE;
                }
            });
            fileChannelOUT.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
