package ru.mihkopylov.backuper;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "backuper")
@Getter
@Setter
@ToString(exclude = {"diskToken", "dbUser", "dbPassword"})
@Validated
public class Configuration {
    @NotBlank
    private String dbUser;
    @NotBlank
    private String dbPassword;
    @NotBlank
    private String dbDatabase;
    @NotBlank
    private String dbDumpFile;
    @NotBlank
    private String diskUser;
    @NotBlank
    private String diskToken;
    @NotBlank
    private String diskPath;
}
