package botsandbytes.dashboard.backend.builder;

import com.google.common.collect.Sets;

import botsandbytes.dashboard.backend.request.ColumnVO;
import botsandbytes.dashboard.backend.request.GetRowsRequest;
import botsandbytes.dashboard.backend.response.GetRowsResponse;

import org.apache.commons.lang3.tuple.Pair;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.stream.Collectors.*;

public class ResponseBuilder {

	public static GetRowsResponse createResponse(GetRowsRequest request, List<Map<String, Object>> rows,
			Map<String, List<String>> pivotValues) {

		int currentLastRow = request.getStartRow() + rows.size();
		int lastRow = currentLastRow <= request.getEndRow() ? currentLastRow : -1;

		List<ColumnVO> valueColumns = request.getValueCols();

		return new GetRowsResponse(rows, lastRow, getSecondaryColumns(pivotValues, valueColumns));
	}

	private static List<String> getSecondaryColumns(Map<String, List<String>> pivotValues,
			List<ColumnVO> valueColumns) {

		List<Set<Pair<String, String>>> pivotPairs = pivotValues.entrySet().stream().map(e -> e.getValue().stream()
				.map(pivotValue -> Pair.of(e.getKey(), pivotValue)).collect(toCollection(LinkedHashSet::new)))
				.collect(toList());

		return Sets.cartesianProduct(pivotPairs).stream().flatMap(pairs -> {
			String pivotCol = pairs.stream().map(Pair::getRight).collect(joining("_"));

			return valueColumns.stream().map(valueCol -> pivotCol + "_" + valueCol.getField());
		}).collect(toList());
	}
}