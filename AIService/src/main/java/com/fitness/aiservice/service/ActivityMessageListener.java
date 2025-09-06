package com.fitness.aiservice.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepo;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityMessageListener {

	@Autowired
	private ActivityAIService aiService;
	@Autowired
	private RecommendationRepo recRepo;

	@RabbitListener(queues = "activity.queue")
	public void processActivity(Activity activity) {

		System.out.println("ActivityMessageListener.processActivity()   and received activity for processing:{} ");

		// System.out.println("ActivityMessageListener.processActivity() and Generated
		// Recommedation :{}");
		Recommendation recommendation = aiService.generateRecommedation(activity);
		recRepo.save(recommendation);
	}

}
