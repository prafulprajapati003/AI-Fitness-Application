package com.fitness.activity.service;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fitness.activity.Repository.ActivityRepository;
import com.fitness.activity.dto.ActivityRequest;
import com.fitness.activity.dto.ActivityResponse;
import com.fitness.activity.model.Activity;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityService {

	@Autowired
	private ActivityRepository actiRepo;

	@Autowired
	private UserValidateService userValidateService;
	
	@Autowired
	private RabbitTemplate rabbitTemp;
	
	@Value("${rabbitmq.exchange.name}")
	private String exchange;
	
	@Value("${rabbitmq.rounting.key}")
	private String rountingKey;

	public ActivityResponse trackActivity(ActivityRequest request) {

		boolean isValidUser = userValidateService.validateUser(request.getUserId());

		/*if (!isValidUser) {
			throw new RuntimeException("Invalid User: " + request.getUserId());
		}*/

		Activity activity = new Activity();

		activity.setUserId(request.getUserId());
		activity.setType(request.getType());
		activity.setDuration(request.getDuration());
		activity.setCaloriesBurned(request.getCaloriesBurned());
		activity.setStartTime(request.getStartTime());
		activity.setAdditionalMetrics(request.getAdditionalMetrics());

		Activity savedActi = actiRepo.save(activity);
		
		//publish to rabbitmq for ai process
		try {
			rabbitTemp.convertAndSend(exchange,rountingKey,savedActi);
			
		} catch (Exception e) {
			
			System.err.println("Failed to publish activity to RabbitMq :");
			e.printStackTrace();
		}

		return mapToResponse(savedActi);

	}

	private ActivityResponse mapToResponse(Activity acti) {

		ActivityResponse actiRes = new ActivityResponse();

		actiRes.setId(acti.getId());
		actiRes.setUserId(acti.getUserId());
		actiRes.setType(acti.getType());
		actiRes.setDuration(acti.getDuration());
		actiRes.setCaloriesBurned(acti.getCaloriesBurned());
		actiRes.setStartTime(acti.getStartTime());
		actiRes.setAdditionalMetrics(acti.getAdditionalMetrics());
		actiRes.setCreatedAt(acti.getCreatedAt());
		actiRes.setUpdatedAt(acti.getUpdatedAt());

		return actiRes;
	}

	public List<ActivityResponse> getUserActivity(String userId) {

		List<Activity> acti = actiRepo.findByUserId(userId);

		return acti.stream().map(this::mapToResponse).collect(Collectors.toList());
	}

	public ActivityResponse getActivity(String activityId) {
		return actiRepo.findById(activityId).map(this::mapToResponse)
				.orElseThrow(() -> new RuntimeException("Activity not found with id: " + activityId));
	}

}
