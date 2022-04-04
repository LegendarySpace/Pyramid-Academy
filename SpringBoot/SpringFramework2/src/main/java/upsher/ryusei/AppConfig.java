package upsher.ryusei;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
@ComponentScan(basePackages = "upsher.ryusei")
public class AppConfig {

    @Bean
    @Primary
    public Address getAddress() {
        return new Address("Tokyo", "Tokyo-To", "Nihon", "676745");
    }

    @Bean
    public Phone getPhone1() {
        return new Phone("(468) 646 6454");
    }

    @Bean
    public Phone getPhone2() {
        return new Phone("(615)8234972");
    }

    @Bean
    public Phone getPhone3() {
        return new Phone("(123) 4567890");
    }

}
