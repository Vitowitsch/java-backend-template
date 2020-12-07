package botsandbytes.dashboard.backend.storage;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import botsandbytes.dashboard.backend.Config;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class S3MockFromFileTest {

	Config config = new Config();

	@Test
	public void getNewFiles() throws IOException {
		Expectancy e = new Expectancy(true, "bucket", "expected_outcome.json");
		assertTrue(0 < e.get().length());
		e.stop();
	}

}
