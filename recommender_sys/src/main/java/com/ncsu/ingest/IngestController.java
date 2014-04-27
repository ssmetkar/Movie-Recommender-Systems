package com.ncsu.ingest;

/**
 * Calls function to ingest data into MongoDB 
 *
 */
public class IngestController 
{
    public static void main( String[] args )
    {
    	DataIngestor dataIngestor = new DataIngestor();
    	/*dataIngestor.ingestMovieData();
    	dataIngestor.ingestUserData();*/
    	dataIngestor.ingestRatingData();
    }
}
