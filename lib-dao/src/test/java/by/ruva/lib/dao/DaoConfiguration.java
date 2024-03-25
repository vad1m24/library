package by.ruva.lib.dao;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("by.ruva.lib.dao")
@EntityScan("by.ruva.lib.entities")
public class DaoConfiguration {

}
