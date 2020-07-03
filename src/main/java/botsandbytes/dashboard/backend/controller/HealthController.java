package botsandbytes.dashboard.backend.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

	private Logger logger = LogManager.getLogger(this.getClass());

	@RequestMapping(value = "/health")
	@ResponseBody
	@CrossOrigin
	public String getHeartBeat() {
		logger.info("/health called");
		return "up and running";
	}
}