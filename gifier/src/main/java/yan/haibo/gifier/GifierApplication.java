package yan.haibo.gifier;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.annotation.PostConstruct;
import java.io.File;
import java.nio.file.Paths;

@SpringBootApplication
public class GifierApplication {
	@Value("${multipart.location}/gif/")
	private String gifLocation;

	public static void main(String[] args) {
		SpringApplication.run(GifierApplication.class, args);
	}

	@PostConstruct
	private void init() {
		File gifFolder = Paths.get(gifLocation).toFile();
		if (!gifFolder.exists()) {
			gifFolder.mkdir();
		}
	}

	@Bean
	public WebMvcConfigurer webMvcConfigurer() {
		return new WebMvcConfigurerAdapter() {
			@Override
			public void addResourceHandlers(ResourceHandlerRegistry registry) {
				registry.addResourceHandler("/gif/**")
					.addResourceLocations("file:" + gifLocation);
				super.addResourceHandlers(registry);
			}
		};
	}
}
