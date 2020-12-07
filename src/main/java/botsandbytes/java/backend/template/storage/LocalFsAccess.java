package botsandbytes.java.backend.template.storage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import io.findify.s3mock.S3Mock;

public class LocalFsAccess extends S3Access {

	private S3Mock api;

	private int PORT = 8001;

	private String uri;

	private Logger logger = LogManager.getLogger(this.getClass());

	public LocalFsAccess(boolean inMem, String s3MockFolder) {
		this.uri = String.format("http://localhost:%d", this.PORT);
		if (inMem) {
			api = new S3Mock.Builder().withPort(PORT).withInMemoryBackend().build();
		} else {
			api = new S3Mock.Builder().withPort(PORT).withFileBackend(s3MockFolder).build();
		}
		api.start();
	}

	public AmazonS3 getClient() {
		logger.info("starting mock-s3 if");
		EndpointConfiguration endpoint = new EndpointConfiguration(this.uri, "eu-central-1");
		ClientConfiguration clientCfg = new ClientConfiguration();
		clientCfg.setProtocol(Protocol.HTTP);
		clientCfg.setNonProxyHosts("localhost");
		return AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true).withEndpointConfiguration(endpoint)
				.withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
				.withClientConfiguration(clientCfg).build();
	}

	@Override
	public void stop() {
		logger.info("terminating mock-s3 if");
		api.stop();
		api.shutdown();
	}
}
