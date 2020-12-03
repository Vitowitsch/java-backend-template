package botsandbytes.dashboard.backend.storage;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import botsandbytes.dashboard.backend.storage.Expectancy;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class S3MockFromFileTest {

	@Test
	public void getNewFiles() throws IOException {
		assertTrue(0 < new Expectancy().get(true).length());
	}

}
