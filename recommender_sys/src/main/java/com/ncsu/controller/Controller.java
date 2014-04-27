package com.ncsu.controller;

import java.net.UnknownHostException;

import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;

import com.ncsu.recommender.Recommender;

public class Controller {
	
	public static void main(String[] args) {
	
		try {
			MongoDBDataModel dataModel = new MongoDBDataModel("localhost", 27017,
					"movie_data", "ratings", false, false, null, "user_id",
					"movie_id", "rating", "ratings_map");	
			
			
			System.out.println("Recommendation of Items");
			Recommender itemRecommender = new Recommender(dataModel, 3, 3, "item based");
			itemRecommender.recommendItems("2", 4);
			
			/*System.out.println("Recommendation of Users");
			Recommender userRecommender = new Recommender(dataModel, 3, 3, "user based");
			userRecommender.recommendUser("2", 4);*/
			
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}	
		
	}
}
