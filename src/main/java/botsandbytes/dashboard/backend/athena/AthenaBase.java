package botsandbytes.dashboard.backend.athena;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.athena.AthenaClient;
import software.amazon.awssdk.services.athena.model.GetQueryExecutionRequest;
import software.amazon.awssdk.services.athena.model.GetQueryExecutionResponse;
import software.amazon.awssdk.services.athena.model.GetQueryResultsRequest;
import software.amazon.awssdk.services.athena.model.QueryExecutionContext;
import software.amazon.awssdk.services.athena.model.QueryExecutionState;
import software.amazon.awssdk.services.athena.model.ResultConfiguration;
import software.amazon.awssdk.services.athena.model.StartQueryExecutionRequest;
import software.amazon.awssdk.services.athena.model.StartQueryExecutionResponse;
import software.amazon.awssdk.services.athena.model.StopQueryExecutionRequest;
import software.amazon.awssdk.services.athena.paginators.GetQueryResultsIterable;

public class AthenaBase {

	private Logger logger = LogManager.getLogger(this.getClass());

	private static final int CLIENT_EXECUTION_TIMEOUT_MS = 1000 * 60 * 5;
	private static final long SLEEP_AMOUNT_IN_MS = 1000;

	private String outputLocation;

	public AthenaBase(String outputLocation) {
		this.outputLocation = outputLocation;
	}

	private AthenaClient createClient() {
		return AthenaClient.builder().region(Region.EU_CENTRAL_1)
				.credentialsProvider(DefaultCredentialsProvider.create()).build();
	}

	public GetQueryResultsIterable queryAthena(String statement) throws InterruptedException {
		AthenaClient athenaClient = createClient();
		String queryExecutionId = submitAthenaQuery(athenaClient, statement);
		waitForQueryToComplete(athenaClient, queryExecutionId);

		return getResults(athenaClient, queryExecutionId);
	}

	private GetQueryResultsIterable getResults(AthenaClient athenaClient, String queryExecutionId) {
		GetQueryResultsRequest getQueryResultsRequest = GetQueryResultsRequest.builder()
//				.maxResults(Integer.MAX_VALUE)
				.queryExecutionId(queryExecutionId).build();
		return athenaClient.getQueryResultsPaginator(getQueryResultsRequest);
	}

	/**
	 * Wait for an Athena query to complete, fail or to be cancelled. This is done
	 * by polling Athena over an interval of time. If a query fails or is cancelled,
	 * then it will throw an exception.
	 */
	private void waitForQueryToComplete(AthenaClient athenaClient, String queryExecutionId)
			throws InterruptedException {
		GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
				.queryExecutionId(queryExecutionId).build();

		GetQueryExecutionResponse getQueryExecutionResponse;
		long startTime = System.currentTimeMillis();
		boolean isQueryStillRunning = true;
		while (isQueryStillRunning) {
			if ((System.currentTimeMillis() - startTime) > CLIENT_EXECUTION_TIMEOUT_MS) {
				logger.warn("Query " + queryExecutionId + " took to long to respond. Going to be cancelled.");
				stopQueryExecution(athenaClient, queryExecutionId);
			}
			getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
			String queryState = getQueryExecutionResponse.queryExecution().status().state().toString();
			if (queryState.equals(QueryExecutionState.FAILED.toString())) {
				throw new RuntimeException("Query Failed to run with Error Message: "
						+ getQueryExecutionResponse.queryExecution().status().stateChangeReason());
			} else if (queryState.equals(QueryExecutionState.CANCELLED.toString())) {
				throw new RuntimeException("Query was cancelled.");
			} else if (queryState.equals(QueryExecutionState.SUCCEEDED.toString())) {
				isQueryStillRunning = false;
			} else {
				Thread.sleep(SLEEP_AMOUNT_IN_MS);
			}
			logger.debug("Current status of Athena query: " + queryState);
		}
	}

	private void stopQueryExecution(AthenaClient athenaClient, String queryExecutionId) {
		StopQueryExecutionRequest stopQueryExecutionRequest = StopQueryExecutionRequest.builder()
				.queryExecutionId(queryExecutionId).build();

		athenaClient.stopQueryExecution(stopQueryExecutionRequest);
		// Ensure that the query was stopped
		GetQueryExecutionRequest getQueryExecutionRequest = GetQueryExecutionRequest.builder()
				.queryExecutionId(queryExecutionId).build();

		GetQueryExecutionResponse getQueryExecutionResponse = athenaClient.getQueryExecution(getQueryExecutionRequest);
		if (getQueryExecutionResponse.queryExecution().status().state().equals(QueryExecutionState.CANCELLED)) {
			// Query was cancelled.
			logger.info("Query " + queryExecutionId + " was successfully cancelled.");
		} else {
			throw new RuntimeException("Query " + queryExecutionId + " could not be cancelled.");
		}
	}

	/**
	 * Submits a sample query to Athena and returns the execution ID of the query.
	 */
	private String submitAthenaQuery(AthenaClient athenaClient, String statement) {

		QueryExecutionContext queryExecutionContext = QueryExecutionContext.builder().database("db")
				.build();
		logger.info("ATHENA_OUTPUT:" + outputLocation);
		ResultConfiguration resultConfiguration = ResultConfiguration.builder().outputLocation(this.outputLocation)
				.build();

		StartQueryExecutionRequest startQueryExecutionRequest = StartQueryExecutionRequest.builder()
				.queryString(statement).queryExecutionContext(queryExecutionContext)
				.resultConfiguration(resultConfiguration).build();

		StartQueryExecutionResponse startQueryExecutionResponse = athenaClient
				.startQueryExecution(startQueryExecutionRequest);
		return startQueryExecutionResponse.queryExecutionId();
	}

}
