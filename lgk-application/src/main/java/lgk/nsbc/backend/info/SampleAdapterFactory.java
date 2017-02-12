package lgk.nsbc.backend.info;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SampleAdapterFactory {

    @Bean
    @Scope("prototype")
    public SampleAdapter getSampleAdapter() {
        return new SampleAdapter();
    }
}
