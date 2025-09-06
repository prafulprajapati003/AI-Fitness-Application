package com.fitness.activity.contoller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fitness.activity.dto.ActivityRequest;
import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.service.ActivityService;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

	@Autowired
	private ActivityService activityService;

	@PostMapping("/track")
	public ResponseEntity<ActivityResponse> trackActivity(@RequestBody ActivityRequest request) {

		return ResponseEntity.ok(activityService.trackActivity(request));
	}

	@GetMapping("/getactivity")
	public ResponseEntity<List<ActivityResponse>> getUserActivity(@RequestHeader("X-User-ID") String userId) {

		return ResponseEntity.ok(activityService.getUserActivity(userId));
	}
	@GetMapping("/{activityId}")
	public ResponseEntity<ActivityResponse> getActivity(@PathVariable String activityId) {
		
		return ResponseEntity.ok(activityService.getActivity(activityId));
	}

}
