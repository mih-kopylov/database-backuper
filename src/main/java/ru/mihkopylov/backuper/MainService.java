package ru.mihkopylov.backuper;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class MainService implements ApplicationRunner {
    @NonNull
    private final Configuration configuration;
    @NonNull
    private final BackupService backupService;
    @NonNull
    private final UploadService uploadService;

    @Override
    public void run( ApplicationArguments args ) {
        log.info( "main service started" );
        log.info( "configuration: {}", configuration.toString() );
        File backupFile = backupService.run();
        uploadService.run(backupFile);
        log.info( "main service completed" );
    }
}
