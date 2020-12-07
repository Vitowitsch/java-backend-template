package botsandbytes.java.backend.template;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import botsandbytes.java.backend.template.dao.CSRMDao;
import botsandbytes.java.backend.template.response.Row;

@Service
public class SnapshotDataCache {

	@Autowired
	Config config;

	@Autowired
	private CSRMDao csrmDao;

	List<Row> cache = null;

	@Scheduled(fixedRate = 7200000)
	private void updateCache() {
		cache = csrmDao.getAll();
	}

	public List<Row> getRows() {
		return cache;
	}
}
