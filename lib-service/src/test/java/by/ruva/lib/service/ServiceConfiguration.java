package by.ruva.lib.service;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("by.ruva.lib.service")
@EntityScan("by.ruva.lib.entities")
public class ServiceConfiguration {

}
