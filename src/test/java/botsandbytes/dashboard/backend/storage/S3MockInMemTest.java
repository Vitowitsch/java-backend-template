package botsandbytes.dashboard.backend.storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3Object;

import botsandbytes.dashboard.backend.storage.LocalFsAccess;

public class S3MockInMemTest {

	LocalFsAccess s3Mock;
	AmazonS3 client;

	Logger logger = LogManager.getLogger(S3MockInMemTest.class);

	String boogieData = "{\"property\": \"value\"}";

	final static String INBOUND_BUCKET = "my-inbound";
	final static String INTERMEDIATE_BUCKET = "my-intermediate";

	@BeforeEach
	public void init() {
		logger.info("creating s3 access");
		System.setProperty("BUCKET", INBOUND_BUCKET);
		System.setProperty("BUCKET_PREFIX", "FD_VALUES");
		String testDataDir = Paths.get(".").toAbsolutePath().normalize().toString() + "/src/test/resources";
		s3Mock = new LocalFsAccess(true, testDataDir);
		boogieData = boogieData.trim().replaceAll("\r\n", "");
		client = s3Mock.getClient();
		client.createBucket(INBOUND_BUCKET);
		client.createBucket(INTERMEDIATE_BUCKET);
	}

	@AfterEach
	public void destoy() {
		logger.info("destroying s3 access");
		s3Mock.stop();
	}

	@Test
	public void getFile() throws IOException {
		client.putObject(INBOUND_BUCKET, "testfile.json", boogieData);
		S3Object o = client.getObject(INBOUND_BUCKET, "testfile.json");
		BufferedReader reader = new BufferedReader(new InputStreamReader(o.getObjectContent()));
		String receivedData = "";
		String line;
		while ((line = reader.readLine()) != null) {
			receivedData += line;
		}
		assertTrue(boogieData.equals(receivedData));
	}

}
