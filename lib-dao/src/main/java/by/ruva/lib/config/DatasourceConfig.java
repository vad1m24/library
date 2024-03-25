package by.ruva.lib.config;

import by.ruva.lib.utils.config.MapperConfiguration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(value = { MapperConfiguration.class })
public class DatasourceConfig {

}
