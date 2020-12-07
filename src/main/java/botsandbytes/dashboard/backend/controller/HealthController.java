package botsandbytes.dashboard.backend.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/** delivers a heart beat to the load balancer */
@RestController
public class HealthController {

	@RequestMapping(value = "/health")
	@ResponseBody
	@CrossOrigin
	public String getHeartBeat() {
		return "up and running";
	}
}