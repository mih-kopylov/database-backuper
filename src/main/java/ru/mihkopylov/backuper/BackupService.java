package ru.mihkopylov.backuper;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.DiskInfo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class BackupService implements CommandLineRunner {
    @NonNull
    private final Configuration configuration;

    @Override
    public void run( String... args ) {
        log.info( "backup service started" );
        RestClient restClient = new RestClient( new Credentials( configuration.getUser(), configuration.getToken() ) );
        log.info( "configuration: {}", configuration.toString() );
        log.info( "free space: {} Mb", getFreeSpace( restClient ) );
        log.info( "running backup" );

        log.info( "backup service completed" );
    }

    @SneakyThrows
    private long getFreeSpace( @NonNull RestClient restClient ) {
        DiskInfo diskInfo = restClient.getDiskInfo();
        return (diskInfo.getTotalSpace() - diskInfo.getUsedSpace()) / 1024 / 1024;
    }
}
