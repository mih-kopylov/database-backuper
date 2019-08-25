package ru.mihkopylov.backuper;

import com.yandex.disk.rest.Credentials;
import com.yandex.disk.rest.ResourcesArgs;
import com.yandex.disk.rest.RestClient;
import com.yandex.disk.rest.exceptions.http.NotFoundException;
import com.yandex.disk.rest.json.DiskInfo;
import com.yandex.disk.rest.json.Link;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
        log.info( "uploading backup file {}", file );
        RestClient restClient =
                new RestClient( new Credentials( configuration.getDiskUser(), configuration.getDiskToken() ) );
        log.info( "free space on yandex disk: {} Mb", getFreeSpaceMb( restClient ) );
        String uploadPath = getUploadPath( file );
        createMissedDirectories( restClient, uploadPath );
        Link uploadLink = restClient.getUploadLink( uploadPath, true );
        restClient.uploadFile( uploadLink, false, file, null );
        log.info( "backup uploaded" );
        if (file.delete()) {
            log.info( "backup file {} deleted", file );
        } else {
            log.warn( "back file {} was not deleted", file );
        }
    }

    @SneakyThrows
    private void createMissedDirectories( @NonNull RestClient restClient, @NonNull String uploadPath ) {
        log.info( "creating directories for {}", uploadPath );
        List<String> paths = new ArrayList<>( Arrays.asList(uploadPath.split( "/" ) ));
        for (int i = 1; i < paths.size(); i++) {
            String tempPath = String.join( "/", paths.subList( 0, i ) );
            try {
                restClient.getResources( new ResourcesArgs.Builder().setPath( tempPath ).setLimit( 10000 ).build() );
                log.info( "path {} exists", tempPath );
            } catch (NotFoundException e) {
                restClient.makeFolder( tempPath );
                log.info( "path {} created", tempPath );
            }
        }
    }

    @NonNull
    private String getUploadPath( @NonNull File file ) {
        String fileYearMonth = file.getName().substring( 0, 7 );
        String fileYearMonthDay = file.getName().substring( 0, 10 );
        List<String> paths = new ArrayList<>( Arrays.asList( configuration.getDiskPath().split( "/" ) ) );
        paths.addAll( Arrays.asList( fileYearMonth, fileYearMonthDay, file.getName() ) );
        return paths.stream()
                .map( String :: trim )
                .filter( o->!o.equals( "" ) )
                .collect( Collectors.joining( "/" ) );
    }

    @SneakyThrows
    private long getFreeSpaceMb( @NonNull RestClient restClient ) {
        DiskInfo diskInfo = restClient.getDiskInfo();
        return (diskInfo.getTotalSpace() - diskInfo.getUsedSpace()) / 1024 / 1024;
    }
}
