package ru.mihkopylov.backuper;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "backuper")
@Getter
@Setter
@ToString(exclude = "token")
@Validated
public class Configuration {
    @NonNull
    private String user;
    @NonNull
    private String token;
    @NotBlank
    private String diskPath;
}
