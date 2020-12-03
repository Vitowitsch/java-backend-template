package botsandbytes.dashboard.backend.storage;

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

	public LocalFsAccess(boolean inMem, String s3MockFolder) {
		if (inMem) {
			api = new S3Mock.Builder().withPort(8001).withInMemoryBackend().build();
		} else {
			api = new S3Mock.Builder().withPort(8001).withFileBackend(s3MockFolder).build();
		}
		api.start();

	}

	public AmazonS3 getClient() {
		EndpointConfiguration endpoint = new EndpointConfiguration("http://localhost:8001", "eu-central-1");
		ClientConfiguration clientCfg = new ClientConfiguration();
		clientCfg.setProtocol(Protocol.HTTP);
		clientCfg.setNonProxyHosts("localhost");
		return AmazonS3ClientBuilder.standard().withPathStyleAccessEnabled(true).withEndpointConfiguration(endpoint)
				.withCredentials(new AWSStaticCredentialsProvider(new AnonymousAWSCredentials()))
				.withClientConfiguration(clientCfg).build();
	}

	@Override
	public void stop() {
		api.stop();
		api.shutdown();
	}
}
