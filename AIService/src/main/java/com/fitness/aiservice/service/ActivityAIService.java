package com.fitness.aiservice.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ActivityAIService {

	@Autowired
	private GeminiService geminiService;

	public Recommendation generateRecommedation(Activity activity) {
		String promt = createPromptForActivity(activity);
		String aiResponse = geminiService.getAnswer(promt);
		System.out.println("ActivityAIService.generateRecommedation() Response for AI :{}");


		return processAIResponse(activity, aiResponse);

	}

	private Recommendation processAIResponse(Activity activity, String aiResponse) {
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode rootNode = mapper.readTree(aiResponse);

			JsonNode textNode = rootNode.path("candidates").get(0).path("content").path("parts").get(0).path("text");

			String jsonContent = textNode.asText().replaceAll("```json\\n", "").replaceAll("\\n```", "").trim();

			System.out.println("ActivityAIService.processAIResponse() ..... PARSED RESPONSE FROM AI: {}");
			System.out.println(jsonContent);

			// analysis section
			JsonNode analysisJson = mapper.readTree(jsonContent);
			JsonNode analysisNode = analysisJson.path("analysis");

			StringBuilder fullAnalysis = new StringBuilder();
			addAnalysisSection(fullAnalysis, analysisNode, "overall", "Overall:");
			addAnalysisSection(fullAnalysis, analysisNode, "pace", "Pace:");
			addAnalysisSection(fullAnalysis, analysisNode, "heartRate", "Heart Rate:");
			addAnalysisSection(fullAnalysis, analysisNode, "caloriesBurned", "Calories Burned:");

			// improvements section
			List<String> improvements = extractImprovemnts(analysisJson.path("improvements"));
			// suggestions section
			List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
			// safety section
			List<String> safety = extractSafetyGuidelines(analysisJson.path("safety"));

			Recommendation recommendation = new Recommendation();
			recommendation.setActivityId(activity.getId());
			recommendation.setUserId(activity.getUserId());
			recommendation.setActivityType(activity.getType());
			recommendation.setRecommendation(fullAnalysis.toString());
			recommendation.setImprovements(improvements);
			recommendation.setSuggestions(suggestions);
			recommendation.setSafety(safety);
			recommendation.setCreatedAt(LocalDateTime.now());

			return recommendation;
		} catch (Exception e) {
			e.printStackTrace();
			return createDefaultRecommendation(activity);

		}
	}

	private Recommendation createDefaultRecommendation(Activity activity) {

		Recommendation recommendation = new Recommendation();

		recommendation.setActivityId(activity.getId());
		recommendation.setUserId(activity.getUserId());
		recommendation.setActivityType(activity.getType());
		recommendation.setRecommendation("Unable to generate detailed analysis");
		recommendation.setImprovements(Collections.singletonList("Continue with your current routine"));
		recommendation.setSuggestions(Collections.singletonList("Consider consulting a fitness professional"));
		recommendation
				.setSafety(Arrays.asList("Always warm up before exercise", "Stay hydrated", "Listen to your body"));
		recommendation.setCreatedAt(LocalDateTime.now());

		return recommendation;
	}

	private List<String> extractSafetyGuidelines(JsonNode safetyNode) {
		List<String> safety = new ArrayList<>();
		if (safetyNode.isArray()) {
			safetyNode.forEach(item -> safety.add(item.asText()));
		}
		return safety.isEmpty() ? Collections.singletonList("Follow general safety guidelines") : safety;
	}

	private List<String> extractSuggestions(JsonNode suggestionsNode) {
		List<String> suggestions = new ArrayList<>();
		if (suggestionsNode.isArray()) {
			suggestionsNode.forEach(suggestion -> {
				String workout = suggestion.path("workout").asText();
				String description = suggestion.path("description").asText();
				suggestions.add(String.format("%s: %s", workout, description));
			});
		}
		return suggestions.isEmpty() ? Collections.singletonList("No specific suggestions provided") : suggestions;
	}

	private List<String> extractImprovemnts(JsonNode improvementNode) {
		List<String> improvements = new ArrayList<String>();

		if (improvementNode.isArray()) {
			improvementNode.forEach(improvement -> {
				String area = improvement.path("area").asText();
				String details = improvement.path("recommedation").asText();
				improvements.add(String.format("%s: %s", area, details));
			});
		}
		return improvements.isEmpty() ? Collections.singletonList("No specific improvements provided") : improvements;

	}

	private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String prefix) {
		if (!analysisNode.path(key).isMissingNode()) {
			fullAnalysis.append(prefix).append(analysisNode.path(key).asText()).append("\n\n");
		}
	}

	private String createPromptForActivity(Activity activity) {
		return String.format(
				"""
						Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
						{
						  "analysis": {
						    "overall": "Overall analysis here",
						    "pace": "Pace analysis here",
						    "heartRate": "Heart rate analysis here",
						    "caloriesBurned": "Calories analysis here"
						  },
						  "improvements": [
						    {
						      "area": "Area name",
						      "recommendation": "Detailed recommendation"
						    }
						  ],
						  "suggestions": [
						    {
						      "workout": "Workout name",
						      "description": "Detailed workout description"
						    }
						  ],
						  "safety": [
						    "Safety point 1",
						    "Safety point 2"
						  ]
						}

						Analyze this activity:
						Activity Type: %s
						Duration: %d minutes
						Calories Burned: %d
						Additional Metrics: %s

						Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
						Ensure the response follows the EXACT JSON format shown above.
						""",
				activity.getType(), activity.getDuration(), activity.getCaloriesBurned(),
				activity.getAdditionalMetrics());
	}
}
