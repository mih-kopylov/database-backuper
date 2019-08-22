package ru.mihkopylov.backuper;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.json.DiskInfo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class UploadService implements Runnable {
    @NonNull
    private final Configuration configuration;

    @Override
    public void run() {
        log.info( "uploading backup" );
        RestClient restClient =
                new RestClient( new Credentials( configuration.getDiskUser(), configuration.getDiskToken() ) );
        log.info( "free space: {} Mb", getFreeSpace( restClient ) );
        log.info( "backup uploaded" );
    }

    @SneakyThrows
    private long getFreeSpace( @NonNull RestClient restClient ) {

        DiskInfo diskInfo = restClient.getDiskInfo();
        return (diskInfo.getTotalSpace() - diskInfo.getUsedSpace()) / 1024 / 1024;
    }
}
