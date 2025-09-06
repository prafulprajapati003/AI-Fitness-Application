package com.fitness.aiservice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fitness.aiservice.model.Recommendation;
import com.fitness.aiservice.repository.RecommendationRepo;

@Service
public class RecommendationService {

	@Autowired
	private RecommendationRepo recRepo;

	public List<Recommendation> getUserRecommendation(String userId) {
		// TODO Auto-generated method stub
		return recRepo.findByUserId(userId);
	}

	public Recommendation getActivityRecommendation(String activityId) {
		// TODO Auto-generated method stub
		return recRepo.findByActivityId(activityId)
				.orElseThrow(() -> new RuntimeException("No recommendation found for this activity:" + activityId));
	}

}
