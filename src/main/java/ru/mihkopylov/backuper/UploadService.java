package ru.mihkopylov.backuper;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ProgressListener;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.Link;
import java.io.File;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UploadService {
    @NonNull
    private final Configuration configuration;

    @SneakyThrows
    public void run( @NonNull File file ) {
        log.info( "uploading backup" );
        RestClient restClient =
                new RestClient( new Credentials( configuration.getDiskUser(), configuration.getDiskToken() ) );
        log.info( "free space: {} Mb", getFreeSpace( restClient ) );
        String uploadPath = configuration.getDiskPath() + "/" + file.getName();
        Link uploadLink = restClient.getUploadLink( uploadPath, true );
        restClient.uploadFile( uploadLink, false, file, new LoggingProgressListener() );
        log.info( "backup uploaded" );
        if (file.delete()) {
            log.info( "backup file {} deleted", file );
        } else {
            log.warn( "back file {} was not deleted", file );
        }
    }

    @SneakyThrows
    private long getFreeSpace( @NonNull RestClient restClient ) {
        DiskInfo diskInfo = restClient.getDiskInfo();
        return (diskInfo.getTotalSpace() - diskInfo.getUsedSpace()) / 1024 / 1024;
    }

    private static class LoggingProgressListener implements ProgressListener {
        @Override
        public void updateProgress( long loaded, long total ) {
            log.info( "uploaded {}%: {} of {}", loaded * 100 / total, loaded, total );
        }

        @Override
        public boolean hasCancelled() {
            return false;
        }
    }
}
