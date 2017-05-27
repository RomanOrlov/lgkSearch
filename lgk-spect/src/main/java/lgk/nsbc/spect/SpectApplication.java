package lgk.nsbc.spect;

import com.vaadin.spring.annotation.EnableVaadin;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableVaadin
@EnableCaching
@ComponentScan(value = {"lgk.nsbc"})
public class SpectApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpectApplication.class, args);
    }
}
