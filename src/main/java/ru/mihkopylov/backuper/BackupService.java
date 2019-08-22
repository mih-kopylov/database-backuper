package ru.mihkopylov.backuper;

import java.io.File;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class BackupService implements Runnable {
    @NonNull
    private final Configuration configuration;

    @Override
    public void run() {
        log.info( "creating backup" );
        File backupFile = createBackup();
        log.info( "backup created in {}", backupFile );
    }

    @NonNull
    @SneakyThrows
    private File createBackup() {
        String command = String.format( "mysqldump -u %s -p%s --databases %s -r %s", configuration.getDbUser(),
                configuration.getDbPassword(), configuration.getDbDatabase(), configuration.getDbDumpFile() );
        int processExitCode = Runtime.getRuntime().exec( command ).waitFor();
        if (processExitCode != 0) {
            throw new RuntimeException( String.format( "backup process exited with %s code", processExitCode ) );
        }
        File result = new File( configuration.getDbDumpFile() );
        if (!result.exists()) {
            throw new RuntimeException( String.format( "backup file %s was not created", result ) );
        }
        return result;
    }
}
