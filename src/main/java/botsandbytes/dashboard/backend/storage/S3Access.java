package botsandbytes.dashboard.backend.storage;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class S3Access {

	protected String proxyHost;
	protected String proxyPort;
	protected AmazonS3 client;

	Logger logger = LogManager.getLogger(S3Access.class);

	public S3Access() {
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		ClientConfiguration clientCfg = new ClientConfiguration();
		client = builder.withClientConfiguration(clientCfg).withRegion("eu-central-1").build();

	}

	public S3Access(String proxyHost, String proxyPort) {
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		initS3Client(proxyHost, proxyPort);
	}

	public AmazonS3 getClient() {
		initS3Client(proxyHost, proxyPort);
		return client;
	}

	protected void initS3Client(String proxyHost, String proxyPort) {
		AmazonS3ClientBuilder builder = AmazonS3ClientBuilder.standard();
		ClientConfiguration clientCfg = new ClientConfiguration();
		if (proxyHost != null && proxyPort != null) {
			clientCfg.setProtocol(Protocol.HTTPS);
			clientCfg.setProxyHost(proxyHost);
			clientCfg.setProxyPort(Integer.parseInt(proxyPort));
			builder.withClientConfiguration(clientCfg);
		}
		client = builder.withClientConfiguration(clientCfg).withRegion("eu-central-1").build();
	}

	public byte[] get(String bucket, String key) {
		AmazonS3 c = getClient();
		try (S3ObjectInputStream is = c.getObject(bucket, key).getObjectContent();) {
			return IOUtils.toByteArray(is);
		} catch (Exception e) {
			logger.error("could not extract s3 content from " + bucket + ", " + key, e.getMessage(), e);
		} finally {
			c.shutdown();
		}
		return null;
	}

	public void stop() {
		// mock stub needs that method
	}

}
