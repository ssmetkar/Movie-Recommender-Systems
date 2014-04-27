package com.ncsu.recommender;

import java.util.Iterator;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.common.FastIDSet;
import org.apache.mahout.cf.taste.impl.model.mongodb.MongoDBDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.AbstractRecommender;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericItemBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CosineItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.GenericItemSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.ItemSimilarity;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class Recommender {
	
	protected int neighbors = 10;
	
	protected String neighboorHoodType = "nearest";
	
	protected double userThreshold = 0.8;
	
	protected MongoDBDataModel dataModel;
	
	protected int recommendations = 10;
	
	protected UserSimilarity userSimilarity;
	
	protected ItemSimilarity itemSimilarity; 
	
	protected UserNeighborhood neighborhood;
	
	protected AbstractRecommender recommender; 
	
	public Recommender(MongoDBDataModel dataModel,String recommenderType) {
		build(dataModel,recommenderType);
	}
	
	public Recommender(MongoDBDataModel dataModel,
			int neighbors, int maxRecommendation,
			String recommenderType){
		this.neighbors = neighbors;
		this.recommendations = maxRecommendation;
		build(dataModel,recommenderType);
	}
	
	private void build(MongoDBDataModel dataModel, String recommenderType)
	{
		this.dataModel = dataModel;
		if(recommenderType.equalsIgnoreCase("user based"))
		{
			try {
				userSimilarity = new PearsonCorrelationSimilarity(dataModel);
				this.neighborhood = new NearestNUserNeighborhood(3, userSimilarity, dataModel);
				recommender = new GenericUserBasedRecommender(dataModel, neighborhood, userSimilarity);
				CachingRecommender cacheRecommender = new CachingRecommender(recommender);
			} catch (TasteException e) {
				e.printStackTrace();
			}
		}
		else if(recommenderType.equalsIgnoreCase("item based"))
		{
			ItemSimilarity	similarity = new LogLikelihoodSimilarity(dataModel);
			GenericItemSimilarity itemSimilarity;
			try {
				System.out.println("Computing Similarity");	
				itemSimilarity = new GenericItemSimilarity(similarity, dataModel);
				System.out.println("Computed Similarity");	
				recommender = new GenericItemBasedRecommender(dataModel, itemSimilarity);
				CachingRecommender cacheRecommender = new CachingRecommender(recommender);
			} catch (TasteException e) {
				e.printStackTrace();
				System.err.println("Error while pre-computing values");
			}
		}
	}
	
	public void recommendItems(String userId, int howMany)
	{
		try {
			 
			List<RecommendedItem> recommendations = recommender.recommend(Long.parseLong(dataModel.fromIdToLong(userId, false)), howMany);
			
			System.out.println("Recommendations for userId : " + userId);
			System.out.println("Item number\tPreference");
			for(RecommendedItem r : recommendations)
			{
				System.out.println(dataModel.fromLongToId(r.getItemID()) + "\t"
						+ Float.toString(r.getValue()));				
			}
					
		} catch (TasteException e) {
			e.printStackTrace();
			System.err.println("Error while recommending!");
		}
	}
	
	public void recommendUser(String userId,int howMany)
	{
		try
		{
			long[] recommendations = ((GenericUserBasedRecommender) recommender).mostSimilarUserIDs(Long.parseLong(dataModel.fromIdToLong(userId, false)),howMany);
			System.out.println("User Recommendation for user ID : " + userId);
			System.out.println("User ID");
			for(long id : recommendations)
			{
				System.out.println(dataModel.fromLongToId(id));
			}
		}catch(TasteException e)
		{
			e.printStackTrace();
			System.err.println("Error while recommending!");
		}
	}

}
