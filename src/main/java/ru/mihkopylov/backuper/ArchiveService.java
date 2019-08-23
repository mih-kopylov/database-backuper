package ru.mihkopylov.backuper;

import java.io.File;
import java.nio.file.Files;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ArchiveService {
    @NonNull
    @SneakyThrows
    public File run( @NonNull File file ) {
        log.info( "compressing file {}", file );
        File resultFile = new File( file.getParentFile(), file.getName() + ".7z" );
        try (SevenZOutputFile outputFile = new SevenZOutputFile( resultFile )) {
            SevenZArchiveEntry archiveEntry = outputFile.createArchiveEntry( file, file.getName() );
            outputFile.putArchiveEntry( archiveEntry );
            outputFile.write( Files.readAllBytes( file.toPath() ) );
            outputFile.closeArchiveEntry();
        }
        log.info( "compressed file {} is written", resultFile );
        return resultFile;
    }
}
