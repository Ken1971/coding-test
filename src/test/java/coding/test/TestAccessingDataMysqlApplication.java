package coding.test;

import org.springframework.boot.SpringApplication;

public class TestAccessingDataMysqlApplication {

	public static void main(String[] args) {
		SpringApplication.from(CodingTestApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
