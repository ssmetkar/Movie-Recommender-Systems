package com.ncsu.ingest;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;

public class DataIngestor {
	
	public void ingestMovieData()
	{
		MongoClient mongo;
		try {
			
			//Initialize MongoDB configuration
			mongo = new MongoClient("localhost",27017);
			DB db = mongo.getDB("movie_data");
			DBCollection table = db.getCollection("movies");
			
			FileReader fr = new FileReader("D:\\Study Stuff\\Machine Learning\\movies_orig.dat");
			BufferedReader br = new BufferedReader(fr);
			String readLine = "";
			
			while((readLine = br.readLine()) != null )
			{
				BasicDBObject document = new BasicDBObject();
				
				String[] tokens = readLine.split("::");
				document.put("movie_id",Long.valueOf(tokens[0]));
				document.put("movie_name", tokens[1]);
				
				if(tokens.length > 2)
				{
					String[] genreList = tokens[2].split("\\|");
					document.put("genre", genreList);
				}
				table.insert(document);
			}
			
			System.out.println("Successfully ingested movie data");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("\nUnable to find file");
		} catch (IOException e) {
			System.err.println("\nError while reading data");
			e.printStackTrace();
		}
		
	}
	
	public void ingestUserData()
	{
		MongoClient mongo;
		try
		{
			mongo = new MongoClient("localhost",27017);
			DB db = mongo.getDB("movie_data");
			DBCollection table = db.getCollection("users");
			
			FileReader fr = new FileReader("D:\\Study Stuff\\Machine Learning\\users_orig.dat");
			BufferedReader br = new BufferedReader(fr);
			String readLine = "";
			
			while((readLine = br.readLine()) != null )
			{
				BasicDBObject document = new BasicDBObject();
				
				String[] tokens = readLine.split("::");
				document.put("user_id",Long.valueOf(tokens[0]));
				document.put("twitter_id",Long.valueOf(tokens[1]));
				table.insert(document);
			}
			System.out.println("Successfully ingested User data!");
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("\nUnable to find file");
		} catch (IOException e) {
			System.err.println("\nError while reading data");
			e.printStackTrace();
		}
	}

	public void ingestRatingData()
	{
		MongoClient mongo;
		try
		{
			mongo = new MongoClient("localhost",27017);
			DB db = mongo.getDB("movie_data");
			DBCollection table = db.getCollection("ratings");
			
			FileReader fr = new FileReader("D:\\Study Stuff\\Machine Learning\\ratings_orig.dat");
			BufferedReader br = new BufferedReader(fr);
			String readLine = "";
			
			while((readLine = br.readLine()) != null )
			{
				BasicDBObject document = new BasicDBObject();
				String[] tokens = readLine.split("::");
				document.put("user_id",Long.valueOf(tokens[0]));
				document.put("movie_id", Long.valueOf(tokens[1]));
				document.put("rating",Double.valueOf(tokens[2]));
				document.put("created_at", Long.valueOf(tokens[3]));
				table.insert(document);
			}
			
			System.out.println("Successfully ingested rating data");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("\nUnable to find file");
		} catch (IOException e) {
			System.err.println("\nError while reading data");
			e.printStackTrace();
		}
	}
}

